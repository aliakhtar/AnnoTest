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
package com.github.aliakhtar.annoTest.util;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compiler
{
    private final File sourceDir, outputDir;
    private final List<String> options;

    private final List<String> classPaths;

    public Compiler(Class... classPathEntries) throws IOException
    {
        classPaths = new ArrayList<>( classPathEntries.length + 1 );
        classPaths.add( classPathFor(Compiler.class) );

        for (Class clazz : classPathEntries)
        {
            classPaths.add( classPathFor(clazz) );
        }

        sourceDir = createTempDir("sourceDir");
        outputDir = createTempDir("outputDir");

        Builder<String> builder = ImmutableList.builder();
        builder.add("-classpath").add(buildClassPath(outputDir));
        builder.add("-d").add(outputDir.getAbsolutePath());
        this.options = builder.build();
    }

    public File getOutputDir()
    {
        return outputDir;
    }

    public void cleanUp()
    {
        FileUtils.deleteQuietly(outputDir);
        FileUtils.deleteQuietly(sourceDir);
    }

    public boolean compileWithProcessor(Processor processor, SourceFile... sourceFiles)
            throws Exception
    {
        return compile(processor, sourceFiles);
    }

    public boolean compile(SourceFile... sourceFiles) throws Exception
    {
        return compile(null, sourceFiles);
    }

    private boolean compile(Processor processor, SourceFile... sourceFiles)
            throws Exception
    {
        File[] files = new File[sourceFiles.length];
        for (int i = 0; i < sourceFiles.length; i++) {
            files[i] = writeSourceFile(sourceFiles[i]);
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects =
                fileManager.getJavaFileObjects(files);
        CompilationTask compilationTask =
                compiler.getTask(null, null, null, options, null, javaFileObjects);
        if (processor != null)
            compilationTask.setProcessors(Arrays.asList(processor));

        Boolean success = compilationTask.call();
        fileManager.close();
        return success;
    }

    private String buildClassPath(File outputDir)
    {
        ArrayList<String> classPathElements = Lists.newArrayList(classPaths);
        classPathElements.add(outputDir.getAbsolutePath());
        return Joiner.on(System.getProperty("path.separator")).join(classPathElements);
    }

    private static String classPathFor(Class<?> clazz)
    {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    protected File writeSourceFile(SourceFile sourceFile) throws IOException
    {
        File file = new File(sourceDir, sourceFile.getFileName());
        FileUtils.writeLines(file, sourceFile.getContent());
        return file;
    }

    private static File createTempDir(String prefix) throws IOException
    {
        File file = File.createTempFile(prefix, null);
        if (!file.delete()) {
            throw new IOException("Unable to delete temporary file " + file.getAbsolutePath());
        }
        if (!file.mkdir()) {
            throw new IOException("Unable to create temp directory " + file.getAbsolutePath());
        }
        return file;
    }
}