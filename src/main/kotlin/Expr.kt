import java.text.DecimalFormat
import kotlin.math.*

/**
 * A lazy mathematical expression encoded as an algebraic data type (ADT).
 */
sealed class Expr {
    data class Const(val value: Double) : Expr()
    data class Add(val x: Expr, val y: Expr) : Expr()
    data class Subtract(val x: Expr, val y: Expr) : Expr()
    data class Multiply(val x: Expr, val y: Expr) : Expr()
    data class Divide(val x: Expr, val y: Expr) : Expr()
    data class Sqrt(val x: Expr) : Expr()

    /**
     * Compute the final result of the expression
     */
    fun eval(): Double = when(this) {
        is Const -> value
        is Add -> x.eval() + y.eval()
        is Subtract -> x.eval() - y.eval()
        is Multiply -> x.eval() * y.eval()
        is Divide -> x.eval() / y.eval()
        is Sqrt -> sqrt(x.eval())
    }

    companion object : Show<Expr> {

        private val fmt = DecimalFormat("#.##########")

        override fun Expr.show(): String = fmt.format(this.eval())
    }
}
