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

    //第一个表格内容填充
    // function tabsOne() {
    //     var GETAPI_1 = "http://faultest.com:8080/templefuzi/shipDef/getAllShipDef";
    //     $.getJSON(GETCLASSES,{},function(json){
    //        console.log(json);
    //     });
    //     $.getJSON("http://eezzo.com/API/CD",{url:encodeURI(GETAPI_1)},function(json){
    //         // console.log(data);
    //         var t = $("#temp-tabs1-tr").html();
    //         var f = Handlebars.compile(t);
    //         var h = f(json.data);
    //         $("#tabs1-tr").html(h);
    //     });
    // }

    //第二个表格内容填充
    // Handlebars.registerHelper("iswarn", function (value, options) {
    //     console.log("iswarn");
    //     if(value == 1){
    //         return options.fn(this);
    //     }else {
    //         return options.inverse(this);
    //     }
    // });
    // function tabsTwo() {
    //     var GETAPI_2 = "http://faultest.com:8080/templefuzi/shipInfo/getAllShipInfo";
    //     $.getJSON("http://eezzo.com/API/CD",{url:encodeURI(GETAPI_2)},function(json){
    //         var t = $("#temp-tabs2-tr").html();
    //         var f =Handlebars.compile(t);
    //         var h = f(json.data);
    //         $("#tabs2-tr").html(h);
    //     });
    //
    // }

    //第三个页面内容填充
    //地图初始化
    var fzmap = new AMap.Map("tabs3-map-show", {
        center: [118.789496, 32.019428],
        zoom: 12
    });
    fzmap.plugin(['AMap.ToolBar','AMap.Scale'], function () {
        fzmap.addControl(new AMap.ToolBar());
        fzmap.addControl(new AMap.Scale());
    });

    /**
     * 提取坐标并分组（39个一组调用一次转换API）,
     * @param data 坐标组集合
     * @return {Array} 返回切割后的坐标组,四十个坐标一个单元.
     */
    function groupCoord(data){
        var dataLength = data.data.length;
        var keyGroup = []; //转换前标注点组de集合
        if(dataLength >9){
            var num = Math.ceil(dataLength/9); //需要分几组
            for(var i=0; i<num ; i++){
                keyGroup[i] = "11879.6152,3202.6073";//夫子庙点起始标注点
                for(var j=i*9; j<(i+1)*9 && j < dataLength ;j++){
                    keyGroup[i] = keyGroup[i]+"|"+ data.data[j].longitude/100+","+data.data[j].latitude/100;
                }
            }
            return keyGroup;
        }else{
            keyGroup[0] = "118.796152,32.026073";//夫子庙点起始标注点
            for(var i=0; i < dataLength; i++){
                keyGroup[0] =  keyGroup[0]+"|"+ data.data[i].longitude/100+","+data.data[i].latitude/100;
            }
            return keyGroup;
        }
    }

    /**
     * 所有船只坐标地图标注
     */
    function markPoint() {
        var GETAPI_3 = "http://faultest.com:8080/templefuzi/gps/getAllNowGPS";
        return new Promise(function (resolve, reject) {
            $.getJSON("http://eezzo.com/API/CD", {url: encodeURI(GETAPI_3)}, function (json) {
                // console.log(json);
                var keyGroup = [];
                if (json.code == "1") {
                    // console.log("成功拉取坐标数据开始进行切割");
                    keyGroup = groupCoord(json);
                    // console.log(keyGroup);
                } else {
                    console.error("拉取坐标数据失败")
                }
                resolve(keyGroup);
                reject(keyGroup);
            });
        }).then(function (s) {
            // console.log("成功切割坐标数据开始进行坐标变换");
            for (var i = 0; i < s.length; i++) {
                (function (i) {
                    return new Promise(function (resolve, reject) {
                        var API = "http://restapi.amap.com/v3/assistant/coordinate/convert?key=9e3490ab7e2e882522a3ef063baafb41&locations=" + s[i] + "&coordsys=gps&output=json";
                        $.getJSON("http://eezzo.com/API/CD", {url: encodeURI(API)}, function (json) {
                            // console.log(json);
                            // console.log("成功进行第"+i+"组坐标转换开始进行坐标标注");
                            // console.log(json.locations);
                            resolve(json.locations);
                        });
                    }).then(function (s) {
                        var mark = s.split(";"); //每一组的坐标
                        // console.log(mark);
                        for (var j = 1; j < mark.length; j+=1) {
                            (function (j) {
                                // console.log("第"+39*i+j+"次标注");
                                // console.log(i+"|"+j);
                                // console.log(mark[j]);
                                var jwdu = mark[j].split(",");
                                var jing = parseFloat(jwdu[0]);
                                var wei = parseFloat(jwdu[1]);
                                addMarker(jing,wei,39*i+j);
                            })(j);
                        }
                    },function (e) {
                        console.error("第"+i+"组坐标转换失败!"+e);
                    });
                })(i);
            }

        },function (e) {
            console.error("切割数据失败!" + e)
        });
    }

    /**
     * 坐标点标注函数
     * @param position
     * @param title
     */
    function addMarker(positionx,positiony,title) {
        var marker = new AMap.Marker({
            icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
            position: [positionx,positiony],
            title: title+"号船",
            map: fzmap
        });
        marker.setLabel({
            offset: new AMap.Pixel(0, 0),//修改label相对于maker的位置
            content: title
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

    markPoint();
    // tabsFour()


})(jQuery)
