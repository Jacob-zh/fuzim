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
        var GETAPI_1 = "http://127.0.0.1:9090/getShip/SimpleInfo";
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
        var GETAPI_2 = "http://127.0.0.1:9090/getShip/FullInfo";
        $.getJSON(GETAPI_2,{},function(json){
            // console.log(json);
            shipFullInfo = json.data;
            var t = $("#temp-tabs2-tr").html();
            var f =Handlebars.compile(t);
            var h = f(json.data);
            $("#tabs2-tr").html(h);
        });
    }

    //第三个分页的地图初始化函数
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
     * 船只信息标注
     * @return {Promise<any>}
     */
    function markPoint() {
        var GETAPI_3 = "http://127.0.0.1:9090/getGPS/FullInfo";
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
                                    addMarker(shipFullInfo[j],gpsInfoX.longitude,gpsInfoX.latitude);
                                }
                            })(j)
                        }
                        fzmap.setFitView();
                    }else { //所有船只当前信息没有获取重新拉取
                        tabsTwo();
                        var infoL2 = shipFullInfo.length; //船只信息集合的长度
                        for(var j2 =0; j2 <infoL2; j2++){  //循环船只信息集合
                            (function (j2) {
                                if (shipFullInfo[j2].shipId === gpsInfoX.shipId) { //匹配与当前GPS信息中船号shipId相同的船只信息
                                    //船只点标注，将该船GPS信息对象和该船FullInfo对象传入addMarker
                                    addMarker(shipFullInfo[j],gpsInfoX.longitude,gpsInfoX.latitude);
                                }
                            })(j2);
                        }
                        fzmap.setFitView();
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
    function addMarker(shipFullInfoX,longitude,latitude) {
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
        var marker = new AMap.Marker({
            icon: iconUrl, //标注图标类型 <静态文件>
            position: [parseFloat(longitude).toFixed(6),parseFloat(latitude).toFixed(6)], //位置坐标
            title: "该游船的船号："+shipFullInfoX.shipId+"\n当前高德坐标：N:"+shipFullInfoX.latitude+"|E:"+shipFullInfoX.longitude
            +"\n当前地磁偏角："+shipFullInfoX.gpsVardir+":"+shipFullInfoX.gpsMagvar+"\n当前航向角度："+shipFullInfoX.gpsTrackTure, //鼠标滑过提示
            map: fzmap
        });
        marker.setLabel({
            offset: new AMap.Pixel(5, -22),//修改label相对于maker的位置
            content: shipFullInfoX.shipId
        });
        //实例化鼠标点击信息窗体
        var lclickIFWDT;
        var lclickIFWDC = []; //点击弹出窗口内容
        lclickIFWDT = "<span style=\"font-size:14px;color: #000000;\">" + shipFullInfoX.shipId + '号船的全部信息如下：'+"</span>";//点击弹出窗口标题
        lclickIFWDC.push("船只运行时间："+shipFullInfoX.runTime);
        lclickIFWDC.push("<br/>上次停止时刻："+shipFullInfoX.endRunTime);
        lclickIFWDC.push("<br/>上次启动时刻："+shipFullInfoX.startRunTime);
        lclickIFWDC.push("<br/>撞船警报："+shipFullInfoX.collide+" | 船距："+shipFullInfoX.ultrasonicValue);
        lclickIFWDC.push("<br/>漏水警报："+shipFullInfoX.leakage+" | 水位："+shipFullInfoX.waterValue);
        lclickIFWDC.push("<br/>烟雾警报："+shipFullInfoX.overSmog+" | 浓度："+shipFullInfoX.smogValue);
        lclickIFWDC.push("<br/>明火警报："+shipFullInfoX.overFire+" | 火情："+shipFullInfoX.fireValue);
        lclickIFWDC.push("<br/>超速警报："+shipFullInfoX.overSpeed+" | 船速："+shipFullInfoX.speed);
        lclickIFWDC.push("<br/>电机转速："+shipFullInfoX.motorSpeed1+" | "+shipFullInfoX.motorSpeed2);
        lclickIFWDC.push("<br/>电机电流："+shipFullInfoX.motorCurrent1+" | "+shipFullInfoX.motorCurrent2);
        lclickIFWDC.push("<br/>电机电压："+shipFullInfoX.motorVoltage1+" | "+shipFullInfoX.motorVoltage2);
        lclickIFWDC.push("<br/>GPS坐标：E:"+shipFullInfoX.longitude+"|N:"+shipFullInfoX.latitude);
        lclickIFWDC.push("<br/>GPS航角："+shipFullInfoX.gpsTrackTure);
        lclickIFWDC.push("<br/>地磁偏角："+shipFullInfoX.gpsVardir+":"+shipFullInfoX.gpsMagvar);
        marker.content = createInfoWindow(lclickIFWDT,lclickIFWDC); //构造信息窗体
        //设置标注点击事件
        marker.on('click',markerClick);
        // marker.emit('click',{target:marker});  //默认打开一个标注事件
    }
    function markerClick(e){
        leftClickInfoWD.setContent(e.target.content);
        leftClickInfoWD.open(fzmap, e.target.getPosition());
    }
    var leftClickInfoWD = new AMap.InfoWindow({
        isCustom: true,  //使用自定义窗体
        closeWhenClickMap: true,
        offset: new AMap.Pixel(16, -55)
    });
    //构建自定义信息窗体
    function createInfoWindow(title, content) {
        var info = document.createElement("div");
        info.className = "info";
        //可以通过下面的方式修改自定义窗体的宽高
        info.style.width = "240px";
        // 定义顶部标题
        var top = document.createElement("div");
        var titleD = document.createElement("div");
        var closeX = document.createElement("img");
        top.className = "info-top";
        titleD.innerHTML = title;
        closeX.src = "https://webapi.amap.com/images/close2.gif";
        closeX.onclick = closeInfoWindow;
        top.appendChild(titleD);
        top.appendChild(closeX);
        info.appendChild(top);
        // 定义中部内容
        var middle = document.createElement("div");
        middle.className = "info-middle";
        middle.style.backgroundColor = 'white';
        middle.innerHTML = content;
        info.appendChild(middle);
        // 定义底部内容
        var bottom = document.createElement("div");
        bottom.className = "info-bottom";
        bottom.style.position = 'relative';
        bottom.style.top = '0px';
        bottom.style.margin = '0 auto';
        var sharp = document.createElement("img");
        sharp.src = "https://webapi.amap.com/images/sharp.png";
        bottom.appendChild(sharp);
        info.appendChild(bottom);
        return info;
    }
    //关闭信息窗体
    function closeInfoWindow() {
        fzmap.clearInfoWindow();
    }

    tabsOne();
    tabsTwo();
    initMap();
    markPoint();

})(jQuery);
