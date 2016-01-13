<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改密码</title>
</head>
<body>
<!--header start-->
<div class="header">
  <div class="w1200">
    <span class="layout">
       <a href="" class="btnLayout">退出</a>
    </span>
     <span class="logo"></span>
     <div class="welcomeWord">
        <span class="avatar">
          <span class="avatar-shade"></span>
          <span class="avatar-img"><img src="../images/head.gif" width="43" height="43" /></span>
        </span>
        <span class="word">豆沙包欢迎您！</span>
     </div>
  </div>
</div>
<!--header end-->
<!--主题内容 start-->
<div class="w1200">
<jsp:include page="../header/header.jsp"></jsp:include>
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
<script type="text/javascript" src="resource/page/password.js"></script>
 <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-psw">修改密码</h2>
     </div>
     <div class="content">
       <ul class="s-form">
       		<form id="changePwForm">
       			 <input type="hidden" name="password" id="password" value="${user.password }"/>
                 <li><label>旧密码：</label><input type="text" class="input-txt" id="oldPassword" name="oldPassword" value=""> </li>
                 <li><label>新密码：</label><input type="text" class="input-txt Error" id="newPassword" name="newPassword" value=""></li>
                 <li><label>确认密码：</label><input type="text" class="input-txt" id="confirmNewPw" name="confirmNewPw" value=""></li>
       		</form>
       </ul>
       <div class="btnsubmint"><a href="javascript:void(0)" class="btnOrage" id="updatePassword">修 改</a></div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->


</body>
</html>