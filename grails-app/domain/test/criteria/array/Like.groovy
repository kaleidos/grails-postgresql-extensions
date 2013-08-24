package test.criteria.array

import net.kaleidos.hibernate.usertype.IdentityEnumArrayType
import net.kaleidos.hibernate.usertype.IntegerArrayType
import net.kaleidos.hibernate.usertype.LongArrayType
import net.kaleidos.hibernate.usertype.StringArrayType

class Like {

    static belongsTo = User

    Integer[] favoriteNumbers = []
    Long[] favoriteLongNumbers = []
    String[] favoriteMovies = []
    Juice[] favoriteJuices = []

    static enum Juice {
        ORANGE(0),
        APPLE(1),
        GRAPE(2),
        PINEAPPLE(3),
        TOMATO(4),
        GRAPEFRUIT(5),
        CRANBERRY(6),
        CARROT(7),
        LEMON(8)

        private final int value
        Juice(int value)  { this.value = value }
    }

    static mapping = {
        table "pg_extensions_like"

        favoriteNumbers type:IntegerArrayType
        favoriteMovies type:StringArrayType
        favoriteLongNumbers type:LongArrayType
        favoriteJuices type:IdentityEnumArrayType, params: [enumClass: Juice]
    }
}
