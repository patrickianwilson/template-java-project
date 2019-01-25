package com.sample.java;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by pwilson on 11/2/17.
 */
public class SampleBeanTests {

    @Test
    public void testLombokSetter() {
        SampleBean underTest = new SampleBean();
        underTest.setData("todo");

        assertThat(underTest.getData(), is("todo"));
    }
}
