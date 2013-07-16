package net.kaleidos.hibernate.criterion.array;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.util.StringHelper;

/**
 * Constrains a property in an array to be empty
 */
public class PgIsEmptyExpression implements Criterion {

    private static final long serialVersionUID = 2169068982401072268L;

    private final String propertyName;

    private static final TypedValue[] NO_VALUES = new TypedValue[0];

    protected PgIsEmptyExpression(String propertyName) {
        this.propertyName = propertyName;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
                " and ",
                StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " = '{}'")
            );
    }

    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return NO_VALUES;
    }
}




