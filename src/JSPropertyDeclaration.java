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

    public JSPropertyDeclaration(String name, JSFunction getter, JSFunction setter) {
        if (name == null || (getter == null && setter == null)) {
            throw new NullPointerException();
        }
        this.type = JSPropertyType.AccessorProperty;
        this.name = name;
        this.value = null;
        this.getter = getter;
        this.setter = setter;
    }

    private final String name;
    private final JSValue value;
    private final JSFunction getter;
    private final JSFunction setter;
    private final JSPropertyType type;

    public JSProperty toJSProperty() {
        if (this.type == JSPropertyType.DataProperty) {
            return new JSProperty(this.value, true, true, true);
        } else {
            return new JSProperty(this.getter, this.setter, true, true);
        }
    }

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

    public JSPropertyType getType() {
        return this.type;
    }
}