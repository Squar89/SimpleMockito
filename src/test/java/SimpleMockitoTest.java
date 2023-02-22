import org.junit.jupiter.api.Test;

public class SimpleMockitoTest {

    @Test
    void mainTest() {
        // setup mock
        MinimalClass minimalClassMock = SimpleMockito.mock(MinimalClass.class);

        // set when
        SimpleMockito.when(minimalClassMock.foo()).thenReturn("Mocked foo");
        SimpleMockito.when(minimalClassMock.bar()).thenReturn("Mocked bar");

        // invoke mocks
        System.out.println(minimalClassMock.foo());
        System.out.println(minimalClassMock.bar());

        // we can also overwrite previous whens with new values
        SimpleMockito.when(minimalClassMock.foo()).thenReturn("Foo");
        SimpleMockito.when(minimalClassMock.bar()).thenReturn("Bar");

        System.out.println(minimalClassMock.foo());
        System.out.println(minimalClassMock.bar());

        // also works with types other than String
        SimpleMockito.when(minimalClassMock.integerReturningFunction()).thenReturn(2137);

        System.out.println(minimalClassMock.integerReturningFunction());

        // and also with functions taking arguments - mocking based on args
        SimpleMockito.when(minimalClassMock.fooBar(1)).thenReturn(21);
        SimpleMockito.when(minimalClassMock.fooBar(2)).thenReturn(37);

        System.out.println(minimalClassMock.fooBar(1));
        System.out.println(minimalClassMock.fooBar(2));
    }
}
