package net.kaleidos.hibernate.usertype;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.hibernate.usertype.ParameterizedType;

public abstract class AbstractArrayType implements UserType, ParameterizedType {
    protected static final int INTEGER_ARRAY = 90001;
    protected static final int LONG_ARRAY = 90002;
    protected static final int STRING_ARRAY = 90003;
    protected static final int ENUM_INTEGER_ARRAY = 90004;

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == null ? y == null : x.equals(y);
    }

    @Override
    public int hashCode(Object value) throws HibernateException {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public void setParameterValues(Properties parameters) {
    }
}
