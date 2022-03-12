import java.util.Objects;
import java.util.UUID;

class JSSymbol {
    public JSSymbol(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    private final String name;
    private final UUID uuid;

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid, this.name);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JSSymbol symbol) {
            return this.uuid.equals(symbol.uuid);
        } else {
            return false;
        }
    }
}
