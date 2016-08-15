package net.kaleidos.hibernate.utils

import groovy.transform.CompileStatic
import org.hibernate.HibernateException

import java.lang.reflect.Array

/**
 * Utils for the different criteria queries.
 */
@CompileStatic
class PgArrayUtils {

    private static final Map<Class<?>, String> CLASS_TO_TYPE_NAME = [
        (Integer): 'int4',
        (Long)   : 'int8',
        (String) : 'varchar',
        (Float)  : 'float4',
        (Double) : 'float8',
        (UUID)   : 'uuid',
    ]

    /**
     * Returns a new array wrapping the parameter value. The type of the array
     * will be the type passed as parameter
     *
     * @param targetValue The value we want to wrap as an array
     * @param expectedType The expected type of the returned array
     * @param mapFunction If non-null, it will transform each object in the array to a given object.
     * @return an array wrapping the parameter value
     */
    static Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType, MapFunction mapFunction) {
        Object[] arrValue

        if (targetValue instanceof List) {
            List<Object> valueAsList = targetValue as List<Object>
            arrValue = Array.newInstance(expectedType, valueAsList.size()) as Object[]

            int count = valueAsList.size()
            for (int i = 0; i < count; i++) {
                Object object = valueAsList.get(i)
                if (expectedType.isInstance(object)) {
                    arrValue[i] = expectedType.cast(object)
                } else if (mapFunction) {
                    arrValue[i] = expectedType.cast(mapFunction.map(object))
                } else {
                    throw new HibernateException("criteria doesn't support values of type: " +
                        targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead")
                }
            }
        } else if (expectedType.isInstance(targetValue) || mapFunction) {
            arrValue = Array.newInstance(expectedType, 1) as Object[]

            if (mapFunction) {
                arrValue[0] = expectedType.cast(mapFunction.map(targetValue))
            } else {
                arrValue[0] = expectedType.cast(targetValue)
            }
        } else if (targetValue instanceof Object[]) {
            arrValue = targetValue as Object[]
        } else {
            throw new HibernateException("criteria doesn't support values of type: ${targetValue.class.name}." +
                "Try: ${expectedType} or List<${expectedType}> instead")
        }
        return arrValue
    }

    /**
     * Overloaded version of getValueAsArrayOfType that doesn't use a mapFunction
     */
    static Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType) {
        return getValueAsArrayOfType(targetValue, expectedType, null)
    }

    /**
     * Takes an Object and transforms it into a new value.
     */
    interface MapFunction {
        /**
         * Transforms an object into some new value.
         *
         * @param o the object
         * @return some new value
         */
        Object map(Object o)
    }

    static String getNativeSqlType(Class<?> clazz) {
        String typeName = CLASS_TO_TYPE_NAME.get(clazz)
        if (typeName != null) {
            return typeName
        }

        if (clazz.isEnum()) {
            return 'int'
        }

        throw new RuntimeException("Type class not valid: ${clazz}")
    }
}
