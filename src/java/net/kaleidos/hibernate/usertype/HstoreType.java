package net.kaleidos.hibernate.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class HstoreType implements UserType {
    public static int SQLTYPE = 90011;

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { SQLTYPE };
    }

    @Override
    public Class<?> returnedClass() {
        return Map.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object x, Object y) throws HibernateException {
        Map m1 = (Map) x;
        Map m2 = (Map) y;
        return m1.equals(m2);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object deepCopy(Object value) throws HibernateException {
        // It's not a true deep copy, but we store only String instances, and they
        // are immutable, so it should be OK
        Map m = (Map)value;
        return new HashMap(m);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String col = names[0];
        String val = rs.getString(col);

        return HstoreHelper.toMap(val);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
      String s = HstoreHelper.toString((Map) value);
      st.setObject(index, s, Types.OTHER);
    }

}