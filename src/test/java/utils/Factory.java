package utils;

import java.util.HashMap;
import java.util.Map;

public final class Factory {

    private static final Map<String, Object> LOCATOR = new HashMap<>();

    private Factory() {
    }

    public static <T> T get(Class<T> typ) {
        return typ.cast(typ(typ.getTypeName()));
    }

    public static <T> T get(Class<T> typ, String typName) {
        return typ.cast(typ(typName));
    }

    public static void add(String serviceName, Object service) {
        if (!LOCATOR.containsKey(serviceName)) {
            LOCATOR.put(serviceName, service);
        }
    }

    private static Object typ(String typName) {
        return LOCATOR.get(typName);
    }
}
