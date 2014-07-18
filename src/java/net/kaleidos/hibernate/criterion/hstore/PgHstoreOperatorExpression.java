package net.kaleidos.hibernate.criterion.hstore;

import net.kaleidos.hibernate.usertype.HstoreHelper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;

import java.util.Map;

/**
 * Constrains a property in an hstore
 */
public class PgHstoreOperatorExpression implements Criterion {
    private static final long serialVersionUID = 2872183637309166619L;

    private final String propertyName;
    private final Map<Object,String> value;
    private final String operator;

    private static final TypedValue[] NO_VALUES = new TypedValue[0];

    protected PgHstoreOperatorExpression(String propertyName, Map<Object,String> value, String operator) {
        this.propertyName = propertyName;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "");
        for (int i=0; i<columns.length; i++) {
            columns[i] = columns[i] + " " + operator + " '" + HstoreHelper.toString(value) + "'";
        }
        return StringHelper.join( " and ", columns);
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return NO_VALUES;
    }
}
