<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>汇率管理</title>
</head>
<body>
<jsp:include page="../header/headerIndex.jsp"></jsp:include>
<!--主题内容 start-->
<div class="w1200">
  <!--左侧栏目 start-->
  <jsp:include page="../header/header.jsp"></jsp:include>
  <!--左侧栏目 end-->
  <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-fypz">汇率管理</h2>
     </div>
     <div class="content">
      
      <div class="exchangeBox clearFix">
        <dl class="exRate myRate">
          <dt><span class="wt-border"></span></dt>
          <dd class="label">
          	 <div class="m-calculator-con">
                <span class="calculator-tit" id="rateType">美元</span>
                <ul class="calculator-dis" style="display:none">
                    <li>人民币</li>
                    <li>美元</li>
                </ul>
            </div>
          </dd>
          <dd>
            <input type="text" class="input-txt" value="100"/>
          </dd>
        </dl>
        <span class="changeRate"></span>
        <dl class="exRate chainRate">
          <dt><span class="wt-border"></span></dt>
          <dd class="label">
          	<div class="m-calculator-con">
                <span class="calculator-tit" id="exRateType">人民币</span>
            </div>
		  </dd>
          <dd>
            <input type="text" name="myExchangeRate" id="myExchangeRate" class="input-txt" />
          </dd>
        </dl>
        <a href="javascript:void(0)" onClick="insertRate()" class="btnBlue">新增</a>
       </div>
       
       <table cellpadding="0" cellspacing="0" class="dataTable">
        <thead>
          <tr>
            <th class="num"></th>
            <th width="20%">种类</th>
            <th>创建时间</th>
            <th>汇率</th>
            <th class="w120">状态</th>
          </tr>
         </thead>
          <tbody>
          <c:forEach var="rate" items="${rateList }" varStatus="vs">
          	<tr>
            	<td class="num">${vs.index + 1 }</td>
            	<td><span class="colorBlue"><c:if test="${rate.type == 2 }">美元兑换人民币</c:if><c:if test="${rate.type == 1 }">人民币兑换美元</c:if></span></td>
            	<td><fmt:formatDate  value="${rate.createdDate }" type="both" pattern="yyyy-MM-dd" /><br/>
            		<fmt:formatDate  value="${rate.createdDate }" type="both" pattern="HH:mm:ss" />
            	</td>
            	<td><span class="colorGreen font18">${rate.rateHigh }</span></td>
            	<td><c:if test="${rate.status == 0 }">启用</c:if><c:if test="${rate.status == 1 }">禁用</c:if></td>
          	</tr>
          </c:forEach>
          </tbody>
       </table>
        <!-- 分页 -->
       	<jsp:include page="../header/pager.jsp"></jsp:include>
       
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>
<script type="text/javascript">
	function insertRate(){
		var rate = $("#myExchangeRate").val();
		var rateTypeMsg = $("#rateType").text();
		
		if(rateTypeMsg == "美元"){
			rateType = "2";
		}else{
			rateType = "1";
		}
		
		if(rate==""){
			alert("汇率不能为空");
			return false;
		}else{
			$.post(
				"${pageContext.request.contextPath}/fee/insertRate",
				{
					rateHigh:rate,
					type:rateType
				},
				function(data){
					if(data["success"]){
						alert("添加成功");
						window.location.href="${pageContext.request.contextPath}/fee/toPageRateManage";
					}else{
						alert("添加失败");
					}
				},
				"json"
			)
		}
		
	}
</script>
</body>
<script type="text/javascript">
function pageClick(currentPage){
	window.location.href="${pageContext.request.contextPath}/fee/toPageRateManage?currentPage="+currentPage;
}
</script>
</html>