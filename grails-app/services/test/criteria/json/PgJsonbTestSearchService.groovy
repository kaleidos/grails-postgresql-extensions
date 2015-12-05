package test.criteria.json

import test.json.TestMapJsonb

class PgJsonbTestSearchService {
    static transactional = false

    List<TestMapJsonb> search(String criteriaName, String field, value) {
        TestMapJsonb.withCriteria {
            "${criteriaName}" field, value
        }
    }
}