<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>账户管理</title>
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
         <h2 class="icon-zh">账户管理</h2>
     </div>
     
     <div class="content">
     <div class="searchBox clearFix">
       <div class="searchbar">
       		<form action="accountFr/toPageAccountFrManage" method="post" id="searchAccountForm">
       		  <input type="hidden" name="currentPage" id="currentPage"/>
              <input type="text" name="userName" id="userName" class="input-txt"  placeholder="请输入用户名"/> 
              <input type="text" name="accountName" id="accountName" class="input-txt"  placeholder="请输入账户名"/> 
              <input type="text" name="accountCode" id="accountCode" class="input-txt"  placeholder="请输入账户号"/>
              <input type="text" name="accountBank" id="accountBank" class="input-txt"  placeholder="请输入开户行"/>
              <select name="accountType"  class="left select-txt">
				<option value="" selected>请选择账户类型</option>
				<option value="1" >国内账户</option>
				<option value="2" >国外账户</option>
			  </select>
            </form>
             <a href="javascrit:void(0)" onClick="searchAccount()" class="btnSearch">search</a>
       </div>
      </div>
      <ul class="rolebox role-user role-zh tc_zh role-zh2">
      	<c:forEach var="accountFr" items="${afList }">
      		<li>
            <div class="role-date">
            <p><span class="color_888">用户名：</span> <span class="color_666">${accountFr.userName }</span></p>
            <p><span class="color_888">账户名：</span> <span class="color_666">${accountFr.accountName }</span></p>
            </div>
            <div class="role-date">
            <p><span class="color_888">账户号：</span> <span class="color_666">${accountFr.accountCode }</span></p>
            <p><span class="color_888">身份证号：</span> <span class="color_666">${accountFr.creditId }</span></p>
            </div>
            <div class="role-name">
              <p><span class="color_888">开户行：</span> <span class="color_666">${accountFr.accountBank }</span></p>
              <p><span class="color_888">类别：</span> <span class="color_666"><c:if test="${accountFr.accountType == 1}">国内账户</c:if><c:if test="${accountFr.accountType == 2}">海外账户</c:if> </span></p>
           </div>
           <div class="role-deal2">
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnOrage btnck">修改</a> <a href="" class="btnGrey">删除</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>修改账户</h2>
               <div class="tcbox">
                    <ul>
                    <form action="${pageContext.request.contextPath}/accountFr/updateAccountFr" method="post" id="updateAccountFrForm">
                     <li>
                      <label>用户名</label>
                      <span class="color_666">${accountFr.userName }</span>
                    </li>
                    <li>
                      <label>类别</label>
                      <input type="hidden" name="id" id="id" value="${accountFr.id}">
                      <input type="hidden" name="accountType" id="accountType" value="${accountFr.accountType}">
                      <c:if test="${accountFr.accountType == 1}">国内账户</c:if><c:if test="${accountFr.accountType == 2}">海外账户</c:if>
                    </li>
                    <li>
                      <label>账户名</label>
                      <input type="text" name=accountName id="accountName" class="input-txt" placeholder="账户名" value="${accountFr.accountName }"/>
                    </li>
                    <li>
                      <label>账户号</label>
                      <input type="text" name="accountCode" id="accountCode" class="input-txt" placeholder="账户号" value="${accountFr.accountCode }"/>
                    </li>
                    <li>
                      <label>开户行</label>
                      <input type="text" name="accountBank" id="accountBank" class="input-txt" placeholder="开户行" value="${accountFr.accountBank }"/>
                    </li>
                    
                    </form>
                    <li>
                     <a href="javascript:void(0)" id="updateAccountFrBtn" class="btnOrage">确认修改</a>
                    </li>
                   </ul> 
               </div>
           </div>
             <!--弹窗end-->
           </div>
         </li>
      	</c:forEach>
       </ul>
        <!-- 分页 -->
       	<jsp:include page="../header/pager.jsp"></jsp:include>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>
</body>
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	jQuery.validator.addMethod("accountNameVaild", function(value, element) {
		var language;
		var accountType = $("#accountType").val();
		if(accountType == "1"){
			language = /^[\u4e00-\u9fa5]+$/;
		}else{
			language = /^([a-zA-Z]+)$/;
		}
	    return this.optional(element) || (language.test(value));
	}, "只能输入中文或者英文字母");
	
	$("#updateAccountFrForm").validate({
		 onfocusout: function(element){
		        $(element).valid();
		 },
		 success:function(label){
				label.remove();
		 },
		 rules:{
			 "accountName":{
				 required:true,
				 accountNameVaild:true
			 },
			 "accountCode":{
				 required:true,
				 digits:true,
				 remote:{
					 type:"post",
					 data:{
						 accountCode:function(){
							 			return $("#accountCode").val();
						 			 }
					 },
			 		 url:"${pageContext.request.contextPath}/accountFr/validateAccountCode" 
				 }
			 },
			 "accountBank":{
				 required:true
			 }
		 },
		 messages:{
			 "accountName":{
				 required:"请输入账户名",
				 accountNameVaild:"请输入与账户类型相匹配的语言"
			 },
			 "accountCode":{
				 required:"请输入账户号",
				 digits:"只能输入数字",
				 remote:"该账户已存在，请勿重复输入"
			 },
			 "accountBank":{
				 required:"请输入账户开户行"
			 }
		 },
		 errorPlacement: function(error, element) {
				var span = $("<span class='icon errorInfo' />").append(error);
				span.appendTo(element.parent());
		 }

	});
	
	$("[id='updateAccountFrBtn']").click(function(e){
		if($(this).parents(".tcbox").find("#updateAccountFrForm").valid()){
			$(this).parents(".tcbox").find("#updateAccountFrForm").submit();
		}
	});
	
	
})
function searchAccount(){
		$("#currentPage").val("1");
		$("#searchAccountForm").submit();
}
</script>
</html>