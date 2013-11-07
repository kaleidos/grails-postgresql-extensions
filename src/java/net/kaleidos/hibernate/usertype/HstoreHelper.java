package net.kaleidos.hibernate.usertype;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

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

    public static String toString(Map<String, String> m) {
        if (m == null || m.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int n = m.size();
        for (String key : m.keySet()) {
            sb.append('"').append(escapeQuotes(key)).append('"');
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

    public static HstoreDomainType toMap(String s) {
        Map<String, String> m = new HashMap<String, String>();
        if (s == null || s.equals("")) {
            return new HstoreDomainType(m);
        }
        String[] tokens = s.split(", ");
        for (String token : tokens) {
            String[] kv = token.split(K_V_SEPARATOR);
            String k = kv[0];
            k = k.trim().substring(1, k.length() - 1);
            String v = kv[1];
            v = v.trim().substring(1, v.length() - 1);
            m.put(k, v);
        }
        return new HstoreDomainType(m);
    }
}
