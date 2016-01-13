$(document).ready(function() {
	function addRole(){
		var roleName = $("#newRoleName").val();
		if(roleName == ""){
			alert("角色名称不能为空");
		}
		$.ajax({
			url:"user/addRole",
			type:"post",
			data:{
				roleName:roleName
			},
			success:function(data){
				if(data["success"]){
					alert("添加成功");
					window.location.href="user/toRoleManage";
				}else{
					alert("添加失败");
				}
			},
			error:function(){
				alert("添加失败");
			}
			
		});
	}
	
	function updateRole(obj){
		var roleId = $(obj).parent().find("#roleId").val();
		var roleName = $(obj).parent().find("#roleName").val();
		
		if(roleName == ""){
			alert("角色名称不能为空");
		}
		
		$.ajax({
			url:"user/updateRole",
			type:"post",
			data:{
				roleId:roleId,
				roleName:roleName
			},
			success:function(data){
				if(data["success"]){
					alert("修改成功");
					window.location.href="user/toRoleManage";
				}else{
					alert("修改失败");
				}
			},
			error:function(){
				alert("修改失败");
			}
		});
	}
	
	function deleteRole(id){
		if(confirm("确定删除该角色？")){
			$.ajax({
				url:"user/deleteRole",
				type:"post",
				data:{
					roleId:id
				},
				success:function(data){
					if(data["success"]){
						alert("删除成功");
						window.location.href="user/toRoleManage";
					}else{
						alert("删除失败");
					}
				},
				error:function(){
					alert("删除失败");
				}
			});
		}
	}
})