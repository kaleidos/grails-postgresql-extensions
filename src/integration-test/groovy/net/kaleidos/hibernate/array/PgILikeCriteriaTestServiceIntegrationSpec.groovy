package net.kaleidos.hibernate.array

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.array.Like
import test.criteria.array.PgArrayTestSearchService

@Integration
@Rollback
class PgILikeCriteriaTestServiceIntegrationSpec extends Specification {

    @Autowired PgArrayTestSearchService pgArrayTestSearchService

    def setup() {
        Like.executeUpdate('delete from Like')
    }

    @Unroll
    void "check ilike for #movie in an array of strings"() {
        setup:
            new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"]).save(flush: true, failOnError: true)
            new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"]).save(flush: true, failOnError: true)
            new Like(favoriteMovies: ["Starwars"]).save(flush: true, failOnError: true)
            new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save(flush: true, failOnError: true)
            new Like(favoriteMovies: []).save(flush: true, failOnError: true)

        when:
            def result = pgArrayTestSearchService.search('favoriteMovies', 'pgArrayILike', movie)

        then:
            result.size() == resultSize

        where:
            movie        | resultSize
            "%tarwar%"   | 2
            "%ider%"     | 1
            "%Suspects%" | 0
            ""           | 0
            "%"          | 5
    }

}
