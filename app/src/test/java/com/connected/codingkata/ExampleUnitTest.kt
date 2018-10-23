package com.connected.codingkata

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/t
 * `ools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun mock_test() {
        val mocked = mock(Object::class.java);
        `when`(mocked.toString()).thenReturn("asdf");

        assertThat(mocked.toString(), equalTo("asdf"));
    }
}
