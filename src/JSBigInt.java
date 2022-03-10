import java.math.BigInteger;

class JSBigInt extends JSValue implements JSHasPrototype {
    public JSBigInt(BigInteger value) {
        this.value = value;
    }

    private BigInteger value;

    public BigInteger get() {
        return this.value;
    }

    public static JSBigInt add(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().add(value2.get()));
    }

    public static JSBigInt subtract(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().subtract(value2.get()));
    }

    public static JSBigInt multiply(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().multiply(value2.get()));
    }

    public static JSBigInt divide(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().divide(value2.get()));
    }

    public static JSBigInt remainder(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().remainder(value2.get()));
    }

    public static JSBigInt shiftLeft(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().shiftLeft(value2.get().intValue()));
    }

    public static JSBigInt shiftRight(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().shiftRight(value2.get().intValue()));
    }

    public static JSBigInt and(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().and(value2.get()));
    }

    public static JSBigInt or(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().or(value2.get()));
    }

    public static JSBigInt xor(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.get().xor(value2.get()));
    }

    public JSObject getPrototype() {
        return null;
    }
}