package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.util.StringHelper;

/**
 * Constrains a property in an array to a value
 */
public class PgIsContainedByExpression extends PgAbstractArrayExpression {

    private static final long serialVersionUID = 5472619803952496831L;

    protected PgIsContainedByExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " <@ ARRAY[?]")
        );
    }
}
