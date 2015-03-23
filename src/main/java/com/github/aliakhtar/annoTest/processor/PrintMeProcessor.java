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

package com.github.aliakhtar.annoTest.processor;

import com.github.aliakhtar.annoTest.annotation.PrintMe;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Set;


@SupportedAnnotationTypes("com.github.aliakhtar.annoTest.annotation.PrintMe")
@SupportedOptions("processor.parameter.one")
public class PrintMeProcessor extends AbstractProcessor
{
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        for (Map.Entry<String, String> entry : processingEnv.getOptions().entrySet()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, entry.getKey() + ":" + entry.getValue());
        }
    }

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

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
}
