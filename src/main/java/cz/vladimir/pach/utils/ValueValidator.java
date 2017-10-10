package cz.vladimir.pach.utils;

import java.util.function.Function;

import org.slf4j.helpers.MessageFormatter;

public class ValueValidator {
    /**
     * Method validate result of test. The method can be used by a builder for implementation of {@code Builder.build()} method.
     * <p>A build method has to validate all attributes, which are stored in the builder. Bad value of an argument is bad state of the builder.</p>
     *
     * @param testResult
     *         Result of a test
     * @param message
     *         Message in "apache-string" format (place holder is {})
     * @param params
     *         Params for message. It can be omitted.
     * @throws IllegalStateException
     *         If value of the test result is false.
     * @see #validate(boolean, Function, String, Object...)
     */
    public static void validateState(boolean testResult, String message, Object... params) throws IllegalStateException {
        validate(testResult, IllegalStateException::new, message, params);
    }

    /**
     * Method validate result of test. The method can be used by a method for validate inputs.
     * <p>Each algorithm (implementation) has to validate its inputs. Bad value of an argument is illegal argument.</p>
     *
     * @param value
     *         Value of tested argument
     * @param testResult
     *         Result of a test
     * @param message
     *         Message in "apache-string" format (place holder is {})
     * @param params
     *         Params for message. It can be omitted.
     * @param <T>
     *         Type of checked argument
     * @throws IllegalArgumentException
     *         If value of the test result is false.
     * @see #validate(boolean, Function, String, Object...)
     */
    public static <T> T checkArgument(T value, boolean testResult, String message, Object... params) throws IllegalArgumentException {
        validate(testResult, IllegalArgumentException::new, message, params);
        return value;
    }

    /**
     * Method validate result of test. If the test result is false, the method creates message, create new instance of runtime exception and throws the
     * exception.
     *
     * @param testResult
     *         Result of a test
     * @param exception
     *         Factory function, which accept error message and create new instance of a runtime exception. Never return null. The message can be null.
     * @param message
     *         Message in "apache-string" format (place holder is {})
     * @param params
     *         Params for message. It can be omitted.
     * @throws RuntimeException
     *         If value of the test result is false, then get new instance of {@link RuntimeException} from {@code exception} and
     *         throws it.
     */
    public static void validate(boolean testResult, Function<String, RuntimeException> exception, String message, Object... params) {
        if (!testResult) {
            message = (message != null && params != null && params.length != 0) ? MessageFormatter.arrayFormat(message, params).getMessage() : message;
            throw exception.apply(message);
        }
    }
}
