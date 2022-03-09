import java.util.UUID;

public class JSSymbol {
    public JSSymbol(JSString name, UUID uuid) {
        this.name = name.get();
        this.uuid = uuid;
    }

    private String name;
    private UUID uuid;

    public JSString getName() {
        return new JSString(this.name);
    }

    public boolean equals(JSSymbol symbol) {
        return this.uuid.equals(symbol.uuid);
    }
}

