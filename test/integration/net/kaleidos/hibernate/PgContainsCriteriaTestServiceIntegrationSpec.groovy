package net.kaleidos.hibernate

import org.hibernate.HibernateException;

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.User
import test.criteria.Like

class PgContainsCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgContainsCriteriaTestService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            new Like(favoriteNumbers:[3, 7, 20]).save()
            new Like(favoriteNumbers:[5, 17, 9, 6, 20]).save()
            new Like(favoriteNumbers:[3, 4, 20]).save()
            new Like(favoriteNumbers:[9, 4, 20]).save()
            
        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaIntegerArray(number)

        then:
            result.size() == resultSize

        where:
            number      | resultSize
               3        |     2
               17       |     1
               9        |     2
               4        |     2
               1        |     0
               20       |     4
               [3,4]    |     1     
               [3,4,7]  |     0
               [4]      |     2
               [3,20]   |     2
               []       |     4
    }
    
    @Unroll
    void 'search #number in an array of longs'() {
        setup:
            new Like(favoriteLongNumbers:[12383L, 2392348L, 3498239L]).save()
            new Like(favoriteLongNumbers:[12383L, 98978L]).save()
            new Like(favoriteLongNumbers:[-983893849L, 398432423L, 98978L]).save()
            new Like(favoriteLongNumbers:[12383L]).save()
        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaLongArray(number)

        then:
            result.size() == resultSize

        where:
              number            | resultSize
              12383L            |     3
              98978L            |     2
            -983893849L         |     1
              48574L            |     0
              [12383L, 98978L]  |     1
              [12383L]          |     3
              []                |     4
    }

    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies:["Spiderman", "Blade Runner", "Starwars"]).save()
            new Like(favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"]).save()
            new Like(favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()

        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaStringArray(movie)

        then:
            result.size() == resultSize

        where:
            movie                                       | resultSize
            "The Matrix"                                |     1
            "The Lord of the Rings"                     |     2
            "Blade Runner"                              |     2
            "Starwars"                                  |     2
            "The Usual Suspects"                        |     0
            ["Starwars", "Romeo & Juliet"]              |     1
            ["The Lord of the Rings"]                   |     2
            []                                          |     4
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name:'John', like: new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name:'Peter', like: new Like(favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name:'Mary', like: new Like(favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"])).save()
            def user4 = new User(name:'Jonhny', like: new Like(favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()
            
        when:
            def result = pgContainsCriteriaTestService.searchStringWithJoin(movie)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movie = "Starwars"
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def user1 = new User(name:'John', like: new Like(favoriteNumbers:[3, 7], favoriteMovies:["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name:'Peter', like: new Like(favoriteNumbers:[5, 17, 9, 6], favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])).save()
            def user3 = new User(name:'Mary', like: new Like(favoriteNumbers:[3, 4], favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"])).save()
            def user4 = new User(name:'Jonhny', like: new Like(favoriteNumbers:[9, 4], favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])).save()
            
        when:
            def result = pgContainsCriteriaTestService.searchStringOrIntergetWithJoin(movie, number)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movie = "Starwars"
            number = 4
    }
    
    @Unroll
    void 'search a invalid list inside the array of integers'() {
        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaIntegerArray(number)

        then:
            thrown(HibernateException)

        where:
            number << [["Test"], [1, "Test"], [1L], [1, 1L]]
    }

    @Unroll
    void 'search a invalid list inside the array of long'() {
        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaLongArray(number)

        then:
            thrown(HibernateException)

        where:
            number << [["Test"], [1L, "Test"], [1], [1L, 1]]
    }
    
    @Unroll
    void 'search a invalid list inside the array of string'() {
        when:
            def result = pgContainsCriteriaTestService.searchWithCriteriaStringArray(movie)

        then:
            thrown(HibernateException)

        where:
            movie << [[1], ["Test", 1], [1L], ["Test", 1L]]
    }

}