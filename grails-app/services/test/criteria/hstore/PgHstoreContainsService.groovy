package test.criteria.hstore

import test.hstore.TestHstore

class PgHstoreContainsService {
    static transactional = false

    public List<TestHstore> searchElementsWithValues(Map map) {
        def result = TestHstore.withCriteria {
            pgHstoreContains 'testAttributes', map
        }
        return result
    }
}
