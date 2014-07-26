package net.kaleidos.hibernate.postgresql.range

class TimestampRange {
    ObjectRange range

    public TimestampRange(Date from, Date to) {
        range = new ObjectRange(from, to)
    }

    public Date getFrom() {
        return range?.from
    }

    public Date getTo() {
        return range?.to
    }

    public Range toRange() {
        return range
    }
}