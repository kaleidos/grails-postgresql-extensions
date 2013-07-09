package net.kaleidos.hibernate.criterion;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.type.Type;
import org.hibernate.util.StringHelper;

/**
 * Constrains a property in an array to a value
 */
public class PgContainsExpression implements Criterion {

    private static final long serialVersionUID = 1154636989071050823L;

    private final String propertyName;
    private final Object value;

    protected PgContainsExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix( criteriaQuery.findColumns(propertyName, criteria), " @> ARRAY[?]" )
        );
    }

    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Type propertyType = criteriaQuery.getType(criteria, propertyName);
        String propertyTypeName = propertyType.getName();
        
        Object[] arrValue;
        if ("net.kaleidos.hibernate.usertype.IntegerArrayType".equals(propertyTypeName)) {
            arrValue = getValueAsInteger(value);
        } else if ("net.kaleidos.hibernate.usertype.LongArrayType".equals(propertyTypeName)) {
            arrValue = getValueAsLong(value);
        } else if ("net.kaleidos.hibernate.usertype.StringArrayType".equals(propertyTypeName)) {
            arrValue = getValueAsString(value);
        } else {
            throw new HibernateException("Native array for this type is not supported");
        }
        
        return new TypedValue[] {
            criteriaQuery.getTypedValue(criteria, propertyName, arrValue)
        };
    }
    
    private Object[] getValueAsInteger(Object assignedValue) {
        Integer[] arrValue;
        if (assignedValue instanceof Integer) {
            arrValue = new Integer[1];
            arrValue[0] = (Integer) assignedValue;
        } else if (assignedValue instanceof List) {
            List<Object> valueAsList = (List<Object>)assignedValue;
            arrValue = new Integer[valueAsList.size()];
            
            // We will iterate the collection and if the value it's not an Integer we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (valueAsList.get(i) instanceof Integer) {
                    arrValue[i] = (Integer)valueAsList.get(i); 
                } else {
                    throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: Integer or List<Integer>");
                }
            }
        } else {
            throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: Integer or List<Integer>");
        }
        return arrValue;
    }
    
    private Object[] getValueAsLong(Object assignedValue) {
        Long[] arrValue;
        if (assignedValue instanceof Long) {
            arrValue = new Long[1];
            arrValue[0] = (Long) assignedValue;
        } else if (assignedValue instanceof List) {
            List<Object> valueAsList = (List<Object>)assignedValue;
            arrValue = new Long[valueAsList.size()];
            
            // We will iterate the collection and if the value it's not an Integer we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (valueAsList.get(i) instanceof Long) {
                    arrValue[i] = (Long)valueAsList.get(i); 
                } else {
                    throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: Long or List<Long>");
                }
            }
        } else {
            throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: Long or List<Long>");
        }
        return arrValue;
    }

    private Object[] getValueAsString(Object assignedValue) {
        String[] arrValue;
        if (assignedValue instanceof String) {
            arrValue = new String[1];
            arrValue[0] = (String) assignedValue;
        } else if (assignedValue instanceof List) {
            List<Object> valueAsList = (List<Object>)assignedValue;
            arrValue = new String[valueAsList.size()];
            
            // We will iterate the collection and if the value it's not an Integer we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (valueAsList.get(i) instanceof String) {
                    arrValue[i] = (String)valueAsList.get(i); 
                } else {
                    throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: String or List<String>");
                }
            }
        } else {
            throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: String or List<String>");
        }
        return arrValue;
    }

    public String toString() {
        return propertyName + " @> ARRAY[" + value + "]";
    }

}
