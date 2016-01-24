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
               <h2>待办信息</h2>
               <div class="wtbox">
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名：</label> <span>${accInfo.accountName}</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>${accInfo.accountCode}</span> </div>
                     <div class="w235"><label>开户行：</label> <span>${accInfo.accountBank}</span> </div>
                     <div class="w235"><label>应收金额：</label> <span class="color_orange font18">${payMoney}</span> </div>
                 </div>
               </div>
       
				<h2>华创账户信息</h2>
               <div class="wtbox mt10">
			      <div class="wt_skzh clearFix rolebox">
                   <h2 class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnBlue btnck">添加收款账户</a></h2>
                   <input type="hidden" id="rmbAccount"/>
                   <input type="hidden" id="hwAccount" />
               <!--弹窗start-->
             <div class="tcDiv zhtc">
               <span class="close"></span>
               <h2>添加收款账户</h2>
               <div class="tcbox clearFix">
                    <ul class="clearFix">
                    <form action="task/hcAddAccInfo" method="post" id="addAccForm">
                    <li>
                      <input type="radio" name="type" id="type" value="3" checked="checked"/> 国内用户　　　<input type="radio" name="type" id="type" value="4" /> 海外用户</li>
                      <input type="hidden" name="taskId" value="${task.id}"/>
                    <li>
                      <label>账户名</label>
                      <input type="text" name="accountName" id="accountName" class="input-txt"/>
                    </li>
                    <li>
                      <label>账户号</label>
                      <input type="text" name="accountCode" id="accountCode" class="input-txt" />
                    </li>
                    
                    <li>
                      <label>开户行</label>
                      <input type="text" name="accountBank" id="accountBank" class="input-txt" />
                    </li>
                    </form>
                    <li>
                     <a href="javascript:vote(0);" class="btnOrage" id="addAccBtn">确认添加</a>
                    </li>
                   </ul>
               </div>
           </div>
          <!--弹窗end-->
               </div>
			   <div class="clearFix">
                 <div class="clearFix">
                    <table width="100%" class="dataTable zhtable" id="accTab">
                       <tr>
                         <th>账户类型</th>
                         <th>收款人姓名</th>
                         <th>收款人账号</th>
                         <th>开户行</th>
                       </tr>

                    </table>
                 </div>
                 </div>
			   </div>
			   <h2>上传凭证信息</h2>
			   <div class="wtbox mt10">
                 <ul>
                    <li>
                    	<input type="hidden" id="taskId" value="${task.id }"/>
                    	<input type="file" name="uploadimg" id="uploadimg"/>
                    </li>
                 </ul>
               </div>

               <div class="btnDiv tac ck-deal"><a href="javascript:void(0)" id="returnBtn" class="btnGrey">返回</a><a href="javascript:void(0)" id="comfirmBtn" class="btnOrage">确认</a></div>
           </div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>

</body>
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
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
			window.location.href="task/toPageTaskMage";
		}
	});
	
	$("#comfirmBtn").click(function(){
		var rmbAccount = $("#rmbAccount").val();
		var hwAccount = $("#hwAccount").val();
		if(rmbAccount == ""){
			alert("请填写国内账户信息");
		}else if(hwAccount == ""){
			alert("请填写海外账户信息");
		}else{
			$("#uploadimg").uploadify("settings", "formData", {'taskId':$("#taskId").val()}); 
			$("#uploadimg").uploadify('upload','*');
		}
	})
	
	$("#returnBtn").click(function(){
		window.location.href="task/toPageTaskMage";
	})
	
	$("#addAccForm").validate({
		success:function(label){
			label.remove();
		},
		rules:{
			"accountName":{
				required:true
			},
			"accountCode":{
				required:true
			},
			"accountBank":{
				required:true
			}	
		},
		messages:{
			"accountName":{
				required:"收款人不能为空"
			},
			"accountCode":{
				required:"收款账号不能为空"
			},
			"accountBank":{
				required:"开户行不能为空"
			}
		},
		errorPlacement: function(error, element) {
			var span = $("<span class='errorInfo'></span>").append(error);
			span.appendTo(element.parent());
		}
	});
	
	$("#addAccBtn").click(function(e){
		if($("#addAccForm").valid()){
			$.post(
					"task/hcAddAccInfo",
					$("#addAccForm").serialize(),
					function(data){
						if (data.msg == "1") {
							$("#accTab").html("<tr><th>账户类型</th><th>收款人姓名</th><th>收款人账号</th><th>开户行</th></tr>");
							if(data.hcT3 != null){
								var hc3 = "<tr><td>国内账户</td><td>"+data.hcT3.accountName+"</td><td>"+data.hcT3.accountCode+"</td><td>"+data.hcT3.accountBank+"</td></tr>";
								$("#accTab").append(hc3);
								$("#rmbAccount").val("1");
							}
							if(data.hcT4 != null){
								var hc4 = "<tr><td>国外账户</td><td>"+data.hcT4.accountName+"</td><td>"+data.hcT4.accountCode+"</td><td>"+data.hcT4.accountBank+"</td></tr>";
								$("#accTab").append(hc4);
								$("#hwAccount").val("2");
							}
						} else {
							alert("账户信息提交失败");
						}
					},
					"json"	
			);
		}
	});
})

</script>
</html>