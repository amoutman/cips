<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单申请</title>
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
          <span class="avatar-img"><img src="${pageContext.request.contextPath}/resource/images/head.gif" width="43" height="43" /></span>
        </span>
        <span class="word">豆沙包欢迎您！</span>
     </div>
  </div>
</div>
<!--header end-->

<!--主题内容 start-->
<div class="w1200">
<jsp:include page="../header/header.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery.validate.js"></script>
  <!--右侧模块 start-->
  <div class="part-right">
     <div class="r-tit">
         <h2 class="icon-ddsq">新增申请</h2>
     </div>
     <div class="content">
       <ul class="s-form">
       		<form action="${pageContext.request.contextPath}/order/createOrder" id="orderForm" method="post">
			     <input type="hidden" name="rate" id="rate" value="${rate.rateHigh}" />
			     <input type="hidden" name="poundage" id="poundage" value="${poundage.poundageAmount}" />
	    
                 <li><label>申请金额：</label><input type="text" class="input-txt" id="applyAmount" name="applyAmount" value="" ><span class="left hbf">美元</span>
                 </li>
                 <li><label>应付金额：</label><input type="text" class="input-txt" id="payAmount" name="payAmount" value="" readonly="readonly"> <span class="left hbf">元</span>
                 </li>
                 <li><label>应付手续费：</label><input type="text" class="input-txt" id="poundageAmount" name="poundageAmount" value="" readonly="readonly"><span class="left hbf">元</span>
                 </li>
                 <li><label>收款人姓名：</label><input type="text" class="input-txt" id="accountName" name="accountName" value="">
                 </li>
                 <li><label>收款人账号：</label><input type="text" class="input-txt" id="accountCode" name="accountCode" value="">
                 </li>
                 <li><label>开户行：</label><input type="text" class="input-txt" id="accountBank" name="accountBank" value="">
                 </li>
            </form>
       </ul>
       <div class="btnsubmint"><a href="javascript:void(0)" id="insertBtn" class="btnOrage">提  交</a></div>
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->
</body>
<script type="text/javascript">
$(document).ready(function() {
	$("#orderForm").validate({
		success:function(label){
			label.remove();
		},
		rules:{
			"applyAmount":{
				required:true
			},
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
			"applyAmount":{
				required:"申请金额不能为空"
			},
			"accountName":{
				required:"收款人不能为空"
			},
			"accountCode":{
				required:"收款账号不能为空"
			},
			"accountBank":{
				required:"开会行不能为空"
			}
		},
		errorPlacement: function(error, element) {
			var span = $("<span class='errorInfo'></span>").append(error);
			span.appendTo(element.parent());
		}
	});
	
	$("#insertBtn").click(function(e){
		$("#insertBtn")
		if($('#orderForm').valid()){
			$('#orderForm').submit();
		}
	});
	
	$("#applyAmount").blur( function () {
		var applyAmount = $("#applyAmount").val();
		var rate = $("#rate").val();
		var poundage = $("#poundage").val();
		var payAmount = applyAmount * rate;
		$("#payAmount").val(payAmount); 
		var poundageAmount = payAmount * poundage;
		$("#poundageAmount").val(poundageAmount); 
	});
})
</script>
</html>
