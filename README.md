## AnnoTest

This project provides some utility classes to make it easier to
 unit test Java annotation processors. This is done by using
 `javax.tools.JavaCompiler` to invoke the compiler, and compile one or more test
 classes which contain your annotations. This allows the
 processor to be tested, even when `-proc:none` is set.

 The classes are taken from
 [https://github.com/irobertson/jpa-annotation-processor](https://github.com/irobertson/jpa-annotation-processor),
 improved, and modernized a bit.

 To test the code, simply make a unit test, extend it from `AnnoTest`, and pass in
 your processor to the constructor. E.g:

```java
class ExampleTest extends AnnoTest
{
    public ExampleTest() throws Exception
    {
        super( new ExampleProcessor() );
    }
}
```

To pass in classes which will be added to the compiler's classpath
(via `-classpath`), just pass them in through the second argument. E.g:

```java
super( new ExampleProcessor(), Foo.class, Bar.class);
```

 **Complete Example**:

 Here's how to unit test a simple annotation: `@PrintMe`
 and its processor: `PrintMeProcessor`. The processor just prints the
 name of whichever element contains the `@PrintMe` annotation.


1) The annotation:

```java
 public @interface PrintMe {}
```

2) The processor:

```java
 @SupportedAnnotationTypes("com.github.annoTest.annotation.PrintMe")
 public class PrintMeProcessor extends AbstractProcessor
 {
     @Override
     public boolean process(Set<? extends TypeElement> annotations,
                            RoundEnvironment roundEnv)
     {
         for (Element e : roundEnv.getElementsAnnotatedWith(PrintMe.class))
         {
             processingEnv.getMessager()
                     .printMessage(Diagnostic.Kind.NOTE, e.getSimpleName().toString());
         }
         return true;
     }
 }
```

3) Unit test:

```java
public class PrintMeProcessorTest extends AnnoTest
{
    public PrintMeProcessorTest() throws Exception
    {
        super(new PrintMeProcessor());
    }

    @Test
    public void testProcess() throws Exception
    {
        SourceFile testFile = new SourceFile(
                      "PrintMeTest.java",
                      "@com.github.annoTest.annotation.PrintMe",
                      "public class PrintMeTest {}"
                    );
        assertTrue( compiler.compileWithProcessor(processor, testFile) );
        Mockito.verify(messager).printMessage(Diagnostic.Kind.NOTE, "PrintMeTest");
        Mockito.verifyNoMoreInteractions(messager);
    }
}```

In the above example, `compiler` is a utility class with methods for calling
`javax.tools.JavaCompiler`. `messager` is a mock of the
`javax.annotation.processing.Messager` which is passed to your processor,
 when it calls `processingEnv.getMessager()`. A 3rd field,
`processor`, is a wrapper around `javax.annotation.processing.Processor`. This
is the `processingEnv` which is sent to the processor by the `Compiler`.

All three of these protected variables are set by the `AnnoTest`.