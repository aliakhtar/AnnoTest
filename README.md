## AnnoTest

This project provides some utility classes to make it easier to
 unit test Java annotation processors. This is done by using
 `javax.tools.JavaCompiler` to invoke the compiler, and compile a test
 class which contains your chosen annotations. This allows the
 processor to be tested, even when `-proc:none` is passed to the compiler.

 The classes are taken from
 [https://github.com/irobertson/jpa-annotation-processor](https://github.com/irobertson/jpa-annotation-processor),
 improved, and modernized a bit, to work with newer versions of the dependencies.

 To test the code, simply make a unit test, extend it from `AnnoTest`, and pass in
 your processor to the constructor. E.g:

    class ExampleTest extends AnnoTest
    {
        public ExampleTest()
        {
            super( new ExampleProcessor() );
        }
    }

To pass in classes which will be added to the compiler's classpath
(via `-classpath`), just pass them in through the second argument. E.g:

    super( new ExampleProcessor(), Foo.class, Bar.class);

 **Complete Example**:

 Here's a complete example of how to unit test a simple annotation, `@PrintMe`
 and its processor, `PrintMeProcessor`. The processor just prints the
 name of whichever element contains the `@PrintMe` annotation.


1) The annotation:

     public @interface PrintMe {}


2) The processor:

     @SupportedAnnotationTypes("com.github.annoTest.annotation.PrintMe")
     public class PrintMeProcessor extends AbstractProcessor
     {
         @Override
         public boolean process(Set<? extends TypeElement> annotations,
                                RoundEnvironment roundEnv)
         {
             for (Element e : roundEnv.getElementsAnnotatedWith(PrintMe.class))
             {
                //Just print the element which has the annotation:
                 processingEnv.getMessager()
                         .printMessage(Diagnostic.Kind.NOTE, e.getSimpleName().toString());
             }
             return true;
         }
     }


3) Unit test:

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
    }