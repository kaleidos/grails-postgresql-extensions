package net.kaleidos.hibernate.criterion.hstore;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.CriteriaQuery;

/**
 * Do an hstore content any value that ilikes the parameter?
 */
public class PgHstoreILikeValueFunction extends PgHstoreValueFunction {

    protected PgHstoreILikeValueFunction(String propertyName, Object value) {
        super(propertyName, value, "");
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = StringHelper.suffix(criteriaQuery.findColumns(propertyName, criteria), "");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = "text(avals(" + columns[i] + ")) ilike ?";
        }
        return StringHelper.join(" and ", columns);
    }
}
