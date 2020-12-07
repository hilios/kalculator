data class PostfixCalculator(private val memory: Stack<Expr> = Stack.Empty) {

    private fun undo(expr: Expr): Stack<Expr> = when(expr) {
        is Expr.Const -> Stack.Empty
        is Expr.Add -> Stack.one(expr.x).add(expr.y)
        is Expr.Subtract -> Stack.one(expr.x).add(expr.y)
        is Expr.Multiply -> Stack.one(expr.x).add(expr.y)
        is Expr.Divide -> Stack.one(expr.x).add(expr.y)
        is Expr.Sqrt -> Stack.one(expr.x)
    }

    fun eval(input: String): Either<CalculatorError, Stack<Expr>> = when(input) {
        "+" -> requirePair(memory).map { (y, x) ->
            memory.drop(2).add(Expr.Add(x, y))
        }
        "-" -> requirePair(memory).map { (y, x) ->
            memory.drop(2).add(Expr.Subtract(x, y))
        }
        "*" -> requirePair(memory).map { (y, x) ->
            memory.drop(2).add(Expr.Multiply(x, y))
        }
        "/" -> requirePair(memory).map { (y, x) ->
            memory.drop(2).add(Expr.Divide(x, y))
        }
        "sqrt" -> requireOne(memory).map { x ->
            memory.drop(1).add(Expr.Sqrt(x))
        }
        "undo" -> requireOne(memory).map { x ->
            memory.drop(1).append(undo(x))
        }
        "clear" ->
            Right(Stack.Empty)
        else ->
            Either.catchExceptions { Expr.Const(input.toDouble()) }
                .leftMap { InputError(input) }
                .map { e -> memory.add(e) }
    }

    override fun toString(): String = memory.map { expr -> expr.eval() }.mkString(" ")

    companion object {
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
    }
}

sealed class CalculatorError
data class InputError(val input: String) : CalculatorError()
object InsufficientParametersError : CalculatorError()
