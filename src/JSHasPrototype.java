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
    

    public JSValue getPrototypeProperty(JSString jsKey) throws JSValue {
        return this.prototype.getProperty(jsKey);
    }

    public JSValue callPrototypeMethod(JSString jsKey) throws JSValue {

    }
}