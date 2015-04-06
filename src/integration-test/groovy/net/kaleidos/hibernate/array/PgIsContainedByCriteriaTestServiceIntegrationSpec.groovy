package net.kaleidos.hibernate.array

import org.hibernate.HibernateException
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.array.Like
import test.criteria.array.User

class PgIsContainedByCriteriaTestServiceIntegrationSpec extends Specification {

    def pgArrayTestSearchService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            new Like(favoriteNumbers: [1, 2, 3, 4, 5]).save()
            new Like(favoriteNumbers: [4, 5, 6, 7]).save()
            new Like(favoriteNumbers: [4, 5]).save()
            new Like(favoriteNumbers: [1, 20]).save()
            new Like(favoriteNumbers: [2]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayIsContainedBy', number)

        then:
            result.size() == resultSize

        where:
            number                          | resultSize
            1                               | 0
            2                               | 1
            [1, 20]                         | 1
            [4, 5, 6, 7]                    | 2
            [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] | 4
            []                              | 0
            [1, 20] as Integer[]            | 1
            [4, 5, 6, 7] as Integer[]       | 2
            [] as Integer[]                 | 0
    }


    @Unroll
    void 'search #number in an array of long'() {
        setup:
            new Like(favoriteLongNumbers: [1L, 2L, 3L, 4L, 5L]).save()
            new Like(favoriteLongNumbers: [4L, 5L, 6L, 7L]).save()
            new Like(favoriteLongNumbers: [4L, 5L]).save()
            new Like(favoriteLongNumbers: [1L, 20L]).save()
            new Like(favoriteLongNumbers: [2L]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayIsContainedBy', number)

        then:
            result.size() == resultSize

        where:
            number                                    | resultSize
            1L                                        | 0
            2L                                        | 1
            [1L, 20L]                                 | 1
            [4L, 5L, 6L, 7L]                          | 2
            [1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L] | 4
            []                                        | 0
            [1L, 20L] as Long[]                       | 1
            [4L, 5L, 6L, 7L] as Long[]                | 2
            [] as Long[]                              | 0
    }

    @Unroll
    void 'search #number in an array of float'() {
        setup:
            new Like(favoriteFloatNumbers: [1f, 2f, 3f, 4f, 5f]).save()
            new Like(favoriteFloatNumbers: [4f, 5f, 6f, 7f]).save()
            new Like(favoriteFloatNumbers: [4f, 5f]).save()
            new Like(favoriteFloatNumbers: [1f, 20f]).save()
            new Like(favoriteFloatNumbers: [2f]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayIsContainedBy', number)

        then:
            result.size() == resultSize

        where:
            number                                    | resultSize
            1f                                        | 0
            2f                                        | 1
            [1f, 20f]                                 | 1
            [4f, 5f, 6f, 7f]                          | 2
            [1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f] | 4
            []                                        | 0
            [1f, 20f] as Float[]                      | 1
            [4f, 5f, 6f, 7f] as Float[]               | 2
            [] as Float[]                             | 0
    }


    @Unroll
    void 'search #number in an array of double'() {
        setup:
            new Like(favoriteDoubleNumbers: [1d, 2d, 3d, 4d, 5d]).save()
            new Like(favoriteDoubleNumbers: [4d, 5d, 6d, 7d]).save()
            new Like(favoriteDoubleNumbers: [4d, 5d]).save()
            new Like(favoriteDoubleNumbers: [1d, 20d]).save()
            new Like(favoriteDoubleNumbers: [2d]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayIsContainedBy', number)

        then:
            result.size() == resultSize

        where:
            number                                    | resultSize
            1d                                        | 0
            2d                                        | 1
            [1d, 20d]                                 | 1
            [4d, 5d, 6d, 7d]                          | 2
            [1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d, 10d] | 4
            []                                        | 0
            [1d, 20d] as Double[]                     | 1
            [4d, 5d, 6d, 7d] as Double[]              | 2
            [] as Double[]                            | 0
    }

    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies: ["A", "B", "C", "D", "E"]).save()
            new Like(favoriteMovies: ["D", "E", "F", "G"]).save()
            new Like(favoriteMovies: ["A", "Z"]).save()
            new Like(favoriteMovies: ["B"]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteMovies', 'pgArrayIsContainedBy', movie)

        then:
            result.size() == resultSize

        where:
            movie                                              | resultSize
            "A"                                                | 0
            "B"                                                | 1
            ["A", "Z"]                                         | 1
            ["B", "D", "E", "F", "G"]                          | 2
            ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z"] | 4
            []                                                 | 0
            ["A", "Z"] as String[]                             | 1
            ["B", "D", "E", "F", "G"] as String[]              | 2
            [] as String[]                                     | 0
    }

    @Unroll
    void 'search #juice in an array of enums'() {
        setup:
            new Like(favoriteJuices: [Like.Juice.ORANGE]).save()
            new Like(favoriteJuices: [Like.Juice.PINEAPPLE, Like.Juice.GRAPE, Like.Juice.CARROT, Like.Juice.CRANBERRY]).save()
            new Like(favoriteJuices: [Like.Juice.APPLE]).save()
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.CARROT]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteJuices', 'pgArrayIsContainedBy', juice)

        then:
            result.size() == resultSize

        where:
            juice                                  | resultSize
            Like.Juice.CRANBERRY                   | 0
            Like.Juice.ORANGE                      | 1
            Like.Juice.LEMON                       | 0
            Like.Juice.APPLE                       | 1
            Like.Juice.GRAPE                       | 0
            Like.Juice.PINEAPPLE                   | 0
            Like.Juice.TOMATO                      | 0
            Like.Juice.CARROT                      | 0
            Like.Juice.GRAPEFRUIT                  | 0
            [Like.Juice.ORANGE, Like.Juice.GRAPE]  | 1
            [Like.Juice.GRAPE, Like.Juice.PINEAPPLE,
             Like.Juice.ORANGE, Like.Juice.CARROT] | 2
            [Like.Juice.APPLE]                     | 1
            [Like.Juice.CARROT, Like.Juice.ORANGE] | 2
            []                                     | 0
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name: 'Abe', like: new Like(favoriteMovies: ["A", "B", "D"])).save()
            def user2 = new User(name: 'Bernard', like: new Like(favoriteMovies: ["A", "C"])).save()
            def user3 = new User(name: 'Carl', like: new Like(favoriteMovies: ["A"])).save()
            def user4 = new User(name: 'Dave', like: new Like(favoriteMovies: ["A", "C", "D"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoin('favoriteMovies', 'pgArrayIsContainedBy', movies)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movies = ["A", "B", "C"]
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def user1 = new User(name: 'Abe', like: new Like(favoriteNumbers: [1, 3], favoriteMovies: ["A", "B", "D"])).save()
            def user2 = new User(name: 'Bernard', like: new Like(favoriteNumbers: [1], favoriteMovies: ["A", "C"])).save()
            def user3 = new User(name: 'Carl', like: new Like(favoriteNumbers: [2], favoriteMovies: ["A"])).save()
            def user4 = new User(name: 'Dave', like: new Like(favoriteNumbers: [1, 2], favoriteMovies: ["A", "B", "D"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoinByStringOrInteger('pgArrayIsContainedBy', favoriteMovies: movies, favoriteNumbers: numbers)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movies = ["A", "B", "C"]
            numbers = [1, 2]
    }

    void 'search a invalid list inside the array of integers'() {
        when:
            pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayIsContainedBy', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1, "Test"], [1L], [1, 1L]]
    }

    void 'search a invalid list inside the array of long'() {
        when:
            pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayIsContainedBy', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1L, "Test"], [1], [1L, 1]]
    }

    void 'search a invalid list inside the array of float'() {
        when:
            pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayIsContainedBy', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1f, "Test"], [1], [1f, 1]]
    }

    void 'search a invalid list inside the array of double'() {
        when:
            pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayIsContainedBy', number)

        then:
            thrown HibernateException

        where:
            number << [["Test"], [1d, "Test"], [1], [1d, 1]]
    }

    void 'search a invalid list inside the array of string'() {
        when:
            pgArrayTestSearchService.search('favoriteMovies', 'pgArrayIsContainedBy', movie)

        then:
            thrown HibernateException

        where:
            movie << [[1], ["Test", 1], [1L], ["Test", 1L]]
    }

    void 'search an invalid list juice inside the array of enum'() {
        when:
            pgArrayTestSearchService.search('favoriteJuices', 'pgArrayIsContainedBy', juice)

        then:
            thrown HibernateException

        where:
            juice << [["Test"], ["Test", Like.Juice.ORANGE], [1L], ["Test", 1L]]
    }

}
