package net.kaleidos.hibernate.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class HstoreMapType implements UserType {

    public static int SQLTYPE = 90011;

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public int[] sqlTypes() {
        return new int[]{SQLTYPE};
    }

    public Class<?> returnedClass() {
        return Map.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        Map m1;
        Map m2;

        if (x == null) {
            return y == null;
        }

        m1 = (Map) x;
        m2 = (Map) y;

        return m1.equals(m2);
    }

    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            Map m = (Map) value;
            return new HashMap(m);
        } else {
            return null;
        }
    }

    public boolean isMutable() {
        return true;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @SuppressWarnings({"unchecked"})
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        Map m = (Map) value;
        String s = HstoreHelper.toString(m);
        st.setObject(index, s, Types.OTHER);
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String col = names[0];
        String val = rs.getString(col);

        return HstoreHelper.toMap(val);
    }
}
