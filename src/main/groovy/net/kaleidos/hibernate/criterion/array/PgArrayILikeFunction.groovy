package net.kaleidos.hibernate.criterion.array

import groovy.transform.CompileStatic
import org.hibernate.Criteria
import org.hibernate.HibernateException
import org.hibernate.annotations.common.util.StringHelper
import org.hibernate.criterion.CriteriaQuery
import org.hibernate.criterion.Criterion
import org.hibernate.engine.spi.TypedValue
import org.hibernate.type.StringType

@CompileStatic
class PgArrayILikeFunction implements Criterion {

    private static final long serialVersionUID = 7475136611436979257L

    private final String propertyName
    private final String value

    protected PgArrayILikeFunction(String propertyName, String value) {
        this.propertyName = propertyName
        this.value = value
    }

    @Override
    String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "")
        for (int i = 0; i < columns.length; i++) {
            columns[i] = "text(${columns[i]}) ilike ?"
        }
        return StringHelper.join(" and ", columns)
    }

    @Override
    TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        new TypedValue(new StringType(), value) as TypedValue[]
    }
}
