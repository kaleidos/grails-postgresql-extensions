package net.kaleidos.hibernate.usertype;

import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHstoreType implements UserType {
    public static int SQLTYPE = 90011;

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public int[] sqlTypes() {
        return new int[] { SQLTYPE };
    }

    public Class<?> returnedClass() {
        return Map.class;
    }

    @SuppressWarnings("rawtypes")
    public boolean equals(Object x, Object y) throws HibernateException {
        Map m1;
        Map m2;

        if (x == null) {
            return y == null;
        }

        if (x instanceof HstoreDomainType) {
            m1 = ((HstoreDomainType)x).getDataStore();
        } else {
            m1 = (Map) x;
        }

        if (y instanceof HstoreDomainType) {
            m2 = ((HstoreDomainType)y).getDataStore();
        } else {
            m2 = (Map) y;
        }

        return m1.equals(m2);
    }

    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            Map m;
            if (value instanceof HstoreDomainType) {
                m = ((HstoreDomainType)value).getDataStore();
            } else {
                m = (Map)value;
            }

            if (m == null) {
                m = new HashMap();
            }
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        Map m = new HashMap();
        if (value instanceof HstoreDomainType) {
            m = ((HstoreDomainType)value).getDataStore();
        } else if (value instanceof Map) {
            m = (Map)value;
        }
        String s = HstoreHelper.toString(m);
        st.setObject(index, s, Types.OTHER);
    }
}
