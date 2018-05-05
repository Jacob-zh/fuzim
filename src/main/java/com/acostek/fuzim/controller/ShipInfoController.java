package com.acostek.fuzim.controller;

import com.acostek.fuzim.util.ForwardRequest;
import com.acostek.fuzim.util.ShipInfoDeal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
//            e.printStackTrace();
            return "{"+"\"code\""+":"+"\"0\""+","+"\"msg\""+":"+"\"error\""+"}";
        }
        return info;
    }

    @RequestMapping(value = "FullInfo",method = RequestMethod.GET)
    public String shipFullInfoAPI(){
        String info = null;
        Map map = new HashMap();
        try {
            String str = ForwardRequest.net("GET","","http://faultest.com:8080/templefuzi/shipInfo/getAllShipInfo",map,"");
            info = ShipInfoDeal.sortShipFullInfo(str);
        } catch (Exception e) {
//            e.printStackTrace();
            return "{"+"\"code\""+":"+"\"0\""+","+"\"msg\""+":"+"\"error\""+"}";
        }
        return info;
    }



    @RequestMapping(value = "SimpleInfoTest",method = RequestMethod.GET)
    public String shipInfoAPITest(HttpServletResponse response){
        String info = null;
        try {
            Map map = new HashMap();
            info = ForwardRequest.net("GET","","http://127.0.0.1:9090/shipInfo.json",map,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        return info;
    }

    @RequestMapping(value = "FullInfoTest",method = RequestMethod.GET)
    public String shipFullInfoAPITest(HttpServletResponse response){
        String info = null;
        try {
            Map map = new HashMap();
            String str = ForwardRequest.net("GET","","http://127.0.0.1:9090/shipFullInfo.json",map,"");
            info = ShipInfoDeal.sortShipFullInfo(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        return info;
    }
}
