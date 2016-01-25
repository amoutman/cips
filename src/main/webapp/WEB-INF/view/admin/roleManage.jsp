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
<link href="resource/css/jquery.fancybox-1.3.1.css" rel="stylesheet" type="text/css" />
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
      <form action="${pageContext.request.contextPath}/user/toPageRoleManage" method="post" id="searchRoleForm">
      <input type="hidden" name="currentPage" id="currentPage"/>
      </form>
      	<c:forEach var="role" items="${roleList }">
         <li>
           <div class="role-img"><img src="resource/images/role-tx1.gif" width="51" height="51" /></div>
           <div class="role-name">角色名称：${role.roleName }</div>
           <div class="role-date">创建时间：<fmt:formatDate value="${role.createdDate }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></div>
           <div class="role-deal">
              <div class="ck-deal"><a href="javascript:vote(0)" id="btnDelete" class="btnOrage btnck">修改</a> <a onclick="deleteRole(${role.id })" href="javascript:void(0)" class="btnGrey">删除</a></div>
             <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>修改角色</h2>
               <div class="tcbox">
               		<input type="hidden" name="roleId" id="roleId" value="${role.id }">
                    <input type="text" name="roleName" id="roleName" class="input-txt"  placeholder="角色名称" value="${role.roleName }"/> <a href="javascript:void(0)" onclick="updateRole(this)" id="btnUpdate" class="btnOrage">确认修改</a>
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
     <!-- 上传图片测试
     <div class="wtbox mt10">-->
     <input type="file" name="uploadimg" id="uploadimg"/>
                 <ul>
                    <li>
                    	<input type="hidden" id="taskId" value="1001001001"/>
                    	<!-- <a href="javascript:void(0);" onClick="upload()"  class="btnUpload">上传图片</a>  
                    	<a href="javascript:$('#uploadimg').uploadify('upload','*')" class="btnUpload">上传所有文件</a>&nbsp;&nbsp;
                    	<a href="javascript:$('#uploadimg').uploadify('cancel','*')" class="btnUpload">取消上传所有文件</a> -->
                    	
                    </li>
                 </ul>
                 <div id="imgShow" class="lightBox imgShow clearFix">
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
<script type="text/javascript" src="resource/js/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="resource/js/jquery.fancybox-1.3.1.js"></script>
<script type="text/javascript" src="resource/js/pngobject.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	showBigPic();
	
	$("#uploadimg").uploadify({
		'uploader':'uploadUserImg;jsessionid=<%=session.getId()%>',
		'swf':'resource/uploadify/uploadify.swf',
		'cancelImg':'resource/uploadify/uploadify-cancel.png',
		'buttonText':'上传凭证',
		'removeCompleted':true,
		'auto':true,
		'fileTypeExts':'*.jpg; *.png; *.gif',
		'uploadLimit':5,
		'fileObjectName':'file',
		'mult':true,
		'onUploadSuccess':function(file,data,response){
			//var dataArray = data.split(",");
			//var img = "";
			//for(var i=0;i<dataArray.length;i++){
				//if(img == ""){
			
			var	img = "<a rel='example_group' href='uploadImgFiles/"+data+"' title='Lorem ipsum dolor sit amet'><img alt='' src='uploadImgFiles/"+data+"' width='100' height='100'/></a>";
					
				//}
				//img = img + "<a rel='example_group' href='uploadImgFiles/"+data+"' title=''><img alt='' src='uploadImgFiles/"+data+"' width='100' height='100'/></a>";
			//}
			//var imgShow = $("#imgShow").html();
			$("#imgShow").html("<span id='picli'><input type='hidden' id='filePath' value='"+data+"'><a rel='example_group' href='uploadImgFiles/"+data+"' title='Lorem ipsum dolor sit amet'>"+
			"<img alt='' src='uploadImgFiles/"+data+"' width='100' height='100'/></a><br><a href='javascript:void(0)' onclick='deletePic(this)'>删除该凭证</a></span>");
			window.onload = showBigPic();
		}
	});
	


});

function deletePic(obj){
	var filePath = $(obj).parents("#picli").find("#filePath").val();
	$.post(
			"user/deletePic",
			{
				filePath:filePath
			},
			function(data){
				if(data.success){
					alert("删除成功");
					$(obj).parents("#picli").html("");
				}else{
					alert("删除失败");
				}
			},
			"json"
	);
}

function upload(){
	$("#uploadimg").uploadify("settings", "formData", {"taskId":$('#taskId').val()}); 
	$("#uploadimg").uploadify('upload','*');
}

function addRole(){
	
	var roleName = $("#newRoleName").val();
	if(roleName == ""){
		alert("角色名称不能为空");
	}else{
		$.post(
				"user/insertRole",
				{
					roleName:roleName
				},
				function(data){
					if(data['success']){
						alert("添加成功");
						window.location.href="${pageContext.request.contextPath}/user/toPageRoleManage";
					}else{
						alert("添加失败");
					}
				},
				"json"
		);
	}
	
}

function updateRole(obj){
	var roleId = $(obj).parents(".tcbox").find("#roleId").val();
	var roleName = $(obj).parents(".tcbox").find("#roleName").val();

	if(roleName == ""){
		alert("角色名称不能为空");
	}else{
		$.post(
				"user/updateRole",
				{
					roleId:roleId,
					roleName:roleName
				},
				function(data){
					if(data['success']){
						alert("修改成功");
						window.location.href="${pageContext.request.contextPath}/user/toPageRoleManage";
					}else{
						alert("修改失败");
					}
				},
				"json"
		);
	}
}

function deleteRole(id){
	if(confirm("是否删除该角色")){
		$.post(
				"user/deleteRole",
				{
					roleId:id
				},
				function(data){
					if(data['success']){
						alert("删除成功");
						window.location.href="${pageContext.request.contextPath}/user/toPageRoleManage";
					}else{
						alert("删除失败");
					}
				},
				"json"
		);
	}
}

function pageClick(currentPage){
	$("#currentPage").val(currentPage);
	$("#searchRoleForm").submit();
}

function showBigPic(){
	/*
	*   Examples - images
	*/

	$("a#example1").fancybox({
		'titleShow'		: false
	});

	$("a#example2").fancybox({
		'titleShow'		: false,
		'transitionIn'	: 'elastic',
		'transitionOut'	: 'elastic'
	});

	$("a#example3").fancybox({
		'titleShow'		: false,
		'transitionIn'	: 'none',
		'transitionOut'	: 'none'
	});

	$("a#example4").fancybox();

	$("a#example5").fancybox({
		'titlePosition'	: 'inside'
	});

	$("a#example6").fancybox({
		'titlePosition'	: 'over'
	});

	$("a[rel=example_group]").fancybox({
		'transitionIn'		: 'none',
		'transitionOut'		: 'none',
		'titlePosition' 	: 'over',
		'titleFormat'		: function(title, currentArray, currentIndex, currentOpts) {
			return '<span id="fancybox-title-over">Image ' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
		}
	});

	/*
	*   Examples - various
	*/

	$("#various1").fancybox({
		'titlePosition'		: 'inside',
		'transitionIn'		: 'none',
		'transitionOut'		: 'none'
	});

	$("#various2").fancybox();

	$("#various3").fancybox({
		'width'				: '75%',
		'height'			: '75%',
		'autoScale'			: false,
		'transitionIn'		: 'none',
		'transitionOut'		: 'none',
		'type'				: 'iframe'
	});

	$("#various4").fancybox({
		'padding'			: 0,
		'autoScale'			: false,
		'transitionIn'		: 'none',
		'transitionOut'		: 'none'
	});
}


</script>
</html>