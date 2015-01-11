package test.criteria.hstore

import test.hstore.TestHstore

class PgHstoreTestSearchService {
    static transactional = false

    List<TestHstore> search(String field, String criteriaName, value) {
        TestHstore.withCriteria {
            "${criteriaName}" field, value
        }
    }
}
