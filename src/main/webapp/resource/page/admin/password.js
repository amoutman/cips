$(document).ready(function() {
	$('#changePwForm').validate({
		success:function(){
			$(".errorinfo").remove();
		},
		rules:{
			"oldPassword":{
				required:true,
				equalTo:"#password"
			},
			"newPassword":{
				required:true,
				minlength:8
			},
			"confirmNewPw":{
				equalTo:"#newPassword"
			}
		},
		messages:{
			"oldPassword":{
				required:"旧密码不能为空",
				equalTo:"旧密码不正确"	
			},
			"newPassword":{
				required:"新密码不能为空",
				minlength:"新密码长度必须大于8"
			},
			"confirmNewPw":{
				equalTo:"请输入相同的新密码"
			}
			
		},
	  	errorPlacement: function(error, element) {
	  			error.removeClass("error");
 				var span = $("<span class='icon errorinfo' />").append(error);
 				span.appendTo(element.parent());
 		}
	});
	
	$("#updatePassword").click(function(){
		if($("#changePwForm").valid()){
			var password = $("#newPassword").val();
			$.ajax({
				url:"user/changePassword",
				data:{
					password:password
				},
				method:"post",
				success:function(data){
					if(data["success"]){
						alert("修改密码成功");
						window.location.href="";
					}else{
						alert("修改密码失败");
					}
				},
				error:function(){
					alert("修改密码失败");
				}
			});
		}
		
	});
})