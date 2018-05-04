package com.acostek.fuzim.controller;

import com.acostek.fuzim.util.ForwardRequest;
import com.acostek.fuzim.util.ShipInfoDeal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "getShip")
public class ShipInfoController {

    @RequestMapping(value = "SimpleInfo",method = RequestMethod.GET)
    public String shipInfoAPI(){
        String info = null;
        Map map = new HashMap();
        try {
            info = ForwardRequest.net("GET","","http://faultest.com:8080/templefuzi/shipDef/getAllShipDef",map,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @RequestMapping(value = "FullInfo",method = RequestMethod.GET)
    public String shipFullInfoAPI(){
        String fullInfo = null;
        Map map = new HashMap();
        try {
             String str = ForwardRequest.net("GET","","http://faultest.com:8080/templefuzi/shipInfo/getAllShipInfo",map,"");
            fullInfo = ShipInfoDeal.sortShipFullInfo(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullInfo;
    }

}
