package net.kaleidos.hibernate.array

import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.array.Like

class PgILikeCriteriaTestServiceIntegrationSpec extends Specification {

    def pgArrayTestSearchService

    @Unroll
    void "check ilike for #movie in an array of strings"() {
        setup:
            new Like(favoriteMovies: ["The Matrix", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: ["Spiderman", "Blade Runner", "Starwars"]).save()
            new Like(favoriteMovies: ["Starwars"]).save()
            new Like(favoriteMovies: ["Romeo & Juliet", "Blade Runner", "The Lord of the Rings"]).save()
            new Like(favoriteMovies: []).save()

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
