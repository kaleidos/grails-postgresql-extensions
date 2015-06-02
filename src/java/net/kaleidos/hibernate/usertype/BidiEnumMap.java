package net.kaleidos.hibernate.usertype;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BidiEnumMap implements Serializable {
    private static final long serialVersionUID = 3325751131102095834L;

    public static final String ENUM_ID_ACCESSOR = "getId";

    private static final Log LOG = LogFactory.getLog(BidiEnumMap.class);
    private final Map enumToKey;
    private final Map keytoEnum;

    public BidiEnumMap(Class<?> enumClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Building Bidirectional Enum Map...");
        }

        EnumMap enumToKey = new EnumMap(enumClass);
        HashMap keytoEnum = new HashMap();

        Method idAccessor = getIdAccessor(enumClass);

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

    private Method getIdAccessor(Class<?> enumClass) throws NoSuchMethodException {
        for (Method method : enumClass.getMethods()) {
            if (method.getName().equals(ENUM_ID_ACCESSOR)) {
                return method;
            }
        }
        return enumClass.getMethod("ordinal");
    }

    public Object getEnumValue(int id) {
        return keytoEnum.get(id);
    }

    public int getKey(Object enumValue) {
        return (Integer) enumToKey.get(enumValue);
    }
}
