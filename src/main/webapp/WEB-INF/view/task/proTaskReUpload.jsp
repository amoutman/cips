<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="resource/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<link href="resource/css/jquery.fancybox-1.3.1.css" rel="stylesheet" type="text/css" />
<title>代办事项</title>
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
         <h2 class="icon-db">待办事项</h2>
     </div>
     <div class="content">
       <div class="wtcon">
               <h2>订单信息</h2>
               <div class="wtbox">
                 <div class="clearFix">
                 <div class="wt-t btp0">收款人账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名：</label> <span>${accInfo.accountName}</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>${accInfo.accountCode}</span> </div>
                     <div class="w235"><label>开户行：</label> <span>${accInfo.accountBank}</span> </div>
                     <div class="w235"><label>${title1 }</label> <span class="color_orange font18">${payMoney}</span> </div>
                 </div>
                 </div>
               </div>
               <c:if test="${task.remark != null}">
               <div class="wtbox mt10">
                 <div class="clearFix">
                 <div class="wt-t btp0">驳回原因</div>
                 <div class="clearFix">
                     <div class="w470">${task.remark}</div>
                 </div>
                 </div>
               </div>
               </c:if>
               <h2>驳回凭证信息</h2>
               <div class="wtbox mt10">
                 <div id="ImgShow" class="imgShow clearFix">
                 <c:forEach var="oc" items="${ocList}">
                 	<a rel="example_group" href="uploadImgFiles/${oc.certPic}" title="Lorem ipsum dolor sit amet"><img alt="" src="uploadImgFiles/${oc.certPic}" width="100" height="100"/></a>
                 </c:forEach>
                 </div>
               </div>
               <h2>${title2 }</h2>
               <div class="wtbox mt10">
                 <ul>
                    <li>
                    	<input type="hidden" id="taskId" value="${task.id }"/>
                    	<input type="file" name="uploadimg" id="uploadimg"/>
                    	<!--a href="javascript:void(0);"  class="btnUpload">上传</a>  -->
                    </li>
                 </ul>
                 <div id="ImgPr" class="imgShow clearFix">
               </div>
               <div class="btnDiv tac"><a href="javascript:void(0)" id="returnBtn" class="btnGrey">返回</a><a href="javascript:void(0)" id="comfirmBtn" class="btnOrage">确认</a></div>
           </div>
     </div>
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
		'uploader':'uploadImg;jsessionid=<%=session.getId()%>',
		'swf':'resource/uploadify/uploadify.swf',
		'cancelImg':'resource/uploadify/uploadify-cancel.png',
		'buttonText':'上传凭证',
		'removeCompleted':true,
		'auto':true,
		'fileTypeExts':'*.jpg; *.png; *.gif',
		'uploadLimit':5,
		'fileObjectName':'file',
		'mult':true,
		'onUploadStart':function(){
			$("#uploadimg").uploadify("settings", "formData", {"taskId":$('#taskId').val()}); 
		},
		'onUploadSuccess':function(file,data,response){
			var	img = "<a id='showPicA' rel='example_group' href='uploadImgFiles/"+data+"' title='Lorem ipsum dolor sit amet'><img alt='' src='uploadImgFiles/"+data+"' width='100' height='100'/></a>";
			var imgPr = $("#ImgPr").html();
			$("#ImgPr").html(imgPr + img);
			window.onload = showBigPic();
		}
	});
	
	$("#comfirmBtn").click(function(){
		var imgCount = 0;
		$("#ImgPr").find("#showPicA").each(function(){
			imgCount = imgCount + 1;
		})
		if(imgCount==0){
			alert("请上传凭证");
		}else{
			$.post(
			"${pageContext.request.contextPath}/task/uploadConfirm",
			{
				taskId:$('#taskId').val()
			},
			function(data){
				if(data.msg == "1"){
					alert("任务成功完成");
					window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage"
				}else{
					alert(data.msg);
					window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage"
				}
			},
			"json"
			)
		}
		
	})
	
	$("#returnBtn").click(function(){
		window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage";
	})

});
function downloadCert(path){
	$.post(
			"task/toDownLoad",
			{
				fileName:path
			},
			function(data){
				if (data.msg != "1") {
					// 失败了
					alert(data.msg);
				}
			},
			"json"	
	);
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
