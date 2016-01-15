<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户管理</title>
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
          <span class="avatar-img"><img src="resource/images/head.gif" width="43" height="43" /></span>
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
<script type="text/javascript" src="resource/page/user.js"></script>
 <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-user">用户管理</h2>
         <div class="xlineb w430">
            <div class="searchbar">
              <input type="text" name="userInfo" id="userInfo" class="input-txt"  placeholder="请输入用户名/电话/身份证/邮箱"/> <a href="" class="btnSearch">search</a>
            </div>
         </div>
     </div>
     <div class="addbar">
          <div class="addrole">
            <div class="ck-deal"><a class="btnAdd btnck" onclick="javascript:showDiv()" href="javascript:vote(0)">新增用户</a></div>
           <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>新增用户</h2>
               <div class="tcbox">
                    <ul>
                    <form action="user/addUser" method="post" id="addUserForm">
                     <li class="li_user">
                      <label>用户名</label>
                      <input type="text" name="userName" id="userName" class="input-txt"/>
                    </li>
                    <li class="li_sex">
                       <label>角色<input type="hidden" name="roleId" id="roleId"/></label>
                       <ul class="jsSelect clearFix">
                      	<c:forEach var="role" items="${roleList }">
                      		<li><input type="checkbox" name="roleIds" id="roleIds" value="${role.id }" />${role.roleName }</li>
                      	</c:forEach>
                       </ul>
                    </li>
                    <li class="clearFix"></li> 
                    <li>
                      <label>电话</label>
                      <input type="text" name="mobile" id="mobile" class="input-txt"/>
                    </li>
                    <li>
                      <label>身份证号码</label>
                      <input type="text" name="creditId" id="creditId" class="input-txt"/>
                    </li>
                    <li>
                      <label>邮箱</label>
                      <input type="text" name="email" id="email" class="input-txt" />
                    </li>
                    </form>
                   </ul>
                    <a href="javascript:void(0)" class="btnOrage" id="btnAddUser">确认添加</a>
               </div>
               
           </div>
          <!--弹窗end-->
         </div>
    </div>
     <div class="content">
      
      <ul class="rolebox role-user">
      	<c:forEach var="user" items="${userList }">
         <li>
           <div class="role-img"><img src="resource/images/role-tx1.gif" width="51" height="51" /></div>
           <div class="role-name">
               <p>用户名</</p>
               <p class="colorBlue">ID:${user.userCode }</p>
           </div>
           <div class="role-date">
              <p><span class="color_888">姓名：</span> <span class="color_666">${user.userName }</span></p>
              <p><span class="color_888">角色：</span> <span class="color_666">${user.roleNames }</span></p>
              <p><span class="color_888">电话：</span> <span class="color_666">${user.mobile }</span></p>
           </div>
           <div class="role-deal">
              <p><span class="color_888">身份证号码：</span> <span class="color_666">${user.creditId }</span></p>
              <p><span class="color_888">邮箱：</span> <span class="color_666">${user.email }</span></p>
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnOrage btnck">修改</a> <a href="javascript:void(0)" onClick="deleteUser(${user.id})" class="btnGrey">删除</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>修改用户</h2>
               <div class="tcbox">
                    <ul>
                    <form action="user/updateUser" method="post" id="updateUserForm">
                     <li class="li_user">
                      <label>用户名</label>
                      <input type="text" name="userName" id="userName" class="input-txt" value="${user.userName }"/>
                    </li>
                    <li class="li_sex">
                     <label>角色<input type="hidden" name="roleId" id="roleId"/></label>
                      <ul class="jsSelect clearFix">
                      		<c:forEach var="role" items="${user.roleList }">
                      			<c:if test="${role.isCheck == 1 }">
                      				<li><input type="checkbox" name="roleIds" id="roleIds" value="${role.id }" checked="true"/>${role.roleName }</li>
                      			</c:if>
                      			<c:if test="${role.isCheck == 0 }">
                      				<li><input type="checkbox" name="roleIds" id="roleIds" value="${role.id }"/>${role.roleName }</li>
                      			</c:if>
                      		</c:forEach>
                      </ul>
                    </li>
                    <li class="clearFix"></li> 
                    <li>
                      <label>电话</label>
                      <input type="text" name="mobile" id="mobile" class="input-txt" value="${user.mobile }"/>
                    </li>
                    <li>
                      <label>身份证号码</label>
                      <input type="text" name="creditId" id="creditId" class="input-txt" value="${user.creditId }"/>
                    </li>
                    <li>
                      <label>邮箱</label>
                      <input type="text" name="email" id="email" class="input-txt" placeholder="${user.email }"/>
                    </li>
                    </form>
                   </ul>
                    <a href="javascript:void(0)" class="btnOrage" id="btnUpdateUser">确认修改</a>
               </div>
               
           </div>
          <!--弹窗end-->
           </div>
         </li>
         </c:forEach>
       </ul>
       
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>

</body>
</html>