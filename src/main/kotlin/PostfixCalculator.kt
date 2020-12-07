object PostfixCalculator {

    private fun <A> requireOne(stack: Stack<A>): Either<CalculatorError, A> = when(stack) {
        is Stack.Elem -> Right(stack.head)
        is Stack.Empty -> Left(InsufficientParametersError)
    }
    private fun <A> requirePair(stack: Stack<A>): Either<CalculatorError, Pair<A, A>> {
        val ea0 = Either.fromOption(stack.get(0), { InsufficientParametersError })
        val ea1 = Either.fromOption(stack.get(1), { InsufficientParametersError })
        return ea0.flatMap { a0 ->
            ea1.map { a1 -> Pair(a0, a1) }
        }
    }

    fun eval(stack: Stack<Expr>, input: String): Either<CalculatorError, Stack<Expr>> = when(input) {
        "+" -> requirePair(stack).map { (x, y) ->
            stack.drop(2).add(Expr.Add(x, y))
        }
        "-" -> requirePair(stack).map { (x, y) ->
            stack.drop(2).add(Expr.Subtract(x, y))
        }
        "*" -> requirePair(stack).map { (x, y) ->
            stack.drop(2).add(Expr.Multiply(x, y))
        }
        "/" -> requirePair(stack).map { (x, y) ->
            stack.drop(2).add(Expr.Divide(x, y))
        }
        "sqrt" -> requireOne(stack).map { x ->
            stack.drop(1).add(Expr.Sqrt(x))
        }
        "undo" -> requireOne(stack).map { x ->
            stack.drop(1).append(undo(x))
        }
        "clear" ->
            Right(Stack.Empty)
        else ->
            Either.catchExceptions { Expr.Const(input.toDouble()) }
                .leftMap { InputError(input) }
                .map { e -> stack.add(e) }
    }

    fun undo(expr: Expr): Stack<Expr> = when(expr) {
        is Expr.Const -> Stack.Empty
        is Expr.Add -> Stack.one(expr.y).add(expr.x)
        is Expr.Subtract -> Stack.one(expr.y).add(expr.x)
        is Expr.Multiply -> Stack.one(expr.y).add(expr.x)
        is Expr.Divide -> Stack.one(expr.y).add(expr.x)
        is Expr.Sqrt -> Stack.one(expr.x)
    }
}

sealed class CalculatorError
data class InputError(val input: String) : CalculatorError()
data class MathError(val throwable: Throwable) : CalculatorError()
object InsufficientParametersError : CalculatorError()
