package net.kaleidos.hibernate.usertype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType;

/**
 * Helper class to convert Maps to String according to hstore syntax
 * and vice versa
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
            for (String key : m.keySet()) {
                result.add(key);
                result.add(String.valueOf(m.get(key)));
            }
        }
        return result;
    }

    public static HstoreDomainType toHstoreDomainType(String s) {
        Map m = HstoreHelper.toMap(s);

        return new HstoreDomainType(m);
    }

    public static Map toMap(String s) {
        Map<String, String> m = new HashMap<String, String>();
        if (s == null || s.equals("")) {
            return m;
        }
        HstoreParser parser = new HstoreParser(s);
        m = parser.asMap();

        return m;
    }
}
