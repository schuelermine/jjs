public class JSString extends JSValue {
    public JSString(String string) {
        this.string = string;
    }
    private String string;
    public String getString() {
        return this.string;
    }
}
