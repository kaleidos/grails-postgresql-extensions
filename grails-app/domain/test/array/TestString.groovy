package test.array

import net.kaleidos.hibernate.usertype.StringArrayType

class TestString {

    String[] stringArray

    static mapping = {
        stringArray type:StringArrayType
    }
}
