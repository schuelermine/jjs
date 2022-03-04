public class JSString extends JSValue {
    public JSString(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        this.string = string;
    }
    private String string;
    public String getString() {
        return this.string;
    }
}
