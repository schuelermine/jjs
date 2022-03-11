class JSBoolean extends JSValue implements JSHasPrototype {
    public JSBoolean(boolean value) {
        this.value = value;
    }

    private final boolean value;

    public boolean getBoolean() {
        return this.value;
    }

    public JSObject getPrototype() {
        return null;
    }
}
