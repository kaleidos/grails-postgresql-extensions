package net.kaleidos.hibernate.usertype;

import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Converts Maps to String and vice versa according to hstore syntax.
 */
public class HstoreHelper {

    private static final String K_V_SEPARATOR = "=>";

    private static String escapeQuotes(String text) {
        return text.replaceAll("\"", "'");
    }

    public static String toString(Map<Object, String> m) {
        if (m == null || m.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int n = m.size();
        for (Object key : m.keySet()) {
            String stringKey = String.valueOf(key);

            sb.append('"').append(escapeQuotes(stringKey)).append('"');
            sb.append(K_V_SEPARATOR);
            sb.append('"').append(escapeQuotes(String.valueOf(m.get(key)))).append('"');

            if (n > 1) {
                sb.append(", ");
                n--;
            }
        }
        return sb.toString();
    }

    public static String asStatement(Map<String, String> m) {
        if (m == null || m.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int n = m.size();
        for (String key : m.keySet()) {
            sb.append("\"?\"" + K_V_SEPARATOR + "\"" + "?" + "\"");
            if (n > 1) {
                sb.append(", ");
                n--;
            }
        }
        return sb.toString();
    }

    public static List<String> asListKeyValue(Map<String, String> m) {
        List<String> result = new LinkedList<String>();
        if (m != null && !m.isEmpty()) {
            for (Map.Entry<String, String> entry : m.entrySet()) {
                result.add(entry.getKey());
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public static HstoreDomainType toHstoreDomainType(String s) {
        Map m = HstoreHelper.toMap(s);

        return new HstoreDomainType(m);
    }

    @SuppressWarnings("rawtypes")
    public static Map toMap(String s) {
        if (s == null || s.equals("")) {
            return new HashMap<String, String>();
        }
        HstoreParser parser = new HstoreParser(s);
        return parser.asMap();
    }
}
