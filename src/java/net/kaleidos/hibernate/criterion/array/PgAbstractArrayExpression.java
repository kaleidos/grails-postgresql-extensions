package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.type.Type;

public abstract class PgAbstractArrayExpression implements Criterion {

    private static final long serialVersionUID = 8243965673902347268L;

    protected final PgCriteriaUtils pgCriteriaUtils = new PgCriteriaUtils();
    protected String propertyName;
    protected Object value;

    @Override
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
