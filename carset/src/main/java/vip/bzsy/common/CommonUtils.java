package vip.bzsy.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import vip.bzsy.exception.CommonException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonUtils {



    //--------------------加密MD5utils----------------------------

    //--------------------jackson json 转化 utils----------------------------
    private static ObjectMapper objectMapper = new ObjectMapper();

    //1.0
    public static String BeanToJson(Object obj) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        objectMapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }

    //1.0
    public static <T> T JsonToBean(String jsonStr, Class<T> objClass)
            throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(jsonStr, objClass);
    }

    //--------------------参数为空utils----------------------------
    /**
     * 是否为空，对象为空返回true
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj){
        return obj == null;
    }

    /**
     * 是否不为空，对象不为空返回true
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj){
        return !isNull(obj);
    }

    /**
     * 是否为空，对象为空返回true
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection) obj).isEmpty();
        else if (obj instanceof Map) return ((Map) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;
        else if (obj instanceof String) {return obj == null || obj == "";}
        return false;
    }

    /**
     * 是否不为空，对象不为空返回true
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }
    //--------------------日期转换utils----------------------------
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    public static Date parseStringDate(String dateString)
            throws CommonException {

        try {
            return DateUtils.parseDate(
                    dateString, parsePatterns
            );
        } catch (Exception ex) {
            throw new CommonException(ex.getMessage());
        }
    }

    //--------------------io流----------------------------

    /**
     * 获取编码格式FileInputStream
     * 但是会读取前3个内容 使后面继续会少
     */
    public static Map<String,String> resolveCode(InputStream inputStream) throws Exception {
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2 )
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1 )
            code = "Unicode";
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
            code = "UTF-8";
        String text = new String(head,code);
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        map.put("text",text);
        return map;
    }


}
