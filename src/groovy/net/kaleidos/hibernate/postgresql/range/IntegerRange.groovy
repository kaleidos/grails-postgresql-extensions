package net.kaleidos.hibernate.postgresql.range

class IntegerRange {
    IntRange range

    public IntegerRange(Integer from, Integer to) {
        range = new IntRange(from, to)
    }

    public Integer getFrom() {
        return range?.from
    }

    public Integer getTo() {
        return range?.to
    }

    public Range toRange() {
        return range
    }
}