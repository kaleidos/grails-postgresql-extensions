package net.kaleidos.hibernate.array

import org.hibernate.HibernateException

import grails.plugin.spock.*
import spock.lang.*

import test.criteria.array.User
import test.criteria.array.Like

class PgIsNotEmptyCriteriaTestServiceIntegrationSpec extends IntegrationSpec {

    def pgIsNotEmptyCriteriaTestService

    void 'search for empty integer arrays'() {
        setup:
            new Like(favoriteNumbers:[3, 7, 20]).save()
            new Like(favoriteNumbers:[]).save()
            new Like(favoriteNumbers:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyIntegerArray()

        then:
            result.size() == 1
    }

    void 'search for empty long arrays'() {
        setup:
            new Like(favoriteLongNumbers:[3L, 7L, 20L]).save()
            new Like(favoriteLongNumbers:[]).save()
            new Like(favoriteLongNumbers:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyLongArray()

        then:
            result.size() == 1
    }

    void 'search for empty float arrays'() {
        setup:
            new Like(favoriteFloatNumbers:[3f, 7f, 20f]).save()
            new Like(favoriteFloatNumbers:[]).save()
            new Like(favoriteFloatNumbers:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyFloatArray()

        then:
            result.size() == 1
    }

    void 'search for empty double arrays'() {
        setup:
            new Like(favoriteDoubleNumbers:[3d, 7d, 20d]).save()
            new Like(favoriteDoubleNumbers:[]).save()
            new Like(favoriteDoubleNumbers:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyDoubleArray()

        then:
            result.size() == 1
    }

    void 'search for empty string arrays'() {
        setup:
            new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies:[]).save()
            new Like(favoriteMovies:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyStringArray()

        then:
            result.size() == 1
    }

    void 'search for empty enum arrays'() {
        setup:
            new Like(favoriteJuices:[Like.Juice.APPLE, Like.Juice.TOMATO]).save()
            new Like(favoriteJuices:[]).save()
            new Like(favoriteJuices:[]).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyEnumArray()

        then:
            result.size() == 1
    }

    void 'search in an array of strings with join with another domain class'() {
        setup:
            def user1 = new User(name:'John', like: new Like(favoriteMovies:["The Matrix", "The Lord of the Rings"])).save()
            def user2 = new User(name:'Peter', like: new Like(favoriteMovies:[])).save()
            def user3 = new User(name:'Mary', like: new Like(favoriteMovies:[])).save()

        when:
            def result = pgIsNotEmptyCriteriaTestService.searchNonEmptyStringArrayWithJoin()

        then:
            result.size() == 1
            result.contains(user1) == true
    }
}
