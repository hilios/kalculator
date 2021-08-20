/**
 * An ADT of all output errors of the calculator.
 */
sealed class CalculatorError {

    data class InputError(val input: String) : CalculatorError()
    data class TraceableError(
        val operation: String,
        val position: Int,
        val cause: CalculatorError
    ) : CalculatorError()
    object InsufficientParametersError : CalculatorError()

    companion object : Show<CalculatorError> {
        override fun CalculatorError.show(): String = when(this) {
            is TraceableError -> "operator $operation (position: $position): ${cause.show()}"
            is InputError -> "input not valid"
            is InsufficientParametersError -> "insufficient parameters"
        }
    }
}