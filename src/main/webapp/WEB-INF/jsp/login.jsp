<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>用户登录页</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
<link rel="stylesheet" href="/resource/css/login.css?v=1.0" />
</head>
<body class="session-authentication">
	<div class="container">
		<div class="header commentAvatarDiv">
			<a href="/index">
				<img alt="home_page" src="/resource/image/index.png" class="commentAvatarImg">
			</a>
		</div>
		<div class="auth-form px-3">
			<form action="/account/session" method="post" accept-charset="UTF-8" id="form">
				<div class="auth-form-header px-0">
					<h1>Please Sign in</h1>
				</div>
				
				<div id="flash" class="<c:if test="${empty msg}">auth-flash-body</c:if> mt-3">
					<div class="alert alert-danger alert-dismissible" role="alert">
  							<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
 							<span>Incorrect username or password.</span>
					</div>
				</div>
				<div class="auth-form-body mt-3">
					<label for="login_field">Username or email address</label>
					<input type="text" autocapitalize="off" autocorrect="off" autofocus="autofocus" class="form-control input-block" name="login" id="login_field" tabindex="0"/>
					<label for="password">
						Password
						<a href="/account/password_reset<c:if test='${not empty return_to}'>?return_to=${return_to}</c:if>" class="label-link">Forget the password?</a>
					</label>
					<input type="password" class="form-control input-block" id="password" name="password" tabindex="1" />
					<input type="hidden" name="return_to" value="${return_to}" />
					<input type="button" class="btn btn-primary btn-block" name="commit" id="submitBtn" data-disable-with="Signing in..." data-enable-with="Sign in" value="Sign in" tabindex="2">
				</div>
			</form>
			<p class="create-account-callout mt-3">
				New Accounter? 
				<a href="/account/join<c:if test='${not empty return_to}'>?return_to=${return_to}</c:if>" data-ga-click="Sign in, switch to sign up">Create an account</a>
			</p>
		</div>
	</div>
</body>
<script type="text/javascript" src="/resource/script/login.js?v=1"></script>
<script type="text/javascript">
	$(function() {
		login.init({
			'formId' : 'form',
			'submitId' : 'submitBtn',
			'flash' : 'flash',
			'checkValid' : [ 'login_field', 'password' ]
		});
	});
</script>
</html>