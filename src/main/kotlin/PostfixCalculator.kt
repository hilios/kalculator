typealias CalculatorResult<T> = Either<CalculatorError, T>

/**
 * A reverse polish notation calculator (a.k.a. Postfix) implementation based on a stack and a lazy mathematical
 * expression.
 *
 * Given an initial state and one or more inputs evaluates the next state.
 * This data type is implemented as an pure (without side-effects) immutable type, following the idea of the State monad.
 */
data class PostfixCalculator(private val state: Stack<Expr> = Stack.Empty) {

    /**
     * Unfolds an mathematical expression into its original bits.
     */
    private fun undo(expr: Expr): Stack<Expr> = when(expr) {
        is Expr.Const -> Stack.Empty
        is Expr.Add -> Stack.one(expr.x).add(expr.y)
        is Expr.Subtract -> Stack.one(expr.x).add(expr.y)
        is Expr.Multiply -> Stack.one(expr.x).add(expr.y)
        is Expr.Divide -> Stack.one(expr.x).add(expr.y)
        is Expr.Sqrt -> Stack.one(expr.x)
    }

    /**
     * Evaluate the input string and apply a transformation to the internal state and finally return either a new state
     * or an input error.
     */
    fun eval(input: String): CalculatorResult<Stack<Expr>> = when(input) {
        "+" -> requirePair(state).map { (y, x) ->
            state.drop(2).add(Expr.Add(x, y))
        }
        "-" -> requirePair(state).map { (y, x) ->
            state.drop(2).add(Expr.Subtract(x, y))
        }
        "*" -> requirePair(state).map { (y, x) ->
            state.drop(2).add(Expr.Multiply(x, y))
        }
        "/" -> requirePair(state).map { (y, x) ->
            state.drop(2).add(Expr.Divide(x, y))
        }
        "sqrt" -> requireOne(state).map { x ->
            state.drop(1).add(Expr.Sqrt(x))
        }
        "undo" -> requireOne(state).map { x ->
            state.drop(1).append(undo(x))
        }
        "clear" ->
            Right(Stack.Empty)
        else ->
            Either.catchExceptions { Expr.Const(input.toDouble()) }
                .leftMap { InputError(input) }
                .map { e -> state.add(e) }
    }

    /**
     * Evaluate a series of inputs, short-circuiting on the first error found. It returns the last state that succeeded
     * in the chain, wrapping any error into a traceable data type.
     */
    fun eval(vararg inputs: String): CalculatorResult<Stack<Expr>> {
        val init: Pair<Int, CalculatorResult<Stack<Expr>>> = Pair(1, Right(state))

        return inputs.fold(init, { (pos, result), input ->
            Pair(pos + input.length + 1, result.flatMap { currentState ->
                PostfixCalculator(currentState).eval(input).leftMap { cause ->
                    TraceableError(input, pos, currentState, cause)
                }
            })
        }).second
    }

    companion object {
        /**
         * Helper function to check and extract one element in the stack.
         */
        private fun <A> requireOne(stack: Stack<A>): Either<CalculatorError, A> = when(stack) {
            is Stack.Elem -> Right(stack.head)
            is Stack.Empty -> Left(InsufficientParametersError)
        }

        /**
         * Helper function to check and extract a pair of elements in the stack.
         */
        private fun <A> requirePair(stack: Stack<A>): Either<CalculatorError, Pair<A, A>> {
            val ea0 = Either.fromOption(stack.get(0), { InsufficientParametersError })
            val ea1 = Either.fromOption(stack.get(1), { InsufficientParametersError })
            return ea0.flatMap { a0 ->
                ea1.map { a1 -> Pair(a0, a1) }
            }
        }
    }
}