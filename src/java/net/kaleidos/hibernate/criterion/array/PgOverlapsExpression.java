package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.util.StringHelper;

/**
 * Check if two arrays overlaps
 */
public class PgOverlapsExpression extends PgAbstractArrayExpression {

    private static final long serialVersionUID = 8263961731207700428L;

    protected PgOverlapsExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " && ARRAY[?]")
        );
    }
}
