package test.criteria.hstore

import test.hstore.TestHstoreMap

class PgHstoreTestSearchService {
    static transactional = false

    List<TestHstoreMap> search(String field, String criteriaName, value) {
        TestHstoreMap.withCriteria {
            "${criteriaName}" field, value
        }
    }
}
