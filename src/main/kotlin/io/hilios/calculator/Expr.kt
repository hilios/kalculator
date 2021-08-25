package io.hilios.calculator

import io.hilios.data.Show
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat

/**
 * A lazy mathematical expression encoded as an algebraic data type (ADT).
 */
sealed class Expr {
    data class Const(val value: BigDecimal) : Expr() {
        constructor(value: Int) : this(BigDecimal(value.toString()))
        constructor(value: Double) : this(BigDecimal(value.toString()))
        constructor(value: String) : this(BigDecimal(value))
    }
    data class Add(val x: Expr, val y: Expr) : Expr()
    data class Subtract(val x: Expr, val y: Expr) : Expr()
    data class Multiply(val x: Expr, val y: Expr) : Expr()
    data class Divide(val x: Expr, val y: Expr) : Expr()
    data class Sqrt(val x: Expr) : Expr()

    /**
     * Compute the final result of the expression
     */
    @Throws(ArithmeticException::class)
    fun eval(): BigDecimal = when(this) {
        is Const -> value
        is Add -> x.eval() + y.eval()
        is Subtract -> x.eval() - y.eval()
        is Multiply -> x.eval() * y.eval()
        is Divide -> x.eval() / y.eval()
        is Sqrt -> x.eval().sqrt(MathContext.DECIMAL128)
    }

    companion object : Show<Expr> {
        private val fmt = DecimalFormat("#.##########")

        override fun Expr.show(): String =
            try {
                fmt.format(this.eval())
            } catch (e: ArithmeticException) {
                "NaN"
            }
    }
}
