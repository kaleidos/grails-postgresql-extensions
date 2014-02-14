package net.kaleidos.hibernate.array

import org.hibernate.HibernateException

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.array.User
import test.criteria.array.Like

class PgIsEmptyOrContainsCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgIsEmptyOrContainsCriteriaTestService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            def l1 = new Like(favoriteNumbers:[3, 7]).save()
            def l2 = new Like(favoriteNumbers:[]).save()
            def l3 = new Like(favoriteNumbers:[3, 4]).save()

        when:
            def result = pgIsEmptyOrContainsCriteriaTestService.searchWithCriteriaIntegerArray(number)

        then:
            result.size() == resultSize

        where:
            number | resultSize
            []     | 1
            [3]    | 2
            [4]    | 1
            [3, 4] | 1
    }

    @Unroll
    void 'search #number in an array of longs'() {
        setup:
            new Like(favoriteLongNumbers:[12383L, 2392348L, 3498239L]).save()
            new Like(favoriteLongNumbers:[12383L, 98978L]).save()
            new Like(favoriteLongNumbers:[]).save()
            new Like(favoriteLongNumbers:[12383L]).save()
        when:
            def result = pgIsEmptyOrContainsCriteriaTestService.searchWithCriteriaLongArray(number)

        then:
            result.size() == resultSize

        where:
              number           | resultSize
              [12383L, 98978L] |     1
              [12383L]         |     3
              []               |     1
    }

    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies:[]).save()
            new Like(favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"]).save()
            new Like(favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()

        when:
            def result = pgIsEmptyOrContainsCriteriaTestService.searchWithCriteriaStringArray(movie)

        then:
            result.size() == resultSize

        where:
            movie                          | resultSize
            ["The Matrix"]                 |     1
            ["Starwars", "Romeo & Juliet"] |     1
            ["The Lord of the Rings"]      |     2
            []                             |     1
    }

    @Unroll
    void 'search #juice in an array of enums'() {
        setup:
            new Like(favoriteJuices:[Like.Juice.ORANGE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices:[Like.Juice.PINEAPPLE, Like.Juice.GRAPE]).save()
            new Like(favoriteJuices:[]).save()
            new Like(favoriteJuices:[Like.Juice.ORANGE]).save()

        when:
            def result = pgIsEmptyOrContainsCriteriaTestService.searchWithCriteriaEnumArray(juice)

        then:
            result.size() == resultSize

        where:
            juice                                       | resultSize
               [Like.Juice.ORANGE]                      |     2
               [Like.Juice.GRAPE, Like.Juice.PINEAPPLE] |     1
               []                                       |     1
    }

    @Unroll
    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name:'John', like: new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name:'Peter', like: new Like(favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name:'Mary', like: new Like(favoriteMovies:[])).save()
            def user4 = new User(name:'Jonhny', like: new Like(favoriteMovies:["Romeo & Juliet", "Blade Runner", "Spiderman"])).save()

        when:
            def result = pgIsEmptyOrContainsCriteriaTestService.searchStringWithJoin(movie)

        then:
            result.size() == resultSize

        where:
            movie                         | resultSize
            ['The Matrix']                | 1
            ['Blade Runner', 'Spiderman'] | 2
            []                            | 1
    }
}
