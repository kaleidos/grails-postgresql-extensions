package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.StringType;

/**
 * Constrains a value "ilike" in an array
 */
public class PgArrayILikeFunction implements Criterion {

    private static final long serialVersionUID = 7475136611436979257L;

    private final String propertyName;
    private final String value;

    protected PgArrayILikeFunction(String propertyName, String value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = "text(" + columns[i] + ") ilike ?";
        }
        return StringHelper.join(" and ", columns);
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[]{
            new TypedValue(new StringType(), value)
        };
    }
}
