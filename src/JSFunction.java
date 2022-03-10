import java.util.Map;

class JSFunction extends JSObject {
    public JSFunction(JSFunctionLambda function, Map<String, JSProperty> entries, JSObject prototype) {
        super(entries, prototype);
        if (function == null || entries == null) {
            throw new NullPointerException();
        }
        this.function = function;
    }

    private JSFunctionLambda function;

    public JSValue call(JSValue newTarget, JSValue jsThis, JSValue[] jsArgs) throws JSValue {
        if (newTarget == null || jsThis == null) {
            throw new NullPointerException();
        }
        if (jsArgs == null) {
            jsArgs = new JSValue[0];
        }
        return this.function.call(newTarget, jsThis, jsArgs);
    }
}

interface JSFunctionLambda {
    JSValue call(JSValue newTarget, JSValue jsThis, JSValue[] jsArgs) throws JSValue;
}
