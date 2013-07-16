package net.kaleidos.hibernate.criterion.arrays;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.util.StringHelper;

/**
 * Constrains a property in an array to a value
 */
public class PgContainsExpression extends PgAbstractArrayExpression {

    private static final long serialVersionUID = 1154636989071050823L;

    protected PgContainsExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " @> ARRAY[?]")
        );
    }
}