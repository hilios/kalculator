sealed class Input
object Plus : Input()
object Minus : Input()
object Times : Input()
object Div : Input()
object Sqrt : Input()
object Undo : Input()
object Clear : Input()
data class Number(val value: Double) : Input()
data class Invalid(val input: String) : Input()

object InputParser : Parser<Input> {
    private val invalidParser: Parser<Invalid> = Parser.catchExceptions { i -> Invalid(i) }
    private val numberParser: Parser<Number> = Parser.catchExceptions { i -> Number(i.toDouble()) }
    private val plusParser: Parser<Plus>  = Parser.equals("+", Plus)
    private val minusParser: Parser<Minus>  = Parser.equals("-", Minus)
    private val timesParser: Parser<Times>  = Parser.equals("*", Times)
    private val divParser: Parser<Div> = Parser.equals("/", Div)
    private val sqrtParser: Parser<Sqrt>  = Parser.equals("sqrt", Sqrt)
    private val undoParser: Parser<Undo> = Parser.equals("undo", Undo)
    private val clearParser: Parser<Clear> = Parser.equals("clear", Clear)
    private val inputParser: Parser<Input> =
        numberParser
            .orElse(plusParser)
            .orElse(minusParser)
            .orElse(timesParser)
            .orElse(divParser)
            .orElse(sqrtParser)
            .orElse(undoParser)
            .orElse(clearParser)
            .orElse(invalidParser)

    override fun parse(input: String): ParserResult<Input> = inputParser.parse(input)
}
