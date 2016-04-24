package net.kaleidos.hibernate.usertype

import groovy.transform.CompileStatic

/**
 * Converts Maps to String and vice versa according to hstore syntax.
 */
@CompileStatic
class HstoreHelper {

    private static final String K_V_SEPARATOR = "=>"

    private static String escapeQuotes(String text) {
        return text.replaceAll("\"", "'")
    }

    static String toString(Map<Object, String> m) {
        if (!m) {
            return ""
        }

        StringBuilder sb = new StringBuilder()
        int n = m.size()
        for (Object key : m.keySet()) {
            String stringKey = String.valueOf(key)

            sb.append('"').append(escapeQuotes(stringKey)).append('"')
            sb.append(K_V_SEPARATOR)
            sb.append('"').append(escapeQuotes(String.valueOf(m.get(key)))).append('"')

            if (n > 1) {
                sb.append(", ")
                n--
            }
        }
        return sb.toString()
    }

    static String asStatement(Map<String, String> m) {
        if (!m) {
            return ""
        }
        StringBuilder sb = new StringBuilder()
        int n = m.size()
        for (String key : m.keySet()) {
            sb.append('"?"' + K_V_SEPARATOR + '"?"')
            if (n > 1) {
                sb.append(", ")
                n--
            }
        }
        return sb.toString()
    }

    static List<String> asListKeyValue(Map<String, String> m) {
        List<String> result = new LinkedList<String>()
        if (m) {
            for (Map.Entry<String, String> entry : m.entrySet()) {
                result << entry.key
                result << entry.value
            }
        }
        return result
    }

    static Map toMap(String s) {
        if (!s) {
            return new HashMap<String, String>()
        }
        HstoreParser parser = new HstoreParser(s)
        return parser.asMap()
    }
}
