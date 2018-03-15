package cn.bluemobi.dylan.http;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuandl on 2017-03-31.
 */

public class JsonParse {
    private static volatile JsonParse jsonParse = null;

    private JsonParse() {
    }

    public static JsonParse getJsonParse() {
        if (jsonParse == null) {
            synchronized (Http.class) {
                if (jsonParse == null) {
                    jsonParse = new JsonParse();
                }
            }
        }
        return jsonParse;
    }

    /**
     * 请求接口返回码
     */
    private String code = "code";
    /**
     * 请求接口返回信息
     */
    private String msg = "msg";
    /**
     * 请求接口返回数据
     */
    private String data = "data";
    /**
     * 请求接口成功的响应码
     */
    private int successCode = 1;

    public JsonParse setCode(String code) {
        this.code = code;
        return this;
    }

    public JsonParse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JsonParse setData(String data) {
        this.data = data;
        return this;
    }

    public JsonParse setSuccessCode(int successCode) {
        this.successCode = successCode;
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    public int getSuccessCode() {
        return successCode;
    }

    /**
     * 初始化json解析
     *
     * @param code        状态码字段
     * @param data        数据字段
     * @param msg         消息字段
     * @param successCode 成功的标识值
     * @return 本类对象
     */
    public JsonParse initJson(String code, String data, String msg, int successCode) {
        setCode(code).setData(data).setMsg(msg).setSuccessCode(successCode);
        return jsonParse;
    }


    /**
     * Json解析
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public ArrayMap<String, Object> jsonParse(String json) throws JSONException {
        ArrayMap<String, Object> arrayMap = JSON.parseObject(json, new TypeReference<ArrayMap<String, Object>>() {
        }.getType());
        ArrayMap<String, Object> returnData = new ArrayMap<String, Object>();
        ArrayMap<String, Object> rrData = new ArrayMap<>();
        String dataStrKey = data;
        if (arrayMap.containsKey(dataStrKey)) {
            Object data = arrayMap.get(dataStrKey);
            if (data instanceof String
                    || data instanceof Integer
                    || data instanceof Long
                    || data instanceof Float
                    || data instanceof Double
                    || data instanceof Character
                    || data instanceof Boolean
                    || data instanceof Byte
                    || data instanceof Short) {
                rrData.put(dataStrKey, data.toString());
                returnData.put(dataStrKey, rrData);
            } else if (data instanceof JSONArray) {
                rrData.put(dataStrKey, data);
                returnData.put(dataStrKey, rrData);
            } else if (data instanceof com.alibaba.fastjson.JSONObject) {
                rrData = JSON.parseObject(data.toString(), new TypeReference<ArrayMap<String, Object>>() {
                }.getType());
                returnData.put(dataStrKey, rrData);
            } else {
                returnData.put(dataStrKey, rrData);
            }
        }
        Set<String> keys2 = arrayMap.keySet();
        for (String s : keys2) {
            if (!s.equals(dataStrKey)) {
                rrData.put(s, arrayMap.get(s));
            }
        }
        returnData.put(dataStrKey, rrData);

        returnData.put(code, getString(arrayMap, code));
        returnData.put(msg, getString(arrayMap, msg));
        return returnData;
    }

    /**
     * 获取map中的值
     *
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static String getString(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return "";
        } else if (isNull(key)) {
            return "";
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data instanceof String) {
                if (isNull2((String) map.get(key))) {
                    return "";
                } else {
                    return map.get(key).toString();
                }
            } else {
                return String.valueOf(map.get(key));
            }

        } else {
            return "";
        }
    }


    /**
     * 获取map中的int值
     *
     * @param map map
     * @param key map的key
     * @return map的int值
     */
    public static int getInt(Map<String, Object> map, String key) {
        return getInt(map, key, 0);
    }

    /**
     * 获取map中的int值
     *
     * @param map          map
     * @param key          map的key
     * @param defaultValue 默认值
     * @return map的int值
     */
    public static int getInt(Map<String, Object> map, String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(map, key));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    /**
     * 获取map中的long值
     *
     * @param map map
     * @param key map的key
     * @return map的int值
     */
    public static long getLong(Map<String, Object> map, String key) {
        return getLong(map, key, 0);
    }

    /**
     * 获取map中的int值
     *
     * @param map          map
     * @param key          map的key
     * @param defaultValue 默认值
     * @return map的int值
     */
    public static long getLong(Map<String, Object> map, String key, int defaultValue) {
        try {
            return Long.parseLong(getString(map, key));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 获取map中的boolean值
     *
     * @param map map
     * @param key map的key
     * @return map的boolean值
     */
    public static boolean getBoolean(Map<String, Object> map, String key) {
        return getBoolean(map, key, false);
    }

    /**
     * 获取map中的boolean值
     *
     * @param map          map
     * @param key          map的key
     * @param defaultValue 默认值
     * @return map的boolean值
     */
    public static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(getString(map, key));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    /**
     * 获取map中的double值
     *
     * @param map map
     * @param key map的key
     * @return map的int值
     */
    public static double getDouble(Map<String, Object> map, String key) {
        return getDouble(map, key, 0.0);
    }

    /**
     * 获取map中的double值
     *
     * @param map map
     * @param key map的key
     * @return map的int值
     */
    public static double getDouble(Map<String, Object> map, String key, double defaultValue) {
        try {
            return Double.parseDouble(getString(map, key));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0.0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 获取map中的值并格式化为人民币的格式
     *
     * @param map map
     * @param key map的key
     * @return map的int值
     */
    public static String getMoney(Map<String, Object> map, String key) {
        try {
            return formatMoney(getDouble(map, key));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "0.00";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0.00";
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * 获取map中的Map对象
     *
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        Map<String, Object> newMap = new ArrayMap<>();
        if (map == null || map.size() == 0) {
            return newMap;
        } else if (isNull(key)) {
            return newMap;
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data == null) {
                return newMap;
            } else {
                if (data instanceof Map) {
                    return (Map<String, Object>) data;
                } else {
                    return newMap;
                }
            }
        } else {
            return newMap;
        }
    }

    /**
     * 获取map中的Map对象
     *
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static List<Map<String,Object>> getList(Map<String, Object> map, String key) {
        List<Map<String,Object>> list = new ArrayList<>();
        if (map == null || map.size() == 0) {
            return list;
        } else if (isNull(key)) {
            return list;
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data == null) {
                return list;
            } else {
                if (data instanceof List) {
                    return (List<Map<String,Object>>) data;
                } else {
                    return list;
                }
            }
        } else {
            return list;
        }
    }

    /**
     * 获取map中的Map对象
     *
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static <T> List<T> getList(Map<String, Object> map, String key, Class<T> beanClass) {
        List<T> list = new ArrayList<>();
        if (map == null || map.size() == 0) {
            return list;
        } else if (isNull(key)) {
            return list;
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data == null) {
                return list;
            } else {
                if (data instanceof List) {
                    return JSON.parseArray(JSON.toJSONString(data), beanClass);
                } else {
                    return list;
                }
            }
        } else {
            return list;
        }
    }


    /**
     * 格式化double为人民币格式
     *
     * @return map的int值
     */
    public static String formatMoney(double money) {
        return new DecimalFormat("0.00").format(money);
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        return null == s || s.equals("") || s.equalsIgnoreCase("null");

    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull2(String s) {
        return null == s || s.equals("");

    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
