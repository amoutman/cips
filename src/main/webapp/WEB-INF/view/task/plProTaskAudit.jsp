<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String imgPath = request.getSession().getServletContext().getRealPath("/");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="resource/css/jquery.fancybox-1.3.1.css" rel="stylesheet" type="text/css" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>代办事项</title>
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
         <h2 class="icon-db">待办事项</h2>
     </div>
     <div class="content">
       <div class="wtcon">
               <h2>订单信息</h2>
               <div class="wtbox">
                 <div class="clearFix">
                 <div class="wt-t btp0">${title3 }</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名：</label> <span>${accInfo.accountName}</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>${accInfo.accountCode}</span> </div>
                     <div class="w235"><label>开户行：</label> <span>${accInfo.accountBank}</span> </div>
                     <div class="w235"><label>${title1 }</label> <span class="color_orange font18">${payMoney}</span> </div>
                 </div>
                 </div>
               </div>
               <c:if test="${task.taskType == 5 || task.taskType == 7}">
               <h2>华创客户账户信息</h2>
               <div class="wtbox">
			   <div class="clearFix">
                 <div class="clearFix">
                    <table width="100%" class="dataTable zhtable" id="accTab">
                       <tr>
                         <th>账户类型</th>
                         <th>收款人姓名</th>
                         <th>收款人账号</th>
                         <th>开户行</th>
                       </tr>
                       <tr>
                       	 <td>国内账户</td>
                       	 <td>${hcT3.accountName }</td>
                       	 <td>${hcT3.accountCode }</td>
                       	 <td>${hcT3.accountBank }</td>
                       </tr>
                       <tr>
                       	 <td>海外账户</td>
                       	 <td>${hcT4.accountName }</td>
                       	 <td>${hcT4.accountCode }</td>
                       	 <td>${hcT4.accountBank }</td>
                       </tr>
                    </table>
                 </div>
                 </div>
			   </div>
			   </c:if>
			   <h2>${title2 }</h2>
               <div class="wtbox">
                 <div id="imgShow" class="lightBox imgShow clearFix">
                 <c:forEach var="oc" items="${ocList}">
             		<a rel="example_group" onclick="javascript:movePic()" href="uploadImgFiles/${oc.certPic}" title="Lorem ipsum dolor sit amet"><img alt="" src="uploadImgFiles/${oc.certPic}" width="100" height="100"/></a>
                 </c:forEach>
                 </div>
                 
               </div>
               <div class="btnDiv tac ck-deal"><a href="task/toPageTaskMage" class="btnGrey">返回</a>
               <a href="javascript:void(0);" class="btnOrage" onclick="taskConfirm('${task.id}');">确认</a> 
               <a onclick="javascript:showDiv();" href="javascript:void(0);" class="btnOrage btnck">驳回</a></div>
               <!--弹窗start-->
               <div class="tcDiv back_tc" style="width:260px;">
	               <span class="close"></span>
	               <h2>驳回原因</h2>
	               <div class="tcbox">
	                  <ul class="s-form" style="padding-top:20px;">
	                   <li><textarea name="textarea" id="remark" class="area" onkeyup="this.value = this.value.substring(0, 30);"></textarea> </li>
	                  </ul>
	                 <a href="javascript:void(0);" class="btnOrage" onclick="taskRejected('${task.id}')">提交</a>
	                </div>
	           	</div>
	            <!--弹窗end uploadImgFiles/${oc.certPic}-->
           </div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end

-->

<div class="bg"></div>

</body>
<script type="text/javascript" src="resource/js/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="resource/js/jquery.fancybox-1.3.1.js"></script>
<script type="text/javascript" src="resource/js/pngobject.js"></script>

<script type="text/javascript">
$(document).ready(function() {
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
	
	//$("#fancybox-wrap").dragDiv();
});
function taskConfirm(taskId){
	$.post(
			"task/plpProTaskConfirm",
			{
				"taskId":taskId
			},
			function(data){
				if (data.msg == "1") {
					//访问待办
					window.location.href = "${pageContext.request.contextPath}/task/toPageTaskMage";
				} else {
					// 失败了
					alert(data.msg);
				}
			},
			"json"	
	);
}

function taskRejected(taskId){
	var remark = $("#remark").val();
	if(remark == null || remark == ""){
		$("#remark").parent().after("<li class='color_red'>请填写驳回原因</li>");
		return;
	}
	$.post(
			"${pageContext.request.contextPath}/task/plpProTaskRejected",
			{
				"taskId":taskId,
				"remark":remark
			},
			function(data){
				if (data.msg == "1") {
					//访问待办
					window.location.href = "${pageContext.request.contextPath}/task/toPageTaskMage";
				} else {
					// 失败了
					alert(data.msg);
				}
			},
			"json"	
	);
}

function downloadCert(path){
	$.post(
			"${pageContext.request.contextPath}/task/toDownload",
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

function clickImg(obj){
	obj.width=360;
    obj.height=300;
}

function movePic(){
	$('#fancybox-wrap').dragDiv();
}
</script>
</html>
