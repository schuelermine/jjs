public class JSNumber extends JSValue {
    public JSNumber(double value) {
        this.value = value;
    }

    private double value;

    public double get() {
        return this.value;
    }
}
