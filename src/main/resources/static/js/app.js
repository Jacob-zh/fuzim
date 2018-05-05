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

    var fzmap; //地图显示对象
    var shipFullInfo = null; //船只全部信息

    //第一个表格内容填充函数
    function tabsOne() {
        var GETAPI_1 = "http://127.0.0.1:9090/getShip/SimpleInfoTest";
        $.getJSON(GETAPI_1,{},function(json){
            // console.log(json);
            var t = $("#temp-tabs1-tr").html();
            var f = Handlebars.compile(t);
            var h = f(json.data);
            $("#tabs1-tr").html(h);
        });
        // $.getJSON("http://eezzo.com/API/CD",{url:encodeURI(GETAPI_1)},function(json){ });
    }

    //第二个表格内容填充函数
    Handlebars.registerHelper("iswarn", function (value, options) {
        // console.log("iswarn");
        if(value == 1){
            return options.fn(this);
        }else {
            return options.inverse(this);
        }
    });
    function tabsTwo() {
        var GETAPI_2 = "http://127.0.0.1:9090/getShip/FullInfoTest";
        $.getJSON(GETAPI_2,{},function(json){
            // console.log(json);
            shipFullInfo = json.data;
            var t = $("#temp-tabs2-tr").html();
            var f =Handlebars.compile(t);
            var h = f(json.data);
            $("#tabs2-tr").html(h);
        });
    }

    //第三个页面的地图初始化函数
    function initMap() {
        fzmap = new AMap.Map('tabs3-map-show', {
            center: [118.789496, 32.019428],
            zoom: 12
        });
        fzmap.plugin(['AMap.ToolBar','AMap.Scale'], function () {
            fzmap.addControl(new AMap.ToolBar());
            fzmap.addControl(new AMap.Scale());
        });
    }

    /**
     *
     * @return {Promise<any>}
     */
    function markPoint() {
        var GETAPI_3 = "http://127.0.0.1:9090/getGPS/FullInfoTest";
        return new Promise(function (resolve, reject) {
            $.getJSON(GETAPI_3, {}, function (json) {
                if (json.code === "1") {
                    console.log("成功拉取GPS坐标数据");
                } else {
                    console.error("拉取GPS坐标数据失败")
                }
                resolve(json.data);
                reject(json);
            });
        }).then(function (s) {
            console.log(s);
            var gpsInfo = s;  //存放GPS信息
            var length = gpsInfo.length; //GPS坐标点的个数
            for (var i = 0; i < length; i++) {  //循环GPS坐标信息
                (function (i) {
                    var gpsInfoX = gpsInfo[i]; //当前GPS坐标信息
                    if(shipFullInfo != null){  //判断所有船只当前信息已经获取
                        var infoL = shipFullInfo.length; //船只信息集合的长度
                        for(var j =0; j <infoL; j++){  //循环船只信息集合
                            (function (j) {
                                if (shipFullInfo[j].shipId === gpsInfoX.shipId) { //匹配与当前GPS信息中船号shipId相同的船只信息
                                    //船只点标注，将该船GPS信息对象和该船FullInfo对象传入addMarker
                                    addMarker(gpsInfoX,shipFullInfo[j]);
                                }
                            })(j)
                        }
                    }else { //所有船只当前信息没有获取重新拉取
                        tabsTwo();
                        var infoL2 = shipFullInfo.length; //船只信息集合的长度
                        for(var j2 =0; j2 <infoL2; j2++){  //循环船只信息集合
                            (function (j2) {
                                if (shipFullInfo[j2].shipId === gpsInfoX.shipId) { //匹配与当前GPS信息中船号shipId相同的船只信息
                                    //船只点标注，将该船GPS信息对象和该船FullInfo对象传入addMarker
                                    addMarker(gpsInfoX,shipFullInfo[j2]);
                                }
                            })(j2);
                        }
                    }
                })(i);
            }
        },function (e) {
            console.error("拉取坐标数据失败" + e)
        });
    }

    /**
     * 船只标注函数
     * @param gpsInfoX GPS信息Json对像
     * @param shipFullInfoX 船只FullInfo对象
     */
    function addMarker(gpsInfoX,shipFullInfoX) {
        //船类型逻辑判断！选择不同的标注类型
        console.log("*调用一次标注*");
        var iconUrl = "img/ship_w.png"; //设置标注图标
        if (shipFullInfoX.collide === "1" || shipFullInfoX.overFire === "1" || shipFullInfoX.leakage === "1" || shipFullInfoX.overSmog === "1") {
            iconUrl = "img/ship_r.png";
        } else if (shipFullInfoX.wait === "1") {
            iconUrl = "img/ship_b.png";
        } else if (shipFullInfoX.overSpeed === "1") {
            iconUrl = "img/ship_y.png";
        }
        var longitude = parseFloat(gpsInfoX.longitude).toFixed(6);
        var latitude = parseFloat(gpsInfoX.latitude).toFixed(6);
        var marker = new AMap.Marker({
            icon: iconUrl, //标注图标类型 <静态文件>
            position: [longitude,latitude], //位置坐标
            title: gpsInfoX.shipId+"号船"+"\n"+shipFullInfoX.startRunTime, //鼠标滑过提示
            map: fzmap
        });
        marker.setLabel({
            offset: new AMap.Pixel(5, -22),//修改label相对于maker的位置
            content: gpsInfoX.shipId
        });

    }


    //第四个页面填充
    function tabsFour(){
        videoInit();
        // $("#fz-video").on("click",videoInit);

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

    tabsOne();
    tabsTwo();
    initMap();
    markPoint();

})(jQuery);
