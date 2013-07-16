package net.kaleidos.hibernate.array

import org.hibernate.HibernateException

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.array.User
import test.criteria.array.Like

class PgIsEmptyCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgIsEmptyCriteriaTestService

    void 'search for empty integer arrays'() {
        setup:
            new Like(favoriteNumbers:[3, 7, 20]).save()
            new Like(favoriteNumbers:[]).save()
            new Like(favoriteNumbers:[]).save()

        when:
            def result = pgIsEmptyCriteriaTestService.searchEmptyIntegerArray()

        then:
            result.size() == 2
    }

    void 'search for empty long arrays'() {
        setup:
            new Like(favoriteLongNumbers:[3L, 7L, 20L]).save()
            new Like(favoriteLongNumbers:[]).save()
            new Like(favoriteLongNumbers:[]).save()

        when:
            def result = pgIsEmptyCriteriaTestService.searchEmptyLongArray()

        then:
            result.size() == 2
    }

    void 'search for empty string arrays'() {
        setup:
            new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies:[]).save()
            new Like(favoriteMovies:[]).save()

        when:
            def result = pgIsEmptyCriteriaTestService.searchEmptyStringArray()

        then:
            result.size() == 2
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name:'John', like: new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name:'Peter', like: new Like(favoriteMovies:[])).save()
            def user3 = new User(name:'Mary', like: new Like(favoriteMovies:[])).save()

        when:
            def result = pgIsEmptyCriteriaTestService.searchEmptyStringArrayWithJoin()

        then:
            result.size() == 2
            result.contains(user2) == true
            result.contains(user3) == true
    }
}