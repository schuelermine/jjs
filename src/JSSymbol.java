import java.util.Objects;
import java.util.UUID;

class JSSymbol {
    public JSSymbol(JSString name, UUID uuid) {
        this.name = name.getString();
        this.uuid = uuid;
    }

    private final String name;
    private final UUID uuid;

    public JSString getName() {
        return new JSString(this.name);
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
