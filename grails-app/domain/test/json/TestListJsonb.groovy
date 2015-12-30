package test.json

import net.kaleidos.hibernate.usertype.JsonbListType
import test.Address

class TestListJsonb {

    String name
    List<Address> addresses

    static mapping = {
        addresses type: JsonbListType
    }
}
