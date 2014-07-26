package net.kaleidos.hibernate.utils

import net.kaleidos.hibernate.postgresql.range.IntegerRange
import net.kaleidos.hibernate.postgresql.range.LongRange
import net.kaleidos.hibernate.postgresql.range.DateRange
import net.kaleidos.hibernate.postgresql.range.TimestampRange

public class PgRangeUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd"
    private static final String TS_FORMAT = "yyyy-MM-dd hh:mm:ss"

    public static IntegerRange parseIntegerRange(String pgResult) {
        String parentRemoved = pgResult.substring(1, pgResult.length() -1)
        String[] splitted = parentRemoved.split(",")
        return new IntegerRange(new Integer(splitted[0]), new Integer(splitted[1]) -1)
    }

    public static LongRange parseLongRange(String pgResult) {
        String parentRemoved = pgResult.substring(1, pgResult.length() -1)
        String[] splitted = parentRemoved.split(",")
        return new LongRange(new Long(splitted[0]), new Long(splitted[1]) -1)
    }

    public static DateRange parseDateRange(String pgResult) {
        String parentRemoved = pgResult.substring(1, pgResult.length() -1)
        String[] splitted = parentRemoved.split(",")
        return new DateRange(Date.parse(DATE_FORMAT, splitted[0]), Date.parse(DATE_FORMAT, splitted[1]-1))
    }

    public static TimestampRange parseTimestampRange(String pgResult) {
        String parentRemoved = pgResult.substring(1, pgResult.length() -1)
        String[] splitted = parentRemoved.split(",")
        return new TimestampRange(Date.parse(TS_FORMAT, splitted[0]), Date.parse(TS_FORMAT, splitted[1]))
    }

    public static String format(IntegerRange range) {
        if (!range) {
            return null
        }
        return "[" + range.getFrom() + ", " + range.getTo() +  "]"
    }

    public static String format(LongRange range) {
        if (!range) {
            return null
        }
        return "[" + range.getFrom() + ", " + range.getTo() +  "]"
    }

    public static String format(DateRange range) {
        if (!range) {
            return null
        }
        return "[" + range.getFrom().format(DATE_FORMAT) + ", " + range.getTo().format(DATE_FORMAT) +  "]"
    }

    public static String format(TimestampRange range) {
        if (!range) {
            return null
        }
        return "[" + range.getFrom().format(TS_FORMAT) + ", " + range.getTo().format(TS_FORMAT) +  "]"
    }
}
