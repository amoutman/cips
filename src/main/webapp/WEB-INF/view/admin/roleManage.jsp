<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
<title>角色管理</title>
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

 <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-role">角色管理</h2>
         <div class="xlineb"></div>
     </div>
     <div class="addbar">
          <div class="addrole">
            <div class="ck-deal"><a class="btnAdd btnck" onclick="javascript:showDiv()" href="javascript:vote(0)">新增角色</a></div>
           <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>新增角色</h2>
               <div class="tcbox">
                    <input type="text" name="newRoleName" id="newRoleName" class="input-txt"  placeholder="请输入角色名称"/> <a href="javascript:void(0);" onClick="addRole();" id="btnAddRole" class="btnOrage">确认添加</a>
               </div>
               
           </div>
          <!--弹窗end-->
         </div>
    </div>
     <div class="content">
      
      <ul class="rolebox">
      	<c:forEach var="role" items="${roleList }">
         <li>
           <div class="role-img"><img src="resource/images/role-tx1.gif" width="51" height="51" /></div>
           <div class="role-name">角色名称：${role.roleName }</div>
           <div class="role-date">创建时间：<fmt:formatDate value="${role.createdDate }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></div>
           <div class="role-deal">
              <div class="ck-deal"><a onclick="deleteRole(${role.id })" href="javascript:vote(0)" id="btnDelete" class="btnOrage btnck">修改</a> <a href="" class="btnGrey">删除</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>修改角色</h2>
               <div class="tcbox">
               		<input type="hidden" name="roleId" id="roleId" value="${role.id }">
                    <input type="text" name="roleName" id="roleName" class="input-txt"  placeholder="角色名称"/> <a href="javascript:void(0)" onclick="updateRole(this)" id="btnUpdate" class="btnOrage">确认修改</a>
               </div>
               
             </div>
            <!--弹窗end-->
           </div>
         </li>
         </c:forEach>
       </ul>
     </div>
     <div class="wtbox mt10">
   <div class="wt_skzh clearFix">
  <h2 class="ck-deal"><a onclick="getAccountInfo()" href="javascript:vote(0)" class="btnBlue btnck">选择收款账户信息</a></h2>
	                <!--弹窗start-->
	               <div class="tcDiv xzzh_tc">
	               <span class="close"></span>
	               <h2>选择收款账户信息</h2>
	               <div class="tcbox">
	                  <div class="searchBox clearFix">
	                   <div class="searchbar">
	                          <input type="text" name="textfield" id="textfield" class="input-txt"  placeholder="请输入用户名"/> 
	                          <input type="text" name="textfield" id="textfield" class="input-txt"  placeholder="请输入开户行"/>
	                          <input type="text" name="textfield" id="textfield" class="input-txt"  placeholder="请输入身份证号"/> <a href="" class="btnSearch">search</a>
	                   </div>
	                  </div>
	                  <table width="100%" class="xzzh_table">
	                    <tr>
	                      <th><label>用户名</label> </th>
	                      <th><label>开户行</label> </th>
	                      <th><label>账户名</label> </th>
	                      <th><label>账户号</label> </th>
	                      <th>操作</td>
	                    </tr>
	                    <tbody id="accountInfo">
	                    </tbody>
	                  </table>
		               <!--分页 start-->
				       <div class="page"><a href="">&lt;</a><a href="">1</a><a href="">2</a><a href="" class="active">3</a><a href="">4</a><a href="">5</a>...<a href="">9</a><a href="">&gt;</a></div>
				       <!--分页 end-->
	               </div>
	           </div>
	             <!--弹窗end-->
	             </div>
	             </div>
  </div>
  
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>

</body>
<script type="text/javascript" src="resource/page/role.js"></script>
<script type="text/javascript">
function addRole(){
	
	var roleName = $("#newRoleName").val();
	if(roleName == ""){
		alert("角色名称不能为空");
	}
	$.post(
		"user/insertRole",
		{
			roleName:roleName
		},
		function(data){
			if(data['success']){
				alert("添加成功");
				window.location.href="user/toRoleManage";
			}else{
				alert("添加失败");
			}
		},
		"json"
	);
}

function getAccountInfo(){
	$.post(
		"accountFr/toPageAccountFrMap",
		function(data){
			//var jsonObj=eval("("+data+")");
			var showHtml = "";
			alert(data.afList);
			$.each(data.afList, function (i, item) {  
				if(showHtml == ""){
					showHtml = "<tr><td><label>"+item.userName+"</label></td><td><label>"+item.userName+"</label></td><td><label>"+
					item.accountName+"</label></td><td><label>"+item.accountCode+"</label></td><td><label>"+item.accountBank+"</label></td><td><label>";
				}else{
					showHtml = showHtml + "<tr><td><label>"+item.userName+"</label></td><td><label>"+item.userName+"</label></td><td><label>"+
					item.accountName+"</label></td><td><label>"+item.accountCode+"</label></td><td><label>"+item.accountBank+"</label></td><td><label>";
				}          
			});  
			$("#accountInfo").html(showHtml);
			showDiv();
		},
		"json"
	);
}
</script>
</html>