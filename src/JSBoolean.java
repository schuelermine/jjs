public class JSBoolean extends JSValue {
    public JSBoolean(boolean value) {
        this.value = value;
    }

    private boolean value;

    public boolean get() {
        return this.value;
    }
}
