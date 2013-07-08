package test.criteria

import net.kaleidos.hibernate.usertype.IntegerArrayType
import net.kaleidos.hibernate.usertype.LongArrayType
import net.kaleidos.hibernate.usertype.StringArrayType

class Like {

    static belongsTo = User

    Integer[] favoriteNumbers = []
    Long[] favoriteLongNumbers = []
    String[] favoriteMovies = []

    static mapping = {
        table "pg_extensions_like"

        favoriteNumbers type:IntegerArrayType
        favoriteMovies type:StringArrayType
        favoriteLongNumbers type:LongArrayType
    }
}