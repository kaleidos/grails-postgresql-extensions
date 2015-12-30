package net.kaleidos.hibernate.usertype;

public class JsonbListType extends JsonListType {

    public static int SQLTYPE = 90024;

    @Override
    public int[] sqlTypes() {
        return new int[]{SQLTYPE};
    }
}
