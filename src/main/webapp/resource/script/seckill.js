/**
 * 秒杀相关操作交互 javascript 模块化
 */
var seckill = {
	// 封装秒杀相关ajax url
	// 方便维护后端请求路径
	URL : {
		// 获取服务器时间url
		now : function() {
			return '/seckill/time/now';
		},
		exposer : function(seckillId) {
			return '/seckill/' + seckillId + '/exposer';
		},
		execution : function(seckillId, md5) {
			return '/seckill/' + seckillId + '/' + md5 + '/execution';
		}
	},
	handleSeckillKill : function(seckillId, node) {
		// 获取秒杀地址，处理秒杀逻辑
		node
				.hide()
				.html(
						'<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		console.log("exposer:"+seckill.URL.exposer(seckillId));
		$.post(seckill.URL.exposer(seckillId), {}, function(result) {
			if (result && result['success']) {
				var exposer = result['data'];
				if (exposer['exposed']) {
					// 开启秒杀
					var seckillUrl=seckill.URL.execution(exposer['seckillId'],exposer['md5']);
					console.log("seckillUrl"+seckillUrl);
					//click一直绑定，one只绑定一次点击事件
					$("#killBtn").one('click',function(){
						//执行秒杀请求
						$(this).addClass("disabled");
						$.post(seckillUrl,{},function(result){
							if(result&&result['success']){
								var killResult=result['data'];
								var state=killResult['state'];
								var stateInfo=killResult['stateInfo'];
								//显示结果
								node.html('<span class="label label-success">'+stateInfo+'</span>');
							}
						});
					});
					node.show();
				} else {
					// 未开始秒杀,重新计时
					var now = exposer['now'];
					var startTime = exposer['start'];
					var endTime = exposer['end'];
					seckill.countdown(seckillId, nowTime, startTime, endTime);
				}
			} else {
				console.log("result:" + result);
			}
		});
	},
	// 验证手机号
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		}
		return false;
	},
	countdown : function(seckillId, nowTime, startTime, endTime) {
		// 时间判断
		var seckillBox = $('#seckill-box');
		if (nowTime > endTime) {
			// 秒杀结束
			seckillBox.html('秒杀结束！');
		} else if (nowTime < startTime) {
			// 秒杀未开始！，计时开始
			var killTime = new Date(Number(startTime) + 1000);
			seckillBox.countdown(killTime, function(event) {
				// 控制时间格式
				var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
				seckillBox.html(format);
			}).on('finish.countdown', function() {
				// .on操作用意是：时间完成回调事件，获取秒杀地址，控制逻辑，执行秒杀
				seckill.handleSeckillKill(seckillId, seckillBox);
			});
		} else {
			// 秒杀开始
			seckill.handleSeckillKill(seckillId, seckillBox);
		}
	},
	detail : {
		// 详情页初始化
		init : function(params) {
			// 用户手机验证和登录，倒计时交互
			// 规划交互流程
			// 在cookie查找手机号
			var killPhone = $.cookie('killPhone');
			// 验证手机号
			if (!seckill.validatePhone(killPhone)) {
				// 绑定手机号
				var killPhoneModal = $("#killPhoneModal");
				killPhoneModal.modal({
					show : true,// 显示弹出层
					backdrop : 'static',// 禁止拖动
					keyboard : false
				});
				$('#killPhoneBtn')
						.click(
								function() {
									var inputPhone = $('#killPhoneKey').val();
									if (seckill.validatePhone(inputPhone)) {
										// 手机号写入cookie
										$.cookie('killPhone', inputPhone, {
											expires : 7,
											path : '/seckill'
										});
										// 刷新页面
										window.location.reload();
									} else {
										$('#killPhoneMessage')
												.hide()
												.html(
														'<label class="label label-danger">手机号错误！</label>')
												.show(300);
									}
								});
			}
			// 已经登录
			// 计时操作
			var seckillId = params['seckillId'];
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			$.get(seckill.URL.now(), {}, function(result) {
				if (result && result['success']) {
					var nowTime = result['data'];
					// 时间判断
					seckill.countdown(seckillId, nowTime, startTime, endTime);
				} else {
					console.log("result:"+result);
				}
			});
		}
	}
}
