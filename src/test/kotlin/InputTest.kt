import kotlin.test.*

class InputTest {
    @Test
    fun `InputParser should parse numbers from an input string or Invalid if it was impossible to perform the conversion`() {
        assertEquals(Right(Number(0.0)), "0".parse(InputParser))
        assertEquals(Right(Number(0.0)), "000".parse(InputParser))
        assertEquals(Right(Number(123.0)), "123".parse(InputParser))
        assertEquals(Right(Number(+123.0)), "+123".parse(InputParser))
        assertEquals(Right(Number(-123.0)), "-123".parse(InputParser))
        assertEquals(Right(Number(123.456)), "123.456".parse(InputParser))
        assertEquals(Right(Number(123.456789)), "123.456789".parse(InputParser))

        assertEquals(Right(Invalid("foo")), "foo".parse(InputParser))
        assertEquals(Right(Invalid("1,23")), "1,23".parse(InputParser))
    }

    @Test
    fun `it should parse the basic algebraic operations`() {
        assertEquals(Right(Plus), "+".parse(InputParser))
        assertEquals(Right(Minus), "-".parse(InputParser))
        assertEquals(Right(Times), "*".parse(InputParser))
        assertEquals(Right(Div), "/".parse(InputParser))
        assertEquals(Right(Sqrt), "sqrt".parse(InputParser))
    }

    @Test
    fun `it should parse other commands`() {
        assertEquals(Right(Clear), "clear".parse(InputParser))
        assertEquals(Right(Undo), "undo".parse(InputParser))
    }

    @Test
    fun `it should return Invalid otherwise`() {
        assertEquals(Right(Invalid("Hello, World!")), "Hello, World!".parse(InputParser))
        assertEquals(Right(Invalid("^")), "^".parse(InputParser))
        assertEquals(Right(Invalid("!")), "!".parse(InputParser))
        assertEquals(Right(Invalid("sin")), "sin".parse(InputParser))
        assertEquals(Right(Invalid("cos")), "cos".parse(InputParser))
        assertEquals(Right(Invalid("1,234,567.00")), "1,234,567.00".parse(InputParser))
    }
}