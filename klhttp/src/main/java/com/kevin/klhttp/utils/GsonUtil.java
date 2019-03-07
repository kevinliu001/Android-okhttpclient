package com.kevin.klhttp.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Xuan on 2015/9/21.
 */
public class GsonUtil {

    static Gson gson = new Gson();
    /**
     * convert object to json
     * @param obj
     * @return
     */
    static public String ObjectToString(Object obj){
        if(gson != null){
            return gson.toJson(obj);
        }
        return null;
    }

    /**
     * convert json to object
     * @param json
     * @param obj
     * @return
     */
    static public Object StringToObject(String json, Class<?> obj){
        if(gson != null){
            try {
                return gson.fromJson(json, obj);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * convert json to special type
     * @param json
     * @param type special type
     * @return
     */
    static public Object StringToType(String json, Type type){
        if(gson != null){
            try {
                return gson.fromJson(json, type);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
