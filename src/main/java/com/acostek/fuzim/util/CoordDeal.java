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
    private final static short num = 39; //高的地图API一次请求最多转换坐标数

    /**
     * 将原坐标转换为高德坐标并以json字符串返回新坐标
     * @param coordStr 原坐标以json字符串的形式传入
     * @return 新坐标 Json格式String
     */
    public static String coordDeal(String coordStr){
        JsonObject gpsInfo = new JsonParser().parse(coordStr).getAsJsonObject();
        if("1".equals(gpsInfo.get("code").getAsString())){
            JsonArray dataArray = gpsInfo.getAsJsonArray("data");
            int gpsInfoNum = dataArray.size(); //gps信息的个数
            ArrayList<GPSInfoModel> gpsInfoModels = new ArrayList<>(gpsInfoNum); //GPS信息集合数组
            //填充集合
            Gson gson = new Gson();
            for(JsonElement obj : dataArray ){
                GPSInfoModel gps = gson.fromJson( obj , GPSInfoModel.class);
                gpsInfoModels.add(gps);
            }
            int transNum = gpsInfoNum/num + 1; //需要调用几次转换API
            String[] coordParams = new String[transNum]; //存放每次要转换的坐标串
//            System.out.println("num"+num);
            if(transNum > 1){
                for(int i=0; i < transNum; i++){
                    coordParams[i] = "118.796152,32.026073"; //夫子庙点起始标注点
                    for(int j=i*num; j<(i+1)*num && j < gpsInfoNum ;j++){
                       coordParams[i] = coordParams[i] + "|" + gpsInfoModels.get(j).getLongitude()+","+gpsInfoModels.get(j).getLatitude() ;
                    }
                }
            }else{
                coordParams[0] ="118.796152,32.026073";
                for(int i=0; i < gpsInfoNum; i++){
                    coordParams[0] =  coordParams[0] + "|" + gpsInfoModels.get(i).getLongitude()+","+gpsInfoModels.get(i).getLatitude();
                }
            }
            //转换坐标
            JsonArray JsonArray = new JsonArray(); //返回的Json坐标集合
            JsonObject JsonObject = new JsonObject(); //返回的Json对象
            for (int i =0; i<transNum; i++){
                Map<String, String> map = new HashMap<>();
                map.put("key","9e3490ab7e2e882522a3ef063baafb41");
                map.put("locations", coordParams[i]);
                map.put("coordsys","gps");
                map.put("output","json");
                try {
                    String reStr = net("GET","","http://restapi.amap.com/v3/assistant/coordinate/convert",map,"");
                    JsonObject locationStr = new JsonParser().parse(reStr).getAsJsonObject();
                    if(locationStr.get("infocode").getAsString().equals("10000")){
                        String locations = locationStr.get("locations").getAsString();
                        //切割字符串
                        String[] coords = locations.split(";");
                        //更改GPS对象中的坐标数据
                        String[] locationCorrd;
                        for (int k = 1; k < coords.length ; k++) {
                            int index = i * num + k - 1; //要更改的GPS对象所引
                            locationCorrd = coords[k].split(",");
                            gpsInfoModels.get(index).setLongitude(Double.parseDouble(locationCorrd[0]));
                            gpsInfoModels.get(index).setLatitude(Double.parseDouble(locationCorrd[1]));
                            JsonArray.add(gpsInfoModels.get(index).toString()); //构建返回坐标集合
                        }
                    }else{
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JsonObject.addProperty("code","1");
            JsonObject.add("data",JsonArray); //构建返回对象
            newGpsInfo = JsonObject.toString(); //返回Json字符串
        }else {
            newGpsInfo = coordStr;
        }
        return newGpsInfo;
    }
}
