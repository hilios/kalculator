import kotlin.test.*

class ParserTest {

    object FlakyParser : Parser<Int> {
        val ex: ParserResult<Int> = Left(ParserException("You didn't say the magic word"))

        override fun parse(input: String): ParserResult<Int> = ex
    }

    object IntParser : Parser<Int> {
        override fun parse(input: String): ParserResult<Int> =
            Either.catchExceptions { input.toInt() }
                .leftMap { ex -> ParserException("Value $input is not an integer", ex) }
    }

    @Test
    fun `#parse should return the resulting conversion at the Right`() {
        val int = "123".parse(IntParser)

        assertEquals(Right(123), int)
    }

    @Test
    fun `it should return a failure in the Left`() {
        val int = "123".parse(FlakyParser)

        assertEquals(FlakyParser.ex, int)
    }

    @Test
    fun `#orElse should return the first result which yield a result`() {
        val int1 = "321".parse(FlakyParser.orElse(IntParser))
        val int2 = "321".parse(IntParser.orElse(FlakyParser))

        assertEquals(Right(321), int1)
        assertEquals(Right(321), int2)
    }

    @Test
    fun `#pure should always return the same value`() {
        val always321 = Parser.pure(321)
        val int = "foo".parse(always321)

        assertEquals(Right(321), int)
    }

    @Test
    fun `#equals should return some value if the input`() {
        val plus123 = Parser.equals("+", 123)
        val plus = "+".parse(plus123)
        val minus = "-".parse(plus123)

        assertEquals(Right(123), plus)
        assertTrue(minus is Left<*, *>)
    }

    @Test
    fun `#catchExceptions should return a parser from a function that catch all exceptions`() {
        val alwaysFail = Parser.catchExceptions { throw Exception() }
        val failed = "foo".parse(alwaysFail)

        assertTrue(failed is Left<*, *>)
    }
}
