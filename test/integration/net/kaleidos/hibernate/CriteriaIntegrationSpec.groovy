package net.kaleidos.hibernate

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.User
import test.criteria.Like

class CriteriaIntegrationSpec extends IntegrationSpec {

    def criteriaService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            def like1 = new Like(favoriteNumbers:[3, 7], favoriteLongNumbers:[], favoriteMovies:[])
            def user1 = new User(name:'John', like:like1.save())
            user1.save()

            def like2 = new Like(favoriteNumbers:[5, 17, 9, 6], favoriteLongNumbers:[], favoriteMovies:[])
            def user2 = new User(name:'Peter', like:like2.save())
            user2.save()

            def like3 = new Like(favoriteNumbers:[3, 4], favoriteLongNumbers:[], favoriteMovies:[])
            def user3 = new User(name:'Mary', like:like3.save())
            user3.save()

            def like4 = new Like(favoriteNumbers:[9, 4], favoriteLongNumbers:[], favoriteMovies:[])
            def user4 = new User(name:'Jonhny', like:like4.save())
            user4.save()

        when:
            def result = criteriaService.searchWithCriteriaIntegerArray(number)

        then:
            result.size() == resultSize

        where:
            number    | resultSize
               3      |     2
               17     |     1
               9      |     2
               4      |     2
               1      |     0
               [3,4]  |     1     
               [4]    |     2
               []     |     4
    }

    @Unroll
    void 'search #number in an array of longs'() {
        setup:
            def like1 = new Like(favoriteNumbers:[], favoriteLongNumbers:[12383L, 2392348L, 3498239L], favoriteMovies:[])
            def user1 = new User(name:'John', like:like1.save())
            user1.save()

            def like2 = new Like(favoriteNumbers:[], favoriteLongNumbers:[12383L, 98978L], favoriteMovies:[])
            def user2 = new User(name:'Peter', like:like2.save())
            user2.save()

            def like3 = new Like(favoriteNumbers:[], favoriteLongNumbers:[-983893849L, 398432423L, 98978L], favoriteMovies:[])
            def user3 = new User(name:'Mary', like:like3.save())
            user3.save()

            def like4 = new Like(favoriteNumbers:[], favoriteLongNumbers:[12383L], favoriteMovies:[])
            def user4 = new User(name:'Jonhny', like:like4.save())
            user4.save()

        when:
            def result = criteriaService.searchWithCriteriaLongArray(number)

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
            def like1 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["The Matrix", "The Lord of the Rings"])
            def user1 = new User(name:'John', like:like1.save())
            user1.save()

            def like2 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])
            def user2 = new User(name:'Peter', like:like2.save())
            user2.save()

            def like3 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"])
            def user3 = new User(name:'Mary', like:like3.save())
            user3.save()

            def like4 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])
            def user4 = new User(name:'Jonhny', like:like4.save())
            user4.save()

        when:
            def result = criteriaService.searchWithCriteriaStringArray(movie)

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
            def like1 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["The Matrix", "The Lord of the Rings"])
            def user1 = new User(name:'John', like:like1.save())
            user1.save()

            def like2 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])
            def user2 = new User(name:'Peter', like:like2.save())
            user2.save()

            def like3 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"])
            def user3 = new User(name:'Mary', like:like3.save())
            user3.save()

            def like4 = new Like(favoriteNumbers:[], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])
            def user4 = new User(name:'Jonhny', like:like4.save())
            user4.save()

        when:
            def result = criteriaService.searchStringWithJoin(movie)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movie = "Starwars"
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def like1 = new Like(favoriteNumbers:[3, 7], favoriteLongNumbers:[], favoriteMovies:["The Matrix", "The Lord of the Rings"])
            def user1 = new User(name:'John', like:like1.save())
            user1.save()

            def like2 = new Like(favoriteNumbers:[5, 17, 9, 6], favoriteLongNumbers:[], favoriteMovies:["Spiderman", "Blade Runner", "Starwars"])
            def user2 = new User(name:'Peter', like:like2.save())
            user2.save()

            def like3 = new Like(favoriteNumbers:[3, 4], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Casablanca", "Starwars"])
            def user3 = new User(name:'Mary', like:like3.save())
            user3.save()

            def like4 = new Like(favoriteNumbers:[9, 4], favoriteLongNumbers:[], favoriteMovies:["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"])
            def user4 = new User(name:'Jonhny', like:like4.save())
            user4.save()

        when:
            def result = criteriaService.searchStringOrIntergetWithJoin(movie, number)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movie = "Starwars"
            number = 4
    }

}