package com.acostek.fuzim.util;

import com.acostek.fuzim.GPSInfoModel;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.acostek.fuzim.util.ForwardRequest.net;


/**
 * 处理坐标：将获取的坐标进行高德坐标转换
 */
public class CoordDeal {

    /**
     * 将原坐标转换为高德坐标并以json字符串返回新坐标
     * @param coordStr 原坐标以json字符串的形式传入
     * @return 新坐标 Json格式String
     */
    public static String coordDeal(String coordStr){
        Gson gson = new Gson();
        JsonObject gpsInfo = new JsonParser().parse(coordStr).getAsJsonObject();

        if(gpsInfo.get("code").equals("1") && !gpsInfo.get("data").isJsonNull()){
            JsonArray dataArray = gpsInfo.getAsJsonArray("data");

            ArrayList<GPSInfoModel> gpsModels = new ArrayList<GPSInfoModel>();
            for(JsonElement obj : dataArray ){
                GPSInfoModel gps = gson.fromJson( obj , GPSInfoModel.class);
                gpsModels.add(gps);
            }
            int dataLength = gpsModels.size(); //gps信息的个数
            int num = (int)Math.ceil(dataLength/9); //需要调用几次转换API
            String[] coordParams = new String[num];
            if(num > 1){
                for(int i=0; i<num ; i++){
                    coordParams[i] = "118.796152,32.026073";
                    for(int j=i*9; j<(i+1)*9 && j < dataLength ;j++){
                       coordParams[i] = coordParams[i] + "|"+ gpsModels.get(j).getLatitude()+","+gpsModels.get(j).getLongitude();
                    }
                }
            }else{
                coordParams[0] = "118.796152,32.026073";//夫子庙点起始标注点
                for(int i=0; i < dataLength; i++){
                    coordParams[0] =  coordParams[0]+"|"+ gpsModels.get(i).getLatitude()+","+gpsModels.get(i).getLongitude();
                }
            }
            for (int i =0; i<coordParams.length;i++){
                Map map = new HashMap();
                map.put("key","9e3490ab7e2e882522a3ef063baafb41");
                map.put("locations",coordParams[i].toString());
                map.put("coordsys","gps");
                map.put("output","json");
                try {
                    net("GET","","http://restapi.amap.com/v3/assistant/coordinate/convert",map,"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return "";
    }
}
