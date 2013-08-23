package net.kaleidos.hibernate.usertype;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;

import grails.util.GrailsWebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
* This class behaves almost identical to IntegerArrayType in that it
* stores and retrieves an array of ints. The difference, however, is that
* this is used with an Array of Enums, rather than Ints. The Enums are serialized
* to their ordinal value before persisted to the database. On retrieval,
* they are then converted back into their original Enum type.
*
* A lot of this code was pieced together using Grails' Hibernate's IdentityEnumType, as seen here:
* http://grepcode.com/file_/repo1.maven.org/maven2/org.grails/grails-hibernate/2.1.1/org/codehaus/groovy/grails/orm/hibernate/cfg/IdentityEnumType.java/
*
* The BidiEnumMap was lifted 100% from that class. Although it is static, it is declared as Private
* so we are unable to access it without copying the code :(.
*/
public class IdentityEnumArrayType extends IntegerArrayType implements ParameterizedType {
    public static int SQLTYPE = AbstractArrayType.ENUM_INTEGER_ARRAY;
    private static final Log LOG = LogFactory.getLog(IdentityEnumArrayType.class);

    private Class<? extends Enum<?>> enumClass;
    public static final String ENUM_ID_ACCESSOR = "getId";
    public static final String PARAM_ENUM_CLASS = "enumClass";

    @Override
    public Class<?> returnedClass() {
        // This may not be 100%.
        // Is there anyway to return the actual enumClass as an array and not cause issues?
        return Enum[].class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { SQLTYPE };
    }

    public Object idToEnum(Object id) throws HibernateException, SQLException {
        try {
            BidiEnumMap bidiMap = new BidiEnumMap(enumClass);
            return bidiMap.getEnumValue(id);
        } catch (Exception e) {
            throw new HibernateException("Uh oh. Unable to create bidirectional enum map for " + enumClass + " with nested exception: " + e.toString());
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Object result = super.nullSafeGet(rs, names, owner);

        if (result == null) {
            return result;
        } else {
            Integer[] results = (Integer[]) result;
            Object converted = java.lang.reflect.Array.newInstance(enumClass, results.length);

            for (int i = 0; i < results.length; i++) {
                java.lang.reflect.Array.set(converted, i, idToEnum(results[i]));
            }

            return converted;
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        Object converted = value;

        if (value != null) {
            Object[] o = (Object[])value;
            for (int i = 0; i < o.length; i++) {
                if (! (o[i] instanceof Integer)) {
                    o[i] = ((Enum)o[i]).ordinal();
                }
            }
            converted = o;
        }

        super.nullSafeSet(st, converted, index);
    }

    public void setParameterValues(Properties properties) {
        try {
            enumClass = (Class<? extends Enum<?>>)properties.get(PARAM_ENUM_CLASS);
        } catch (Exception e) {
            throw new MappingException("Error mapping Enum Class using IdentityEnumArrayType", e);
        }
    }

    // This code was lifted from IdentityEnumType.java (see class comments for more info)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class BidiEnumMap implements Serializable {

        private static final long serialVersionUID = 3325751131102095834L;
        private final Map enumToKey;
        private final Map keytoEnum;
        private Class keyType;

        private BidiEnumMap(Class<? extends Enum> enumClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Building Bidirectional Enum Map..."));
            }

            @SuppressWarnings("hiding")
            EnumMap enumToKey = new EnumMap(enumClass);
            @SuppressWarnings("hiding")
            HashMap keytoEnum = new HashMap();

            Method idAccessor = enumClass.getMethod(ENUM_ID_ACCESSOR);

            keyType = idAccessor.getReturnType();

            Method valuesAccessor = enumClass.getMethod("values");
            Object[] values = (Object[]) valuesAccessor.invoke(enumClass);

            for (Object value : values) {
                Object id = idAccessor.invoke(value);
                enumToKey.put((Enum) value, id);
                if (keytoEnum.containsKey(id)) {
                    LOG.warn(String.format("Duplicate Enum ID '%s' detected for Enum %s!", id, enumClass.getName()));
                }
                keytoEnum.put(id, value);
            }

            this.enumToKey = Collections.unmodifiableMap(enumToKey);
            this.keytoEnum = Collections.unmodifiableMap(keytoEnum);
        }

        public Object getEnumValue(Object id) {
            return keytoEnum.get(id);
        }

        public Object getKey(Object enumValue) {
            return enumToKey.get(enumValue);
        }
    }
}
