/**
 * A reverse polish notation calculator (a.k.a. Postfix) implementation based on a stack and a lazy mathematical
 * expression.
 */
object PostfixCalculator : Calculator<Expr> {

    /**
     * Evaluate the input string and apply a transformation to the internal state and finally return either a new state
     * or an input error.
     */
    override fun parse(input: String): Memory<Expr> = when (input) {
        "+" -> Calculator.requires2 { x, y -> Stack.one(Expr.Add(x, y)) }
        "-" -> Calculator.requires2 { x, y -> Stack.one(Expr.Subtract(x, y)) }
        "*" -> Calculator.requires2 { x, y -> Stack.one(Expr.Multiply(x, y)) }
        "/" -> Calculator.requires2 { x, y -> Stack.one(Expr.Divide(x, y)) }
        "sqrt" -> Calculator.requires1 { x -> Stack.one(Expr.Sqrt(x)) }
        "undo" -> Calculator.requires1 {
            // Unfolds an expression into its original bits.
            when(it) {
                is Expr.Const -> Stack.Empty
                is Expr.Add -> Stack.one(it.x).add(it.y)
                is Expr.Subtract -> Stack.one(it.x).add(it.y)
                is Expr.Multiply -> Stack.one(it.x).add(it.y)
                is Expr.Divide -> Stack.one(it.x).add(it.y)
                is Expr.Sqrt -> Stack.one(it.x)
            }
        }
        "clear" -> Calculator.pure  { Stack.Empty }
        else -> Calculator.fromEither {
            Either.catchExceptions { Stack.one(Expr.Const(input)) }.leftMap { CalculatorError.InputError(input) }
        }
    }
}
