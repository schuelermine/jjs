class JSNumber extends JSValue implements JSHasPrototype {
    public JSNumber(double value) {
        this.value = value;
    }

    private final double value;
    
    public double getDouble() {
        return this.value;
    }
    
    public static JSNumber add(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.getDouble() + value2.getDouble());
    }
    
    public static JSNumber subtract(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.getDouble() - value2.getDouble());
    }
    
    public static JSNumber multiply(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.getDouble() * value2.getDouble());
    }
    
    public static JSNumber divide(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.getDouble() / value2.getDouble());
    }
    
    public static JSNumber remainder(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.getDouble() % value2.getDouble());
    }
    
    public static JSNumber shiftLeft(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.getDouble() << (int) value2.getDouble());
    }
    
    public static JSNumber shiftRight(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.getDouble() >> (int) value2.getDouble());
    }
    
    public static JSNumber and(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.getDouble() & (int) value2.getDouble());
    }

    public static JSNumber or(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.getDouble() | (int) value2.getDouble());
    }
    
    public static JSNumber xor(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.getDouble() ^ (int) value2.getDouble());
    }
    
    public JSObject getPrototype() {
        return null;
    }
}
