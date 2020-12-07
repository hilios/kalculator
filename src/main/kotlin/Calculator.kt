interface Calculator<T> {
    fun T.eval(stack: Stack<Double>): Either<CalculatorError, Stack<Double>>
}

sealed class CalculatorError
data class InputError(val input: String) : CalculatorError()
data class MathError(val throwable: Throwable) : CalculatorError()
object InsufficientParametersError : CalculatorError()
