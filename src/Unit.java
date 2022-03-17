import java.util.Objects;

public class Unit {
    @Override
    public int hashCode() {
        return Objects.hash(); // = 1
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ESNull;
    }
}
