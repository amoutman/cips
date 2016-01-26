$(document).ready(function () {
       //导航滑上效果
	    $(".part-left .menu li").hover(function(){
			   $(this).addClass("Mhover")
			},function(){
				$(this).removeClass("Mhover")
		})
		
		//表格背景颜色改变
		$(".dataTable tbody tr").hover(function(){
			   $(this).css("backgroundColor","#fafafa");
			},function(){
				$(this).css("backgroundColor","#fff");
		})
		
		
		//选择中间人
		$(".middleman li:nth-child(2n)").css("float","right");
		$(".middleman li:first-child").addClass("selectMan");
		$(".middleman li").click(function(){
          $(this).addClass("selectMan").siblings().removeClass("selectMan");
			})
		
		/*弹出层关闭*/
         function showDiv() {document.getElementById('bg').style.display = 'block';}
		//弹窗
	    $(".ck-deal .btnck").click(function(){
				var bW=Math.ceil(($(window).width()-$(".tcDiv").outerWidth())/2);
				var bH=Math.ceil(($(window).height()-$(".tcDiv").outerHeight())/2);
				$(this).parent(".ck-deal").next(".tcDiv").fadeIn(500).css({
					left:bW+"px",
					top:bH+$(window).scrollTop()+"px"
					});
			    $(".bg").show();
			})
		  $(".tcDiv .close").click(function(){
			 $(".tcDiv").fadeOut(300);
			  $('div.bg').fadeOut(200);
			})
		
		/*选择性别*/	
	   $(".li_sex dl dd:first-child").addClass("selectOn");
		$(".li_sex dl dd").click(function(){
          $(this).addClass("selectOn").siblings().removeClass("selectOn");
			})
			
	//点击弹出
	 $('.m-calculator-con').click(function () {
        $(this).children('.calculator-dis').show();
        $(this).siblings('.m-calculator-con').children('.calculator-dis').hide()
    })
	 //筛选
    $('.calculator-dis li').click(function () {
        var this_text = $(this).text();
      
        $(this).parents('.m-calculator-con').children('.calculator-tit').text(this_text);
        if($(this).html() == "美元"){
        	$("#exRateType").html("人民币");
        }else{
        	$("#exRateType").html("美元");
        }
        $(this).parents('.calculator-dis').fadeOut('fast');
    })

    //点击以外的区域子 弹窗隐藏
    $(document).bind("click", function (e) {
        var target = $(e.target);
        if (target.closest(".m-calculator-con").length == 0) {
            $(".calculator-dis").fadeOut('fast');
        }
    })
			
})