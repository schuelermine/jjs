// This class is only there for debugging purposes, in real JS you’d never see a list of all symbols

import java.util.Set;
import java.util.UUID;

class JSSymbolFactory {
    public JSSymbolFactory(Set<UUID> takenUUIDs) {
        this.takenUUIDs = takenUUIDs;
    }

    private final Set<UUID> takenUUIDs;

    public JSSymbol newSymbol(JSString name) {
        UUID uuid = UUID.randomUUID();
        this.takenUUIDs.add(uuid);
        return new JSSymbol(name, uuid);
    }
}
