import java.util.HashMap;

public class JS {
    // This is where future high-level user-facing implementations of JS operations will go
    private static JSObject objectPrototype = new JSObject(null, null);

    public static JSObject createObject(HashMap<String,JSPropertyDeclaration> entries) {
        JSObject prototype = null
        return new JSObject(entries, prototype);
    }

    public static JSObject globalThis = createObject();

    public class JSPropertyDeclaration {
        private JSPropertyDeclarationType kind;
    
        public enum JSPropertyDeclarationType {
            Value,
            Getter,
            Setter
        }
    }
    
}