package com.acostek.fuzim.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.acostek.fuzim.util.ForwardRequest.net;

@RestController
public class ShipInfoController {

    @RequestMapping(value = "getShipInfo",method = RequestMethod.GET)
    public String shipInfoAPI(){
        Map map = new HashMap();
        try {
            System.out.println(net("GET","http://faultest.com:8080/templefuzi/shipDef/getAllShipDef",map,""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return " ";
    }
}
