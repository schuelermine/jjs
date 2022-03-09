import java.util.Map;

public class JSObject extends JSHasPrototype {
    public JSObject(Map<String, JSProperty> entries, JSObject prototype) {
        super(prototype);
        if (entries == null) {
            throw new NullPointerException();
        }
        this.entries = entries;
        this.frozen = false;
        this.sealed = false;
    }

    private Map<String, JSProperty> entries;
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

    public JSValue getProperty(JSString jsKey) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            return this.entries.get(key).get(this);
        } else if (this.getPrototype() != null) {
            return this.getPrototype().getProperty(jsKey);
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
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).set(this, value);
        } else {
            JSProperty prop = new JSProperty(value, true, true, true);
            this.entries.put(key, prop);
        }
    }

    public void configure(JSString jsKey, JSValue value, Boolean writable, Boolean enumerable, Boolean configurable)
            throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        String key = jsKey.get();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).configure(value, writable, enumerable, configurable);
        } else {
            throw new IndexOutOfBoundsException();
        }
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
}