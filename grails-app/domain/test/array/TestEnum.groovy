package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestEnum {

    static enum Day {
        MONDAY(0),
        TUESDAY(1),
        WEDNESDAY(2),
        THURSDAY(3),
        FRIDAY(4),
        SATURDAY(5),
        SUNDAY(6)

        final int id
        Day(int id)  { this.id = id }
    }

    Day[] days

    static mapping = {
        days type: ArrayType, params: [type: Day]
    }

    static constraints = {
        days nullable:true
    }
}
