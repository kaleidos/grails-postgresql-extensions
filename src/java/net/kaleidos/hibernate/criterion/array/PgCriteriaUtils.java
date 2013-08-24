package net.kaleidos.hibernate.criterion.array;

import java.lang.reflect.Array;
import java.util.List;

import org.hibernate.HibernateException;

/**
 * Protected class with utils for the different criteria queries
 */
class PgCriteriaUtils {
    /**
     * Returns a new array wrapping the parameter value. The type of the array
     * will be the type passed as parameter
     * @param targetValue The value we want to wrap as an array
     * @param expectedType The expected type of the returned array
     * @param mapFunction If non-null, it will transform each object in the array to a given object.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType, MapFunction mapFunction) {
        Object[] arrValue;
        if (targetValue instanceof List) {
            List<Object> valueAsList = (List<Object>)targetValue;
            arrValue = (Object[]) Array.newInstance(expectedType, valueAsList.size());

            // We will iterate the collection and if the value it's not a valid value we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (expectedType.isInstance(valueAsList.get(i))) {
                    arrValue[i] = expectedType.cast(valueAsList.get(i));
                } else if (mapFunction != null) {
                    arrValue[i] = expectedType.cast(mapFunction.map(valueAsList.get(i)));
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
        } else {
            throw new HibernateException("criteria doesn't support values of type: " +
                        targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead");
        }
        return arrValue;
    }

    /**
     * Overloaded version of getValueAsArrayOfType that doesn't use a mapFunction
     */
    public Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType) {
        return getValueAsArrayOfType(targetValue, expectedType, null);
    }

    /**
     * Simple class for passing a closure that takes an Object and transforms it into a new value.
     */
    public static abstract class MapFunction {
        /**
         * Transforms an object into some new value.
         * @param o the object we want to transform
         * @return some transformed version of the object
         */
        public abstract Object map(Object o);
    }
}
