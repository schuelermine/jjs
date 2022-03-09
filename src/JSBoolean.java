public class JSBoolean extends JSValue implements JSHasPrototype {
    public JSBoolean(boolean value) {
        this.value = value;
    }

    private boolean value;

    public boolean get() {
        return this.value;
    }

    public JSObject getPrototype() {
        return null;
    }
}
