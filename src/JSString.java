class JSString extends JSValue implements JSHasPrototype {
    public JSString(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    private final String value;

    public String getString() {
        return this.value;
    }

    public JSObject getPrototype() {
        return null;
    }
}
