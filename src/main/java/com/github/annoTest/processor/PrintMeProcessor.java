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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.util.Set;


@SupportedAnnotationTypes("com.github.annoTest.annotation.PrintMe")
public class PrintMeProcessor extends AbstractProcessor
{

    private TypeElement printMeType;
    private DeclaredType printMeDeclaredType;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        printMeType = processingEnv.getElementUtils()
                                   .getTypeElement(PrintMe.class.getCanonicalName());

        printMeDeclaredType = processingEnv.getTypeUtils().getDeclaredType(printMeType);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv)
    {
        Set<? extends Element> toPrint
                = roundEnv.getElementsAnnotatedWith(printMeType);

        for (Element e : toPrint)
        {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.NOTE, e.getSimpleName().toString());
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.RELEASE_7;
    }
}
