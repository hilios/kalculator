interface Parser<out T> {

    infix fun parse(input: String): ParserResult<T>

    companion object {
        fun <T> pure(value: T) = object : Parser<T> {
            override fun parse(input: String): ParserResult<T> = Right(value)
        }

        fun <T> equals(value: String, out: T): Parser<T>  = object : Parser<T> {
            override fun parse(input: String): ParserResult<T> =
                if (input == value) Right(out) else Left(ParserException("Value $input is not $input"))
        }

        fun <T> catchExceptions(f: (String) -> T) = object : Parser<T> {
            override fun parse(input: String): ParserResult<T> =
                Either.catchExceptions { f(input) }
                    .leftMap { ex -> ParserException("Value $input could not be parsed", ex) }
        }
    }
}

typealias ParserResult<T> = Either<ParserException, T>

data class ParserException(override val message: String, override val cause: Throwable? = null) : Exception()

fun <T> Parser<T>.orElse(other: Parser<T>): Parser<T> {
    return object : Parser<T> {
        override fun parse(input: String): ParserResult<T> =
            this@orElse.parse(input).orElse(other.parse(input))
    }
}

fun <T> String.parse(parser: Parser<T>): ParserResult<T> = parser.parse(this)