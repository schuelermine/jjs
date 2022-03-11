import java.math.BigInteger;

class JSBigInt extends JSValue implements JSHasPrototype {
    public JSBigInt(BigInteger value) {
        this.value = value;
    }

    private final BigInteger value;

    public BigInteger getBigInteger() {
        return this.value;
    }

    public static JSBigInt add(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().add(value2.getBigInteger()));
    }

    public static JSBigInt subtract(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().subtract(value2.getBigInteger()));
    }

    public static JSBigInt multiply(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().multiply(value2.getBigInteger()));
    }

    public static JSBigInt divide(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().divide(value2.getBigInteger()));
    }

    public static JSBigInt remainder(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().remainder(value2.getBigInteger()));
    }

    public static JSBigInt shiftLeft(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().shiftLeft(value2.getBigInteger().intValue()));
    }

    public static JSBigInt shiftRight(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().shiftRight(value2.getBigInteger().intValue()));
    }

    public static JSBigInt and(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().and(value2.getBigInteger()));
    }

    public static JSBigInt or(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().or(value2.getBigInteger()));
    }

    public static JSBigInt xor(JSBigInt value1, JSBigInt value2) {
        return new JSBigInt(value1.getBigInteger().xor(value2.getBigInteger()));
    }

    public JSObject getPrototype() {
        return null;
    }
}