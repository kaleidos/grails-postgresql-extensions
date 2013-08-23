package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.type.Type;
import org.hibernate.util.StringHelper;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Constrains a property in an array
 */
public class PgArrayExpression implements Criterion {

    private static final long serialVersionUID = 2872183637309166619L;

    private final PgCriteriaUtils pgCriteriaUtils = new PgCriteriaUtils();

    private final String propertyName;
    private final Object value;
    private final String op;

    protected PgArrayExpression(String propertyName, Object value, String op) {
        this.propertyName = propertyName;
        this.value = value;
        this.op = op;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " " + op + " ARRAY[?]")
        );
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Type propertyType = criteriaQuery.getType(criteria, propertyName);
        String propertyTypeName = propertyType.getName();

        Object[] arrValue;
        if ("net.kaleidos.hibernate.usertype.IntegerArrayType".equals(propertyTypeName)) {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(value, Integer.class);
        } else if ("net.kaleidos.hibernate.usertype.IdentityEnumArrayType".equals(propertyTypeName)) {
            if (value instanceof List) {
                List valueAsList = (List)value;
                arrValue = (Object[]) Array.newInstance(Integer.class, valueAsList.size());

                for(int i=0; i < valueAsList.size(); i++) {
                    arrValue[i] = ((Enum)(valueAsList.get(i))).ordinal();
                }
            } else {
                Object converted = ((Enum)value).ordinal();
                arrValue = pgCriteriaUtils.getValueAsArrayOfType(converted, Integer.class);
            }

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