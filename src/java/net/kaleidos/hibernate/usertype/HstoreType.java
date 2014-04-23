package net.kaleidos.hibernate.usertype;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;

public class HstoreType extends AbstractHstoreType {

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String col = names[0];
        String val = rs.getString(col);

        return HstoreHelper.toHstoreDomainType(val);
    }

}
