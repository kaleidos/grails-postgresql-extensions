package net.kaleidos.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
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

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
    throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix( criteriaQuery.findColumns(propertyName, criteria), " @> ARRAY[?]" )
        );
    }

    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
    throws HibernateException {
        Object[] arrValue = new Object[1];
        if (value instanceof Integer) {
            arrValue = new Integer[1];
            arrValue[0] = value;
        } else if (value instanceof String) {
            arrValue = new String[1];
            arrValue[0] = value;
        } else if (value instanceof Long) {
            arrValue = new Long[1];
            arrValue[0] = value;
        } else {
            throw new HibernateException("Native array for this type is not supported");
        }
        return new TypedValue[] {
            criteriaQuery.getTypedValue(criteria, propertyName, arrValue)
        };
    }

    public String toString() {
        return propertyName + " @> ARRAY[" + value + "]";
    }

}
