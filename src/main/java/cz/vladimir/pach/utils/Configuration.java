package cz.vladimir.pach.utils;

import java.util.NoSuchElementException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example of use:
 * <pre>
 *  public void anyFunctionWithConfiguration(Configuration&lt;String&gt; configuration) {
 *      String a = configuration.getProperty(CFG_KEY).orElse(DEFAULT_KEY_VALUE);
 *      Integer b = configuration.getProperty(CFG_INT_KEY).map(Integer::parseInt).orElse(DEFAULT_INT_KEY_VALUE);
 *      ....
 *  }
 * </pre>
 */
@FunctionalInterface
public interface Configuration<T> {
    Property<T> getProperty(String key);

    final class Property<T> {
        private Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

        private final String key;
        private final T value;

        public Property(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public <R> Property<R> map(Function<T, R> mapper) {
            Property<R> result = (Property<R>) this;
            if (value != null) {
                try {
                    result = new Property<>(key, mapper.apply(value));
                } catch (Exception e) {
                    LOGGER.error("Cannot parse value of property {}.", key);
                    result = new Property<>(key, null);
                }
            }
            return result;
        }

        public T get() {
            if (value == null) {
                throw new NoSuchElementException("No value present for configuration property " + key);
            }
            return value;
        }

        public T orElse(T defValue) {
            return (value != null) ? value : defValue;
        }
    }
}