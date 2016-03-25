[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("shop.register.title")}[#if showPowered] - Powered By JSHOP[/#if]</title>
<meta name="author" content="JSHOP Team" />
<meta name="copyright" content="JSHOP" />

<link rel="stylesheet" type="text/css" href="${base}/resources/shop/${theme}/css_mdh/main.css" />
<script type="text/javascript" src="${base}/resources/shop/${theme}/js_mdh/third/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js_mdh/third/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js_mdh/main/views/base.js"></script>

<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/shop/${theme}/datePicker/WdatePicker.js"></script>
[#--
<script type="text/javascript" src="${base}/resources/shop/${theme}/js_mdh/main/views/register.js"></script>
<script type="text/javascript">
$().ready(function() {
	alert("123");
	var $registerForm_mdh = $("#registerForm_mdh");
	var $phone = $("#phone");

	var $getPhoneCaptcha=$("#getPhoneCaptcha");
	var $submit = $("input:submit");
	$getPhoneCaptcha.click(function() {
		alert($phone.val());
		
		if($phone.val()!=""){
			$.ajax({
				url: "${base}/register/phone_captcha.jhtml",
				data:{"phone":$phone.val()},
				type: "GET",
				dataType: "text",
				cache: true,
				success: function(data) {
					alert(data);
					$("#compareCode").val(data);
					$("#compareCode2").val(${phonecaptcha});
				},
				error: function(data) {
    				alert("error");
    			}
			});
		}
	});

	// 表单验证
	$registerForm_mdh.validate({
		rules: {
			phone: {
				required: true,
				pattern: /^[1][358][0-9]{9}$/,
				remote: {
					url: "${base}/register/check_phone.jhtml",
					cache: false
				}
			},
			password: {
				required: true,
				minlength: ${setting.passwordMinLength}
			},
			rePassword: {
				required: true,
				equalTo: "#password"
			},
			captcha: {
				required: true,
				minlength: ${setting.passwordMinLength},
				pattern: /^[0-9][0-9][0-9][0-9]$/,
				equalTo: "#compareCode",
			}
		},
		messages: {
			phone: {
				pattern: "${message("shop.register.phoneIllegal")}",
				remote: "${message("shop.register.phoneExist")}"
			},
			captcha: {
				pattern: "${message("shop.captcha.invalid")}",
				remote: "${message("shop.captcha.invalid")}"
			},
		}//,submitHandler: function(form) {}
	});
});
</script>
--]
<script>
/**
 * 注册验证和提交表单
 * version 0.0.1
 */

;$(function () {

  // 验证
if ($.validator != null) {
    
    $.validator.setDefaults({
      errorClass: "fieldError",
      ignore: ".ignore",
      ignoreTitle: true,
      errorPlacement: function(error, element) {
        var fieldSet = element.closest("span.fieldSet");
        if (fieldSet.size() > 0) {
          error.appendTo(fieldSet);
        } else {
          error.insertAfter(element);
        }
      },
      submitHandler: function(form) {
        $(form).find("input:submit").prop("disabled", true);
        form.submit();
      }
    });

  }

  var $registerForm = $("#registerForm");
  var $phone = $("#phone");
  var $password = $("#password");
  var $code = $("#code");
  var $agreement = $("#agreement");
  var $submit = $("button:submit");
  var $codeButton = $('#codeButton');


  // 验证密码的强弱
  $password.keyup(function(e) {
      var isShow = checkPassword(this);
      if (0 == isShow) {
        $('.one').css('visibility', 'hidden');
        $('.two').css('visibility', 'hidden');
        $('.three').css('visibility', 'hidden');
      } else if (1 == isShow || 2 == isShow) {
        $('.one').css('visibility', 'visible');
      } else if (4 == isShow || 3 == isShow) {
        $('.two').css('visibility', 'visible');
      } else if (5 == isShow ) {
        $('.three').css('visibility', 'visible');
      }
  });

  function checkPassword(pwdinput) {
      var maths, smalls, bigs, corps, cat, num;
      var str = $(pwdinput).val()
      var len = str.length;

      var cat = /.{16}/g
      if (len == 0) return 1;
      if (len > 16) { $(pwdinput).val(str.match(cat)[0]); }
      cat = /.*[\u4e00-\u9fa5]+.*$/
      if (cat.test(str)) {
        return -1;
      }
      cat = /\d/;
      var maths = cat.test(str);
      cat = /[a-z]/;
      var smalls = cat.test(str);
      cat = /[A-Z]/;
      var bigs = cat.test(str);
      var corps = corpses(pwdinput);
      var num = maths + smalls + bigs + corps;

      if (len < 6) { return 1; }

      if (len >= 6 && len <= 8) {
        if (num == 1) return 1;
        if (num == 2 || num == 3) return 2;
        if (num == 4) return 3;
      }

      if (len > 8 && len <= 11) {
        if (num == 1) return 2;
        if (num == 2) return 3;
        if (num == 3) return 4;
        if (num == 4) return 5;
      }

      if (len > 11) {
        if (num == 1) return 3;
        if (num == 2) return 4;
        if (num > 2) return 5;
      }
    }

    function corpses(pwdinput) {
      var cat = /./g
      var str = $(pwdinput).val();
      var sz = str.match(cat)
      for (var i = 0; i < sz.length; i++) {
        cat = /\d/;
        maths_01 = cat.test(sz[i]);
        cat = /[a-z]/;
        smalls_01 = cat.test(sz[i]);
        cat = /[A-Z]/;
        bigs_01 = cat.test(sz[i]);
        if (!maths_01 && !smalls_01 && !bigs_01) { return true; }
      }
      return false;
    }

  // 点击获取手机验证码
  $codeButton.bind('click', function (){
    var phone = $.trim($phone.val());
    var pattern = /1[3|4|5|7|8|9]\d{9}/;
    if (!pattern.test(phone)) {
      alert('请输入正确的手机号码');
      return ;
    }
    $.ajax({
      url: '${base}/register/phone_captcha.jhtml',
      type: "GET",
      data: {
        "phone": $phone.val()
      },
      dataType: "text",
      cache: true,
      beforeSend: function() {
        requestPhone();
      },
      success: function(message) {
      	alert(message);
      	$("#compareCode").val(message);
        if (message.type != "success") {
          clearInterval(setInter);
          aginphoneText('获取验证码');
        }
      }
    });
  });

  // 表单验证
  $registerForm.validate({
    rules: {
      phone: {
        required: true,
        pattern: /1[3|4|5|7|8|9]\d{9}/,
        minlength: 11,
        remote: {
			url: "${base}/register/check_phone.jhtml",
			cache: false
		}
      },
      password: {
        required: true,
        pattern: /.{4}/,
      },
      rePassword: {
        required: true,
        equalTo: "#password"
      },

      code: {
        required: true,
        equalTo: "#compareCode",	
        digits : true
      },

      agreement: {
        required: true,
      }
    },
    messages: {
      phone: {
        required : "请输入手机号码",
        pattern: "${message("shop.register.phoneIllegal")}",
        remote: "${message("shop.register.phoneExist")}"
      },

      password: {
        required : "六位数以上的密码",
        pattern: "六位数以上的密码",
      },

      rePassword: {
        required : "六位数以上的密码",
        equalTo: "密码不相同"
      },

      code: {
        required : "请输入验证码",
        pattern: "只允许输入正确的手机号",
        equalTo: "验证码不对！请重新输",
        digits : "验证码应该输入数字"
      },

      agreement: {
        required: "必须接受条款",
      }
    },
    submitHandler: function(form) {
    	alert("submitHandler");
    	$.ajax({
				url: "${base}/common/public_key.jhtml",
				type: "GET",
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success: function(data) {
					alert("data"+data);
					var rsaKey = new RSAKey();
					rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
					var enPassword = hex2b64(rsaKey.encrypt($password.val()));
					alert("enPassword"+enPassword);
					$.ajax({
						url: $registerForm.attr("action"),
						type: "POST",
						data: {
							"phone": $phone.val(),
							"enPassword": enPassword
							},
						dataType: "json",
						cache: false,
						success: function(message) {
							setTimeout(function() {
									$submit.prop("disabled", false);
									location.href = "${base}/";
							}, 1000);
							$submit.prop("disabled", false);	
							alert("注册成功！");
						}
					});
				}
		});			
    }

  });
  // 获取手机验证码按钮倒计时
  function requestPhone () {
      var time = 30;
      aginphoneText(time+'秒');
      setInter = setInterval(function () {
          --time;
          if (time >= 0) {
              $codeButton.html(time+'秒');
          }
          else {
             clearInterval(setInter);
             aginphoneText('获取验证码');
          }
      }, 1000);
  }
  // 获取手机验证码按钮禁止点击
  function aginphoneText (options) {
      if (typeof options == 'number')
          $codeButton
              .html(options)
              .attr('disabled', 'disabled')
              .addClass('clickBackground');
      else 
          $codeButton
              .html(options)
              .removeAttr('disabled')
              .removeClass('clickBackground');
  }

});
</script>
</head>
<body>
	[#--
	<input type="text" id="compareCode" value="关于验证码"/>
	<input type="text" id="compareCode2" value="关于验证码2"/>
	    <header class="landing"></header>
    	<div class="landing">
			<h1></h1>
			<span>欢迎注册</span>
			<form id="registerForm_mdh" action="${base}/register/submit.jhtml" method="post">
				<input class="tet" type="text"     id="phone" name="phone"    maxlength="200" autocomplete="off" placeholder="请输入手机号" />
				<input class="tet" type="password" name="password" maxlength="${setting.passwordMaxLength}" autocomplete="off" placeholder="请输入密码"/>
				<input class="tet" type="password" name="rePassword" maxlength="${setting.passwordMaxLength}" autocomplete="off" placeholder="请重新输入密码"/>
				<input class="tet" type="password" name="captcha" maxlength="4" autocomplete="off" placeholder="短信验证码"/>
				<button class="register" type="button" name="getPhoneCaptcha" id="getPhoneCaptcha">获取验证码</button>
				<p class="clearfix register">
					<input class="names" type="radio" value="我已阅读并接受服务条款" name="nex" />
					<a>我已阅读并接受服务条款</a>
				</p>
				<button class="registered">立即注册</button>
				<p class="last clearfix">已注册可　<a href="">直接登陆</a>
					<a class="email" href="">通过邮箱注册》</a>
				</p>
				<div class="register-fix">
					<div class="register-infix  clearfix">
						<span class="one"></span>
						<span class="two"></span>
						<span class="three"></span>
					</div>
					<p>弱中强</p>
				</div>
			</form>
		</div>
		--]
		<input type="text" id="compareCode" value="关于验证码"/>
		<div id = "header" class="landing"></div>
    	<div class="landing">
			<h1></h1>
			<span>欢迎注册</span>
			<div class = "form" >
				<form id="registerForm" action="${base}/register/submit.jhtml" method="post">
					<div class = "username">
						<input class="tet" type="text" placeholder="请输入手机号" id="phone" name="phone" maxlength="11" />
					</div>
					<div class = "password">
						<input class="tet" type="password" placeholder="请输入密码" id="password" name="password" maxlength="20" autocomplete="off" />
					</div>
					<div class = "re-password">
						<input class="tet" type="password" placeholder="请重新输入密码" name="rePassword" maxlength="20" autocomplete="off" />
					</div>
					<div class = "code">
						<input class="tet" type="text" placeholder="短信验证码" name = "code" maxlength = "6" id = "code"/>
						<button class="register" type = "button" id = "codeButton" >获取验证码</button>
					</div>
					<div class = "agreement">
						<p class="clearfix register">
							<input class="names" type="checkbox" name="agreement" id = "agreement" />
							<a href = "javascript:;">我已阅读并接受服务条款</a>
						</p>
					</div>
					<button class="registered" type = "submit" >立即注册</button>
					<input type="submit" class="registered" value="立即注册"/>

<input type="submit" class="submit" value="${message("shop.register.submit")}" />

					<p class="last clearfix">${message("shop.register.hasAccount")}　
						<a href="${base}/login.jhtml">${message("shop.register.login")}</a>
						<a class="email" href="javascrpt:;">通过邮箱注册》</a>
					</p>
					<div class="register-fix">
						<div class="register-infix  clearfix">
							<span class="one"></span>
							<span class="two"></span>
							<span class="three"></span>
						</div>
						<p>弱中强</p>
					</div>
				</form>
			</div>
			
			
		</div>
</body>
</html>
[/#escape]