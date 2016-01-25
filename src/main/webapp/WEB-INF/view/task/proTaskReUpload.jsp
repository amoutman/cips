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
<title>代办事项</title>
</head>

<body>
<!--header start-->
<jsp:include page="../header/headerIndex.jsp" /></jsp:include>
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
               <h2>驳回原因</h2>
               <div class="wtbox mt10">
                 <div class="clearFix">
                     <div class="w470">${task.remark}</div>
                 </div>
                 </div>
               </div>
               </c:if>
               <h2>驳回凭证信息</h2>
               <div class="wtbox mt10">
                 <div id="ImgPr" class="imgShow clearFix">
                 <c:forEach var="oc" items="${ocList}">
                 	<img id="imgShow_WU_FILE_0" src="uploadImgFiles/${oc.certPic }" width="100" height="100" />
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
<script type="text/javascript">
$(document).ready(function(){
	$("#uploadimg").uploadify({
		'uploader':'uploadConfirm;jsessionid=<%=session.getId()%>',
		'swf':'resource/uploadify/uploadify.swf',
		'cancelImg':'resource/uploadify/uploadify-cancel.png',
		'buttonText':'上传凭证',
		'removeCompleted':false,
		'auto':false,
		'fileTypeExts':'*.jpg; *.png; *.gif',
		'uploadLimit':5,
		'fileObjectName':'file',
		'mult':true,
		'onUploadSuccess':function(file,data,response){
			window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage";
		}
	});
	
	$("#comfirmBtn").click(function(){
		$("#uploadimg").uploadify("settings", "formData", {"taskId":$('#taskId').val()}); 
		$("#uploadimg").uploadify('upload','*');
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
</script>
</html>
