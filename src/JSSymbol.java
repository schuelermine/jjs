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

    public boolean equals(JSSymbol symbol) {
        return this.uuid.equals(symbol.uuid);
    }
}

