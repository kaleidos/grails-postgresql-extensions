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
public class PgEmptinessExpression implements Criterion {

    private static final long serialVersionUID = 2169068982401072268L;

    private final String propertyName;
    private final String op;

    private static final TypedValue[] NO_VALUES = new TypedValue[0];

    protected PgEmptinessExpression(String propertyName, String op) {
        this.propertyName = propertyName;
        this.op = op;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
                " and ",
                StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " " + op + " '{}'")
            );
    }

    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return NO_VALUES;
    }
}




