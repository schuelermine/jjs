import java.util.Map;

class JSObject extends JSValue implements JSHasPrototype {
    public JSObject(Map<String, JSProperty> entries, JSObject prototype) {
        if (entries == null) {
            throw new NullPointerException();
        }
        this.unfreezable = false;
        this.entries = entries;
        this.prototype = prototype;
        this.frozen = false;
        this.sealed = false;
    }

    public JSObject(Map<String, JSProperty> entries, JSObject prototype, boolean unfreezable) {
        if (entries == null) {
            throw new NullPointerException();
        }
        this.unfreezable = unfreezable;
        this.entries = entries;
        this.prototype = prototype;
        this.frozen = false;
        this.sealed = false;
    }

    private final boolean unfreezable;
    private final Map<String, JSProperty> entries;
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
        for (JSProperty property : this.entries.values()) {
            property.setConfigurability(false);
        }
        return this.sealed;
    }

    public JSValue getProperty(JSString jsKey) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        final String key = jsKey.getString();
        if (this.entries.containsKey(key)) {
            return this.entries.get(key).getValue(this);
        } else if (this.prototype != null) {
            return this.prototype.getProperty(jsKey);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public JSValue getOwnProperty(JSString jsKey) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        final String key = jsKey.getString();
        if (this.entries.containsKey(key)) {
            return this.entries.get(key).getValue(this);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public JSValue callMethod(JSString jsKey, JSValue[] jsArgs) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (jsArgs == null) {
            jsArgs = new JSValue[0];
        }
        final String key = jsKey.getString();
        final JSValue entry = this.entries.get(key).getValue(this);
        if (entry instanceof JSUndefined) {
            throw new JSString("Method not defined");
        }
        if (entry instanceof JSFunction f) {
            return f.call(new JSUndefined(), this, jsArgs);
        } else {
            throw new JSString("Cannot call non-method");
        }
    }

    public void setOrCreateProperty(JSString jsKey, JSValue value) throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (this.frozen) {
            throw new JSString("Cannot set property on frozen object");
        }
        final String key = jsKey.getString();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).setValue(this, value);
        } else {
            if (this.sealed) {
                throw new JSString("Cannot add property on sealed object");
            }
            final JSProperty property = new JSProperty(value, true, true, true);
            this.entries.put(key, property);
        }
    }

    public void configureProperty(JSString jsKey, JSValue value, Boolean writable, Boolean enumerable,
            Boolean configurable)
            throws JSValue {
        if (jsKey == null) {
            throw new NullPointerException();
        }
        if (this.frozen) {
            throw new JSString("Cannot configure property on frozen object");
            // This is not vanilla behaviour! If the new configuration is identical JS would
            // not throw.
            // This can be bypassed by altered surrounding code as frozen status is
            // available.
        }
        final String key = jsKey.getString();
        if (this.entries.containsKey(key)) {
            this.entries.get(key).configure(value, writable, enumerable, configurable);
        } else {
            throw new IndexOutOfBoundsException();
            // Another reason why this should be wrapped to provide behaviour more like
            // defineProperty().
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
    ACCESSOR,
    DATA
}

class JSProperty {
    // This class has two constructors for data and accessor properties

    public JSProperty(JSValue value, boolean writable, boolean enumerable, boolean configurable) {
        setup(value, writable, enumerable, configurable);
    }

    public JSProperty(JSFunction getter, JSFunction setter, boolean enumerable, boolean configurable) {
        setup(getter, setter, enumerable, configurable);
    }

    private void setup(JSValue value, boolean writable, boolean enumerable, boolean configurable) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.type = JSPropertyType.DATA;
        this.value = value;
        this.getter = null;
        this.setter = null;
        this.writable = writable;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    private void setup(JSFunction getter, JSFunction setter, boolean enumerable, boolean configurable) {
        this.type = JSPropertyType.ACCESSOR;
        this.value = null;
        this.getter = getter;
        this.setter = setter;
        this.writable = null;
        this.enumerable = enumerable;
        this.configurable = configurable;
    }

    private JSPropertyType type;
    private JSValue value;
    private JSFunction getter;
    private JSFunction setter;
    private Boolean writable;
    private boolean enumerable;
    private boolean configurable;

    private static final String CANNOT_CONFIGURE = "Cannot configure property";

    public JSValue getValue(JSValue jsThis) throws JSValue {
        if (jsThis == null) {
            throw new NullPointerException();
        } else if (this.type == JSPropertyType.DATA) {
            return this.value;
        } else if (this.getter != null) {
            return this.getter.call(new JSUndefined(), jsThis, new JSValue[0]);
        } else {
            throw new JSString("Cannot get this property with only a setter");
        }
    }

    public void setValue(JSValue jsThis, JSValue value) throws JSValue {
        if (this.type == JSPropertyType.DATA) {
            if (this.writable) {
                this.value = value;
            } else {
                throw new JSString("Cannot set unwritable property");
                // This is the wrong value to throw! Also, this happens in loads of other
                // places.
                // Might be replaced if the full error prototype chain is ever done.
            }
        } else if (this.setter != null) {
            this.setter.call(new JSUndefined(), jsThis, new JSValue[0]);
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
        if (this.type != JSPropertyType.DATA) {
            if (!this.configurable) {
                throw new JSString(CANNOT_CONFIGURE);
            }
            this.setup(value, writable, enumerable, configurable);
            return;
        }
        if (!this.configurable && (writable != null || enumerable != null || configurable != null)) {
            throw new JSString(CANNOT_CONFIGURE);
        }
        if (value != null) {
            if (Boolean.FALSE.equals(this.writable)) {
                throw new JSString("Cannot set unwritable property");
            }
            this.value = value;
        }
        this.getter = null;
        this.setter = null;
        if (writable != null) {
            if (Boolean.TRUE.equals(this.writable)) {
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

    public void configure(JSFunction getter, JSFunction setter, Boolean enumerable, Boolean configurable)
            throws JSValue {
        if (!this.configurable) {
            throw new JSString(CANNOT_CONFIGURE);
        }
        if (this.type != JSPropertyType.ACCESSOR) {
            this.setup(getter, setter, enumerable, configurable);
            return;
        }
        this.value = null;
        if (getter != null) {
            this.getter = getter;
        }
        if (setter != null) {
            this.setter = setter;
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