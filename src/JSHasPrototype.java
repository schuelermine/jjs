public abstract class JSHasPrototype extends JSValue {
    public JSHasPrototype() {
        this.prototype = null;
    }

    public JSHasPrototype(JSObject prototype) {
        this.prototype = prototype;
    }

    private JSObject prototype;

    public JSObject getPrototype() {
        return this.prototype;
    }
}