import kotlin.test.*

class PostfixCalculatorTest {

    @Test
    fun `#eval should parse numbers from the input`() {
        assertEquals(Right(Stack.one(Expr.Const(0.0))), PostfixCalculator().eval("0"))
        assertEquals(Right(Stack.one(Expr.Const(0.0))), PostfixCalculator().eval("000"))
        assertEquals(Right(Stack.one(Expr.Const(12.0))), PostfixCalculator().eval("012"))
        assertEquals(Right(Stack.one(Expr.Const(123.0))), PostfixCalculator().eval("123"))
        assertEquals(Right(Stack.one(Expr.Const(+123.0))), PostfixCalculator().eval("+123"))
        assertEquals(Right(Stack.one(Expr.Const(-123.0))), PostfixCalculator().eval("-123"))
        assertEquals(Right(Stack.one(Expr.Const(123.456))), PostfixCalculator().eval("123.456"))
        assertEquals(Right(Stack.one(Expr.Const(123.456789))), PostfixCalculator().eval("123.456789"))

    }

    @Test
    fun `it should parse the basic algebraic operations and build complex expressions with it`() {
        val one = Expr.Const(1.0)
        val two = Expr.Const(2.0)
        val stack = Stack.one(one).add(two)

        assertEquals(Right(Stack.one(Expr.Add(one, two))), PostfixCalculator(stack).eval("+"))
        assertEquals(Right(Stack.one(Expr.Subtract(one, two))), PostfixCalculator(stack).eval("-"))
        assertEquals(Right(Stack.one(Expr.Multiply(one, two))), PostfixCalculator(stack).eval("*"))
        assertEquals(Right(Stack.one(Expr.Divide(one, two))), PostfixCalculator(stack).eval("/"))
        assertEquals(Right(Stack.one(one).add(Expr.Sqrt(two))), PostfixCalculator(stack).eval("sqrt"))
    }

    @Test
    fun `it should clear the memory`() {
        val one = Expr.Const(1.0)
        val two = Expr.Const(2.0)
        val stack = Stack.one(one).add(two)

        assertEquals(Right(Stack.Empty), PostfixCalculator(stack).eval("clear"))
    }

    @Test
    fun `it should return on the left otherwise`() {
        assert(PostfixCalculator().eval("Hello, World!") is Left<*, *>)
        assert(PostfixCalculator().eval("^") is Left<*, *>)
        assert(PostfixCalculator().eval("!") is Left<*, *>)
        assert(PostfixCalculator().eval("sin") is Left<*, *>)
        assert(PostfixCalculator().eval("cos") is Left<*, *>)
        assert(PostfixCalculator().eval("1,234,567.00") is Left<*, *>)
    }
}