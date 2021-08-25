package io.hilios.calculator

import io.hilios.calculator.CalculatorError.Companion.show
import kotlin.test.*

class CalculatorErrorTest {
    @Test
    fun `#show should return a string`() {
        val inputError = CalculatorError.InputError("foo").show()
        assertEquals("input not valid", inputError)

        val insufficientParameters = CalculatorError.InsufficientParametersError.show()
        assertEquals("insufficient parameters", insufficientParameters)

        val traceable = CalculatorError.TraceableError(
            "*",
            15,
            CalculatorError.InsufficientParametersError
        ).show()
        assertEquals("operator * (position: 15): insufficient parameters", traceable)
    }
}