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
<jsp:include page="../header/headerIndex.jsp"></jsp:include>
<!--header end-->
<!--主题内容 start-->
<div class="w1200">
<jsp:include page="../header/header.jsp"></jsp:include>

 <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-user">用户管理</h2>
         <div class="xlineb w430">
            <div class="searchbar">
              <form action="user/toPageUserManage" id="searchUserForm" method="post">
              	<input type="text" name="userInfo" id="userInfo" class="input-txt"  placeholder="请输入用户名/电话/身份证/邮箱" value="${userInfo }"/>
              	<input type="hidden" name="currentPage" id="currentPage"/>
              </form>
               <a href="javascript:void(0);" onClick="searchUser()" class="btnSearch">search</a>
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
                    <form action="user/insertUser" method="post" id="addUserForm">
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
                   
                    <a href="javascript:void(0);" class="btnOrage" id="btnAddUser">确认添加</a>
               </div>
               
           </div>
          <!--弹窗end-->
         </div>
    </div>
     <div class="content">
      
      <ul class="rolebox role-user">
      	<c:forEach var="user" items="${userList }" varStatus="vs">
         <li>
           <div class="role-img"><img src="resource/images/role-tx1.gif" width="51" height="51" /></div>
           <div class="role-date">
              <p><span class="color_888">姓名：</span> <span class="color_666">${user.userName }</span></p>
              <p><span class="color_888">角色：</span> <span class="color_666">${user.roleNames }</span></p>
              <p><span class="color_888">电话：</span> <span class="color_666">${user.mobile }</span></p>
           </div>
           <div class="role-deal">
              <p><span class="color_888">身份证号码：</span> <span class="color_666">${user.creditId }</span></p>
              <p><span class="color_888">邮箱：</span> <span class="color_666">${user.email }</span></p>
              <div class="ck-deal"> <input type="hidden" name="userId" id="userId" value="${user.id }"/><a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnOrage btnck">修改</a>  <a href="javascript:void(0);" id="btnDeleteUser" onClick="return deleteUser()" class="btnGrey">删除</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>修改用户</h2>
               <div class="tcbox">
                    <ul>
                    <form action="user/updateUser" method="post" id="updateUserForm${vs.index + 1 }">
                     <li class="li_user">
                      <label>用户名</label>
                      <input type="hidden" name="id" id="id" value="${user.id }"/>
                      <input type="text" name="userName" id="userName" class="input-txt" value="${user.userName }"/>
                    </li>
                    <li class="li_sex">
                     <label>角色<input type="hidden" name="roleId" id="roleId" value="${user.roleId }"/></label>
                      <ul class="jsSelect clearFix">
                      		<c:forEach var="role" items="${user.roleList }">
								<c:if test="${role.isCheck == 1 }">
                      				<li><input type="checkbox" name="roleIds" id="roleIds" value="${role.id }" checked="checked"/>${role.roleName }</li>
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
                      <input type="text" name="email" id="email" class="input-txt" value="${user.email }"/>
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
	
	$("[id='btnDeleteUser']").click(function(){
		var userId = $(this).parent().find("#userId").val();
		alert(userId);
		if(confirm("确定删除该用户？")){
			$.post(
				"user/deleteUser",
				{
					userId:userId
				},
				function(data){
					if(data["success"]){
						alert("删除成功");
						window.location.href="user/toPageUserManage";
					}else{
						alert("删除失败");
					}
				},
				"json"
			)
		}
	})
	
	$("[id^='updateUserForm']").validate({
		success:function(label){
			label.remove();
		},
		rules:{
			"userName":{
				required:true
			},
			"mobile":{
				required:true
			},
			"creditId":{
				required:true
			},
			"email":{
				required:true,
				email:true
			}	
		},
		messages:{
			"userName":{
				required:"用户名不能为空"
			},
			"mobile":{
				required:"电话号码不能为空"
			},
			"creditId":{
				required:"身份证号不能为空"
			},
			"email":{
				required:"邮件不能为空",
				email:"请输入正确的邮箱地址"
			}
		},
		errorPlacement: function(error, element) {
			var span = $("<span class='icon errorInfo' />").append(error);
			span.appendTo(element.parent());
		}
	});

$("[id='btnUpdateUser']").click(function(){
	//var validForm = $(this).parents(".tcbox").find("#updateUserForm");
	//var index = $(this).parents(".tcbox").find("#index").val();
	//alert(index);
	//var obj = "updateUserForm" + index;
	//var validForm = $(this).parents(".tcbox").find("form[id^='updateUserForm']");
	if( $(this).parents(".tcbox").find("[id^='updateUserForm']").valid()){
		 $(this).parents(".tcbox").find("[id^='updateUserForm']").submit();
	}else{
		alert("验证失败");
	}
});

$('#addUserForm').validate({
	onfocusout: function(element){
        $(element).valid();
 	},
	success:function(label){
		label.remove();
	},
	rules:{
		"userName":{
			required:true,
			remote:{
				 type:"post",
				 data:{
					 userInx:"userName",
					 userInfo:function(){
						 			return $("#addUserForm").find("#userName").val();
					 			 }
				 },
		 		 url:"user/validate" 
			 }
		},
		"roleId":{
			required:true
		},
		"mobile":{
			required:true,
			remote:{
				 type:"post",
				 data:{
					 userInx:"mobile",
					 userInfo:function(){
						 			return $("#addUserForm").find("#mobile").val();
					 			 }
				 },
		 		 url:"user/validate" 
			 }
		},
		"creditId":{
			required:true,
			remote:{
				 type:"post",
				 data:{
					 userInx:"creditId",
					 userInfo:function(){
						 			return $("#addUserForm").find("#creditId").val();
					 			 }
				 },
		 		 url:"user/validate" 
			 }
		},
		"email":{
			required:true,
			email:true,
			remote:{
				 type:"post",
				 data:{
					 userInx:"email",
					 userInfo:function(){
						 			return $("#addUserForm").find("#email").val();
					 			 }
				 },
		 		 url:"user/validate" 
			 }
		}	
	},
	messages:{
		"userName":{
			required:"用户名不能为空",
			remote:"用户名已注册"
		},
		"roleId":{
			required:"至少选择一个角色"
		},
		"mobile":{
			required:"电话号码不能为空",
			remote:"电话已注册"
		},
		"creditId":{
			required:"身份证号不能为空",
			remote:"身份证已注册"
		},
		"email":{
			required:"邮件不能为空",
			email:"请输入正确的邮箱地址",
			remote:"邮箱已注册"
		}
	},
	errorPlacement: function(error, element) {
		var span = $("<span class='icon errorInfo' />").append(error);
		span.appendTo(element.parent());
	}
});

$("#btnAddUser").click(function(e){
	if($('#addUserForm').valid()){
		$('#addUserForm').submit();
	}
});

$("input[type=checkbox]").change(function(){
	$(this).parents(".li_sex").find("#roleId").val("");
	var roleIds = "";
	$(this).parents(".jsSelect").find("input[type=checkbox]").each(function(){
		if($(this).is(":checked")){
			if(roleIds == ""){
				roleIds = $(this).val();
			}else{
				roleIds = roleIds + "," + $(this).val();
			}
		}
		
	});
	$(this).parents(".li_sex").find("#roleId").val(roleIds);
});
	
})
function pageClick(currentPage){
	$("#currentPage").val(currentPage);
	$("#searchUserForm").submit();
}

function searchUser(){
	$("#currentPage").val("1");
	$("#searchUserForm").submit();
}

</script>
</html>