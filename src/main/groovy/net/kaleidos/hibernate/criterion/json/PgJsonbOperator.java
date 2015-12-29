package net.kaleidos.hibernate.criterion.json;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;

public class PgJsonbOperator implements Criterion {

    private static final long serialVersionUID = -6374409134611856585L;

    private final String propertyName;
    private final Object value;
    private final String op;

    protected PgJsonbOperator(String propertyName, Object value, String op) {
        this.propertyName = propertyName;
        this.value = value;
        this.op = op;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
            " and ",
            StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), " " + op + " ?")
        );
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[]{
            criteriaQuery.getTypedValue(criteria, propertyName, value)
        };
    }
}