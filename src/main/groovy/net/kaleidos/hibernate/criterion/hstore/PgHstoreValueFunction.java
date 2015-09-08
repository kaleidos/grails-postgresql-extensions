package net.kaleidos.hibernate.criterion.hstore;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.StringType;

/**
 * Constrains a property in an hstore
 */
public class PgHstoreValueFunction implements Criterion {
    private static final long serialVersionUID = 2872183637309166619L;

    protected final String propertyName;
    protected final Object value;
    protected final String function;

    protected PgHstoreValueFunction(String propertyName, Object value, String function) {
        this.propertyName = propertyName;
        this.value = value;
        this.function = function;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = function + "(" + columns[i] + "," + "?)";
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
