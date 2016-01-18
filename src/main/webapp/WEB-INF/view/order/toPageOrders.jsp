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
<title>订单查询</title>
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
         <h2 class="icon-ddcx">订单查询</h2>
     </div>
     <div class="content">
       <div class="search clearFix">
          <label>订单号</label><input type="text" name="textfield" id="textfield" class="inpt1" /><label>提交人</label><input type="text" name="textfield" id="textfield" class="inpt1"/><label>提交时间</label><input type="text" name="textfield" id="textfield" class="inpt1"/><label>至</label><input type="text" name="textfield" id="textfield" class="inpt1"/></div>
       <div class="btnDiv clearFix">
         <div class="w210 right"><a href="" class="btnOrage">查询</a></div>
       </div>
       <table cellpadding="0" cellspacing="0" class="dataTable">
        <thead>
          <tr>
            <th class="num"></th>
            <th>订单号</th>
            <th>兑换金额</th>
            <th>支付金额</th>
            <th>撮合进度</th>
            <th>提交人</th>
            <th>时间</th>
            <th class="w120">操作</th>
          </tr>
         </thead>
          <tbody>
          <tr>
            <td class="num">1</td>
            <td>1234567890000</td>
            <td><span class="colorBlue">$200,000,000</span></td>
            <td><span class="color_orange">$200,000,000</span></td>
            <td>
            <dl class="barbox">
              <dd class="barline">
                <div class="charts" style="width:50%;"></div>
              </dd>
              <dd>50%</dd>
            </dl></td>
            <td>豆沙包</td>
            <td>2015-01-12   AM:10:10:10</td>
            <td class="t-cz">
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)"  class="colorBlue btnck">查看</a> <a href="" class="color_888">删除</a></div>
              <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>订单信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>订  单  号：</label> <span>324356786576455</span> </div>
                     <div class="w235"><label>提  交  人：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>提交时间：</label> <span>2015-01-01</span> </div>
                     <div class="w235"><label>订单状态：</label> <span>已撮合</span> </div>
                     <div class="w235"><label>撮合进度：</label> <span>60%</span> </div>
                     <div class="w235"><label>支付金额：</label> <span class="color_orange font18">58000￥</span> </div>
                     <div class="w235"><label>申请金额：</label> <span class="color_orange font18">58000￥</span> </div>
                 </div>
                 <div class="wt-t">海外账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
                <h2>代收人信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
           </div>
           <!--弹窗end-->
            </td>
          </tr>
          <tr>
            <td class="num">2</td>
            <td>1234567890000</td>
            <td><span class="colorBlue">$200,000,000</span></td>
            <td><span class="color_orange">$200,000,000</span></td>
            <td><dl class="barbox">
              <dd class="barline">
                <div class="charts" style="width:100%;"></div>
              </dd>
              <dd>100%</dd>
            </dl></td>
            <td>豆沙包</td>
            <td>2015-01-12   AM:10:10:10</td>
            <td class="t-cz">
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)"  class="colorBlue btnck">查看</a> <a href="" class="color_888">删除</a></div>
              <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>订单信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>订  单  号：</label> <span>324356786576455</span> </div>
                     <div class="w235"><label>提  交  人：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>提交时间：</label> <span>2015-01-01</span> </div>
                     <div class="w235"><label>订单状态：</label> <span>已撮合</span> </div>
                     <div class="w235"><label>撮合进度：</label> <span>60%</span> </div>
                     <div class="w235"><label>支付金额：</label> <span class="color_orange font18">58000￥</span> </div>
                     <div class="w235"><label>申请金额：</label> <span class="color_orange font18">58000￥</span> </div>
                 </div>
                 <div class="wt-t">海外账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
                <h2>代收人信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
           </div>
           <!--弹窗end-->
            </td>
          </tr>
          <tr>
            <td class="num">3</td>
            <td>1234567890000</td>
            <td><span class="colorBlue">$200,000,000</span></td>
            <td><span class="color_orange">$200,000,000</span></td>
            <td>
            <dl class="barbox">
              <dd class="barline">
                <div class="charts" style="width:30%;"></div>
              </dd>
              <dd>30%</dd>
            </dl>
            </td>
            <td>豆沙包</td>
            <td>2015-01-12   AM:10:10:10</td>
            <td class="t-cz">
              <div class="ck-deal"><a onclick="javascript:showDiv()" href="javascript:vote(0)"  class="colorBlue btnck">查看</a> <a href="" class="color_888">删除</a></div>
              <!--弹窗start-->
             <div class="tcDiv">
               <span class="close"></span>
               <h2>订单信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>订  单  号：</label> <span>324356786576455</span> </div>
                     <div class="w235"><label>提  交  人：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>提交时间：</label> <span>2015-01-01</span> </div>
                     <div class="w235"><label>订单状态：</label> <span>已撮合</span> </div>
                     <div class="w235"><label>撮合进度：</label> <span>60%</span> </div>
                     <div class="w235"><label>支付金额：</label> <span class="color_orange font18">58000￥</span> </div>
                     <div class="w235"><label>申请金额：</label> <span class="color_orange font18">58000￥</span> </div>
                 </div>
                 <div class="wt-t">海外账户信息</div>
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
                <h2>代收人信息</h2>
               <div class="tcbox">
                 <div class="clearFix">
                     <div class="w235"><label>收款人姓名： </label> <span>doushabao</span> </div>
                     <div class="w235"><label>收款人账号：</label> <span>doushabao</span> </div>
                     <div class="w235"><label>开户行</label> <span>doushabao</span> </div>
                 </div>
               </div>
           </div>
           <!--弹窗end-->
            </td>
          </tr>
          </tbody>
       </table>
       <!--分页 start-->
       <div class="page"><a href="">&lt;</a><a href="">1</a><a href="">2</a><a href="">3</a><a href="">4</a><a href="">5</a>...<a href="">9</a><a href="">&gt;</a></div>
       <!--分页 end-->
     </div>
  </div>
  <!--右侧模块 end-->
</div>
<!--主题内容 end-->

<div id="bg" class="bg"></div>

</body>
</html>
