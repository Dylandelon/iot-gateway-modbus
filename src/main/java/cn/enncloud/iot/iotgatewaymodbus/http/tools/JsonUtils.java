package cn.enncloud.iot.iotgatewaymodbus.http.tools;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JSON utility <br>
 *
 * @author lixiangk
 * dependency jackson-databind-2.8.5
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 设置FAIL_ON_EMPTY_BEANS属性，当序列化空对象不要抛异常
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //属性为NULL 不序列化,只对bean起作用；对Map List不起作用
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 设置FAIL_ON_UNKNOWN_PROPERTIES属性，当JSON字符串中存在Java对象没有的属性，忽略
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String writeValueAsString(Object obj) {
        if (obj == null) {
            return null;
        }
        String result;
        try {
            long startTime = 0;
            if (logger.isTraceEnabled()) {
                startTime = System.nanoTime();
            }
            result = OBJECT_MAPPER.writeValueAsString(obj);
            if (logger.isTraceEnabled()) {
                logger.trace("Object2Json 耗时{}ms", TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)));
            }
            return result;
        } catch (JsonProcessingException e) {
            logger.warn("JSON转换失败！obj:" + obj.toString(), e);
            return null;
        }
    }


    public static <T> T readObject(String jsonString, TypeReference valueTypeRef) {
        if (jsonString == null) {
            return null;
        }
        T result;
        try {
            result = OBJECT_MAPPER.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            logger.warn("JSON字符串解析报错！json:" + jsonString, e);
            result = null;
        }
        return result;
    }

    public static <T> T readObject(String jsonString, Class<T> valueType) {
        if (jsonString == null) {
            return null;
        }
        T result;
        try {
            result = OBJECT_MAPPER.readValue(jsonString, valueType);
        } catch (IOException e) {
            logger.warn("JSON字符串解析报错！json:" + jsonString, e);
            result = null;
        }
        return result;
    }

    /**
     * @param jsonString     JSON字符串
     * @param elementClasses 数组元素类型
     * @return List对象
     */
    public static <T> List<T> readArray(String jsonString, Class<T> elementClasses) {
        if (jsonString == null) {
            return null;
        }
        List<T> result;
        try {
            long startTime = 0;
            if (logger.isTraceEnabled()) {
                startTime = System.nanoTime();
            }
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class,
                    elementClasses);
            result = mapper.readValue(jsonString, javaType);
            if (logger.isTraceEnabled()) {
                logger.trace("Json2Array 耗时{}ms", TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)));
            }
        } catch (IOException e) {
            logger.warn("JSON字符串解析报错！json:" + jsonString, e);
            result = null;
        }
        return result;
    }

    public static String readAsStringExcept(Object obj, String... properties) {
        if (null == obj) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
        mapper.setFilterProvider(filters);
        if (obj instanceof List) {
            mapper.addMixIn(((List) obj).get(0).getClass(), DynamicFilter.class);
        } else {
            mapper.addMixIn(obj.getClass(), DynamicFilter.class);
        }
        String content;
        try {
            content = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            logger.error("对象转换JSON字符串报错！" + obj.getClass().getName(), ex);
            content = null;
        }
        return content;
    }

    public static JsonNode readTree(String content) {
        if (null == content) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(content);
        } catch (IOException ex) {
            logger.error("Json字符串转换为树形结构报错！json:" + content, ex);
            return null;
        }
    }

    @JsonFilter("dynamicFilter")
    private interface DynamicFilter {
    }

}
