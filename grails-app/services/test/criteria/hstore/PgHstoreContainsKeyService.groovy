package test.criteria.array

import test.hstore.TestHstore

class PgHstoreContainsKeyService {
    static transactional = false

    public List<TestHstore> searchElementsWithKey(String key) {
        def result = TestHstore.withCriteria {
            pgHstoreContainsKey 'testAttributes', key
        }
        return result
    }
}
