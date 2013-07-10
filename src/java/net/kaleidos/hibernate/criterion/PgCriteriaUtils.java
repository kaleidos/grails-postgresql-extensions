package net.kaleidos.hibernate.criterion;

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
     * @return
     */
    @SuppressWarnings("unchecked")    
    public Object[] getValueAsArrayOfType(Object targetValue, Class<?> expectedType) {
        Object[] arrValue;
        if (expectedType.isInstance(targetValue)) {
            arrValue = (Object[]) Array.newInstance(expectedType, 1);
            arrValue[0] = expectedType.cast(targetValue);
        } else if (targetValue instanceof List) {
            List<Object> valueAsList = (List<Object>)targetValue;
            arrValue = (Object[]) Array.newInstance(expectedType, valueAsList.size());
            
            // We will iterate the collection and if the value it's not an Integer we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (expectedType.isInstance(valueAsList.get(i))) {
                    arrValue[i] = expectedType.cast(valueAsList.get(i));
                } else {
                    throw new HibernateException("pgContains doesn't support values of type: " + 
                               targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead");
                }
            }
        } else {
            throw new HibernateException("pgContains doesn't support values of type: " + 
                        targetValue.getClass().getName() + ". Try: " + expectedType + " or List<" + expectedType + "> instead");
        }
        return arrValue;
    }
}
