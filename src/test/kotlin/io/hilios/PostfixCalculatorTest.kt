package io.hilios

import io.hilios.calculator.Expr
import io.hilios.data.Either.Left
import io.hilios.data.Stack
import io.hilios.data.add
import kotlin.test.*

class PostfixCalculatorTest {

    @Test
    fun `#eval should parse numbers from the input`() {
        assertEquals(Stack.one(Expr.Const(0)), PostfixCalculator.parse("000").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(0.0)), PostfixCalculator.parse("0.0").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(12)), PostfixCalculator.parse("012").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(123)), PostfixCalculator.parse("123").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(+123)), PostfixCalculator.parse("+123").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(-123)), PostfixCalculator.parse("-123").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(123.456)), PostfixCalculator.parse("123.456").get(Stack.Empty).first)
        assertEquals(Stack.one(Expr.Const(123.456789)), PostfixCalculator.parse("123.456789").get(Stack.Empty).first)
    }

    @Test
    fun `it should parse the basic algebraic operations and build complex expressions with it`() {
        val one = Expr.Const(1.0)
        val two = Expr.Const(2.0)
        val stack = Stack.one(one).add(two)

        assertEquals(Stack.one(Expr.Add(one, two)), PostfixCalculator.parse("+").get(stack).first)
        assertEquals(Stack.one(Expr.Subtract(one, two)), PostfixCalculator.parse("-").get(stack).first)
        assertEquals(Stack.one(Expr.Multiply(one, two)), PostfixCalculator.parse("*").get(stack).first)
        assertEquals(Stack.one(Expr.Divide(one, two)), PostfixCalculator.parse("/").get(stack).first)
        assertEquals(Stack.one(one).add(Expr.Sqrt(two)), PostfixCalculator.parse("sqrt").get(stack).first)
    }

    @Test
    fun `it should clear the memory`() {
        val one = Expr.Const(1.0)
        val two = Expr.Const(2.0)
        val stack = Stack.one(one).add(two)

        assertEquals(Stack.Empty, PostfixCalculator.parse("clear").get(stack).first)
    }

    @Test
    fun `it should return on the left otherwise`() {
        assert(PostfixCalculator.parse("Hello, World!").run(Stack.Empty) is Left)
        assert(PostfixCalculator.parse("^").run(Stack.Empty) is Left)
        assert(PostfixCalculator.parse("!").run(Stack.Empty) is Left)
        assert(PostfixCalculator.parse("sin").run(Stack.Empty) is Left)
        assert(PostfixCalculator.parse("cos").run(Stack.Empty) is Left)
        assert(PostfixCalculator.parse("1,234,567.00").run(Stack.Empty) is Left)
    }
}