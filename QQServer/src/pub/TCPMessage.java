package pub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TCPMessage implements Serializable{
    private static final long serialVersionUID = 234567890L;

    private String head = null;
    private Map<String , Object> map = new HashMap<String, Object>();

    public String getHead() {
        return head;
    }

    /**
     * 设置
     * @param head
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * 获取
     * @return map
     */
    public void setMap(String key ,Object value){
        this.map.put(key,value);
    }

    public Object getMap(String key){
        return this.map.get(key);
    }

    public String toString() {
        return "TCPMessage{head = " + head + ", map = " + map + "}";
    }

    public void setBody(String key, Object value) {
        this.map.put(key, value);
    }
    public Object getBody(String key) {
        return this.map.get(key);
    }
}
