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

    private static String newGpsInfo;
    /**
     * 将原坐标转换为高德坐标并以json字符串返回新坐标
     * @param coordStr 原坐标以json字符串的形式传入
     * @return 新坐标 Json格式String
     */
    public static String coordDeal(String coordStr){
        Gson gson = new Gson();
        JsonObject gpsInfo = new JsonParser().parse(coordStr).getAsJsonObject();
//        System.out.println("222"+gpsInfo.toString());

        if("1".equals(gpsInfo.get("code").getAsString())){
            JsonArray dataArray = gpsInfo.getAsJsonArray("data");
//            System.out.println("333"+dataArray.toString());
            ArrayList<GPSInfoModel> gpsModels = new ArrayList<GPSInfoModel>();
            for(JsonElement obj : dataArray ){
                GPSInfoModel gps = gson.fromJson( obj , GPSInfoModel.class);
                gpsModels.add(gps);
            }
            int dataLength = gpsModels.size(); //gps信息的个数
//            System.out.println("gps信息的个数"+dataLength);
            int num = dataLength/9 + 1; //需要调用几次转换API
            String[] coordParams = new String[num];
//            System.out.println("num"+num);
            if(num > 1){
                for(int i=0; i<num ; i++){
                    coordParams[i] = "118.796152,32.026073";
                    for(int j=i*9; j<(i+1)*9 && j < dataLength ;j++){
                       coordParams[i] = coordParams[i] + "|"+ gpsModels.get(j).getLongitude()+","+gpsModels.get(j).getLatitude();
                    }
                }
            }else{
                coordParams[0] = "118.796152,32.026073";//夫子庙点起始标注点
                for(int i=0; i < dataLength; i++){
                    coordParams[0] =  coordParams[0]+"|"+ gpsModels.get(i).getLongitude()+","+gpsModels.get(i).getLatitude();
                }
            }
            for (int i =0; i<coordParams.length;i++){
                Map map = new HashMap();
                map.put("key","9e3490ab7e2e882522a3ef063baafb41");
                map.put("locations",coordParams[i].toString());
                map.put("coordsys","gps");
                map.put("output","json");
                try {
                    newGpsInfo = net("GET","","http://restapi.amap.com/v3/assistant/coordinate/convert",map,"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return newGpsInfo;
    }
}
