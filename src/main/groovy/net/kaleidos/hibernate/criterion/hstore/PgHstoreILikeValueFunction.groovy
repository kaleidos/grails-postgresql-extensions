package net.kaleidos.hibernate.criterion.hstore

import groovy.transform.CompileStatic
import org.hibernate.Criteria
import org.hibernate.HibernateException
import org.hibernate.annotations.common.util.StringHelper
import org.hibernate.criterion.CriteriaQuery

@CompileStatic
class PgHstoreILikeValueFunction extends PgHstoreValueFunction {

    protected PgHstoreILikeValueFunction(String propertyName, Object value) {
        super(propertyName, value, "")
    }

    @Override
    String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "")
        for (int i = 0; i < columns.length; i++) {
            columns[i] = "text(avals(${columns[i]})) ilike ?"
        }
        return StringHelper.join(" and ", columns)
    }
}
