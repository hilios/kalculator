import kotlin.math.sqrt

sealed class Operator
object Plus : Operator()
object Minus : Operator()
object Times : Operator()
object Div : Operator()
object Sqrt : Operator()
object Undo : Operator()
object Clear : Operator()
data class Number(val value: Double) : Operator()

object PostfixCalculator : Calculator<Operator>, Parser<Operator>, Show<Operator> {

    override fun Operator.show(): String = when(this) {
        is Plus -> "+"
        is Minus -> "-"
        is Times -> "*"
        is Div -> "/"
        is Sqrt -> "sqrt"
        is Undo -> "undo"
        is Clear -> "clear"
        is Number -> "${this.value}%f"
    }

    private val numberParser: Parser<Number> = Parser.catchExceptions { i -> Number(i.toDouble()) }
    private val plusParser: Parser<Plus>  = Parser.equals(Plus.show(), Plus)
    private val minusParser: Parser<Minus>  = Parser.equals(Minus.show(), Minus)
    private val timesParser: Parser<Times>  = Parser.equals(Times.show(), Times)
    private val divParser: Parser<Div> = Parser.equals(Div.show(), Div)
    private val sqrtParser: Parser<Sqrt>  = Parser.equals(Sqrt.show(), Sqrt)
    private val undoParser: Parser<Undo> = Parser.equals(Undo.show(), Undo)
    private val clearParser: Parser<Clear> = Parser.equals(Clear.show(), Clear)
    private val operatorParser: Parser<Operator> =
        numberParser
            .orElse(plusParser)
            .orElse(minusParser)
            .orElse(timesParser)
            .orElse(divParser)
            .orElse(sqrtParser)
            .orElse(undoParser)
            .orElse(clearParser)

    override fun parse(input: String): ParserResult<Operator> = operatorParser.parse(input)

    // Calculator
    private fun <A> getOne(stack: Stack<A>): Either<CalculatorError, A> = when(stack) {
        is Stack.Elem -> Right(stack.head)
        is Stack.Empty -> Left(InsufficientParametersError)
    }

    private fun <A> getPair(stack: Stack<A>): Either<CalculatorError, Pair<A, A>> {
        val ea0 = Either.fromOption(stack.get(0), { InsufficientParametersError })
        val ea1 = Either.fromOption(stack.get(1), { InsufficientParametersError })

        return ea0.flatMap { a0 ->
            ea1.map { a1 -> Pair(a0, a1) }
        }
    }

    override fun Operator.eval(stack: Stack<Double>): Either<CalculatorError, Stack<Double>> = when(this) {

        is Plus -> getPair(stack).map { (x, y) ->
            stack.drop(2).add(x + y)
        }

        is Minus -> getPair(stack).map { (x, y) ->
            stack.drop(2).add(x - y)
        }

        is Times -> getPair(stack).map { (x, y) ->
            stack.drop(2).add(x * y)
        }

        is Div -> getPair(stack).flatMap { (x, y) ->
            Either.catchExceptions { stack.drop(2).add(x / y) }.leftMap { ex -> MathError(ex) }
        }

        is Sqrt -> getOne(stack).map { num ->
            stack.drop(1).add(sqrt(num))
        }

        is Number -> Right(stack.add(value))

        is Clear -> Right(Stack.Empty)

        is Undo -> Right(stack.drop(1))
    }
}

