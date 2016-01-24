<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${pageContext.request.contextPath}/resource/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resource/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/style.js"></script>
    <!--[if lte IE 7]>
    <script type="text/javascript">
        window.location.href = 'https://www.alipay.com/x/kill-ie.htm';
    </script>
    <![endif]-->
    <!--[if lte IE 8]>
    <style>
        .slogan, .main-entry a .title i, .logo {
            background-image: none !important;
        }

        .slogan {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB1POhqIFXXXXXbXFXXXXXXXXXX.png", sizingMethod="scale");
        }

        .main-entry a .title i {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB1uh30IpXXXXXKXVXXXXXXXXXX.png", sizingMethod="scale");
        }

        .main-entry a .title .seller {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB12JNkIFXXXXXBXXXXXXXXXXXX.png", sizingMethod="scale");
        }

        .logo {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB17ghmIFXXXXXAXFXXXXXXXXXX.png", sizingMethod="scale");
        }

        .item1 {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB1h9xxIFXXXXbKXXXXXXXXXXXX.jpg", sizingMethod="scale");
        }

        .item2 {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB1pfG4IFXXXXc6XXXXXXXXXXXX.jpg", sizingMethod="scale");
        }

        .item3 {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="https://img.alicdn.com/tps/TB1sXGYIFXXXXc5XpXXXXXXXXXX.jpg", sizingMethod="scale");
        }
    </style>
    <![endif]-->
<title>登录</title>
<script type="text/javascript">
	function login(){
		var userName = $("#login-account").val();
		var password = $("#login-password").val();
		
		if(userName==""){
			alert("用户名不能为空！");
			return false;
		}else if((/[ \t\n\x0B\f\r]+/).test(userName)){
			alert("用户名不能包含空格！");
			$("#login-account").val("");
			$("#login-password").val("");
			return false;
		}else if(password==""){
			alert("密码不能为空！");
			$("#login-account").val("");
			return false;
		}else{
			$.post(
				"${pageContext.request.contextPath}/user/login",
				{
					userName:userName,
					password:password
				},
				function(data){
					if(data["success"]){
						if(data["isFirstLogin"]){
							window.location.href="${pageContext.request.contextPath}/user/toChangePassword";
						}else{
							window.location.href="${pageContext.request.contextPath}/user/toPageUserManage";
						}
					}else{
						alert("登录失败");
					}
				},
				"json"
			);
		}
	}
</script>
</head>
<body>
<div class="main">
    <div class="header">
        <div class="nav">
            <div class="logo"><img src="${pageContext.request.contextPath}/resource/images/logo.gif" width="190" height="65" /></div>
        </div>
    </div>
    <div class="container">
        <div class="content">
            <div class="wrap">
               <div class="wrapbox">
                <div class="exchangeItem">
                  <h2><img src="${pageContext.request.contextPath}/resource/images/login-txt.png" width="338" height="109" /></h2>
                  <p>Low Cost Low fees displayed up-front.FastFastTransfer to many countries.TrustedTrustedIndustry-leading payment security.</p>
                  <div class="todayEx">
                    <span class="exnum">今日汇率：${currentRate }</span>
                    <span class="exbg"></span>
                  </div>
              </div>
                <div class="loginForm">
                   <h2>登录 <span>LOGIN</span></h2>
                   <div class="logoin-com login-account"><input type="text" value=""  placeholder="请输入账号" class="text-input" id="login-account"></div>
                   <div class="logoin-com login-password"><input type="password" value=""  placeholder="请输入密码" class="text-input" id="login-password"></div>
                   <div class="logoin-com clearFix">
                    <span class="left"><input type="checkbox" name="checkbox" id="checkbox" /></span><span class="left">记住账号</span>
                    <span class="right"><a href="">忘记密码？</a></span>
                    </div>
                   <div class="logoin-com"><a href="javascript:void(0);" onClick="login()" class="logbutton btnOrage">登  录</a></div>
             </div>
             </div>
            </div>
        </div>
        <div class="back">
            <div class="items">
                <div class="item item1" style="background-image:url(${pageContext.request.contextPath}/resource/images/loginBg.jpg)"></div>
            </div>
        </div>
    </div>
    
</div>
</body>
</html>