package com.acostek.fuzim.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 对船只信息进行排序，含有警告的放前，正常的放后
 */
public class ShipInfoDeal {

    public static String sortShipFullInfo(String shipFullinfo){
        JsonObject shipInfoStr = new JsonParser().parse(shipFullinfo).getAsJsonObject();
        JsonArray shipInfos; //船只信息Json集合

        List<String> shipInfoIncollide = new ArrayList<>();  //有撞船警告的船只信息
        List<String> shipInfoInleakage = new ArrayList<>();  //漏水
        List<String> shipInfoInoverSmog = new ArrayList<>(); //烟雾
        List<String> shipInfoInoverFire = new ArrayList<>(); //明火
        List<String> shipInfoInwait = new ArrayList<>(); //会船
        List<String> shipInfoInoverSpeed = new ArrayList<>(); //超速
        List<String> shipInfoInNomal = new ArrayList<>();  //无警告的船只信息

        String newShipInfo; //要返回的字符串
        if(shipInfoStr.get("code").getAsString().equals("1")){
        shipInfos = shipInfoStr.getAsJsonArray("data");
        int shipInfoNum = shipInfos.size();
            if(shipInfoNum != 0){
                for(int i = 0; i < shipInfoNum; i++){
                    JsonObject shipInfo = shipInfos.get(i).getAsJsonObject();
                    if(shipInfo.get("collide").getAsString().equals("1")){
                        shipInfoIncollide.add(shipInfo.toString());
                    }else if (shipInfo.get("leakage").getAsString().equals("1")){
                        shipInfoInleakage.add(shipInfo.toString());
                    }else if (shipInfo.get("overSmog").getAsString().equals("1")){
                        shipInfoInoverSmog.add(shipInfo.toString());
                    }else if (shipInfo.get("overFire").getAsString().equals("1")){
                        shipInfoInoverFire.add(shipInfo.toString());
                    }else if (shipInfo.get("wait").getAsString().equals("1")){
                        shipInfoInwait.add(shipInfo.toString());
                    }else if (shipInfo.get("overSpeed").getAsString().equals("1")){
                        shipInfoInoverSpeed.add(shipInfo.toString());
                    }else {
                        shipInfoInNomal.add(shipInfo.toString());
                    }
                }
            List<String> newShipInfos = new ArrayList<>(shipInfoNum);
            if(!shipInfoIncollide.isEmpty())
                newShipInfos.addAll(shipInfoIncollide);
            if(!shipInfoInleakage.isEmpty())
                newShipInfos.addAll(shipInfoInleakage);
            if(!shipInfoInoverSmog.isEmpty())
                newShipInfos.addAll(shipInfoInoverSmog);
            if(!shipInfoInoverFire.isEmpty())
                newShipInfos.addAll(shipInfoInoverFire);
            if(!shipInfoInwait.isEmpty())
                newShipInfos.addAll(shipInfoInwait);
            if(!shipInfoInoverSpeed.isEmpty())
                newShipInfos.addAll(shipInfoInoverSpeed);
            if(!shipInfoInNomal.isEmpty())
                newShipInfos.addAll(shipInfoInNomal);
            //构造返回字符串
            newShipInfo = "{"+"\"code\""+":"+"\"1\""+","+"\"data\""+":"+ newShipInfos.toString() + "}";
            System.out.println(newShipInfo);
            }else{
                newShipInfo = shipInfos.toString();
            }
        }else {
            newShipInfo = "{"+"\"code\""+":"+"\"1\""+","+"\"data\""+":"+ "[]"+ "}";
        }
    return newShipInfo;
    }
}
