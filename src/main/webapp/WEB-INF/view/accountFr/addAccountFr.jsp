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
<title>增加账户</title>
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
         <h2 class="icon-zh">新增账户</h2>
         <div class="xlineb w430 clearFix">
            <div class="searchbar right mr50">
              <input type="text" name="textfield" id="textfield" class="input-txt"  placeholder="请输入用户名/电话/身份证/邮箱"/> <a href="" class="btnSearch">search</a>
            </div>
         </div>
     </div>

     <div class="content">
      
      <ul class="rolebox role-user role-zh tc_zh">
      	<c:forEach var="user" items="${frUserList }">
      		         <li>
           <div class="role-info">
              <p class="left"><span class="color_888">姓名：</span> <span class="color_666">${user.userName }</span></p>
              <p class="left"><span class="color_888">电话：</span> <span class="color_666">${user.mobile }</span></p>
              <p class="left"><span class="color_888">身份证号：</span> <span class="color_666">${user.creditId }</span></p>
           </div>
           <div class="role-deal2">
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnOrage btnAdd btnck">新增账户</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>新增账户</h2>
               <div class="tcbox clearFix">
                    <ul class="clearFix">
                    <form action="accountFr/insertAccountFr" method="post" id="insertAccountForm">
                    <li>
                      <input type="radio" name="accountType" id="accountType" value="1" checked/>国内账户　　　
                      <input type="radio" name="accountType" id="accountType" value="2" />海外账户
                      <input type="hidden" name="userId" id="userId" value="${user.id }"/>
                    </li>
                    <li>
                      <label>账户名</label>
                      <input type="text" name="accountName" id="accountName" class="input-txt"/>
                    </li>
                    <li>
                      <label>账户号</label>
                      <input type="text" name="accountCode" id="accountCode" class="input-txt" />
                    </li>
                    
                    <li>
                      <label>开户行</label>
                      <input type="text" name="accountBank" id="accountBank" class="input-txt" />
                    </li>
                    </form>
                    <li>
                     <a href="javascript:void(0)" id="addAccountBtn" class="btnOrage">确认添加</a>
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
	
	$("#insertAccountForm").validate({
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
			 		 url:"accountFr/validateAccountCode" 
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
	
	$("#addAccountBtn").click(function(){
		if($(this).parents(".clearFix").find("#insertAccountForm").valid()){
			$(this).parents(".clearFix").find("#insertAccountForm").submit();
		}
	});
})
</script>
</html>