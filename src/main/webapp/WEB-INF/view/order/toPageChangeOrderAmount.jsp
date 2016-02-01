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
<title>订单金额撮合</title>
</head>

<body>
<!--header start-->
<jsp:include page="../header/headerIndex.jsp"></jsp:include>
<!--header end-->

<!--主题内容 start-->
<div class="w1200">
<jsp:include page="../header/header.jsp"></jsp:include>
  <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-ddcx">订单金额撮合</h2>
     </div>
     <div class="content">
       <div class="search clearFix">
       <form action="order/toPageMatchOrderAmount" id="searchForm" method="post">
          <label>订单号</label><input type="text" name="orderNo" id="orderNo" class="inpt1" value="${order.orderNo}"/><label>提交时间</label><input type="text" name="applyBDate" id="applyBDate" class="inpt1" value="${order.applyBDate}"/><label>至</label><input type="text" name="applyEDate" id="applyEDate" class="inpt1" value="${order.applyEDate}"/></div>
          <input type="hidden" name="currentPage" id="currentPage"/>
       </form>
       <div class="btnDiv clearFix">
         <div class="w210 right"><a href="javascript:void(0);" class="btnOrage" onclick="searchOrders()">查询</a></div>
       </div>
       <table cellpadding="0" cellspacing="0" class="dataTable">
        <thead>
          <tr>
            <th>订单号</th>
            <th>申请金额</th>
            <th>华创申请金额</th>
            <th>已维护金额</th>
            <th>时间</th>
            <th class="w120">操作</th>
          </tr>
         </thead>
          <tbody>
          <c:if test="${orderNum <= 0}"><h2>您还没有提交过订单申请</h2></c:if>
          <c:if test="${orderNum > 0}">
          <c:forEach items="${orders}" var="order" >
          <tr>
            <td>${order.orderNo}</td>
            <td><span class="colorBlue">$${order.applyAmount}</span></td>
            <td><span class="colorBlue">$${order.hcApplyAmount}</span></td>
            <td><span class="color_grey">$${order.matchAmount }</span></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${order.applyDate}" type="both"/></td>
            <td>
             <c:if test="${order.isMatch == 0 }"><a href="${pageContext.request.contextPath}/order/viewMatchOrderAmount?orderId=${order.id }" class="colorBlue">修改撮合金额</a></c:if>
            </td>
          </tr>
          </c:forEach>
          </c:if>
          </tbody>
       </table>
       <!--分页 start-->
       <c:if test="${orderNum > 0}"><jsp:include page="../header/pager.jsp"></jsp:include></c:if>
       <!--分页 end-->
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div id="bg" class="bg"></div>

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
	$("#applyBDate").datepicker(); 
	$("#applyEDate").datepicker(); 
});

function pageClick(currentPage){
	$("#currentPage").val(currentPage);
	$("#searchForm").submit();
}

function searchOrders(){
	$("#currentPage").val("1");
	$("#searchForm").submit();
}
</script>
</html>
