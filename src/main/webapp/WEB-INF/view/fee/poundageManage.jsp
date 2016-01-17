<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>费用配置</title>
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
         <h2 class="icon-fypz">费用配置</h2>
     </div>
     <div class="content">
      
        <dl class="costpz clearFix">
          <dd class="pt">
            <input type="text" name="poundageAmount" id="poundageAmount" class="input-txt"  placeholder="请输入配置比例"/>
          </dd>
          <dd class="label"><span class="color_888">汇率</span> <span class="colorGreen font24 blod">4.90%</span></dd>
          <dd> <a href="javascript:void(0);" onClick="addPoundage()" class="btnOrage">确认配置</a></dd>
        </dl>
 
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div class="bg"></div>
<script type="text/javascript">
	function addPoundage(){
		var poundageAmount = $("#poundageAmount").val();
		if(poundageAmount==""){
			alert("手续费比例不能为空！");
			return false;
		}
		//else if(poundageAmount==""){
			
		//}
		$.post(
			"fee/insertPoundage",
			{
				pRatio:poundageAmount
			}
			function(data){
				if(data['success']){
					alert("添加成功");
				}else{
					alert("添加失败");
				}
			},
			"json"
		)
		
	}
</script>
</body>
</html>