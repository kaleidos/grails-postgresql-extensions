package test.criteria

class User {

    String name
    Like like

    static mapping = {
        table "pg_extensions_user"
    }

    public String toString() {
        return name
    }
}