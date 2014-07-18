package net.kaleidos.hibernate.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HstoreMapType extends AbstractHstoreType {

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String col = names[0];
        String val = rs.getString(col);

        return HstoreHelper.toMap(val);
    }
}
