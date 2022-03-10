import java.util.Set;
import java.util.UUID;

class JSSymbolFactory {
    public JSSymbolFactory(Set<UUID> takenUUIDs) {
        this.takenUUIDs = takenUUIDs;
    }

    private Set<UUID> takenUUIDs;

    public JSSymbol mkSymbol(JSString name) {
        UUID uuid = UUID.randomUUID();
        this.takenUUIDs.add(uuid);
        return new JSSymbol(name, uuid);
    }
}
