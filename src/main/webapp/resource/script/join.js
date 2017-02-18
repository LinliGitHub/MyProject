//has-success--〉glyphicon-ok 用于处理正确输入或验证通过的样式显示
var sign = {
	// 维护注册功能后台交互请求url
	URL : {
		// 检查用户名或email有无被使用
		signup_check : function(username) {
			return '/account/' + username + '/signup_check';
		},
		transmission : function(phone) {
			return '/account/' + phone + '/transmission';
		},
		// 验证码验证
		verification : function() {
			return '/account/verification';
		}
	},
	// 倒计时操作
	countdown : function(target) {
		var $target = $('#' + target);
		var $showBox = $target.next();
		$showBox.show();
		var now = new Date().getTime() + 60000;
		$showBox
				.countdown(
						now,
						function(event) {
							// 禁用手机号修改,取消事件绑定
							$target.attr("readonly", true);
							$target.unbind('blur');
							$(this)
									.html(
											event
													.strftime('<label class="label label-info">%H:%M:%S 秒后重新发送验证码 </label>'));
						}).on('finish.countdown', function() {
					// 启用编辑，重新绑定事件
					$target.bind('blur', sign.checkPhone);
					$target.attr("readonly", false);
					$showBox.hide();
				});
	},
	// 检测用户名或邮箱是否正确有效
	checkInvalidOrTaken : function(event) {
		var $input = $(event.target);
		var inputElement = $input.get(0);
		var inputVal = $input.val();
		var isEmail = event.data.flag;
		var result = false;
		$input.removeClass("correct");
		if (isEmail) {
			// 验证邮箱
			result = sign.validate.email(inputVal);
		} else {
			// 简单验证户名
			result = sign.validate.rule(false, inputVal);
		}
		if (result) {
			// 通过基本验证后，再请求后台进行验证
			// 发送请求到后台验证邮箱和用户名是否存在
			$.post(sign.URL.signup_check(inputVal), {}, function(result) {
				// console.log(result);
				if (result && result['success'] && result['data'] == 0) {
					$input.addClass("correct");
				}
				sign.tips(inputElement);
			});
		} else {
			sign.tips(inputElement);
		}
	},
	// 检测手机号，并且发送验证码
	checkPhone : function(event) {
		var $input = $(event.target);
		var inputElement = $input.get(0);
		var phone = $input.val();
		$input.removeClass("correct");
		var result = false;
		result = sign.validate.phone(phone);
		if (result) {
			// 手机号通过基本验证后，再请求后台发送验证码
			$.post(sign.URL.transmission(phone), {}, function(result) {
				if (result && result['success']) {
					var data = result['data'];
					// console.log(data);
					var code = data['code'];
					if (code == 0) {
						$input.addClass("correct");
						// 发送手机验证码成功后，一分钟内不可编辑
						sign.countdown('inputPhone');
					} else {
						console.log("该手机号已被注册，请登录");
					}
					sign.tips(inputElement);
				}
			});
		}
		sign.tips(inputElement);
	},
	checkValidCode : function(event) {
		var $input = $(event.target);
		var inputElement = $input.get(0);
		var validCode = $input.val();
		var $phoneInput = $('#' + event.data.phoneInput);
		var phone = $phoneInput.val();
		$input.removeClass("correct");
		if (!$phoneInput.hasClass("correct"))
			return;
		$.post(sign.URL.verification(), {
			'phone' : phone,
			'code' : validCode
		}, function(result) {
			if (result && result['success']) {
				var flag = result['data'];
				if (flag) {
					$input.addClass("correct");
				}
				sign.tips(inputElement);
			}
		});
		sign.tips(inputElement);
	},
	checkPassword : function(event) {
		var $input = $(event.target);
		var inputElement = $input.get(0);
		var password = $input.val();
		var isPassword = event.data.isPassword;
		var result = false;
		result = sign.validate.rule(isPassword, password);
		$input.removeClass("correct");
		if (result) {
			$input.addClass("correct");
		}
		sign.tips(inputElement);
	},
	// 多种不同的验证
	validate : {
		// 表单非空验证及处理
		form : function(event) {
			// 找出该表单下所有的input
			var $form = $('#' + event.data.formId);
			$form.find("input[class='form-control']").each(
					function(index, element) {
						sign.tips(element);
					});
			// 判断是否有非空标识class样式
			var error = $('.has-error');
			if (error && error.length == 0) {
				// console.log('can submit the form');
				$form.submit();
			} else {
				console.log('stop the form submit');
			}
		},
		// 验证手机号
		phone : function(phone) {
			var reg = /^1\d{10}$/;
			if (phone && reg.test(phone)) {
				return true;
			}
			return false;
		},
		// 邮箱验证
		email : function(email) {
			var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})$/;
			if (email && reg.test(email)) {
				return true;
			}
			return false;
		},
		// 普通用户名和密码规则验证
		rule : function(isPassword, value) {
			if (isPassword) {
				// 验证密码规则-->1:长度为6~14个字符 2:支持数字,大小写字母和标点符号 3:不允许有空格
				var reg = /^\S{6,14}$/;
				return reg.test(value);
			} else {
				// 验证用户名规则1:仅支持中英文、数字和下划线,且不能为纯数字 7个汉字，
				// 判断是否有中文 .代表除\n外的单个字符
				var regN = /^[\d]+$/;
				var regY = /[a-zA-Z0-9_]*[\u4e00-\u9fa5]*[a-zA-Z0-9_]*$/;
				if (!regN.test(value) && regY.test(value)) {
					var len = 0;
					for (var i = 0; i < value.length; i++) {
						var char = value.charCodeAt(i);
						if (char > 127) {
							len += 2;
						} else {
							len++;
						}
					}
					return len <= 14 && len >= 6;
				}
				return false;
			}
		}
	},
	// 显示提示或空值警告flag为true则说明检测值是否为空，为false则直接显示
	tips : function(inputElement) {
		var $input = $(inputElement);
		// 找到父元素和第一个兄弟元素
		var $parent = $input.parent(".form-group");
		var $next = $input.next(".icon_right");
		var p_error_class = 'has-error';
		var p_success_class = 'has-success';
		var n_error_class = 'glyphicon-remove';
		var n_success_class = 'glyphicon-ok';
		var correct = $input.hasClass("correct");
		var val = $input.val();
		if (val == null || val == '') {
			$parent.removeClass(p_error_class).removeClass(p_success_class);
			$next.removeClass(n_error_class).removeClass(n_success_class);
			$parent.addClass(p_error_class);
			$next.addClass(n_error_class);
		} else if (correct) {
			$parent.removeClass(p_error_class).removeClass(p_success_class);
			$next.removeClass(n_error_class).removeClass(n_success_class);
			$parent.addClass(p_success_class);
			$next.addClass(n_success_class);
		} else {
			$parent.removeClass(p_error_class).removeClass(p_success_class);
			$next.removeClass(n_error_class).removeClass(n_success_class);
			$parent.addClass(p_error_class);
			$next.addClass(n_error_class);
		}
	},
	join : {
		init : function(params) {
			var formId = params['form'];
			var $submitBtn = $('#' + params['button']);
			$submitBtn.bind('click', {
				'formId' : formId
			}, sign.validate.form);
			// 失去焦点时触发事件绑定
			var blurArr = params.blur;
			// 数组绑定失焦事件
			for (var i = 0; i < blurArr.length; i++) {
				var blurInput = blurArr[i];
				if (blurInput['checkInvalidOrTaken']) {
					var tempArr = blurInput['checkInvalidOrTaken'];
					var flag = false;
					for (var j = 0; j < tempArr.length; j++) {
						var checkInput = tempArr[j];
						if (checkInput.indexOf('Email') > 0
								|| checkInput.indexOf('email') > 0) {
							flag = true;
						}
						// 绑定验证用户名与邮箱是否有效
						$('#' + checkInput).bind("blur", {
							'flag' : flag
						}, sign.checkInvalidOrTaken);
					}
				} else if (blurInput['phone']) {
					// 绑定手机号，发送验证码
					$('#' + blurInput['phone']).bind("blur", sign.checkPhone);
				} else if (blurInput['identifying']) {
					var identifyingArr = blurInput['identifying'];
					var phoneInput = identifyingArr[0];
					var validInput = identifyingArr[1];
					// 验证码验证
					$('#' + validInput).bind("blur", {
						'phoneInput' : phoneInput
					}, sign.checkValidCode);
				} else if (blurInput['password']) {
					// 密码简单规则验证
					$('#' + blurInput['password']).bind("blur", {
						'isPassword' : true
					}, sign.checkPassword);
				}
			}
		}
	}
}