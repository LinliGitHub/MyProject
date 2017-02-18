<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>秒杀详情页</title>
<%@include file="common/head.jsp"%>
<%@include file="common/tag.jsp"%>
</head>
<body>
	<div class="container">

		<div class="panel panel-default text-center">
			<div class="panel-heading">
				<h2>${seckill.name}</h2>
			</div>
			<div class="panel-body">
				<!-- time图标 -->
				<span class="glyphicon glyphicon-time"></span>
				<!-- 显示倒计时 -->
				<span class="glypyicon" id="seckill-box"></span>
			</div>
		</div>
	</div>
	<!--  登录弹出层-->
	<div id="killPhoneModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-head">
					<h3 class="modal-title text-center">
						<span class="glyphicon glyphicon-time"></span>
					</h3>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-8 col-xs-offset-2">
							<h2 class="from-signin-heading">Please sign in</h2>
							<label class="sr-only" for="usernameInput">phone/email/username</label>
							<input type="text" id="usernameInput" class="form-control" placeholder="phone / email / username" required="" autofocus="" />
							<label class="sr-only" for="inputPassword">Password</label>
							<input type="text" id="inputPassword" class="form-controll" />
							<input type="text" name="killPhone" id="killPhoneKey"
								placeholder="请输入手机号^_^" class="form-control" />
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<span id="killPhoneMessage" class="glyphicon"></span>
					<button type="button" id="killPhoneBtn" class="btn btn-success">
						<span class="glyphicon glyphicon-phone"></span> Submit
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script type="text/javascript" src="/resource/script/seckill.js"></script>
<script type="text/javascript">
	$(function() {
		//使用el表达式传入参数
		seckill.detail.init({
			seckillId : '${seckill.seckillId}',
			startTime : '${seckill.startTime.time}',
			endTime : '${seckill.endTime.time}'
		});
	});
</script>
</html>