
import org.json.JSONObject;

import java.util.Date;


public class JsonFilter {
    public static String FromJsonToString(String json) {
        try {
            JSONObject jsonObject =new JSONObject(json);
            return jsonObject.getString("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static String FromStringToJson(String json){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("data",json);
            jsonObject.put("time",(new Date()).toString());
            jsonObject.put("number",100);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
