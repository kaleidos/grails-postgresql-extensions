package test.criteria.hstore

import test.hstore.TestHstore

class PgHstoreIsContainedService {
    static transactional = false

    public List<TestHstore> searchElementsWithValues(Map map) {
        def result = TestHstore.withCriteria {
            pgHstoreIsContained 'testAttributes', map
        }
        return result
    }
}
