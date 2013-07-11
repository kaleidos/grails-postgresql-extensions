package net.kaleidos.hibernate.criterion;

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
public class PgIsContainedByExpression implements Criterion {
    private static final long serialVersionUID = 5472619803952496831L;
    
    private final PgCriteriaUtils pgCriteriaUtils = new PgCriteriaUtils();
    private final String propertyName;
    private final Object value;

    protected PgIsContainedByExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix( criteriaQuery.findColumns(propertyName, criteria), " <@ ARRAY[?]" )
        );
    }

    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Type propertyType = criteriaQuery.getType(criteria, propertyName);
        String propertyTypeName = propertyType.getName();
        
        Object[] arrValue;
        if ("net.kaleidos.hibernate.usertype.IntegerArrayType".equals(propertyTypeName)) {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(value, Integer.class);
        } else if ("net.kaleidos.hibernate.usertype.LongArrayType".equals(propertyTypeName)) {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(value, Long.class);
        } else if ("net.kaleidos.hibernate.usertype.StringArrayType".equals(propertyTypeName)) {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(value, String.class);
        } else {
            throw new HibernateException("Native array for this type is not supported");
        }
        
        return new TypedValue[] {
            criteriaQuery.getTypedValue(criteria, propertyName, arrValue)
        };
    }
}
