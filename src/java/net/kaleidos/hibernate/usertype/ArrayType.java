package net.kaleidos.hibernate.usertype;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.hibernate.usertype.ParameterizedType;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class ArrayType implements UserType, ParameterizedType {
    public static final int INTEGER_ARRAY = 90001;
    public static final int LONG_ARRAY = 90002;
    public static final int STRING_ARRAY = 90003;
    public static final int ENUM_INTEGER_ARRAY = 90004;
    public static final int FLOAT_ARRAY = 90005;
    public static final int DOUBLE_ARRAY = 90006;

    private Class<?> typeClass;

    private static Class[] ALLOWED_CLASSES = new Class[]{
        Integer.class,
        Long.class,
        String.class,
        Float.class,
        Double.class
    };

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
        this.typeClass = (Class<?>)parameters.get("type");
        if (typeClass == null) {
            throw new RuntimeException("The user type needs to be configured with the type. None provided");
        }

        // Validate that the passed type is inside the allowed classes
    }




    @Override
    public Class<?> returnedClass() {
        return java.lang.reflect.Array.newInstance(this.typeClass, 0).getClass();
    }

    @Override
    public int[] sqlTypes() {
        if (Integer.class.equals(this.typeClass)){
            return new int[] { INTEGER_ARRAY };
        }

        if (Long.class.equals(this.typeClass)){
            return new int[] { LONG_ARRAY };
        }

        if (String.class.equals(this.typeClass)){
            return new int[] { STRING_ARRAY };
        }

        if (Float.class.equals(this.typeClass)){
            return new int[] { FLOAT_ARRAY };
        }

        if (Double.class.equals(this.typeClass)){
            return new int[] { DOUBLE_ARRAY };
        }

        return new int[] { INTEGER_ARRAY };
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Object[] result = null;
        Class typeArrayClass = java.lang.reflect.Array.newInstance(typeClass, 0).getClass();
        Array array = (Array) rs.getArray(names[0]);
        if (!rs.wasNull()) {
            result = (Object[])typeArrayClass.cast(array.getArray());
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.ARRAY);
        } else {
            Array array;
            Class typeArrayClass = java.lang.reflect.Array.newInstance(typeClass, 0).getClass();
            array = st.getConnection().createArrayOf(getNativeSqlType(typeClass), (Object[])typeArrayClass.cast(value));
            st.setArray(index, array);
        }
    }

    private String getNativeSqlType(Class clazz) {
        if (Integer.class.equals(this.typeClass)){
            return "int";
        }

        if (Long.class.equals(this.typeClass)){
            return "int8";
        }

        if (String.class.equals(this.typeClass)){
            return "varchar";
        }

        if (Float.class.equals(this.typeClass)){
            return "real";
        }

        if (Double.class.equals(this.typeClass)){
            return "double precision";
        }
        return "int";
    }


    public Class<?> getTypeClass() {
        return this.typeClass;
    }
}
