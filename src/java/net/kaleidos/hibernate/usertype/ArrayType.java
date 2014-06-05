package net.kaleidos.hibernate.usertype;

import net.kaleidos.hibernate.utils.PgArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class ArrayType implements UserType, ParameterizedType {
    public static final int INTEGER_ARRAY = 90001;
    public static final int LONG_ARRAY = 90002;
    public static final int STRING_ARRAY = 90003;
    public static final int ENUM_INTEGER_ARRAY = 90004;
    public static final int FLOAT_ARRAY = 90005;
    public static final int DOUBLE_ARRAY = 90006;

    private Class<?> typeClass;
    private BidiEnumMap bidiMap;

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
        this.typeClass = (Class<?>) parameters.get("type");
        if (typeClass == null) {
            throw new RuntimeException("The user type needs to be configured with the type. None provided");
        }
    }

    @Override
    public Class<?> returnedClass() {
        return java.lang.reflect.Array.newInstance(this.typeClass, 0).getClass();
    }

    @Override
    public int[] sqlTypes() {
        if (Integer.class.equals(this.typeClass)) {
            return new int[]{INTEGER_ARRAY};
        }

        if (Long.class.equals(this.typeClass)) {
            return new int[]{LONG_ARRAY};
        }

        if (String.class.equals(this.typeClass)) {
            return new int[]{STRING_ARRAY};
        }

        if (Float.class.equals(this.typeClass)) {
            return new int[]{FLOAT_ARRAY};
        }

        if (Double.class.equals(this.typeClass)) {
            return new int[]{DOUBLE_ARRAY};
        }

        if (this.typeClass.isEnum()) {
            return new int[]{ENUM_INTEGER_ARRAY};
        }

        throw new RuntimeException("The type " + this.typeClass + " is not a valid type");
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object[] result = null;
        Class typeArrayClass = java.lang.reflect.Array.newInstance(typeClass, 0).getClass();
        Array array = rs.getArray(names[0]);
        if (!rs.wasNull()) {
            if (typeClass.isEnum()) {
                int length = java.lang.reflect.Array.getLength(array);
                Object converted = java.lang.reflect.Array.newInstance(typeClass, length);
                for (int i = 0; i < length; i++) {
                    java.lang.reflect.Array.set(converted, i, idToEnum(java.lang.reflect.Array.get(array, i)));
                }
            } else {
                result = (Object[]) typeArrayClass.cast(array.getArray());
            }
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.ARRAY);
            return;
        }

        Object[] valueToSet = (Object[]) value;
        Class typeArrayClass = java.lang.reflect.Array.newInstance(typeClass, 0).getClass();

        if (typeClass.isEnum()) {
            typeArrayClass = Integer[].class;
            Integer[] converted = new Integer[valueToSet.length];

            for (int i = 0; i < valueToSet.length; i++) {
                if (valueToSet[i] instanceof Integer) {
                    converted[i] = (Integer) valueToSet[i];
                } else {
                    converted[i] = ((Enum) valueToSet[i]).ordinal();
                }
            }
            valueToSet = converted;
        }

        Array array = st.getConnection().createArrayOf(PgArrayUtils.getNativeSqlType(typeClass), (Object[]) typeArrayClass.cast(valueToSet));
        st.setArray(index, array);
    }

    public Class<?> getTypeClass() {
        return this.typeClass;
    }

    private Object idToEnum(Object id) throws HibernateException, SQLException {
        try {
            if (bidiMap == null) {
                bidiMap = new BidiEnumMap(this.typeClass);
            }
            return bidiMap.getEnumValue(id);
        } catch (Exception e) {
            throw new HibernateException("Unable to create bidirectional enum map for " + typeClass + " with nested exception: " + e.toString());
        }
    }
}
