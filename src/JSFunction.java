import java.util.Map;

public class JSFunction extends JSObject {
    public JSFunction(JSFunctionLambda function, Map<String, JSProperty> entries, JSObject prototype) {
        super(entries, prototype);
        this.function = function;
    }

    private JSFunctionLambda function;

    public JSValue call(JSValue newTarget, JSValue jsThis, JSValue[] jsArgs) throws JSValue {
        return this.function.call(newTarget, jsThis, jsArgs);
    }
}

interface JSFunctionLambda {
    JSValue call(JSValue newTarget, JSValue jsThis, JSValue[] jsArgs) throws JSValue;
}
