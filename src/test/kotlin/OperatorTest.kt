import kotlin.test.*

class OperatorTest {
    @Test
    fun `InputParser should parse numbers from an input string or Invalid if it was impossible to perform the conversion`() {
        assertEquals(Right(Number(0.0)), "0".parse(PostfixCalculator))
        assertEquals(Right(Number(0.0)), "000".parse(PostfixCalculator))
        assertEquals(Right(Number(12.0)), "012".parse(PostfixCalculator))
        assertEquals(Right(Number(123.0)), "123".parse(PostfixCalculator))
        assertEquals(Right(Number(+123.0)), "+123".parse(PostfixCalculator))
        assertEquals(Right(Number(-123.0)), "-123".parse(PostfixCalculator))
        assertEquals(Right(Number(123.456)), "123.456".parse(PostfixCalculator))
        assertEquals(Right(Number(123.456789)), "123.456789".parse(PostfixCalculator))


        assert("1,23".parse(PostfixCalculator) is Left<*, *>)
    }

    @Test
    fun `it should parse the basic algebraic operations`() {
        assertEquals(Right(Plus), "+".parse(PostfixCalculator))
        assertEquals(Right(Minus), "-".parse(PostfixCalculator))
        assertEquals(Right(Times), "*".parse(PostfixCalculator))
        assertEquals(Right(Div), "/".parse(PostfixCalculator))
        assertEquals(Right(Sqrt), "sqrt".parse(PostfixCalculator))
    }

    @Test
    fun `it should parse other commands`() {
        assertEquals(Right(Clear), "clear".parse(PostfixCalculator))
        assertEquals(Right(Undo), "undo".parse(PostfixCalculator))
    }

    @Test
    fun `it should return on the left otherwise`() {
        assert("Hello, World!".parse(PostfixCalculator) is Left<*, *>)
        assert("^".parse(PostfixCalculator) is Left<*, *>)
        assert("!".parse(PostfixCalculator) is Left<*, *>)
        assert("sin".parse(PostfixCalculator) is Left<*, *>)
        assert("cos".parse(PostfixCalculator) is Left<*, *>)
        assert("1,234,567.00".parse(PostfixCalculator) is Left<*, *>)
    }
}