package net.kaleidos.hibernate.array

import org.hibernate.HibernateException
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.array.Like
import test.criteria.array.User

class PgContainsCriteriaTestServiceIntegrationSpec extends Specification {

    def pgArrayTestSearchService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            new Like(favoriteNumbers: [3, 7, 20]).save()
            new Like(favoriteNumbers: [5, 17, 9, 6, 20]).save()
            new Like(favoriteNumbers: [3, 4, 20]).save()
            new Like(favoriteNumbers: [9, 4, 20]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayContains', number)

        then:
            result.size() == resultSize

        where:
            number              | resultSize
            3                   | 2
            17                  | 1
            9                   | 2
            4                   | 2
            1                   | 0
            20                  | 4
            [3, 4]              | 1
            [3, 4, 7]           | 0
            [4]                 | 2
            [3, 20]             | 2
            []                  | 4
            3 as Integer[]      | 2
            [] as Integer[]     | 4
            [3, 4] as Integer[] | 1
    }

    @Unroll
    void 'search #number in an array of longs'() {
        setup:
            new Like(favoriteLongNumbers: [12383L, 2392348L, 3498239L]).save()
            new Like(favoriteLongNumbers: [12383L, 98978L]).save()
            new Like(favoriteLongNumbers: [-983893849L, 398432423L, 98978L]).save()
            new Like(favoriteLongNumbers: [12383L]).save()
        when:
            def result = pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayContains', number)

        then:
            result.size() == resultSize

        where:
            number                     | resultSize
            12383L                     | 3
            98978L                     | 2
            -983893849L                | 1
            48574L                     | 0
            [12383L, 98978L]           | 1
            [12383L]                   | 3
            []                         | 4
            [12383L, 98978L] as Long[] | 1
            [12383L] as Long[]         | 3
            [] as Long[]               | 4
    }

    @Unroll
    void 'search #number in an array of floats'() {
        setup:
            new Like(favoriteFloatNumbers: [12383f, 2392348f, 3498239f]).save()
            new Like(favoriteFloatNumbers: [12383f, 98978f]).save()
            new Like(favoriteFloatNumbers: [-983893849f, 398432423f, 98978f]).save()
            new Like(favoriteFloatNumbers: [12383f]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayContains', number)

        then:
            result.size() == resultSize

        where:
            number                      | resultSize
            12383f                      | 3
            98978f                      | 2
            -983893849f                 | 1
            48574f                      | 0
            [12383f, 98978f]            | 1
            [12383f]                    | 3
            []                          | 4
            [12383f, 98978f] as Float[] | 1
            [12383f] as Float[]         | 3
            [] as Float[]               | 4
    }

    @Unroll
    void 'search #number in an array of double'() {
        setup:
            new Like(favoriteDoubleNumbers: [12383d, 2392348d, 3498239d]).save()
            new Like(favoriteDoubleNumbers: [12383d, 98978d]).save()
            new Like(favoriteDoubleNumbers: [-983893849d, 398432423d, 98978d]).save()
            new Like(favoriteDoubleNumbers: [12383d]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayContains', number)

        then:
            result.size() == resultSize

        where:
            number                       | resultSize
            12383d                       | 3
            98978d                       | 2
            -983893849d                  | 1
            48574d                       | 0
            [12383d, 98978d]             | 1
            [12383d]                     | 3
            []                           | 4
            [12383d, 98978d] as Double[] | 1
            [12383d] as Double[]         | 3
            [] as Double[]               | 4
    }

    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"]).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Casablanca", "Starwars"]).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteMovies', 'pgArrayContains', movie)

        then:
            result.size() == resultSize

        where:
            movie                                      | resultSize
            "The Matrix"                               | 1
            "The Lord of the Rings"                    | 2
            "Blade Runner"                             | 2
            "Starwars"                                 | 2
            "The Usual Suspects"                       | 0
            ["Starwars", "Romeo & Juliet"]             | 1
            ["The Lord of the Rings"]                  | 2
            []                                         | 4
            ["Starwars", "Romeo & Juliet"] as String[] | 1
            ["The Lord of the Rings"] as String[]      | 2
            [] as String[]                             | 4
    }

    @Unroll
    void 'search #juice in an array of enums'() {
        setup:
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices: [Like.Juice.PINEAPPLE, Like.Juice.GRAPE, Like.Juice.CARROT, Like.Juice.CRANBERRY]).save()
            new Like(favoriteJuices: [Like.Juice.APPLE, Like.Juice.TOMATO, Like.Juice.CARROT]).save()
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.TOMATO, Like.Juice.CARROT]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteJuices', 'pgArrayContains', juice)

        then:
            result.size() == resultSize

        where:
            juice                                    | resultSize
            Like.Juice.CRANBERRY                     | 1
            Like.Juice.ORANGE                        | 2
            Like.Juice.LEMON                         | 0
            Like.Juice.APPLE                         | 1
            Like.Juice.GRAPE                         | 2
            Like.Juice.PINEAPPLE                     | 1
            Like.Juice.TOMATO                        | 2
            Like.Juice.CARROT                        | 3
            Like.Juice.GRAPEFRUIT                    | 0
            [Like.Juice.ORANGE, Like.Juice.GRAPE]    | 1
            [Like.Juice.GRAPE, Like.Juice.PINEAPPLE] | 1
            [Like.Juice.CARROT]                      | 3
            [Like.Juice.CARROT, Like.Juice.TOMATO]   | 2
            []                                       | 4
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name: 'John', like: new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name: 'Peter', like: new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name: 'Mary', like: new Like(favoriteMovies: ["Romeo & Juliet", "Casablanca", "Starwars"])).save()
            def user4 = new User(name: 'Jonhny', like: new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoin('favoriteMovies', 'pgArrayContains', movie)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movie = "Starwars"
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def user1 = new User(name: 'John', like: new Like(favoriteNumbers: [3, 7], favoriteMovies: ["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name: 'Peter', like: new Like(favoriteNumbers: [5, 17, 9, 6], favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name: 'Mary', like: new Like(favoriteNumbers: [3, 4], favoriteMovies: ["Romeo & Juliet", "Casablanca", "Starwars"])).save()
            def user4 = new User(name: 'Jonhny', like: new Like(favoriteNumbers: [9, 4], favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoinByStringOrInteger('pgArrayContains', favoriteMovies: movie, favoriteNumbers: number)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movie = "Starwars"
            number = 4
    }

    void 'search an invalid list inside the array of integers'() {
        when:
            pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayContains', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1, "Test"], [1L], [1, 1L]]
    }

    void 'search an invalid list inside the array of long'() {
        when:
            pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayContains', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1L, "Test"], [1], [1L, 1]]
    }

    void 'search an invalid list inside the array of float'() {
        when:
            pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayContains', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1f, "Test"], [1], [1f, 1]]
    }

    void 'search an invalid list inside the array of double'() {
        when:
            pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayContains', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1d, "Test"], [1], [1d, 1]]
    }

    void 'search an invalid list inside the array of string'() {
        when:
            pgArrayTestSearchService.search('favoriteMovies', 'pgArrayContains', movie)

        then:
            thrown HibernateException

        where:
            movie << [[1], ["Test", 1], [1L], ["Test", 1L]]
    }

    void 'search an invalid list inside the array of enum'() {
        when:
            pgArrayTestSearchService.search('favoriteJuices', 'pgArrayContains', juice)

        then:
            thrown HibernateException

        where:
            juice << [["Test"], [Like.Juice.ORANGE, "Test"], [1L], [Like.Juice.APPLE, 1L]]
    }
}
