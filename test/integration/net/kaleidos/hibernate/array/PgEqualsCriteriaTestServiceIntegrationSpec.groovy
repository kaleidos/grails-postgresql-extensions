package net.kaleidos.hibernate.array

import grails.plugin.spock.IntegrationSpec
import org.hibernate.HibernateException
import spock.lang.Unroll
import test.criteria.array.Like
import test.criteria.array.User

class PgEqualsCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgArrayTestSearchService

    @Unroll
    void 'check equals for #number in an array of integers'() {
        setup:
            new Like(favoriteNumbers: [3, 7, 20]).save()
            new Like(favoriteNumbers: [17]).save()
            new Like(favoriteNumbers: [3, 4, 20]).save()
            new Like(favoriteNumbers: [9, 4, 20]).save()
            new Like(favoriteNumbers: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayEquals', number)

        then:
            result.size() == resultSize

        where:
            number                  | resultSize
            3                       | 0
            17                      | 1
            [3, 7, 20]              | 1
            [3, 4, 20]              | 1
            [4, 20, 3]              | 0
            []                      | 1
            17 as Integer[]         | 1
            [] as Integer[]         | 1
            [3, 4, 20] as Integer[] | 1
    }

    @Unroll
    void 'check equals for #number in an array of longs'() {
        setup:
            new Like(favoriteLongNumbers: [1L, 23L, 34L]).save()
            new Like(favoriteLongNumbers: [7L]).save()
            new Like(favoriteLongNumbers: [-9L, 16L, 7L]).save()
            new Like(favoriteLongNumbers: [1L]).save()
            new Like(favoriteLongNumbers: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayEquals', number)

        then:
            result.size() == resultSize

        where:
            number         | resultSize
            1L             | 1
            7L             | 1
            [-9L, 16L, 7L] | 1
            [16L, -9L, 7L] | 0
            [1L]           | 1
            []             | 1
            7L as Long[]   | 1
    }

    @Unroll
    void 'check equals for #number in an array of floats'() {
        setup:
            new Like(favoriteFloatNumbers: [12f, 23f, 34f]).save()
            new Like(favoriteFloatNumbers: [12f, 98f]).save()
            new Like(favoriteFloatNumbers: [-98f, 39f, 97]).save()
            new Like(favoriteFloatNumbers: [12f]).save()
            new Like(favoriteFloatNumbers: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayEquals', number)

        then:
            result.size() == resultSize

        where:
            number                | resultSize
            12f                   | 1
            [12f, 98f]            | 1
            [98f, 12f]            | 0
            [12f]                 | 1
            []                    | 1
            [] as Float[]         | 1
            [12f, 98f] as Float[] | 1
    }

    @Unroll
    void 'check equals for #number in an array of doubles'() {
        setup:
            new Like(favoriteDoubleNumbers: [12d, 23d, 34d]).save()
            new Like(favoriteDoubleNumbers: [12d, 98d]).save()
            new Like(favoriteDoubleNumbers: [-98d, 39d, 97d]).save()
            new Like(favoriteDoubleNumbers: [12d]).save()
            new Like(favoriteDoubleNumbers: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayEquals', number)

        then:
            result.size() == resultSize

        where:
            number            | resultSize
            12d               | 1
            [12d]             | 1
            [12d, 98d]        | 1
            []                | 1
            [39d, 97d, -98d]  | 0
            [12d] as Double[] | 1
            [] as Double[]    | 1
    }

    @Unroll
    void 'check equals for #number in an array of strings'() {
        setup:
            new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"]).save()
            new Like(favoriteMovies: ["Starwars"]).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteMovies', 'pgArrayEquals', movie)

        then:
            result.size() == resultSize

        where:
            movie                                               | resultSize
            "Starwars"                                          | 1
            ["Starwars"]                                        | 1
            ["Starwars"] as String[]                            | 1
            "The Usual Suspects"                                | 0
            ["Spiderman", "Blade Runner", "Starwars"]           | 1
            []                                                  | 1
            ["The Matrix", "The Lord of the Rings"] as String[] | 1
            ["The Lord of the Rings", "The Matrix"] as String[] | 0
            [] as String[]                                      | 1
    }

    @Unroll
    void 'check equals for #juice in an array of enums'() {
        setup:
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices: [Like.Juice.PINEAPPLE]).save()
            new Like(favoriteJuices: [Like.Juice.APPLE, Like.Juice.TOMATO, Like.Juice.CARROT]).save()
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.TOMATO, Like.Juice.CARROT]).save()
            new Like(favoriteJuices: []).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteJuices', 'pgArrayEquals', juice)

        then:
            result.size() == resultSize

        where:
            juice                                 | resultSize
            Like.Juice.PINEAPPLE                  | 1
            [Like.Juice.ORANGE, Like.Juice.GRAPE] | 1
            []                                    | 1
            [Like.Juice.GRAPE, Like.Juice.ORANGE] | 0
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name: 'John', like: new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name: 'Peter', like: new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name: 'Mary', like: new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user4 = new User(name: 'Jonhny', like: new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoin('favoriteMovies', 'pgArrayEquals', movie)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movie = ["Spiderman", "Blade Runner", "Starwars"]
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def user1 = new User(name: 'John', like: new Like(favoriteNumbers: [3, 7], favoriteMovies: ["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name: 'Peter', like: new Like(favoriteNumbers: [5, 17, 9, 6], favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name: 'Mary', like: new Like(favoriteNumbers: [5, 17, 9, 6], favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user4 = new User(name: 'Jonhny', like: new Like(favoriteNumbers: [9, 4], favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoinByStringOrInteger('pgArrayEquals', favoriteMovies: movie, favoriteNumbers: number)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movie = ["Spiderman", "Blade Runner", "Starwars"]
            number = [9, 4]
    }

    void 'search an invalid list inside the array of integers'() {
        when:
            pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayEquals', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1, "Test"], [1L], [1, 1L]]
    }

    void 'search an invalid list inside the array of long'() {
        when:
            pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayEquals', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1L, "Test"], [1], [1L, 1]]
    }

    void 'search an invalid list inside the array of float'() {
        when:
            pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayEquals', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1f, "Test"], [1], [1f, 1]]
    }

    void 'search an invalid list inside the array of double'() {
        when:
            pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayEquals', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1d, "Test"], [1], [1d, 1]]
    }

    void 'search an invalid list inside the array of string'() {
        when:
            pgArrayTestSearchService.search('favoriteMovies', 'pgArrayEquals', movie)

        then:
            thrown HibernateException

        where:
            movie << [[1], ["Test", 1], [1L], ["Test", 1L]]
    }

    void 'search an invalid list inside the array of enum'() {
        when:
            pgArrayTestSearchService.search('favoriteJuices', 'pgArrayEquals', juice)

        then:
            thrown HibernateException

        where:
            juice << [["Test"], [Like.Juice.ORANGE, "Test"], [1L], [Like.Juice.APPLE, 1L]]
    }
}
