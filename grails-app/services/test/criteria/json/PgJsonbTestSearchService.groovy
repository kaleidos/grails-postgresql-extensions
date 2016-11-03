package test.criteria.json

import test.json.TestMapJsonb

class PgJsonbTestSearchService {
    static transactional = false

    List<TestMapJsonb> search(String criteriaName, String field, String jsonAttribute, value) {
        TestMapJsonb.withCriteria {
            "${criteriaName}" field, jsonAttribute, value.toString()
        }
    }

    List<TestMapJsonb> search(String criteriaName, String field, String jsonOp, String jsonAttribute, String sqlOp, value) {
        TestMapJsonb.withCriteria {
            "${criteriaName}" field, jsonOp, jsonAttribute, sqlOp, value.toString()
        }
    }

}
