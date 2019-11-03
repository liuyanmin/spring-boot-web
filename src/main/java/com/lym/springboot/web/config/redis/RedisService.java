package com.lym.springboot.web.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @ClassName RedisService
 * @Description 封装了常见REDIS缓存操作的service
 * @Author LYM
 * @Date 2018/7/20 16:58
 * @Version 1.3.1
 */
public class RedisService {

    protected RedisTemplate redisTemplate;
    protected ObjectMapper objectMapper;

    private RedisService() {
    }

    public RedisService(RedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 校验参数不能为空
     */
    private void check() {
        Asserts.notNull(redisTemplate,"RedisTemplate");
        Asserts.notNull(objectMapper,"ObjectMapper");
    }

    /**
     * 创建流式API
     * 用例：
     * session.hget("key","field")
     *      .or(()->new ArrayList<T>())
     *      .expire(3600)
     *      .toArray();
     * 注意调用顺序不能混乱
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> RedisSession<T> createSession(Class<T> clazz) {
        check();
        RedisSession<T> session = new RedisSession<>(redisTemplate, objectMapper, clazz);
        return session;
    }

    /**
     * json转换成数组的映射
     * @param clazz
     * @return
     */
    public JavaType arrayType(Class clazz) {
        check();
        return objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
    }

    /**
     * json转换成单个元素的映射
     * @param clazz
     * @return
     */
    public JavaType classType(Class clazz) {
        check();
        return objectMapper.getTypeFactory().constructType(clazz);
    }

    /**
     * 获取hash中的元素并转换，若不存在则从supplier中获取
     * @param key
     * @param field
     * @param supplier
     * @param jsonBind
     * @param <T>
     * @return
     */
    public <T> T hgetOr(String key, String field, Supplier<T> supplier, JavaType jsonBind) {
        check();
        String json = redisTemplate.hget(key, field);
        T result = null;
        if (StringUtils.isNotEmpty(json)) {
            try {
                result = objectMapper.readValue(json, jsonBind);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = supplier.get();
            hset(key, field, result);
        }
        return result;
    }

    /**
     * 获取hash中的数组并转换，若不存在则从supplier中获取
     */
    public <T> List<T> hgetListOr(String key, String field, Supplier<List<T>> supplier, JavaType jsonBind) {
        check();
        List<T> result = hgetList(key, field, jsonBind);
        if (result == null) {
            result = supplier.get();
            hset(key, field, result);
        }
        return result;
    }

    /**
     * 获取hash中的数组并转换，若不存在则从supplier中获取
     */
    public <T> List<T> hgetListOr(String key, String field, Supplier<List<T>> supplier, JavaType jsonBind, int expire) {
        check();
        List<T> result = hgetList(key, field, jsonBind);
        if (result == null) {
            result = supplier.get();
            hset(key, field, result);
            redisTemplate.expire(key, expire);
        }
        return result;
    }

    /**
     * 将元素添加到json形式的数组中，不校验元素之前是否存在
     * @param key
     * @param field
     * @param item
     * @param <T>
     * @return
     */
    public <T> boolean hAppendList(String key, String field, T item) {
        check();
        List<T> result = hgetList(key, field, arrayType(item.getClass()));
        if (result == null) {
            return false;
        }
        result.add(item);
        hset(key, field, result);
        return true;
    }

    /**
     * 获取json数组
     * @param key
     * @param field
     * @param jsonBind
     * @param <T>
     * @return
     */
    public <T> List<T> hgetList(String key, String field, JavaType jsonBind) {
        check();
        String json = redisTemplate.hget(key, field);
        List<T> result = null;
        if (StringUtils.isNotEmpty(json)) {
            try {
                result = objectMapper.readValue(json, jsonBind);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 将元素替换到json形式的数组中，若不存在此元素则添加一个
     * @param key
     * @param field
     * @param newItem
     * @param <T>
     * @return
     */
    public <T> boolean hReplaceList(String key, String field, T newItem) {
        check();
        List<T> result = hgetList(key, field, arrayType(newItem.getClass()));
        if(result == null) {
            result = new ArrayList<>();
        }
        boolean hasUpdate = false;
        for (T t : result) {
            if(newItem.equals(t)) {
                BeanUtils.copyProperties(newItem, t);
                hasUpdate = true;
                break;
            }
        }
        if(!hasUpdate) {
            result.add(newItem);
        }
        hset(key, field, result);
        return true;
    }

    /**
     * 将数据插入到某个Hash的key中
     * @param key
     * @param field
     * @param obj
     */
    public void hset(String key, String field, Object obj) {
        check();
        try {
            if (obj == null) {
                return;
            }
            redisTemplate.hset(key, field, objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> get(String key, JavaType jsonBind) {
        check();
        String json = redisTemplate.get(key);
        List<T> result = null;
        if (StringUtils.isNotEmpty(json)) {
            try {
                result = objectMapper.readValue(json, jsonBind);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void set(String cache, Object obj) {
        check();
        try {
            if (obj == null) {
                return;
            }
            redisTemplate.set(cache, objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除field 中的某个值
     * @param key
     * @param field
     * @param vo
     * @param <T>
     * @return
     */
    public <T> boolean hDelList(String key, String field, T vo) {
        check();
        List<T> result = hgetList(key, field, arrayType(vo.getClass()));
        if (result == null) {
            return false;
        }
        result.remove(vo);
        hset(key, field, result);
        return true;
    }

    /**
     * 批量删除field 中的某些值
     * @param key
     * @param field
     * @param voList
     * @param <T>
     * @return
     */
    public <T> boolean hDelList(String key, String field, List<T> voList) {
        check();
        if (voList == null || voList.size() == 0) {
            return false;
        }
        Class clz = voList.get(0).getClass();
        List<T> result = hgetList(key, field, arrayType(clz));
        if (result == null) {
            return false;
        }
        for (T vo : voList) {
            result.remove(vo);
        }
        hset(key, field, result);
        return true;
    }

    public <T> List<T> getListOr(String key, Supplier<List<T>> supplier, JavaType jsonBind) {
        check();
        List<T> result = get(key, jsonBind);
        if(result == null) {
            result = supplier.get();
            set(key, result);
        }
        return result;
    }

    public <T> List<T> getListOr(String key, Supplier<List<T>> supplier, JavaType jsonBind, int time) {
        check();
        List<T> result = get(key, jsonBind);
        if (result == null) {
            result = supplier.get();
            set(key, result);
            redisTemplate.expire(key, time);
        }
        return result;
    }

    /**
     * 支持流式API的redis会话
     * @param <T>
     */
    public class RedisSession<T> {
        private class Type {
            /**
             * key类型，0-string类型 1-hash类型 2-mhash类型
             */
            public static final int VALUE = 0;
            public static final int HASH = 1;
            public static final int MHASH = 2;

            /**
             * 基本数据类型
             */
            private static final String STRING_TYPE = "java.lang.String";
            private static final String BYTE_TYPE = "java.lang.Byte";
            private static final String SHORT_TYPE = "java.lang.Short";
            private static final String INT_TYPE = "java.lang.Integer";
            private static final String LONG_TYPE = "java.lang.Long";
            private static final String FLOAT_TYPE = "java.lang.Float";
            private static final String DOUBLE_TYPE = "java.lang.Double";
            private static final String CHAR_TYPE = "java.lang.Character";
            private static final String BOOLEAN_TYPE = "java.lang.Boolean";
        }

        /**
         * 默认写入Redis中的数据
         */
        private static final String NODATA = "no data";
        private RedisTemplate redisTemplate;
        private ObjectMapper objectMapper;
        private Class<T> clazz;
        private String key, field, value;
        private List<String> fieldList, valueList;
        /**
         * redis 数据类型
         */
        private int valueType;
        /**
         * 标识redis中是否获取到数据
         */
        private boolean orFlag = false;
        /**
         * 是否开启supplier中数据写入默认值
         */
        private boolean noDataFlag = false;
        /**
         * 是否移除空元素，hmget 命令使用
         */
        private boolean removeEmptyFlag = false;


        public RedisSession(RedisTemplate redisTemplate, ObjectMapper objectMapper, Class<T> clazz) {
            this.redisTemplate = redisTemplate;
            this.objectMapper = objectMapper;
            this.clazz = clazz;
        }

        public RedisSession<T> setRedisTemplate(RedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
            return this;
        }

        public RedisSession<T> clearSession() {
            key = null;
            field = null;
            value = null;
            return this;
        }

        /**
         * 获取哈希中的值
         * @param key
         * @param field
         * @return
         */
        public RedisSession<T> hget(String key, String field) {
            value = redisTemplate.hget(key, field);
            this.key = key;
            this.field = field;
            this.valueType = Type.HASH;
            return this;
        }

        public RedisSession<T> hmget(String key, List<String> fieldList) {
            valueList = redisTemplate.hmget(key, fieldList);
            this.key = key;
            this.fieldList = fieldList;
            this.valueType = Type.MHASH;
            return this;
        }

        /**
         * 直接获取
         * @param key
         * @return
         */
        public RedisSession<T> get(String key) {
            value = redisTemplate.get(key);
            this.key = key;
            this.valueType = Type.VALUE;
            return this;
        }

        /**
         * 当缓存中数据不存在时，从提供的supplier中获取
         * @param supplier
         * @return
         */
        public RedisSession<T> or(Supplier supplier) {
            if (StringUtils.isEmpty(value)) {
                Object orv = supplier.get();
                try {
                    if (orv == null || (orv instanceof List && ((List) orv).size() == 0)) {
                        if (noDataFlag) {
                            value = NODATA;
                        }
                    } else if (!(orv instanceof String)) {
                        value = objectMapper.writeValueAsString(orv);
                    } else {
                        value = (String) orv;
                    }
                    if (value == null) {
                        return this;
                    }
                    orFlag = true;
                    if (valueType == Type.VALUE) {
                        redisTemplate.set(key, value);
                    } else if (valueType == Type.HASH) {
                        redisTemplate.hset(key, field, value);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public RedisSession<T> orList(List<Supplier> suppliers) {
            try {
                if (valueList == null || valueList.size() == 0) {
                    for (int i = 0; i < suppliers.size(); i++) {
                        Supplier supplier = suppliers.get(i);
                        Object orv = supplier.get();

                        if (orv == null || (orv instanceof List && ((List) orv).size() == 0)) {
                            if (noDataFlag) {
                                valueList.remove(i);
                                valueList.add(i, NODATA);
                            }
                        } else if (!(orv instanceof String)) {
                            valueList.remove(i);
                            valueList.add(i, objectMapper.writeValueAsString(orv));
                        } else {
                            valueList.remove(i);
                            valueList.add(i, (String) orv);
                        }
                        if (valueList.get(i) == null) {
                            continue;
                        }
                        orFlag = true;
                        if (valueType == Type.MHASH) {
                            redisTemplate.hset(key, fieldList.get(i), valueList.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < valueList.size(); i++) {
                        String val = valueList.get(i);
                        if (StringUtils.isNotEmpty(val)) {
                            continue;
                        }

                        Supplier supplier = suppliers.get(i);
                        Object orv = supplier.get();

                        if (orv == null || (orv instanceof List && ((List) orv).size() == 0)) {
                            if (noDataFlag) {
                                valueList.remove(i);
                                valueList.add(i, NODATA);
                            }
                        } else if (!(orv instanceof String)) {
                            valueList.remove(i);
                            valueList.add(i, objectMapper.writeValueAsString(orv));
                        } else {
                            valueList.remove(i);
                            valueList.add(i, (String) orv);
                        }
                        if (valueList.get(i) == null) {
                            continue;
                        }
                        orFlag = true;
                        if (valueType == Type.MHASH) {
                            redisTemplate.hset(key, fieldList.get(i), valueList.get(i));
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return this;
        }

        public RedisSession<T> enableNoData() {
            noDataFlag = true;
            return this;
        }

        public RedisSession<T> removeEmptyElement() {
            removeEmptyFlag = true;
            return this;
        }

        /**
         * 设置超时，默认仅当数据更新时设置
         * @param second
         * @return
         */
        public RedisSession<T> expire(int second) {
            expire(second, false);
            return this;
        }

        /**
         * 设置超时，must=true，则立刻设置，must = false，则仅当数据更新时设置（即执行了or操作）
         * @param sec
         * @param must
         * @return
         */
        public RedisSession<T> expire(int sec, boolean must) {
            if(must || orFlag) {
                redisTemplate.expire(key, sec);
            }
            return this;
        }

        /**
         * 转成集合输出
         * @return
         */
        public List<T> toArray() {
            List<T> list = new ArrayList<>();
            try {
                if (StringUtils.isEmpty(value) || NODATA.equals(value)) {
                    return new ArrayList<>();
                }
                Object obj = getBasicTypeValueArray(clazz, value);
                if (obj != null && obj instanceof List) {
                    return (List<T>) obj;
                }
                JavaType type = arrayType(clazz);
                return objectMapper.readValue(value, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        public List<List<T>> toMArray() {
            List<List<T>> list = new ArrayList<>();
            try {
                if (valueList == null || valueList.size() == 0) {
                    return list;
                }
                for (String val : valueList) {
                    if (StringUtils.isEmpty(val) || NODATA.equals(val)) {
                        if (!removeEmptyFlag) {
                            list.add(null);
                        }
                        continue;
                    }

                    Object obj = getBasicTypeValueArray(clazz, val);
                    if (obj != null && obj instanceof List) {
                        list.add((List<T>) obj);
                        continue;
                    }
                    JavaType type = arrayType(clazz);
                    list.add(objectMapper.readValue(val, type));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        /**
         * 转成pojo输出
         * @return
         */
        public T toClass() {
            try {
                if (StringUtils.isEmpty(value) || NODATA.equals(value)) {
                    return null;
                }

                Object obj = getBasicTypeValue(clazz, value);
                if (obj != null) {
                    return (T) obj;
                }
                JavaType type = classType(clazz);
                return objectMapper.readValue(value, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public List<T> toMClass() {
            List<T> list = new ArrayList<>();
            try {
                if (valueList == null || valueList.size() == 0) {
                    return list;
                }

                for (String val : valueList) {
                    if (StringUtils.isEmpty(val) || NODATA.equals(val)) {
                        if (!removeEmptyFlag) {
                            list.add(null);
                        }
                        continue;
                    }

                    Object obj = getBasicTypeValue(clazz, val);
                    if (obj != null) {
                        list.add((T) obj);
                        continue;
                    }
                    JavaType type = classType(clazz);
                    list.add(objectMapper.readValue(val, type));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        /**
         * 直接返回字符串形式的value
         * @return
         */
        public String toStr() {
            return value;
        }

        public List<String> toMStr() {
            return valueList;
        }

        /**
         * 获取基本数据类型值，包括: String,Byte,Short,Integer,Long,Float,Double,Character,Boolean
         * @param clazz
         * @return
         */
        private Object getBasicTypeValue(Class<T> clazz, String value) {
            String typeName = clazz.getTypeName();
            Object obj;
            switch (typeName) {
                case Type.STRING_TYPE:
                    obj = value;
                    break;
                case Type.BYTE_TYPE:
                    obj = Byte.valueOf(value);
                    break;
                case Type.SHORT_TYPE:
                    obj = Short.valueOf(value);
                    break;
                case Type.INT_TYPE:
                    obj = Integer.valueOf(value);
                    break;
                case Type.LONG_TYPE:
                    obj = Long.valueOf(value);
                    break;
                case Type.FLOAT_TYPE:
                    obj = Float.valueOf(value);
                    break;
                case Type.DOUBLE_TYPE:
                    obj = Double.valueOf(value);
                    break;
                case Type.BOOLEAN_TYPE:
                    obj = Boolean.valueOf(value);
                    break;
                case Type.CHAR_TYPE:
                    obj = value;
                    break;
                default:
                    obj = null;
                    break;
            }
            return obj;
        }

        private Object getBasicTypeValueArray(Class<T> clazz, String value) {
            String typeName = clazz.getTypeName();
            Object obj = null;
            try {
                switch (typeName) {
                    case Type.STRING_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<String>>() {
                        });
                        break;
                    case Type.BYTE_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Byte>>() {
                        });
                        break;
                    case Type.SHORT_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Short>>() {
                        });
                        break;
                    case Type.INT_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Integer>>() {
                        });
                        break;
                    case Type.LONG_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Long>>() {
                        });
                        break;
                    case Type.FLOAT_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Float>>() {
                        });
                        break;
                    case Type.DOUBLE_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Double>>() {
                        });
                        break;
                    case Type.BOOLEAN_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Boolean>>() {
                        });
                        break;
                    case Type.CHAR_TYPE:
                        obj = objectMapper.readValue(value, new TypeReference<List<Character>>() {
                        });
                        break;
                    default:
                        obj = null;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }

        public void set(String key, Object obj, int expire) {
            set(key, obj);
            expire(expire, true);
        }

        public void set(String key, Object obj) {
            try {
                this.key = key;
                value = objectMapper.writeValueAsString(obj);
                redisTemplate.set(key, value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        public void hset(String key, String field, Object obj) {
            try {
                this.key = key;
                this.field = field;
                value = objectMapper.writeValueAsString(obj);
                redisTemplate.hset(key, field, value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        /**
         * 插入到数组中，注意实现equal方法，以实现替换原有pojo
         * @param key
         * @param obj
         */
        public void insertToArray(String key, T obj) {
            List<T> list = get(key).toArray();
            int ind = list.indexOf(obj);
            if(ind > 0) {
                list.remove(ind);
            }
            list.add(obj);
            set(key, list);
        }

        /**
         * 插入到数组中，注意实现equal方法，以实现替换原有pojo
         * @param key
         * @param obj
         */
        public void hInsertToArray(String key, String field, T obj) {
            List<T> list = hget(key, field).toArray();
            int ind = list.indexOf(obj);
            if(ind > 0) {
                list.remove(ind);
            }
            list.add(obj);
            hset(key, field, list);
        }
    }

}
