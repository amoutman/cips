<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${pageContext.request.contextPath}/resource/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/style.js"></script>
<!--左侧栏目 start-->
  <div class="part-left">
  	<ul class="menu">
  	<c:forEach var="menu" items="${menuList }">
  		<c:if test="${menu.menuType == 1 }">
  			<li class="menu0"><a href="${menu.menuPath }">${menu.menuName }</a></li>
  		</c:if>
  	</c:forEach>
    </ul>
    <ul class="menu rolemenu">
    <c:forEach var="menu" items="${menuList }">
  		<c:if test="${menu.menuType == 0 }">
  			<li class="menu0"><a href="${menu.menuPath }">${menu.menuName }</a></li>
  		</c:if>
  	</c:forEach>
    </ul>
  </div>
  <!--左侧栏目 end-->