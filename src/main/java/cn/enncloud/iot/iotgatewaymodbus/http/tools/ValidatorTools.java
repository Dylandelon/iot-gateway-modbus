package cn.enncloud.iot.iotgatewaymodbus.http.tools;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ValidatorTools {
    public static boolean isAllfieldNUll(Object o) throws Exception {
        try{
            for(Field field:o.getClass().getDeclaredFields()){
                field.setAccessible(true);//把私有属性公有化
                String name = field.getName();
                Object object = field.get(o);
                if(object instanceof CharSequence){
                    if(!ObjectUtils.isEmpty((String)object)){
                        return false;
                    }
                }else{
                    if(!ObjectUtils.isEmpty(object)){
                        return false;
                    }
                }

            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return true;
    }
    public static List<String> allfieldIsNUll(Object o) throws Exception {
        List<String> emptyValue = new ArrayList<>(16);
        try{
            for(Field field:o.getClass().getDeclaredFields()){
                field.setAccessible(true);
                Object object = field.get(o);
                if(object instanceof CharSequence){
                    if(!ObjectUtils.isEmpty((String)object)){
                        continue;
                    }
                }else{
                    if(!ObjectUtils.isEmpty(object)){
                        continue;
                    }
                }
                emptyValue.add(field.getName());

            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return emptyValue;
    }
}
