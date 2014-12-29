package net.kaleidos.hibernate.postgresql.range

class DateRange {
    ObjectRange range

    public DateRange(Date from, Date to) {
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