/**
 * An ADT of all output errors of the calculator.
 */
sealed class CalculatorError {
    companion object : Show<CalculatorError> {
        override fun CalculatorError.show(): String = when(this) {
            is TraceableError -> "operator $operation (position: $position): ${cause.show()}"
            is InputError -> "input not valid"
            is InsufficientParametersError -> "insufficient parameters"
        }
    }
}
data class TraceableError(val operation: String,
                          val position: Int,
                          val stack: Stack<Expr>,
                          val cause: CalculatorError) : CalculatorError()
data class InputError(val input: String) : CalculatorError()
object InsufficientParametersError : CalculatorError()