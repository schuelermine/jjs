import java.util.Map;

public class JSObject extends JSValue implements JSHasPrototype {
    public JSObject(Map<String, JSProperty> entries, JSObject prototype) {
        if (entries == null) {
            throw new NullPointerException();
        }
        this.entries = entries;
        this.prototype = prototype;
        this.frozen = false;
        this.sealed = false;
        this.unfreezable = false;
    }

    public JSObject(Map<String, JSProperty> entries, JSObject prototype, boolean unfreezable) {
        if (entries == null) {
            throw new NullPointerException();
        }
        this.entries = entries;
        this.prototype = prototype;
        this.frozen = false;
        this.sealed = false;
        this.unfreezable = unfreezable;
    }

    private boolean unfreezable;

    private Map<String, JSProperty> entries;
    private JSObject prototype;
    private boolean frozen;
    private boolean sealed;

    public void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen() throws JSValue {
        if (this.unfreezable) {
            throw new JSString("Cannot freeze this object");
        }
        return this.frozen;
    }

    public void seal() {
        this.sealed = true;
    }

    public boolean isSealed() {
        for (String key : this.entries.keySet()) {
            this.entries.get(key).setConfigurability(false);
        }
        return this.sealed;
    }

    public JSValue get(JSString jsKey) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            return this.entries.get(key).get(this);
        } else if (this.prototype != null) {
            return this.prototype.get(jsKey);
        } else {
            return new JSUndefined();
        }
    }

    public JSValue callMethod(JSString jsKey, JSValue[] jsArgs) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (jsArgs == null) {
            jsArgs = new JSValue[0];
        }
        String key = jsKey.get();
        JSValue entry = this.entries.get(key).get(this);
        if (entry instanceof JSUndefined) {
            throw new JSString("Method not defined");
        }
        if (!(entry instanceof JSFunction)) {
            throw new JSString("Cannot call non-method");
        }
        JSFunction function = (JSFunction) entry;
        return function.call(new JSUndefined(), this, jsArgs);
    }

    public void set(JSString jsKey, JSValue value) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (this.frozen) {
            throw new JSString("Cannot set property on frozen object");
        }
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).set(this, value);
        } else {
            if (this.sealed) {
                throw new JSString("Cannot add property on sealed object");
            }
            JSProperty prop = new JSProperty(value, true, true, true);
            this.entries.put(key, prop);
        }
    }

    public void configure(JSString jsKey, JSValue value, Boolean writable, Boolean enumerable, Boolean configurable)
            throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (this.frozen) {
            throw new JSString("Cannot configure property on frozen object");
            // This is not vanilla behaviour! If the new configuration is identical JS would not throw.
            // This can be bypassed by altered surrounding code as frozen status is available.
        }
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).configure(value, writable, enumerable, configurable);
        } else {
            throw new IndexOutOfBoundsException();
            // Another reason why this should be wrapped to provide behaviour more like defineProperty().
        }
    }

    public JSObject getPrototype() {
        return this.prototype;
    }

    public void setPrototype(JSObject prototype) throws JSValue {
        if (this.frozen) {
            throw new JSString("Cannot alter prototype of frozen object");
        }
        this.prototype = prototype;
    }
}

enum JSPropertyType {
    AccessorProperty,
    DataProperty
}

class JSProperty {
    // This class has two constructors for data and accessor properties

    public JSProperty(JSValue value, boolean writable, boolean enumerable, boolean configurable) {
        setup(value, writable, enumerable, configurable);
    }

    public JSProperty(JSFunction get, JSFunction set, boolean enumerable, boolean configurable) {
        setup(get, set, enumerable, configurable);
    }

    private void setup(JSValue value, boolean writable, boolean enumerable, boolean configurable) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.type = JSPropertyType.DataProperty;
        this.value = value;
        this.get = null;
        this.set = null;
        this.writable = writable;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    private void setup(JSFunction get, JSFunction set, boolean enumerable, boolean configurable) {
        this.type = JSPropertyType.AccessorProperty;
        this.value = null;
        this.get = get;
        this.set = set;
        this.writable = null;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    private JSPropertyType type;
    private JSValue value;
    private JSFunction get;
    private JSFunction set;
    private Boolean writable;
    private boolean enumerable;
    private boolean configurable;

    public JSValue get(JSValue jsThis) throws JSValue {
        if (jsThis == null) {
            throw new NullPointerException();
        } else if (this.value != null) {
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
                // This is the wrong value to throw! Also, this happens in loads of other places.
                // Might be replaced if the full error prototype chain is ever done.
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

    public void configure(JSValue value, Boolean writable, Boolean enumerable, Boolean configurable) throws JSValue {
        if (this.type != JSPropertyType.DataProperty) {
            if (!this.configurable) {
                throw new JSString("Cannot configure property");
            }
            this.setup(value, (boolean) writable, (boolean) enumerable, (boolean) configurable);
            return;
        }
        if (!this.configurable && (writable != null || enumerable != null || configurable != null)) {
            throw new JSString("Cannot configure property");
        }
        if (value != null) {
            if (!this.writable) {
                throw new JSString("Cannot set unwritable property");
            }
            this.value = value;
        }
        this.get = null;
        this.set = null;
        if (writable != null) {
            if (this.writable != false) {
                this.writable = writable;
            } else {
                throw new JSString("Cannot configure unwritable property");
            }
        }
        if (enumerable != null) {
            this.enumerable = enumerable;
        }
        if (configurable != null) {
            this.configurable = configurable;
        }
    }

    public void configure(JSFunction get, JSFunction set, Boolean enumerable, Boolean configurable) throws JSValue {
        if (!this.configurable) {
            throw new JSString("Cannot configure property");
        }
        if (this.type != JSPropertyType.AccessorProperty) {
            this.setup(get, set, enumerable, configurable);
            return;
        }
        this.value = null;
        if (get != null) {
            this.get = get;
        }
        if (set != null) {
            this.set = set;
        }
        this.writable = null;
        if (enumerable != null) {
            this.enumerable = enumerable;
        }
        if (configurable != null) {
            this.configurable = configurable;
        }
    }

    public void setConfigurability(boolean configurable) {
        this.configurable = configurable;
    }
}