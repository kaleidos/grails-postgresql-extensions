package net.kaleidos.hibernate.array

import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.array.Like
import test.criteria.array.User

class PgIsEmptyOrContainsCriteriaTestServiceIntegrationSpec extends Specification {

    def pgArrayTestSearchService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            def l1 = new Like(favoriteNumbers: [3, 7]).save()
            def l2 = new Like(favoriteNumbers: []).save()
            def l3 = new Like(favoriteNumbers: [3, 4]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteNumbers', 'pgArrayIsEmptyOrContains', number)

        then:
            result.size() == resultSize

        where:
            number              | resultSize
            []                  | 1
            [3]                 | 2
            [4]                 | 1
            [3, 4]              | 1
            [] as Integer[]     | 1
            [3] as Integer[]    | 2
            [3, 4] as Integer[] | 1
    }

    @Unroll
    void 'search #number in an array of longs'() {
        setup:
            new Like(favoriteLongNumbers: [12383L, 2392348L, 3498239L]).save()
            new Like(favoriteLongNumbers: [12383L, 98978L]).save()
            new Like(favoriteLongNumbers: []).save()
            new Like(favoriteLongNumbers: [12383L]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteLongNumbers', 'pgArrayIsEmptyOrContains', number)

        then:
            result.size() == resultSize

        where:
            number                     | resultSize
            [12383L, 98978L]           | 1
            [12383L]                   | 3
            []                         | 1
            [12383L, 98978L] as Long[] | 1
            [12383L] as Long[]         | 3
            [] as Long[]               | 1
    }

    @Unroll
    void 'search #number in an array of floats'() {
        setup:
            new Like(favoriteFloatNumbers: [12383f, 2392348f, 3498239f]).save()
            new Like(favoriteFloatNumbers: [12383f, 98978f]).save()
            new Like(favoriteFloatNumbers: []).save()
            new Like(favoriteFloatNumbers: [12383f]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteFloatNumbers', 'pgArrayIsEmptyOrContains', number)

        then:
            result.size() == resultSize

        where:
            number                      | resultSize
            [12383f, 98978f]            | 1
            [12383f]                    | 3
            []                          | 1
            [12383f, 98978f] as Float[] | 1
            [12383f] as Float[]         | 3
            [] as Float[]               | 1
    }

    @Unroll
    void 'search #number in an array of doubles'() {
        setup:
            new Like(favoriteDoubleNumbers: [12383d, 2392348d, 3498239d]).save()
            new Like(favoriteDoubleNumbers: [12383d, 98978d]).save()
            new Like(favoriteDoubleNumbers: []).save()
            new Like(favoriteDoubleNumbers: [12383d]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteDoubleNumbers', 'pgArrayIsEmptyOrContains', number)

        then:
            result.size() == resultSize

        where:
            number                       | resultSize
            [12383d, 98978d]             | 1
            [12383d]                     | 3
            []                           | 1
            [12383d, 98978d] as Double[] | 1
            [12383d] as Double[]         | 3
            [] as Double[]               | 1
    }

    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: []).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Casablanca", "Starwars"]).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteMovies', 'pgArrayIsEmptyOrContains', movie)

        then:
            result.size() == resultSize

        where:
            movie                                      | resultSize
            ["The Matrix"]                             | 1
            ["Starwars", "Romeo & Juliet"]             | 1
            ["The Lord of the Rings"]                  | 2
            []                                         | 1
            ["Starwars", "Romeo & Juliet"] as String[] | 1
            ["The Lord of the Rings"] as String[]      | 2
            [] as String[]                             | 1
    }

    @Unroll
    void 'search #juice in an array of enums'() {
        setup:
            new Like(favoriteJuices: [Like.Juice.ORANGE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices: [Like.Juice.PINEAPPLE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices: []).save()
            new Like(favoriteJuices: [Like.Juice.ORANGE]).save()

        when:
            def result = pgArrayTestSearchService.search('favoriteJuices', 'pgArrayIsEmptyOrContains', juice)

        then:
            result.size() == resultSize

        where:
            juice                                    | resultSize
            [Like.Juice.ORANGE]                      | 2
            [Like.Juice.GRAPE, Like.Juice.PINEAPPLE] | 1
            []                                       | 1
    }

    @Unroll
    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name: 'John', like: new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name: 'Peter', like: new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name: 'Mary', like: new Like(favoriteMovies: [])).save()
            def user4 = new User(name: 'Jonhny', like: new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "Spiderman"])).save()

        when:
            def result = pgArrayTestSearchService.searchWithJoin('favoriteMovies', 'pgArrayIsEmptyOrContains', movie)

        then:
            result.size() == resultSize

        where:
            movie                         | resultSize
            ['The Matrix']                | 1
            ['Blade Runner', 'Spiderman'] | 2
            []                            | 1
    }
}
