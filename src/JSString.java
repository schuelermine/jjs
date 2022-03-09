public class JSString extends JSValue {
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
}
