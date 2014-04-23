package net.kaleidos.hibernate.criterion.array;

import net.kaleidos.hibernate.usertype.ArrayType;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.type.Type;
import org.hibernate.type.CustomType;
import org.hibernate.util.StringHelper;

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

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " " + op + " ARRAY[?]")
        );
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Type propertyType = criteriaQuery.getType(criteria, propertyName);

        if (!(propertyType instanceof CustomType) || !(((CustomType)propertyType).getUserType() instanceof ArrayType)) {
            throw new HibernateException("Property is not an instance of the postgres type ArrayType. Type is: " + propertyType.getClass());
        }

        ArrayType arrayType = (ArrayType)((CustomType)propertyType).getUserType();

        Object[] arrValue;
        if (arrayType.getTypeClass().isEnum()) {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(
                value,
                Integer.class,
                new PgCriteriaUtils.MapFunction() {
                    @SuppressWarnings("rawtypes")
                    public Object map(Object o) {
                        try {
                            return ((Enum)o).ordinal();
                        } catch (ClassCastException e) {
                            throw new HibernateException("Unable to cast object " + o + " to Enum.");
                        }
                    }
                }
            );
        } else {
            arrValue = pgCriteriaUtils.getValueAsArrayOfType(value, arrayType.getTypeClass());
        }

        return new TypedValue[] {
            criteriaQuery.getTypedValue(criteria, propertyName, arrValue)
        };
    }
}
