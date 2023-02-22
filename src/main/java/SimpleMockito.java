import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleMockito {
    private static SimpleMockInvocationHandler lastSimpleMockInvocationHandler;

    /**
     * Create a new mock based on a given class.
     */
    @SuppressWarnings("unchecked")
    static <T> T mock(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                SimpleMockito.class.getClassLoader(),
                new Class[] {clazz},
                new SimpleMockInvocationHandler());
    }

    static <T> When when(T obj) {
        return new When();
    }

    static class When {
        void thenReturn(Object returnObject) {
            lastSimpleMockInvocationHandler.setReturnObject(returnObject);
        }
    }

    static class SimpleMockInvocationHandler implements InvocationHandler {
        private Method lastMethod;
        private Object[] lastArgs;
        private final ArrayList<MockedMethod> mockedMethodsStore = new ArrayList<>();

        /**
         * Intercept method call and match (method, args) pair with stored methods (populated by invoking when on them).
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            lastSimpleMockInvocationHandler = this;
            lastMethod = method;
            lastArgs = args;

            // check if the method was mocked for given args
            for (MockedMethod mockedMethod : mockedMethodsStore) {
                if (mockedMethod.method == method && Arrays.deepEquals(mockedMethod.args, args)) {
                    return mockedMethod.returnObject;
                }
            }

            // return null in case there was no return value set on the (method, args) pair
            return null;
        }

        /**
         * Set the return value for the most recently added (method, args) pair.
         */
        private void setReturnObject(Object returnObject) {
            boolean wasFound = false;

            // overwrite if the method was mocked previously for given args
            for (MockedMethod mockedMethod : mockedMethodsStore) {
                if (mockedMethod.method == lastMethod && Arrays.deepEquals(mockedMethod.args, lastArgs)) {
                    mockedMethod.returnObject = returnObject;
                    wasFound = true;
                }
            }

            // append to (method, args, returnValue) store
            if (!wasFound) {
                mockedMethodsStore.add(new MockedMethod(lastMethod, lastArgs, returnObject));
            }
        }

        /**
         * Store the (method, args, returnValue) tuple
         */
        static class MockedMethod {
            Method method;
            Object[] args;
            Object returnObject;

            MockedMethod(Method method, Object[] args, Object returnObject) {
                this.method = method;
                this.args = args;
                this.returnObject = returnObject;
            }
        }
    }
}
