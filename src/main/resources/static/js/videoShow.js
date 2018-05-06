(function($) {
    'use strict';

    $(function () {
        var $fullText = $('.admin-fullText');
        $('#admin-fullscreen').on('click', function () {
            $.AMUI.fullscreen.toggle();
        });

        $(document).on($.AMUI.fullscreen.raw.fullscreenchange, function () {
            $fullText.text($.AMUI.fullscreen.isFullscreen ? '退出全屏' : '开启全屏');
        });
    });


    //第四个页面填充
    function tabsFour(){
        // videoInit();
        $("#fz-video-show").on("click",videoInit);

    }
    //视频初始化函数
    function videoInit() {
        var player = new EZUIPlayer('fz-video-player');
        player.on('error', function(){
            console.log('error');
        });
        player.on('play', function(){
            console.log('play');
        });
        player.on('pause', function(){
            console.log('pause');
        });
    }


    tabsFour();

})(jQuery);