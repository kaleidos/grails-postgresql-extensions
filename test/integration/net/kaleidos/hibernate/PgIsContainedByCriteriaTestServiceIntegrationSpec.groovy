package net.kaleidos.hibernate

import org.hibernate.HibernateException;

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.User
import test.criteria.Like

class PgIsContainedByCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgIsContainedByCriteriaTestService

    @Unroll
    void 'search #number in an array of integers'() {
        setup:
            new Like(favoriteNumbers:[1, 2, 3, 4, 5]).save()
            new Like(favoriteNumbers:[4, 5, 6, 7]).save()
            new Like(favoriteNumbers:[4, 5]).save()
            new Like(favoriteNumbers:[1, 20]).save()
            new Like(favoriteNumbers:[2]).save()

        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByInteger(number)

        then:
            result.size() == resultSize

        where:
            number                      | resultSize
               1                        |     0
               2                        |     1
               [1,20]                   |     1
               [4,5,6,7]                |     2
               [1,2,3,4,5,6,7,8,9,10]   |     4
               []                       |     0
    }
    
    
    @Unroll
    void 'search #number in an array of long'() {
        setup:
            new Like(favoriteLongNumbers:[1L, 2L, 3L, 4L, 5L]).save()
            new Like(favoriteLongNumbers:[4L, 5L, 6L, 7L]).save()
            new Like(favoriteLongNumbers:[4L, 5L]).save()
            new Like(favoriteLongNumbers:[1L, 20L]).save()
            new Like(favoriteLongNumbers:[2L]).save()

        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByLong(number)

        then:
            result.size() == resultSize

        where:
            number                                          | resultSize
               1L                                           |     0
               2L                                           |     1
               [1L,20L]                                     |     1
               [4L,5L,6L,7L]                                |     2
               [1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L]    |     4
               []                                           |     0
    }
    
    
    @Unroll
    void 'search #movie in an array of strings'() {
        setup:
            new Like(favoriteMovies:["A", "B", "C", "D", "E"]).save()
            new Like(favoriteMovies:["D", "E", "F", "G"]).save()
            new Like(favoriteMovies:["A", "Z"]).save()
            new Like(favoriteMovies:["B"]).save()

        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByString(movie)

        then:
            result.size() == resultSize

        where:
            movie                                                   | resultSize
               "A"                                                  |     0
               "B"                                                  |     1
               ["A", "Z"]                                           |     1
               ["B", "D", "E", "F", "G"]                            |     2
               ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z"]   |     4
               []                                                   |     0
    }
    
    
    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name:'Abe',        like:new Like(favoriteMovies:["A", "B", "D"])).save()
            def user2 = new User(name:'Bernard',    like:new Like(favoriteMovies:["A", "C"])).save()
            def user3 = new User(name:'Carl',       like:new Like(favoriteMovies:["A"])).save()
            def user4 = new User(name:'Dave',       like:new Like(favoriteMovies:["A", "C", "D"])).save()

        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByWithJoin(movies)

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true

        where:
            movies = ["A", "B", "C"]
    }

    void 'search in an array of strings with join with another domain class and or statement'() {
        setup:
            def user1 = new User(name:'Abe',        like:new Like(favoriteNumbers: [1,3], favoriteMovies:["A", "B", "D"])).save()
            def user2 = new User(name:'Bernard',    like:new Like(favoriteNumbers: [1], favoriteMovies:["A", "C"])).save()
            def user3 = new User(name:'Carl',       like:new Like(favoriteNumbers: [2], favoriteMovies:["A"])).save()
            def user4 = new User(name:'Dave',       like:new Like(favoriteNumbers: [1,2], favoriteMovies:["A", "B", "D"])).save()

        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByStringOrInteger(movies, numbers)

        then:
            result.size() == 3
            result.contains(user2) == true
            result.contains(user3) == true
            result.contains(user4) == true

        where:
            movies = ["A","B", "C"]
            numbers = [1, 2]
    }
    
    @Unroll
    void 'search a invalid list inside the array of integers'() {
        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByInteger(number)

        then:
            thrown(HibernateException)

        where:
            number << [["Test"], [1, "Test"], [1L], [1, 1L]]
    }

    @Unroll
    void 'search a invalid list inside the array of long'() {
        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByLong(number)

        then:
            thrown(HibernateException)

        where:
            number << [["Test"], [1L, "Test"], [1], [1L, 1]]
    }
    
    @Unroll
    void 'search a invalid list inside the array of string'() {
        when:
            def result = pgIsContainedByCriteriaTestService.searchIsContainedByString(movie)

        then:
            thrown(HibernateException)

        where:
            movie << [[1], ["Test", 1], [1L], ["Test", 1L]]
    }

}