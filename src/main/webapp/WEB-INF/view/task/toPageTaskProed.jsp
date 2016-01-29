<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>已办事项</title>
</head>

<body>
<!--header start-->
<jsp:include page="../header/headerIndex.jsp"></jsp:include>
<!--header end-->

<!--主题内容 start-->
<div class="w1200">
  <!--左侧栏目 start-->
	<jsp:include page="../header/header.jsp"></jsp:include>
  <!--左侧栏目 end-->
  <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-ybsx">已办事项</h2>
     </div>
     <div class="content">
       <div class="search clearFix">
       <form action="task/toPageProedTasksMage" id="searchForm" method="post">
          <label>订单号</label><input type="text" name="orderNo" id="orderNo" class="inpt1" value="${task.orderNo }"/><label>处理时间</label><input type="text" name="proTaskBTime" id="proTaskBTime" class="inpt1" value="${task.proTaskBTime }"/><label>至</label><input type="text" name="proTaskETime" id="proTaskETime" class="inpt1" value="${task.proTaskETime }"/></div>
       	  <input type="hidden" name="currentPage" id="currentPage"/>
       </form>
       <div class="btnDiv clearFix">
         <div class="w210 right"><a href="javascript:void(0);" class="btnOrage" onclick="searchTask()">查询</a></div>
       </div>
       <table cellpadding="0" cellspacing="0" class="dataTable">
        <thead>
          <tr>
            <th width="30%">订单号</th>
            <th>任务信息</th>
            <th>任务完成时间</th>
            <th class="w120">操作</th>
          </tr>
         </thead>
          <tbody>
          <c:if test="${taskNum > 0}">
          <c:forEach items="${tasks}" var="task">
	          <tr>
	            <td>${task.orderNo}</td>
	            <td>${task.msg}</td> 
	            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${task.endTime}" type="both"/></td>
	            <td><a href="task/viewProTask?taskId=${task.id}" class="colorBlue">查看</a></td>
	          </tr>
          </c:forEach>
          </c:if>
          </tbody>
       </table>
       <c:if test="${taskNum <= 0}"><h2 class="mt10 tac">您还没有处理过待办任务</h2></c:if>
       <!-- 分页 -->
       <c:if test="${taskNum > 0}"><jsp:include page="../header/pager.jsp"></jsp:include></c:if>
     
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->
</body>
<link rel="stylesheet" type="text/css" href="resource/jqueryUI/css/smoothness/jquery-ui-1.10.4.custom.css">
<link rel="stylesheet" type="text/css" href="resource/jqueryUI/timepicker/jquery-ui-timepicker-addon.css">
<script type="text/javascript" src="resource/jqueryUI/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="resource/jqueryUI/js/i18n/jquery.ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="resource/jqueryUI/timepicker/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="resource/jqueryUI/timepicker/i18n/jquery-ui-timepicker-zh-CN.js"></script>
<script type="text/javascript">
/*界面初始化*/
$(function(){
	$("#proTaskBTime").datepicker(); 
	$("#proTaskETime").datepicker(); 
});

function pageClick(currentPage){
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

function searchTask(){
	$("#currentPage").val("1");
	$("#searchForm").submit();
}

function viewTask(taskId){
	$.post(
			"${pageContext.request.contextPath}/task/viewProTask",
			{
				"taskId":taskId
			},
			function(data){
				if (data.msg == "1") {
					//访问
					window.location.href = "${pageContext.request.contextPath}/task/preProTask?taskId="+taskId;
				} else {
					// 失败了
					alert(data.msg);
				}
			},
			"json"	
	);
}
</script>
</html>
