package com.cips.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cips.constants.GlobalPara;
import com.cips.page.PageContext;
import com.cips.page.Pager;
import com.cips.util.SystemUtil;

/** 
  * 查询分页拦截器，用户拦截SQL，并加上分页的参数和高级查询条件 
  *  
  * @author yuanzhao
  *  
*/  


@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) }) 
public class PagerInterceptor implements Interceptor {

	private final Logger logger = LoggerFactory.getLogger(PagerInterceptor.class);  
	private String dialect = "MySQL"; 
	private String mappingSqlId;

	public void setMappingSqlId(String mappingSqlId) {
		this.mappingSqlId = mappingSqlId;
	}

	@Override
	public Object intercept(Invocation ivk) throws Throwable {
		// TODO Auto-generated method stub
		if (!(ivk.getTarget() instanceof RoutingStatementHandler)) {  
		      return ivk.proceed();  
		}
		RoutingStatementHandler statementHandler = (RoutingStatementHandler)ivk.getTarget(); 
		BaseStatementHandler delegate = (BaseStatementHandler)SystemUtil.getValueByFieldName(statementHandler, "delegate");
		MappedStatement mappedStatement = (MappedStatement)SystemUtil.getValueByFieldName(delegate, "mappedStatement"); 
		
		// BoundSql封装了sql语句
		BoundSql boundSql = delegate.getBoundSql();

		// 获得查询对象 
		Object parameterObject = boundSql.getParameterObject();
		// 根据参数类型判断是否是分页方法
		if (!mappedStatement.getId().matches(mappingSqlId)){
			return ivk.proceed();
		}
		logger.debug(" beginning to intercept page SQL..."); 
		Connection connection = (Connection) ivk.getArgs()[0];
		String sql = boundSql.getSql(); 
		Map<String,Object> sMap = (Map<String,Object>)parameterObject; 
		// 查询参数对象 
		Pager pager = null; 
		// 查询条件Map   
		pager = (Pager)sMap.get(GlobalPara.PAGER_SESSION);
		
		// 获取查询数来的总数目
		String countSql = "SELECT COUNT(0) FROM (" + sql + ") as atbale ";
		PreparedStatement countStmt = connection.prepareStatement(countSql); 
	    BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql, 
				 boundSql.getParameterMappings(), parameterObject);
	    setParameters(countStmt, mappedStatement, countBS, parameterObject);   
	    ResultSet rs = countStmt.executeQuery();
	    int count = 0;
	    if(rs.next()){ 
	    	count = rs.getInt(1); 
	    }
	    rs.close();
	    countStmt.close();
	    
	    // 设置总记录数 
	    pager.setTotalResult(count);
	    // 设置总页数
	    pager.setTotalPage((count + pager.getShowCount() - 1)/pager.getShowCount());
	    // 放到作用于
	    PageContext.setPager(pager);
	    // 拼装查询参数
	    String pageSql = generatePageSql(sql, pager);
	    SystemUtil.setValueByFieldName(boundSql, "sql", pageSql); 
	    logger.debug("generated pageSql is : " + pageSql);
	    return ivk.proceed();
	}
	
	private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,
			BoundSql boundSql,Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId()); 
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if(parameterMappings != null){
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null:configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if(parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if(parameterObject == null){
						 value = null;
					}else if(typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
						 value = parameterObject;
					}else if(boundSql.hasAdditionalParameter(propertyName)){  
						 value = boundSql.getAdditionalParameter(propertyName);
					}else if(propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						 value = boundSql.getAdditionalParameter(prop.getName());
						 if (value != null) {
							 value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						 }
					}else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					@SuppressWarnings("unchecked")
					TypeHandler<Object> typeHandler = (TypeHandler<Object>) parameterMapping.getTypeHandler();
					if(typeHandler == null){ 
						throw new ExecutorException("There was no TypeHandler found for parameter " 
								+ propertyName + " of statement "  + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value,parameterMapping.getJdbcType());
				}
			}
		}
	}
	
	private String generatePageSql(String sql, Pager page){ 
		if (page != null && (dialect != null || !dialect.equals(""))){
			StringBuffer pageSql = new StringBuffer();
			if("MySQL".equals(dialect)){
				pageSql.append(sql);
				pageSql.append(" LIMIT " + page.getCurrentResult() + "," + page.getShowCount());
			}else if("oracle".equals(dialect)){ 
				pageSql.append("SELECT * FROM (SELECT t.*,ROWNUM r FROM ("); 
				pageSql.append(sql);
				pageSql.append(") t WHERE ROWNUM <= ");
				pageSql.append(page.getCurrentResult() + page.getShowCount()); 
				pageSql.append(") WHERE r >");
				pageSql.append(page.getCurrentResult()); 
			}
			return pageSql.toString(); 
		}else {  
			return sql;  
		}
	}

	@Override
	public Object plugin(Object arg0) {
		// TODO Auto-generated method stub
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
		// TODO Auto-generated method stub
		dialect = p.getProperty("dialect");
		if (dialect == null || dialect.equals("")) {
			try {
				throw new PropertyException("dialect property is not found!"); 
			}catch (PropertyException e){
				e.printStackTrace(); 
			}
		}
		
		if (dialect == null || dialect.equals("")) {
			try {
				 throw new PropertyException("pageMethodPattern property is not found!");  
			}catch (PropertyException e){
				 e.printStackTrace();
			}
		}

	}

}
