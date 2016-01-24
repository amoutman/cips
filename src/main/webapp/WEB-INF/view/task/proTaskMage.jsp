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
<title>代办事项</title>
</head>

<body>
<!--header start-->
<jsp:include page="../header/headerIndex.jsp"></jsp:include>
<!--header end-->

<!--主题内容 start-->
<div class="w1200">
<jsp:include page="../header/header.jsp"></jsp:include>
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
  <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-db">待办事项</h2>
     </div>
     <div class="content">
       <ul class="waitevent">
       <c:if test="${size <= 0}"><h2>当前没有待办任务</h2></c:if>
       <c:if test="${size > 0}">
       	 <c:forEach items="${tasks}" var="task">
	         <li>
	           <div class="wt-lf"><span class="wt-border"></span></div>
	           <div class="wt-num">订单号：${task.orderNo}</div>
	           <div class="wt-event">${task.msg}</div>
	           <div class="wt-deal"><a href="javascript:void(0);" class="btnBlue"  onclick="processingTask('${task.id}')">处理</a></div>
	         </li>
         </c:forEach>
       </c:if>
       </ul>
       <!-- 分页 -->
       <jsp:include page="../header/pager.jsp"></jsp:include>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>
<form action="task/toPageTaskMage" id="taskForm">
<input type="hidden" name="currentPage" id="currentPage"/>
</form>
</body>
<script type="text/javascript">
function pageClick(currentPage){
	$("#currentPage").val(currentPage);
	$("#taskForm").submit();
}

function processingTask(taskId){
	$.post(
			"${pageContext.request.contextPath}/task/processingTask",
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
