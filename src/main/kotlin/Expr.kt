import kotlin.math.*

sealed class Expr {
    data class Const(val value: Double) : Expr()
    data class Add(val x: Expr, val y: Expr) : Expr()
    data class Subtract(val x: Expr, val y: Expr) : Expr()
    data class Multiply(val x: Expr, val y: Expr) : Expr()
    data class Divide(val x: Expr, val y: Expr) : Expr()
    data class Sqrt(val x: Expr) : Expr()

    fun eval(): Double = when(this) {
        is Const -> value
        is Add -> x.eval() + y.eval()
        is Subtract -> x.eval() - y.eval()
        is Multiply -> x.eval() * y.eval()
        is Divide -> x.eval() / y.eval()
        is Sqrt -> sqrt(x.eval())
    }
}

