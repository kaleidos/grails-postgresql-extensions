package net.kaleidos.hibernate.usertype;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to convert Maps to String according to hstore syntax
 * and vice versa
 */
public class HstoreHelper {

    private static final String K_V_SEPARATOR = "=>";

    public static String toString(Map<String, String> m) {
        if (m == null || m.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int n = m.size();
        for (String key : m.keySet()) {
            sb.append(key + K_V_SEPARATOR + "\"" + String.valueOf(m.get(key)) + "\"");
            if (n > 1) {
                sb.append(", ");
                n--;
            }
        }
        return sb.toString();
    }

    public static Map<String, String> toMap(String s) {
        Map<String, String> m = new HashMap<String, String>();
        if (s == null || s.equals("")) {
            return m;
        }
        String[] tokens = s.split(", ");
        for (String token : tokens) {
            String[] kv = token.split(K_V_SEPARATOR);
            String k = kv[0];
            k = k.trim().substring(1, k.length() - 2);
            String v = kv[1];
            v = v.trim().substring(1, v.length() - 2);
            m.put(k, v);
        }
        return m;
    }
}