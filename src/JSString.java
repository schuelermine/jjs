class JSString extends JSValue implements JSHasPrototype {
    public JSString(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    private String value;

    public String get() {
        return this.value;
    }

    public JSObject getPrototype() {
        return null;
    }
}
