//Courtesy of https://github.com/irobertson/jpa-annotation-processor
package com.github.annotest.util;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

public class SourceFile
{
    private final String fileName;
    private final Collection<String> content;

    public SourceFile(String fileName, String... content)
    {
        this.fileName = fileName;
        this.content = ImmutableList.copyOf(content);
    }

    public String getFileName() { return fileName; }
    public Collection<String> getContent() { return content; }
}
