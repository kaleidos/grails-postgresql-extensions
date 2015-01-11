package net.kaleidos.hibernate.utils;

import org.hibernate.HibernateException;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils for the different criteria queries.
 */
public class PgArrayUtils {

    private static final Map<Class<?>, String> CLASS_TO_TYPE_NAME = new HashMap<Class<?>, String>();

    static {
        CLASS_TO_TYPE_NAME.put(Integer.class, "int");
        CLASS_TO_TYPE_NAME.put(Long.class, "int8");
        CLASS_TO_TYPE_NAME.put(String.class, "varchar");
        CLASS_TO_TYPE_NAME.put(Float.class, "float");
        CLASS_TO_TYPE_NAME.put(Double.class, "float8");
    }

    /**
     * Returns a new array wrapping the parameter value. The type of the array
     * will be the type passed as parameter
     *
     * @param targetValue  The value we want to wrap as an array
     * @param expectedType The expected type of the returned array
     * @param mapFunction  If non-null, it will transform each object in the array to a given object.
     * @return an array wrapping the parameter value
     */
    @SuppressWarnings("unchecked")
    public static Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType, MapFunction mapFunction) {
        Object[] arrValue;

        if (targetValue instanceof List) {
            List<Object> valueAsList = (List<Object>) targetValue;
            arrValue = (Object[]) Array.newInstance(expectedType, valueAsList.size());

            for (int i = 0, count = valueAsList.size(); i < count; i++) {
                Object object = valueAsList.get(i);
                if (expectedType.isInstance(object)) {
                    arrValue[i] = expectedType.cast(object);
                } else if (mapFunction != null) {
                    arrValue[i] = expectedType.cast(mapFunction.map(object));
                } else {
                    throw new HibernateException("criteria doesn't support values of type: " +
                            targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead");
                }
            }
        } else if (expectedType.isInstance(targetValue) || mapFunction != null) {
            arrValue = (Object[]) Array.newInstance(expectedType, 1);

            if (mapFunction != null) {
                arrValue[0] = expectedType.cast(mapFunction.map(targetValue));
            } else {
                arrValue[0] = expectedType.cast(targetValue);
            }
        } else if (targetValue instanceof Object[]) {
            arrValue = (Object[]) targetValue;
        } else {
            throw new HibernateException("criteria doesn't support values of type: " +
                    targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead");
        }
        return arrValue;
    }

    /**
     * Overloaded version of getValueAsArrayOfType that doesn't use a mapFunction
     */
    public static Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType) {
        return getValueAsArrayOfType(targetValue, expectedType, null);
    }

    /**
     * Takes an Object and transforms it into a new value.
     */
    public interface MapFunction {
        /**
         * Transforms an object into some new value.
         *
         * @param o the object
         * @return some new value
         */
        Object map(Object o);
    }

    public static String getNativeSqlType(Class<?> clazz) {
        String typeName = CLASS_TO_TYPE_NAME.get(clazz);
        if (typeName != null) {
            return typeName;
        }

        if (clazz.isEnum()) {
            return "int";
        }

        throw new RuntimeException("Type class not valid: " + clazz);
    }
}
