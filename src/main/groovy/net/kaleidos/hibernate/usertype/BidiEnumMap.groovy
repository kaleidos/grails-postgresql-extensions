package net.kaleidos.hibernate.usertype

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

@Slf4j
@CompileStatic
public class BidiEnumMap implements Serializable {
    static final String ENUM_ID_ACCESSOR = 'getId'

    private static final long serialVersionUID = 3325751131102095834L
    private final Map enumToKey
    private final Map keytoEnum

    BidiEnumMap(Class<?> enumClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        log.debug 'Building Bidirectional Enum Map...'

        EnumMap enumToKey = new EnumMap(enumClass)
        HashMap keytoEnum = new HashMap()

        Method idAccessor = getIdAccessor(enumClass)
        Object[] values = getEnumValues(enumClass)

        for (Object value : values) {
            Object id = idAccessor.invoke(value)
            enumToKey.put(value as Enum, id)
            if (keytoEnum.containsKey(id)) {
                log.warn(String.format("Duplicate Enum ID '%s' detected for Enum %s!", id, enumClass.name))
            }
            keytoEnum.put(id, value)
        }

        this.enumToKey = Collections.unmodifiableMap(enumToKey)
        this.keytoEnum = Collections.unmodifiableMap(keytoEnum)
    }

    @CompileDynamic
    private Object[] getEnumValues(Class<?> enumClass) {
        enumClass.values()
    }

    private Method getIdAccessor(Class<?> enumClass) {
        def idMethod = enumClass.methods.find { it.name == 'getId' }
        if (!idMethod) {
            idMethod = enumClass.getMethod('ordinal')
        }
        return idMethod
    }

    Object getEnumValue(int id) {
        keytoEnum.get(id)
    }

    int getKey(Object enumValue) {
        enumToKey.get(enumValue) as Integer
    }
}
