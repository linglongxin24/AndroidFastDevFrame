package cn.bluemobi.dylan.fastdev.utils;

import java.io.Serializable;
import java.util.Map;

public class SeralizableMap implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
