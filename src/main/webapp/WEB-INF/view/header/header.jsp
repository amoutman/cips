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
<base href="<%=basePath%>">
<link href="resource/css/base.css" rel="stylesheet" type="text/css" />
<link href="resource/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="resource/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="resource/js/style.js"></script>
<script type="text/javascript" src="resource/js/dragDiv.js"></script>
<script type="text/javascript">
$("ul.menu li:first").addClass("curr");
$("ul.menu li").click(function(){
	   $(this).addClass("curr").siblings().removeClass("curr");										  
});

</script>
<!--左侧栏目 start-->
  <div class="part-left">
  	<ul class="menu">
  	<c:forEach var="menu" items="${menuList }">
  		<c:if test="${menu.menuType == 1 }">
  			<li class="menuli menu0"><a href="${menu.menuPath }">${menu.menuName }</a></li>
  		</c:if>
  	</c:forEach>
    </ul>
    <ul class="menu rolemenu">
    <c:forEach var="menu" items="${menuList }">
  		<c:if test="${menu.menuType == 0 }">
  			<li class="menuli menu0"><a href="${menu.menuPath }">${menu.menuName }</a></li>
  		</c:if>
  	</c:forEach>
    </ul>
  </div>
  <!--左侧栏目 end-->