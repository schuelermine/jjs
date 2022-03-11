import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class JSEnvironment {
    public JSEnvironment() {
        this.symbolFactory = new JSSymbolFactory(new HashSet<>());
    }

    // This is where future high-level user-facing
    // implementations of JS operations will go
    private static JSObject objectPrototype = new JSObject(null, null);

    public static JSObject createNormalObject(Set<JSPropertyDeclaration> declarations) {
        final HashMap<String, JSProperty> entries = new HashMap<>();
        for (JSPropertyDeclaration declaration : declarations) {
            entries.put(declaration.getName(), declaration.toJSProperty());
        }
        return new JSObject(entries, objectPrototype);
    }

    private JSSymbol newSymbol(JSString name) {
        return this.symbolFactory.newSymbol(name);
    }

    private final JSSymbolFactory symbolFactory;

    public static final JSObject globalObject = createNormalObject(null);
}