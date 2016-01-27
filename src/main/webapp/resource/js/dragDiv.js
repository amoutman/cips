(function($) {
  $.fn.dragDiv = function(divWrap) {
    return this.each(function() {
      var $divMove = $(this);//鼠标可拖拽区域
      var $divWrap = divWrap ? $divMove.parents(divWrap) : $divMove;//整个移动区域
      var mX = 0, mY = 0;//定义鼠标X轴Y轴
      var dX = 0, dY = 0;//定义div左、上位置
      var isDown = false;//mousedown标记
      if(document.attachEvent) {//ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select: none; -webkit-user-select: none;
        $divMove[0].attachEvent('onselectstart', function() {
          return false;
        });
      }
      $divMove.mousedown(function(event) {
        var event = event || window.event;
        mX = event.clientX;
        mY = event.clientY;
        dX = $divWrap.offset().left;
        dY = $divWrap.offset().top;
        isDown = true;//鼠标拖拽启动
      });
      $(document).mousemove(function(event) {
        var event = event || window.event;
        var x = event.clientX;//鼠标滑动时的X轴
        var y = event.clientY;//鼠标滑动时的Y轴
        if(isDown) {
          $divWrap.css({"left": x - mX + dX, "top": y - mY + dY});//div动态位置赋值
        }
      });
      $divMove.mouseup(function() {
        isDown = false;//鼠标拖拽结束
      });
    });
  };
})(jQuery);
//
