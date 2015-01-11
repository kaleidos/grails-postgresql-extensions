package test.criteria.array

class User {

    String name
    Like like

    static mapping = {
        table "pg_extensions_user"
    }

    String toString() {
        name
    }
}
