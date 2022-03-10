class JSNumber extends JSValue implements JSHasPrototype {
    public JSNumber(double value) {
        this.value = value;
    }

    private double value;
    
    public double get() {
        return this.value;
    }
    
    public static JSNumber add(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.get() + value2.get());
    }
    
    public static JSNumber subtract(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.get() - value2.get());
    }
    
    public static JSNumber multiply(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.get() * value2.get());
    }
    
    public static JSNumber divide(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.get() / value2.get());
    }
    
    public static JSNumber remainder(JSNumber value1, JSNumber value2) {
        return new JSNumber(value1.get() % value2.get());
    }
    
    public static JSNumber shiftLeft(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.get() << (int) value2.get());
    }
    
    public static JSNumber shiftRight(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.get() >> (int) value2.get());
    }
    
    public static JSNumber and(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.get() & (int) value2.get());
    }

    public static JSNumber or(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.get() | (int) value2.get());
    }
    
    public static JSNumber xor(JSNumber value1, JSNumber value2) {
        return new JSNumber((int) value1.get() ^ (int) value2.get());
    }
    
    public JSObject getPrototype() {
        return null;
    }
}
