import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Yangyong
 * 2023-06-04 16:11
 */
public class JavaMS {
    public Map<String,String> initMap = new HashMap<String, String>(){
        {
            put("user1","张三");
            put("user2","李四");
        }
    };
    public static void main(String[] args) throws Exception{
        JavaMS javaMS = new JavaMS();
        javaMS.removeByValue();
    }
    public void removeByValue(){
//        Set<Map.Entry<String,String>> entries = new CopyOnWriteArraySet<>(initMap.entrySet());
//        for (Map.Entry<String ,String> entry:entries){
//            if ("张三".equals(entry.getValue())){
//                initMap.remove(entry.getKey());
//            }
//        }

        ConcurrentMap<String,String> map = new ConcurrentHashMap<>(initMap);
        map.forEach((k,v) ->{
            if ("张三".equals(v)){
                map.remove(k);
            }
        });
        System.out.println(map);
    }
}
