package net.kaleidos.hibernate.postgresql.range

class LongRange {
    ObjectRange range

    public LongRange(Long from, Long to) {
        range = new ObjectRange(from, to)
    }

    public Long getFrom() {
        return range?.from
    }

    public Long getTo() {
        return range?.to
    }

    public Range toRange() {
        return range
    }
}