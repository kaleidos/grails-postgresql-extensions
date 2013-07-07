package net.kaleidos.hibernate.usertype;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;

public class LongArrayType extends AbstractArrayType{
    public static int SQLTYPE = AbstractArrayType.LONG_ARRAY;

    @Override
    public Class<?> returnedClass() {
        return Long[].class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { SQLTYPE };
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Long[] result = null;
        Array array = (Array) rs.getArray(names[0]);

        if (!rs.wasNull()) {
            result = (Long[]) array.getArray();
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.ARRAY);
        } else {
            Array array;
            array = st.getConnection().createArrayOf("int8", (Long[]) value);
            st.setArray(index, array);
        }
    }
}
