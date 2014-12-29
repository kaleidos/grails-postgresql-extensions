package net.kaleidos.hibernate.usertype;

import net.kaleidos.hibernate.postgresql.range.DateRange;
import net.kaleidos.hibernate.postgresql.range.IntegerRange;
import net.kaleidos.hibernate.postgresql.range.LongRange;
import net.kaleidos.hibernate.postgresql.range.TimestampRange;
import net.kaleidos.hibernate.utils.PgRangeUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class RangeType implements UserType, ParameterizedType {
    public static final int INTEGER_RANGE = 90031;
    public static final int LONG_RANGE = 90032;
    public static final int TIMESTAMP_RANGE = 90033;
    public static final int TIMESTAMP_TZ_RANGE = 90034;
    public static final int DATE_RANGE = 90035;

    private Class<?> typeClass;

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
        return this.typeClass;
    }

    @Override
    public int[] sqlTypes() {
        if (IntegerRange.class.equals(this.typeClass)) {
            return new int[]{INTEGER_RANGE};
        }
        if (LongRange.class.equals(this.typeClass)) {
            return new int[]{LONG_RANGE};
        }
        if (DateRange.class.equals(this.typeClass)) {
            return new int[]{DATE_RANGE};
        }
        if (TimestampRange.class.equals(this.typeClass)) {
            return new int[]{TIMESTAMP_RANGE};
        }

        throw new RuntimeException("The type " + this.typeClass + " is not a valid type");
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object result = null;
        result = rs.getObject(names[0]);

        if (IntegerRange.class.equals(this.typeClass)) {
            return PgRangeUtils.parseIntegerRange("" + result);
        }
        if (LongRange.class.equals(this.typeClass)) {
            return PgRangeUtils.parseLongRange("" + result);
        }
        if (DateRange.class.equals(this.typeClass)) {
            return PgRangeUtils.parseDateRange("" + result);
        }
        if (TimestampRange.class.equals(this.typeClass)) {
            return PgRangeUtils.parseTimestampRange("" + result);
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }

        String strValue = null;
        if (IntegerRange.class.equals(this.typeClass)) {
            strValue = PgRangeUtils.format((IntegerRange) value);
        }
        if (LongRange.class.equals(this.typeClass)) {
            strValue = PgRangeUtils.format((LongRange) value);
        }
        if (DateRange.class.equals(this.typeClass)) {
            strValue = PgRangeUtils.format((DateRange) value);
        }
        if (TimestampRange.class.equals(this.typeClass)) {
            strValue = PgRangeUtils.format((TimestampRange) value);
        }
        st.setObject(index, strValue, Types.OTHER);
    }

    public Class<?> getTypeClass() {
        return this.typeClass;
    }
}
