<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改密码</title>
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
         <h2 class="icon-psw">修改密码</h2>
     </div>
     <div class="content">
      <form id="changePwForm">
       <ul class="s-form">
       			 <input type="hidden" name="password" id="password" value="${user.password }"/>
                 <li><label>旧密码：</label><input type="text" class="input-txt" id="oldPassword" name="oldPassword"> </li>
                 <li><label>新密码：</label><input type="text" class="input-txt" id="newPassword" name="newPassword"></li>
                 <li><label>确认密码：</label><input type="text" class="input-txt" id="confirmNewPw" name="confirmNewPw"></li>
       		
       	</ul>
       </form>
       <div class="btnsubmint"><a href="javascript:void(0)" class="btnOrage" id="updatePassword">修 改</a></div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

</body>
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#changePwForm').validate({
		success:function(){
			$(".icon").remove();
		},
		rules:{
			"oldPassword":{
				required:true //,
				//equalTo:"#password"
			},
			"newPassword":{
				required:true,
				minlength:8
			},
			"confirmNewPw":{
				equalTo:"#newPassword"
			}
		},
		messages:{
			"oldPassword":{
				required:"旧密码不能为空" //,
				//equalTo:"旧密码不正确"	
			},
			"newPassword":{
				required:"新密码不能为空",
				minlength:"新密码长度必须大于8"
			},
			"confirmNewPw":{
				equalTo:"请输入相同的新密码"
			}
			
		},
	  	errorPlacement: function(error, element) {
	  			error.removeClass("error");
 				var span = $("<span class='icon errorInfo' />").append(error);
 				span.appendTo(element.parent());
 		}
	});
	
	$('#updatePassword').click(function(e){
		if($('#changePwForm').valid()){
			var password = $("#newPassword").val();
			$.post(
				"user/updatePassword",
				{
					password:password
				},
				function(data){
					if(data['success']){
						alert("修改密码成功");
						window.location.href="user/toPageUserManage";
					}else{
						alert("修改密码失败");
					}
				},
				"json"
			);
		}
	});
})
</script>
</html>