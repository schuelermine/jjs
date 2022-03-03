import java.util.Map;

public class JSObject extends JSValue {
    public JSObject(Map<String, JSProperty> entries, JSObject prototype) {
        this.entries = entries;
        this.prototype = prototype;
        this.frozen = false;
        this.sealed = false;
    }

    private Map<String, JSProperty> entries;
    private JSObject prototype;
    private boolean frozen;
    private boolean sealed;

    public void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public void seal() {
        this.sealed = true;
    }

    public boolean isSealed() {
        return this.sealed;
    }

    public JSValue get(JSString jsKey) throws JSValue {
        String key = jsKey.getString();
        if (this.entries.containsKey(key)) {
            return this.entries.get(key).get(this);
        } else if (this.prototype != null) {
            return this.prototype.get(jsKey);
        } else {
            return new JSUndefined();
        }
    }
}

class JSProperty {
    public JSProperty(JSValue value, boolean writable, boolean enumerable, boolean configurable) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
        this.get = null;
        this.set = null;
        this.writable = writable;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    public JSProperty(JSFunction get, JSFunction set, boolean enumerable, boolean configurable) {
        if (get == null || set == null) {
            throw new NullPointerException();
        }
        this.value = null;
        this.get = get;
        this.set = set;
        this.writable = null;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    private JSValue value;
    private JSFunction get;
    private JSFunction set;
    private Boolean writable;
    private boolean enumerable;
    private boolean configurable;

    public JSValue get(JSValue jsThis) throws JSValue {
        if (this.value != null) {
            return this.value;
        } else if (this.get != null) {
            return this.get.call(new JSUndefined(), jsThis, new JSValue[0]);
        } else {
            return new JSUndefined();
        }
    }

    public void set(JSValue jsThis, JSValue value) throws JSValue {
        if (this.value != null) {
            if (this.writable) {
                this.value = value;
            } else {
                throw new JSString("Cannot set unwritable property");
                // This is the wrong value to throw! Might be replaced if the full error
                // prototype chain is ever done.
            }
        } else if (this.set != null) {
            this.set.call(new JSUndefined(), jsThis, new JSValue[0]);
        } else {
            throw new JSString("Cannot set property with only a getter");
        }
    }

    public boolean isEnumerable() {
        return this.enumerable;
    }

    public boolean isConfigurable() {
        return this.configurable;
    }

    public void configure(JSValue value, JSFunction get, JSFunction set,
            Boolean writable, Boolean enumerable, Boolean configurable) {

    }
}