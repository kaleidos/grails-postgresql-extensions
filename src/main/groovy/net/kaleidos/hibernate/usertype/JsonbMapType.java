package net.kaleidos.hibernate.usertype;

public class JsonbMapType extends JsonMapType {

    public static int SQLTYPE = 90022;

    @Override
    public int[] sqlTypes() {
        return new int[]{SQLTYPE};
    }
}
