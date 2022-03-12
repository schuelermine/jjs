import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JSEnvironment {
    public JSEnvironment() {
        this.symbolFactory = new JSSymbolFactory(wellKnownSymbolUUIDs);
    }

    // This is where future high-level user-facing
    // implementations of JS operations will go
    private static JSObject objectPrototype = new JSObject(null, null);

    public static JSObject createNormalObject(Set<JSPropertyDeclaration> declarations, JSObject[] spreadObjects) {
        final HashMap<String, JSProperty> entries = new HashMap<>();
        for (JSPropertyDeclaration declaration : declarations) {
            entries.put(declaration.getName(), declaration.toJSProperty());
        }
        return new JSObject(entries, objectPrototype);
    }

    public static JSObject createNormalObject(Set<JSPropertyDeclaration> declarations) {
        return createNormalObject(declarations, new JSObject[0]);
    }

    static {
        HashSet<UUID> symbolUUIDs = new HashSet<>();
        final UUID iteratorUUID = UUID.randomUUID();
        iteratorSymbol = new JSSymbol("Symbol.iterator", iteratorUUID);
        symbolUUIDs.add(iteratorUUID);
        wellKnownSymbolUUIDs = symbolUUIDs;
    }

    private static Set<UUID> wellKnownSymbolUUIDs;
    private static JSSymbol iteratorSymbol;
    private final JSSymbolFactory symbolFactory;

    public static JSObject globalObject = createNormalObject(null);

    public static JSObject createNormalArray() {
        return null;
    }

    private JSSymbol newSymbol(JSString name) {
        return this.symbolFactory.newSymbol(name);
    }

}