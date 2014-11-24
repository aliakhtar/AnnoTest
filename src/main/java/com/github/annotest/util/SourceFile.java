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
