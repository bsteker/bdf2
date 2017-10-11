<%@page import="com.bstek.dorado.core.Configure"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
</head>
<body>
<h1>
<%
String loginPage=request.getContextPath()+Configure.getString("bdf2.formLoginUrl");
%>
欢迎使用BDF2.
</h1><br>
<h2>
1.点击<a href="<%=loginPage %>" target="_blank">此处登录</a>
</h2>
<h2>
2.点击<a href="<%=request.getContextPath()+"/bdf2.core.view.register.RegisterWizard.d" %>" target="_blank">此处注册一个新公司的系统管理员账号</a>
</h2>
<h2>
3.点击<a href="<%=request.getContextPath()+"/generate.system.menu"+Configure.getString("bdf2.controllerSuffix") %>" target="_blank">此处初始化系统菜单(<font color='red'>需要登录</font>)</a>
</h2>
</body>
</html>