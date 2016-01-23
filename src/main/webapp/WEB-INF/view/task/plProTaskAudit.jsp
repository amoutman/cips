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
                 <div class="wt-t btp0">收款人账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名：</label> <span>${accInfo.accountName}</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>${accInfo.accountCode}</span> </div>
                     <div class="w235"><label>开户行：</label> <span>${accInfo.accountBank}</span> </div>
                     <div class="w235"><label>应付金额：</label> <span class="color_orange font18">${payMoney}</span> </div>
                 </div>
                 </div>
               </div>
               <c:if test="task.taskType == 5 || task.taskType == 7">
               <h2>华创账户信息</h2>
               <div class="wtbox mt10">
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
               <div class="wtbox mt10">
                 <div id="ImgPr" class="imgShow clearFix">
                 <c:forEach var="oc" items="${ocList}">
                 	<a href="javascript:void(0)" onClick="downloadCert(${oc.CertPic })"><img id="imgShow_WU_FILE_0" src="uploadImgFiles/${oc.CertPic }" width="100" height="100" /></a>
                 </c:forEach>
                 </div>
               </div>
               <div class="btnDiv tac"><a href="task/toPageTaskMage" class="btnGrey" onclick="">返回</a>
               <a href="javascript:vote(0)" class="btnOrage" onclick="taskConfirm('${task.id}');">确认</a> 
               <a onclick="javascript:showDiv()" href="javascript:vote(0)" class="btnOrage btnck">驳回</a></div>
               <!--弹窗start-->
               <div class="tcDiv back_tc" style="width:260px;">
	               <span class="close"></span>
	               <h2>驳回原因</h2>
	               <div class="tcbox">
	                  <ul class="s-form" style="padding-top:20px;">
	                   <li><textarea name="textarea" id="remark" class="area" onkeyup="this.value = this.value.substring(0, 30)"></textarea> </li>
	                  </ul>
	                 <a href="javascript:void(0)" class="btnOrage" onclick="taskRejected('${task.id}')">提交</a>
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
<script type="text/javascript">
function taskConfirm(taskId){
	$.post(
			"task/plcProTaskConfirm",
			{
				"taskId":taskId
			},
			function(data){
				if (data.msg == "1") {
					//访问待办
					window.location.href = "task/toPageTaskMage";
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
	
	$.post(
			"task/plcProTaskRejected",
			{
				"taskId":taskId,
				"remark":remark
			},
			function(data){
				if (data.msg == "1") {
					//访问待办
					window.location.href = "task/toPageTaskMage";
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
