package common;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: common
 * @date: 2020-04-01 23:38:18
 * @describe: 反射调用工具类
 */
public class ReflectUtils {
    private static final Logger logger= LoggerFactory.getLogger(ReflectUtils.class);
    private static final ReflectUtils instance_=new ReflectUtils();

    public static ReflectUtils getInstance(){
        return instance_;
    }

    public Object reflectMethodByParams(Object object, String methodName, Object...params) throws Exception {
        Object target=null;
        Class clazz = object.getClass();
        Object instance = clazz.newInstance();
            List<Class> calssList=new ArrayList<>();
            List<Object> paramList=new ArrayList<>();
            for (Object param : params) {
                calssList.add(param.getClass());
                paramList.add(param);
            }
        try {
            target= clazz.getDeclaredMethod(methodName, calssList.toArray(new Class[calssList.size()]))
                    .invoke(object,paramList.toArray(new Object[paramList.size()]));
            logger.info("{target}"+target);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.info("{reflectUtils}:此处接收被调用方法内部未被捕获的异常");
        }
        return target;
    }

    public Object reflectMethodByParams(String baseName,String keyName,String methodName,Object...params) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName);
        Enumeration<String> keys = bundle.getKeys();
        Object target=null;
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            if (keyName.equals(key)){
                String className = bundle.getString(key);
                Class<?> clazz = Class.forName(className);
                List<Class> classList=new ArrayList<>();
                List<Object> paramsList=new ArrayList<>();
                for (Object param : params) {
                    classList.add(param.getClass());
                    paramsList.add(param);
                }
                target=clazz.getDeclaredMethod(methodName,classList.toArray(new Class[classList.size()]))
                        .invoke(clazz.newInstance(),paramsList.toArray(new Object[paramsList.size()]));
                break;
            }
        }
        return target;
    }
}
