package com.example.mvvmpracticetask

import com.example.mvvmpracticetask.utils.FormValidator
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class FormValidatorTest {
    @Before
    fun setUp() {
    }

    @Test
    fun `validate empty name returns false`() {
        val isValid = FormValidator.isNameValid("")
        assertFalse(isValid)
    }

    @Test
    fun `validate valid name returns true`() {
        val isValid = FormValidator.isNameValid("Divyesh Jivani")
        assertTrue(isValid)
    }

    @Test
    fun `validate age less than 18 returns false`() {
        val isValidDob = FormValidator.isAgeValid(System.currentTimeMillis())
        assertFalse(isValidDob)
    }

    @Test
    fun `validate age greater or equal than 18 returns true`() {
        val dayInMillis : Long = 24L * 60L * 60L * 1000L
        val millisInOneYear: Long = (365.25 * dayInMillis).toLong()
        val millisIn18Years: Long = 18 * millisInOneYear + (24L * 60L * 60L * 1000L)
        val millisIn19Years: Long = 19 * millisInOneYear
        val isValidDob18Years = FormValidator.isAgeValid(System.currentTimeMillis() - millisIn18Years)
        val isValidDob19Years = FormValidator.isAgeValid(System.currentTimeMillis() - millisIn19Years)
        assertTrue(isValidDob18Years)
        assertTrue(isValidDob19Years)
    }
}