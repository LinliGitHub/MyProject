//处理登录login logic js 1：非空2：禁用按钮
var login = {
	validate : {
		// 表单验证
		form : function(params) {
			var json = {};
			var inputs = params.inputs;
			var $flash = $("#" + params.flash);
			json.flag = true;
			for (var i = 0; i < inputs.length; i++) {
				var erro = login.validate.blank(inputs[i]);
				if (!erro) {
					$("#" + inputs[i]).focus();
					$flash.removeClass("auth-flash-body");
					json.flag = false;
					break;
				}
			}
			return json;
		},
		// 表单选项验证
		blank : function(inputElement) {
			var $input = $("#" + inputElement);
			var value = $input.val();
			if (!value) {
				return false;
			}
			return true;
		}
	},
	init : function(params) {
		var formId = params["formId"];
		var submitBtnId = params["submitId"];
		var flash = params['flash'];
		var $submitBtn = $("#" + submitBtnId);
		// 取得要验证非空的字段
		var inputs = params['checkValid'];
		// 绑定click事件，禁用操作，基本验证函数
		var json = {
			'fromId' : formId,
			'inputs' : inputs,
			'flash' : flash
		};
		$submitBtn.click(function() {
			var result = login.validate.form(json);
			$(this).attr("disabled", true);
			$(this).val($(this).attr("data-disable-with"));
			if (result.flag) {
				$("#" + formId).submit();
			} else {
				$(this).val($(this).attr("data-enable-with"));
				$(this).attr("disabled", false);
			}
		});
	}
}