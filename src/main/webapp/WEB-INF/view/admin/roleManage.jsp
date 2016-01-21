<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String sessionid = session.getId();
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="resource/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<title>角色管理</title>
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
     <!-- 上传图片测试 -->
     <div class="wtbox mt10">
     <input type="file" name="uploadimg" id="uploadimg"/>
                 <ul>
                    <li>
                    	
                    	<!--a href="javascript:void(0);"  class="btnUpload">上传</a>  -->
                    	<a href="javascript:$('#uploadimg').uploadify('upload')" class="btnUpload">上传</a>&nbsp;&nbsp;
                    	<a href="javascript:$('#uploadimg').uploadify('cancel')" class="btnUpload">取消</a>&nbsp;&nbsp;
                    	<a href="javascript:$('#uploadimg').uploadify('upload','*')" class="btnUpload">上传所有文件</a>&nbsp;&nbsp;
                    	<a href="javascript:$('#uploadimg').uploadify('cancel','*')" class="btnUpload">取消上传所有文件</a>
                    </li>
                 </ul>
                 <div id="ImgPr" class="imgShow clearFix">
                 </div>
               </div>
               <!-- 上传图片测试 -->
  </div>
  
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>

</body>
<script type="text/javascript" src="resource/uploadify/jquery.uploadify.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#uploadimg").uploadify({
		'uploader':'uploadUserImg;jsessionid=<%=session.getId()%>',
		'swf':'resource/uploadify/uploadify.swf',
		'cancelImg':'resource/uploadify/uploadify-cancel.png',
		'buttonText':'上传凭证',
		'removeCompleted':false,
		'auto':false,
		'fileTypeExts':'*.jpg; *.png; *.gif',
		'uploadLimit':5,
		'fileObjectName':'file',
		'mult':true,
		'onUploadSeccess':function(file,data,response){
			alert(file.name + "上传成功");
		}
	});

});

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
</script>
</html>