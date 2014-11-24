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
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class ToStringMatcher<T> extends BaseMatcher<T> {
    /**
     * @param <T> The type of the argument being matched
     * @param expecteed the expected toString value for the object
     * @param clazz the class of the object
     * @return a {@code ToStringMatcher}.
     */
    public static <T> Matcher<T> hasToString(String expecteed, Class<T> clazz)
    {
        return new ToStringMatcher<T>(expecteed);
    }

    private final String expectedToString;

    public ToStringMatcher(String expectedToString)
    {
        this.expectedToString = expectedToString;
    }

    @Override
    public boolean matches(Object obj)
    {
        return obj != null && expectedToString.equals(obj.toString());
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("An object with a toString of " + expectedToString);
    }

}
