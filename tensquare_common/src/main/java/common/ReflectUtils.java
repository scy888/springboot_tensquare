package common;

import java.util.ArrayList;
import java.util.List;
/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: common
 * @date: 2020-04-01 23:38:18
 * @describe: 反射调用工具类
 */
public class ReflectUtils {
    private static final ReflectUtils instance_=new ReflectUtils();

    public static ReflectUtils getInstance(){
        return instance_;
    }

    public <T,V> V reflectMethodByParams(T t,String methodName,Object...params) throws Exception {
        V v=null;
        Class<T> clazz = (Class<T>) t.getClass();
        T instance = clazz.newInstance();
            List<Class> calssList=new ArrayList<>();
            List<Object> paramList=new ArrayList<>();
            for (Object param : params) {
                calssList.add(param.getClass());
                paramList.add(param);
            }
            v= (V) clazz.getDeclaredMethod(methodName, calssList.toArray(new Class[calssList.size()]))
                    .invoke(instance,paramList.toArray(new Object[paramList.size()]));
        return v;
    }
}
