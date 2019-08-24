package indi.key.mipsemulator.model.info;

/**
 * A range is left-inclusive and right-inclusive.
 */
public class Range<T extends Comparable> {

    private T start;
    private T end;

    public Range(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public Range(Range<T> another) {
        start = another.start;
        end = another.end;
    }

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(T value) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Range<T> another) {
        return start.compareTo(another.start) <= 0 && end.compareTo(another.end) >= 0;
    }

    @SuppressWarnings("unchecked")
    public boolean intersects(Range<T> another) {
        return start.compareTo(another.end) <= 0 && another.start.compareTo(end) <= 0;
    }

    @SuppressWarnings("unchecked")
    public Range<T> intersection(Range<T> another) {
        if (intersects(another)) {
            return new Range<>(start.compareTo(another.start) > 0 ? start : another.start,
                    end.compareTo(another.end) < 0 ? end : another.end);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[" + start.toString() + ", " + end.toString() + "]";
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Range range = (Range) object;
        if (!end.equals(range.end)) {
            return false;
        } else return start.equals(range.start);
    }

    @Override
    public int hashCode() {
        return 31 * start.hashCode() + end.hashCode();
    }
}
