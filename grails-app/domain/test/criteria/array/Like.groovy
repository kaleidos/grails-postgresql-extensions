package test.criteria.array

import net.kaleidos.hibernate.usertype.ArrayType

class Like {

    static belongsTo = User

    Integer[] favoriteNumbers = []
    Long[] favoriteLongNumbers = []
    String[] favoriteMovies = []
    Juice[] favoriteJuices = []
    Double[] favoriteDoubleNumbers = []
    Float[] favoriteFloatNumbers = []
    String[] favoriteMoviesCI = []

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

        favoriteNumbers type:ArrayType, params: [type: Integer]
        favoriteMovies type:ArrayType, params: [type: String]
        favoriteLongNumbers type:ArrayType, params: [type: Long]
        favoriteJuices type:ArrayType, params: [type: Juice]
        favoriteFloatNumbers type:ArrayType, params: [type: Float]
        favoriteDoubleNumbers type:ArrayType, params: [type: Double]
        favoriteMoviesCI type:ArrayType, params: [type: String, caseInsensitive: true]
    }
}
