package com.acostek.fuzim.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.acostek.fuzim.util.ForwardRequest.net;

@RestController
@RequestMapping(value = "getShip")
public class ShipInfoController {

    @RequestMapping(value = "SimpleInfo",method = RequestMethod.GET)
    public String shipInfoAPI(){
        String info = null;
        Map map = new HashMap();
        try {
            info = net("GET","","http://faultest.com:8080/templefuzi/shipDef/getAllShipDef",map,"");
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
            fullInfo = net("GET","","http://faultest.com:8080/templefuzi/shipInfo/getAllShipInfo",map,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullInfo;
    }

}
