package net.kaleidos.hibernate.usertype;

public class HstoreParseException extends Throwable {
    public HstoreParseException(String error, int position) {
        super("Error @"+position+" : "+error);
    }
}
