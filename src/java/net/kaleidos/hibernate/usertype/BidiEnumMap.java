package net.kaleidos.hibernate.usertype;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BidiEnumMap implements Serializable {
    private static final long serialVersionUID = 3325751131102095834L;

    public static final String ENUM_ID_ACCESSOR = "getId";

    private static final Log LOG = LogFactory.getLog(BidiEnumMap.class);
    private final Map enumToKey;
    private final Map keytoEnum;
    private Class keyType;

    public BidiEnumMap(Class<?> enumClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Building Bidirectional Enum Map..."));
        }

        EnumMap enumToKey = new EnumMap(enumClass);
        HashMap keytoEnum = new HashMap();

        Method idAccessor = enumClass.getMethod(ENUM_ID_ACCESSOR);

        keyType = idAccessor.getReturnType();

        Method valuesAccessor = enumClass.getMethod("values");
        Object[] values = (Object[]) valuesAccessor.invoke(enumClass);

        for (Object value : values) {
            Object id = idAccessor.invoke(value);
            enumToKey.put((Enum) value, id);
            if (keytoEnum.containsKey(id)) {
                LOG.warn(String.format("Duplicate Enum ID '%s' detected for Enum %s!", id, enumClass.getName()));
            }
            keytoEnum.put(id, value);
        }

        this.enumToKey = Collections.unmodifiableMap(enumToKey);
        this.keytoEnum = Collections.unmodifiableMap(keytoEnum);
    }

    public Object getEnumValue(Object id) {
        return keytoEnum.get(id);
    }

    public Object getKey(Object enumValue) {
        return enumToKey.get(enumValue);
    }
}
