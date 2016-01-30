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
<title>待办事项</title>
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
                     <div class="w235"><label>订  单  号：</label> <span>${order.orderNo}</span> </div>
                     <div class="w235"><label>订单状态：</label> <span>${order.statusDesc}</span> </div>
                     <div class="w235"><label>提  交  人：</label> <span>${user.userName}</span> </div>
                     <div class="w235"><label>提交时间：</label> <span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${order.applyDate}" type="both"/></span> </div>
					 <div class="w235"><label>申请金额：</label> <span class="color_orange font18">${order.applyAmount}$</span> </div>
                     <div class="w235"><label>应付金额：</label> <span class="color_orange font18">${order.payAmount}￥</span> </div>
                 </div>
                 <div class="clearFix">
                 <div class="wt-t">申请人海外账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>${hwAcc.accountName}</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>${hwAcc.accountCode}</span> </div>
                     <div class="w235"><label>开户行：</label> <span>${hwAcc.accountBank}</span> </div>
                 </div>
                 </div>
               </div>
             <h2>华创客户申请金额</h2>
               <div class="wtbox">
                 <div class="clearFix">
					 <div class="w235"><label>华创客户申请金额：</label> <span class="color_orange font18" id="matchAmount">${order.matchAmount}$</span> </div>
                 </div>
               </div>
			<h2>海外用户人民币账户信息</h2>
               <div class="wtbox">
 			   <div class="wt_skzh clearFix">
                   <h2 class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:void(0)" class="btnBlue btnck">选择收款账户信息</a></h2>
	                <!--弹窗start-->
	               <div class="tcDiv xzzh_tc">
	               <span class="close"></span>
	               <h2>选择收款账户信息</h2>
	               <div class="tcbox">
	                  <div class="searchBox clearFix">
	                   <div class="searchbar">
	                   <form id="searchAccountForm">
	                   	      <input type="text" name="userName" id="userName" class="input-txt"  placeholder="请输入用户名"/> 
	                          <input type="text" name="accountCode" id="accountCode" class="input-txt"  placeholder="请输入账户号"/>
	                          <input type="text" name="creditId" id="creditId" class="input-txt"  placeholder="请输入身份证号"/>
	                   </form>
	                   <a href="javascript:void(0)" onClick="getAccountInfo()" class="btnSearch">search</a>
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
	               </div>
	           </div>
	             <!--弹窗end-->
	                 <div class="clearFix">
	                 	 <input type="hidden" name="taskId" id="taskId" value="${task.id }"/>
	                     <div class="w235"><label>收款人姓名： </label> <span id="accountNameSpan">${hwUserAcc.accountName}</span><input type="hidden" name="accountId" id="accountId" /> </div>
	                     <div class="w235"><label>收款人账号：</label> <span id="accountCodeSpan">${hwUserAcc.accountCode}</span> </div>
	                     <div class="w235"><label>开户行：</label> <span id="accountBankSpan">${hwUserAcc.accountBank}</span> </div>
	                 </div>
	             <c:if test="${task.remark != null}">
	                 <div class="clearFix">
	                 <div class="wt-t">驳回原因</div>
	                 <div class="clearFix">
	                     <div class="w470">${task.remark}</div>
	                 </div>
	                 </div>
	             </c:if>
               </div>
               </div>
               <div class="btnDiv tac"><a href="javascript:void(0)" class="btnGrey">返回</a><a href="javascript:void(0)" class="btnOrage" onClick="confirmOrder()" >确认</a></div>
           </div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->
<div class="bg" style="display:none;"></div>
<!--弹窗start-->
           <div class="tcDiv waitDiv" style="display:none;">
               <h2>待办事项</h2>
               <div class="tcbox">
                   <div class="clearFix">
                     <div class="w470"><label>订  单  号：</label> <span>${order.orderNo}</span> </div>
                     <div class="w470"><label>订单申请金额：</label> <span class="color_orange font18">${order.orderNo}￥</span> </div>
                     <div class="w470"><label>华创申请金额：</label> <input type="text" name="textfield2" id="textfield2" class="input-txt" />
                        <P class="log-txt"><span class="color_red font12">(备注：您填写的金额必须大于订单申请金额.)</span></P>
                     </div>
                     <div class="w470"><label>应付人民币：</label> <span class="color_orange font18">${order.orderNo}￥</span> </div>
                     
                 </div>
                 <div class="btnDiv tac"><a class="btnGrey" href="task/toPageTaskMage">返回</a><a class="btnOrage" href="javascript:void(0)">确认</a></div>
               </div>
               
           </div>
<!--弹窗end-->

</body>
<script type="text/javascript">
$(document).ready(function () {  
	var matchAmount = $("#matchAmount").text();
	if(matchAmount == null || matchAmount == ""){
		$(".bg").show();
		$(".tcDiv waitDiv").show();
	}
});

function getAccountInfo(){
	var userName = $("#searchAccountForm").find("#userName").val();
	var accountCode = $("#searchAccountForm").find("#accountCode").val();
	var creditId = $("#searchAccountForm").find("#creditId").val();
	
	if(userName == "" && accountCode == "" && creditId == ""){
		alert("请输入任一条件进行精确查找");
		return false;
	}
	
	$("#accountInfo").html("");
	var data = $("#searchAccountForm").serializeArray();
	$.post(
		"${pageContext.request.contextPath}/accountFr/toPageAccountFrMap",
        data,
		function(data){
			//var jsonObj=eval("("+data+")");
			var showHtml = "";
			//var selectBtn = "<a href='javascripr:void(0)' onClick='selectAccount(this)' />"
			$.each(data.afList, function (i, item) {  
				if(showHtml == ""){
					showHtml = "<tr><td><label>"+item.userName+"</label><input type='hidden' id='userIdl' value='"+item.userId+"''/></td><td><label id='accountNamel'>"
												+item.accountName+"</label><input type='hidden' id='idl' value='"+item.id+"''/></td><td><label id='accountCodel'>"
												+item.accountCode+"</label></td><td><label id='accountBankl'>"
												+item.accountBank+"</label></td><td><a href='javascript:void(0)' onClick='selectAccount(this)'>选择</a></td></tr>";
				}else{
					showHtml = showHtml + 
					"<tr><td><label>"+item.userName+"<input type='hidden' id='userIdl' value='"+item.userId+"''/></label></td><td><label id='accountNamel'>"
					+item.accountName+"<input type='hidden' id='idl' value='"+item.id+"''/></label></td><td><label id='accountCodel'>"
					+item.accountCode+"</label></td><td><label id='accountBankl'>"
					+item.accountBank+"</label></td><td><a href='javascript:void(0)' onClick='selectAccount(this)'>选择</a></td></tr>";
				}          
			});  
			$("#accountInfo").html(showHtml);
		},
		"json"
	);
}

function selectAccount(obj){
	var userId = $(obj).parent().parent().find("#userIdl").val();
	var accountId = $(obj).parent().parent().find("#idl").val();
	var accountName = $(obj).parent().parent().find("#accountNamel").html();
	var accountCode = $(obj).parent().parent().find("#accountCodel").html();
	var accountBank = $(obj).parent().parent().find("#accountBankl").html();
	
	$("#accountNameSpan").html(accountName);
	$("#accountCodeSpan").html(accountCode);
	$("#accountBankSpan").html(accountBank);
	$("#accountId").val(accountId);
	
	$(".tcDiv").fadeOut(300);
	$('div.bg').fadeOut(200);
}

function confirmOrder(){
	var accountId = $("#accountId").val();
	var taskId = $("#taskId").val();
	if(accountId == ""){
		alert("请选择海外用户账户");
		return false;
	}else{
		$.post(
			"task/plpProTaskT1",
			{
				taskId:taskId,
				accountId:accountId
			},
			function(data){
				if(data.msg == "1"){
					alert("提交成功");
					window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage";
				}else{
					alert(data.msg);
					window.location.href="${pageContext.request.contextPath}/task/toPageTaskMage";
				}
			},
			"json"
			
		)
	}
}
</script>
</html>
