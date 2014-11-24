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

//Courtesy of https://github.com/irobertson/jpa-annotation-processor
package com.github.annoTest.util;

import org.mockito.Mockito;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * A wrapper around a {@code Processor} which will replace the standard {@code Messager}
 * returned from {@link ProcessingEnvironment#getMessager()} with a provided
 * mock Messager.
 */
public class ProcessorWrapper implements Processor
{
    private final Processor wrapped;
    private final Messager mockMessager;

    @Override
    public void init(ProcessingEnvironment processingEnv)
    {
        ProcessingEnvironment spy = Mockito.spy(processingEnv);
        Mockito.when(spy.getMessager()).thenReturn(mockMessager);
        wrapped.init(spy);
    }

    public ProcessorWrapper(Processor wrapped, Messager mockMessager)
    {
        this.wrapped = wrapped;
        this.mockMessager = mockMessager;
    }

    @Override
    public Iterable<? extends Completion>
             getCompletions(Element element, AnnotationMirror annotation,
                            ExecutableElement member, String userText)
    {
        return wrapped.getCompletions(element, annotation, member, userText);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return wrapped.getSupportedAnnotationTypes();
    }

    @Override
    public Set<String> getSupportedOptions()
    {
        return wrapped.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return wrapped.getSupportedSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        return wrapped.process(annotations, roundEnv);
    }
}