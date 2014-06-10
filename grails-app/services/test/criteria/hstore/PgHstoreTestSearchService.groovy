package test.criteria.hstore

import test.hstore.TestHstore

class PgHstoreTestSearchService {
    static transactional = false

    List<TestHstore> search(String field, String criteriaName, Object value) {
        TestHstore.withCriteria {
            "${criteriaName}" field, value
        }
    }
}
