package net.kaleidos.hibernate.criterion.json;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.StringType;

public class PgJsonExpression implements Criterion {

    private static final long serialVersionUID = 8372629374639273L;

    private final String propertyName;
    private final String jsonAttribute;
    private final Object value;
    private final String op;

    protected PgJsonExpression(String propertyName, String jsonAttribute, Object value, String op) {
        this.propertyName = propertyName;
        this.jsonAttribute = jsonAttribute;
        this.value = value;
        this.op = op;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
                " and ",
                StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "->>'" + jsonAttribute + "' " + op + " ?")
        );
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[]{
            new TypedValue(new StringType(), value)
        };
    }
}