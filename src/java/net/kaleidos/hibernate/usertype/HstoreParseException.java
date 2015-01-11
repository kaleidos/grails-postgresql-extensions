package net.kaleidos.hibernate.usertype;

public class HstoreParseException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public HstoreParseException(String error, int position) {
        super("Error @" + position + " : " + error);
    }
}
