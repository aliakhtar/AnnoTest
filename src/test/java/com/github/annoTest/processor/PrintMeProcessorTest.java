/*
 * @author Ali Akhtar
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.annoTest.processor;

import com.github.annoTest.annotation.PrintMe;
import com.github.annoTest.util.AnnoTest;
import com.github.annoTest.util.SourceFile;
import org.junit.Test;
import org.mockito.Mockito;

import javax.tools.Diagnostic;

import static org.junit.Assert.assertTrue;

public class PrintMeProcessorTest extends AnnoTest
{

    public PrintMeProcessorTest() throws Exception
    {
        super(new PrintMeProcessor());
    }

    @Test
    public void testProcess() throws Exception
    {
        SourceFile[] sourceFiles =
        {
           new SourceFile(
             "PrintMeTest.java",
             "@" + PrintMe.class.getCanonicalName(),
             "public class PrintMeTest {",
             "}")
        };
        assertTrue( compiler.compileWithProcessor(processor, sourceFiles) );
        Mockito.verify(messager).printMessage(Diagnostic.Kind.NOTE, "PrintMeTest");
        Mockito.verifyNoMoreInteractions(messager);
    }

}