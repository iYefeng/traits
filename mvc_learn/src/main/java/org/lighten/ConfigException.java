package org.lighten;

/**
 * Created by yefeng on 16/11/27.
 */
public class ConfigException extends IllegalArgumentException {

    private static final long serialVersionUID = 553479187806423727L;

    public ConfigException() {
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
