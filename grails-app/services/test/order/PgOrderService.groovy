package test.order

import static net.kaleidos.hibernate.order.OrderBySqlFormula.sqlFormula

import test.json.TestMapJsonb

class PgOrderService {

    List<TestMapJsonb> orderByJson() {
        return TestMapJsonb.withCriteria {
            order sqlFormula("(data->'name') desc")
        }
    }
}
