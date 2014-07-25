package net.kaleidos.hibernate.usertype;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JsonMapType implements UserType {

    public static int SQLTYPE = 90021;

    private final Type userType = Map.class;

    private final Gson gson = new GsonBuilder().create();

    @Override
    public int[] sqlTypes() {
        return new int[]{SQLTYPE};
    }

    @Override
    public Class returnedClass() {
        return userType.getClass();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String jsonString = rs.getString(names[0]);
        return gson.fromJson(jsonString, userType);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, gson.toJson(value, userType), Types.OTHER);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            Map m = (Map) value;

            if (m == null) {
                m = new HashMap();
            }
            return new HashMap(m);
        } else {
            return null;
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return gson.toJson(value, userType);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return gson.fromJson((String) cached, userType);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}