<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>JOIN US</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
<link rel="stylesheet" href="/resource/css/signup.css?v=6" />
</head>
<body class="session-authentication">
	<div class="container">
		<div class="header commentAvatarDiv">
			<a href="/index">
				<img alt="home_page" src="/resource/image/index.png" class="commentAvatarImg">
			</a>
		</div>
		<div class="row">
			<h2 class="col-md-4 col-md-offset-4">Welcome to JOIN US</h2>
		</div>
		<form role="form" id="form" action="/account/join" method="post"
			class="row">
			<div id="username"
				class="form-group  has-feedback col-md-4 col-md-offset-4">
				<label class="control-label" for="inputUsername">Username</label> <input
					type="text" class="form-control" id="inputUsername" name="username"
					placeholder="Username"> <span
					class="glyphicon form-control-feedback icon_right"></span>
				<p class="note" style="">This will be your username — you can
					use it to sign in.</p>
			</div>
			<div id="phone"
				class="form-group has-feedback col-md-4 col-md-offset-4">
				<label class="control-label" for="inputPhone">Phone</label> <input
					type="text" class="form-control" maxlength="11" id="inputPhone"
					name="phone" placeholder="Phone">
				<!-- 该提示后期加上并优化 -->
				<span class="glyphicon" style="display: none;"><label
					class="label label-info"></label></span> <span
					class="glyphicon form-control-feedback icon_right"></span>
				<p class="note" style="">This will be your phone number — you
					can use it to sign in too or change your password if you forget it.</p>
			</div>
			<div id="validCode"
				class="form-group  has-feedback col-md-4 col-md-offset-4">
				<label class="control-label" for="inputValid">validCode</label> <input
					type="text" class="form-control" id="inputValid" maxlength="6"
					placeholder="validCode"> <span
					class="glyphicon  form-control-feedback icon_right"></span>
				<p class="note" style="">This will check your valid code</p>
			</div>
			<div id="email"
				class="form-group  has-feedback col-md-4 col-md-offset-4">
				<label class="control-label" for="inputEmail">Email address</label>
				<input type="email" class="form-control" id="inputEmail"
					name="email" placeholder="Enter email"> <span
					class="glyphicon  form-control-feedback icon_right"></span>
				<p class="note" style="">This will be your email — you can use
					it to sign in too or change your password too.</p>
			</div>
			<div id="password"
				class="form-group  has-feedback col-md-4 col-md-offset-4">
				<label class="control-label" for="inputPassword">Password</label> <input
					type="password" class="form-control" id="inputPassword"
					name="password" placeholder="Password"> <span
					class="glyphicon  form-control-feedback icon_right"></span>
				<p class="note" style="">This will be your password — you can
					use it to sign in.</p>
			</div>
			<div class="form-group col-md-4 col-md-offset-4">
				<button id="submitBtn" type="button" class="btn btn-info btn-block">Create
					New Account</button>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<script type="text/javascript" src="/resource/script/join.js"></script>
<script type="text/javascript">
	$(function() {
		sign.join.init({
			form : 'form',
			button : 'submitBtn',
			//失去焦点时的绑定事件
			blur : [ {
				checkInvalidOrTaken : [ 'inputUsername', 'inputEmail' ]
			}, {
				phone : 'inputPhone'
			}, {
				identifying : [ 'inputPhone', 'inputValid' ],
			}, {
				password : 'inputPassword'
			} ]
		});
	});
</script>
</html>