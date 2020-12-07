import kotlin.test.*

class ExprTest {

    val one = Expr.Const(1.0)
    val two = Expr.Const(2.0)

    @Test
    fun `#eval should perform basic algebraic operations`() {
        val sum = Expr.Add(one, two).eval()
        assertEquals(3.0, sum)

        val minus = Expr.Subtract(two, one).eval()
        assertEquals(1.0, minus)

        val times = Expr.Multiply(two, two).eval()
        assertEquals(4.0, times)

        val div = Expr.Divide(one, two).eval()
        assertEquals(0.5, div)

        val sqrt = Expr.Sqrt(Expr.Const(4.0)).eval()
        assertEquals(2.0, sqrt)
    }
}