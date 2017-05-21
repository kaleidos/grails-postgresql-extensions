package net.kaleidos.hibernate.usertype

import groovy.transform.CompileStatic
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.usertype.UserType

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

@CompileStatic
class HstoreMapType implements UserType {

    static int SQLTYPE = 90011

    int[] sqlTypes() {
        return SQLTYPE as int[]
    }

    Class<?> returnedClass() {
        Map
    }

    boolean equals(Object x, Object y) throws HibernateException {
        if (x == null) {
            return y == null
        }

        Map m1 = x as Map
        Map m2 = y as Map

        return m1.equals(m2)
    }

    int hashCode(Object x) throws HibernateException {
        x ? x.hashCode() : 0
    }

    Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String col = names[0]
        String val = rs.getString(col)

        return HstoreHelper.toMap(val)
    }

    void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        String s = HstoreHelper.toString(value as Map)
        st.setObject(index, s, Types.OTHER)
    }

    Object deepCopy(Object value) throws HibernateException {
        value == null ? null : new HashMap(value as Map)
    }

    boolean isMutable() {
        true
    }

    Serializable disassemble(Object value) throws HibernateException {
        value as Serializable
    }

    Object assemble(Serializable cached, Object owner) throws HibernateException {
        cached
    }

    Object replace(Object original, Object target, Object owner) throws HibernateException {
        original
    }
}
