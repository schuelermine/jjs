import java.util.HashMap;

public class JS {
    // This is where future high-level user-facing implementations of JS operations will go
    private static JSObject objectPrototype = new JSObject(null, null);

    public static JSObject createObject(HashMap<String,JSPropertyDeclaration> entries) {
        JSObject prototype = null;
        return new JSObject(entries, prototype);
    }

    public static JSObject globalThis = createObject(null);

    public class JSPropertyDeclaration {
        public JSPropertyDeclaration(String name, JSValue value) {
            if (name == null || value == null) {
                throw new NullPointerException();
            }
            this.type = JSPropertyType.DataProperty;
            this.name = name;
            this.value = value;
            this.getter = null;
            this.setter = null;
        }

        public JSPropertyDeclaration(String name, JSFunction get, JSFunction set) {
            if (name == null || value == null) {
                throw new NullPointerException();
            }
            this.type = JSPropertyType.AccessorProperty;
            this.name = name;
            this.value = null;
            this.getter = get;
            this.setter = set;
        }

        private final String name;
        private final JSValue value;
        private final JSFunction getter;
        private final JSFunction setter;
        private final JSPropertyType type;

        public String getName() {
            return this.name;
        }

        public JSValue getValue() {
            return this.value;
        }

        public JSFunction getGet() {
            return this.getter;
        }

        public JSFunction getSet() {
            return this.setter;
        }
    }
}