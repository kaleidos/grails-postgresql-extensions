package test.array

import net.kaleidos.hibernate.usertype.IdentityEnumArrayType

class TestEnum {

    static enum Day {
        MONDAY(0),
        TUESDAY(1),
        WEDNESDAY(2),
        THURSDAY(3),
        FRIDAY(4),
        SATURDAY(5),
        SUNDAY(6)

        private final int value
        Day(int value)  { this.value = value }
    }

    Day[] days

    static mapping = {
        days type: IdentityEnumArrayType, params: [enumClass: Day]
    }
}
