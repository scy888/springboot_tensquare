package common;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: common
 * @date: 2020-03-19 21:43:10
 * @describe:
 */
public class JacksonUtils {
    private static final JacksonUtils _instance=new JacksonUtils();
    private final ObjectMapper objectMapper=new ObjectMapper();

    public static JacksonUtils getInstance(){
        return _instance;
    }

    public String writeValueAsString(Object value) throws Exception {
        return this.objectMapper.writeValueAsString(value);
    }

    public <T> T readValue(String content,Class<T> valueType){
        return this.readValue(content, valueType);
    }

    public <T> T convertValue(Object fromValue,Class<T> toValueType){
        return this.objectMapper.convertValue(fromValue,toValueType );
    }
}
