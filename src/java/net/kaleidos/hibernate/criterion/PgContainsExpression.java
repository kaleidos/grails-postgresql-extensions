package net.kaleidos.hibernate.criterion;

import java.lang.reflect.Array;
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
            arrValue = getValueAs(value, Integer.class);
        } else if ("net.kaleidos.hibernate.usertype.LongArrayType".equals(propertyTypeName)) {
            arrValue = getValueAs(value, Long.class);
        } else if ("net.kaleidos.hibernate.usertype.StringArrayType".equals(propertyTypeName)) {
            arrValue = getValueAs(value, String.class);
        } else {
            throw new HibernateException("Native array for this type is not supported");
        }
        
        return new TypedValue[] {
            criteriaQuery.getTypedValue(criteria, propertyName, arrValue)
        };
    }
    
    @SuppressWarnings("unchecked")    
    private Object[] getValueAs(Object assignedValue, Class<?> classType) {
        Object[] arrValue;
        if (classType.isInstance(assignedValue)) {
            arrValue = (Object[]) Array.newInstance(classType, 1);
            arrValue[0] = classType.cast(assignedValue);
        } else if (assignedValue instanceof List) {
            List<Object> valueAsList = (List<Object>)assignedValue;
            arrValue = (Object[]) Array.newInstance(classType, valueAsList.size());
            
            // We will iterate the collection and if the value it's not an Integer we throw the exception
            for(int i=0; i<valueAsList.size(); i++) {
                if (classType.isInstance(valueAsList.get(i))) {
                    arrValue[i] = classType.cast(valueAsList.get(i));
                } else {
                    throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: " + classType + " or List<" + classType + ">");
                }
            }
        } else {
            throw new HibernateException("pgContains doesn't support values of type: " + assignedValue.getClass().getName() + ". Try: " + classType + " or List<" + classType + ">");
        }
        return arrValue;
    }
}
