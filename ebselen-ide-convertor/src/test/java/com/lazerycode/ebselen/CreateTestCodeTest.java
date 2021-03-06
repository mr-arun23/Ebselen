/*
 * Copyright (c) 2010-2011 Ardesco Solutions - http://www.ardescosolutions.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lazerycode.ebselen;

import com.lazerycode.ebselen.exceptions.IDECommandNotFoundException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CreateTestCodeTest {

    private CreateTestCode codeGenerator = new CreateTestCode();

    @Test
    public void checkThatValidCoordinatesAreReturnedBasedUponLocator() {
        assertThat(codeGenerator.coords("foo"), is(equalTo("(Coordinates) new Point(driver.findElement(By.id(\"foo\")).getLocation().getX(), driver.findElement(By.id(\"foo\")).getLocation().getY())")));
    }

    @Test
    public void checkThatValidCoordinatesAreReturnedBasedUponLocatorWithOffset() {
        assertThat(codeGenerator.coords("20, 25", "foo"), is(equalTo("(Coordinates) new Point(driver.findElement(By.id(\"foo\")).getLocation().getX() + 20, driver.findElement(By.id(\"foo\")).getLocation().getY() + 25)")));
    }

    @Test
    public void checkThatValidCoordinatesStructureIsReturnedUsingPoint() {
        assertThat(codeGenerator.returnCoordinates("new Point(200, 300)"), is(equalTo("(Coordinates) new Point(200, 300)")));
    }

    @Test
    public void checkThatValidCoordinatesStructureIsReturnedUsingXY() {
        assertThat(codeGenerator.returnCoordinates("200", "300"), is(equalTo("(Coordinates) new Point(200, 300)")));
    }

    @Test
    public void checkThatValidWebElementInitialisationIsPerformed() {
        assertThat(codeGenerator.createWebElement("myWebElement", "driver.findElement(By.id(\"foo\"))"), is(equalTo("WebElement myWebElement = driver.findElement(By.id(\"foo\"))")));
    }

    @Test
    public void checkThatItIsPossibleToReturnStoredVariablesCode() {
        assertThat(codeGenerator.store("MyStringObject", "AnotherObject"), is(equalTo("storedVariables.put(MyStringObject, AnotherObject)")));
    }

    @Test
    public void checkThatCommandCanBeConverted() throws Exception {
        assertThat(codeGenerator.convertCommandToEbselenCode("click", "foo", ""), is(equalTo("driver.findElement(By.id(\"foo\")).click()")));
    }

    @Test(expected = IDECommandNotFoundException.class)
    public void checkThatUnknownCommandsThrowAnException() throws Exception {
        codeGenerator.convertCommandToEbselenCode("NotACommand", "foo", "click");
    }
}
