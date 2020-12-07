import CalculatorError.Companion.show
import kotlin.test.*

class CalculatorErrorTest {
    @Test
    fun `#show should return a string`() {
        val inputError = InputError("foo").show()
        assertEquals("input not valid", inputError)

        val insufficientParameters = InsufficientParametersError.show()
        assertEquals("insufficient parameters", insufficientParameters)

        val traceable = TraceableError("*", 15, Stack.Empty, InsufficientParametersError).show()
        assertEquals("operator * (position: 15): insufficient parameters", traceable)
    }
}