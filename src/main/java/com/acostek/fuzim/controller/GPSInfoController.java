package com.acostek.fuzim.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.acostek.fuzim.util.CoordDeal.coordDeal;
import static com.acostek.fuzim.util.ForwardRequest.net;

@RestController
@RequestMapping(value = "getGPS")
public class GPSInfoController {

    @RequestMapping(value = "FullInfo",method = RequestMethod.GET)
    public String GPSInfoAPI(){
        String coord = null;
        try {
            Map map = new HashMap();
            String fullInfo = net("GET","","http://faultest.com:8080/templefuzi/gps/getAllNowGPS",map,"");
            coord = coordDeal(fullInfo);//GPS坐标处理
        } catch (Exception e) {
//            e.printStackTrace();
            return "{"+"\"code\""+":"+"\"0\""+","+"\"msg\""+":"+"\"error\""+"}";
        }
        return coord;
    }
}
