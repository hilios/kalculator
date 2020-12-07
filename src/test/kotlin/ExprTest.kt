import Expr.Companion.show
import kotlin.test.*

class ExprTest {

    private val zero = Expr.Const(0.0)
    private val one = Expr.Const(1.0)
    private val two = Expr.Const(2.0)
    private val four = Expr.Const(4.0)

    @Test
    fun `#eval should perform basic algebraic operations`() {

        val sum = Expr.Add(one, two).eval()
        assertEquals(3.0, sum)

        val minus = Expr.Subtract(two, one).eval()
        assertEquals(1.0, minus)

        val minusOne = Expr.Subtract(one, two).eval()
        assertEquals(-1.0, minusOne)

        val times = Expr.Multiply(two, two).eval()
        assertEquals(4.0, times)

        val div = Expr.Divide(one, two).eval()
        assertEquals(0.5, div)

        val sqrt = Expr.Sqrt(four).eval()
        assertEquals(2.0, sqrt)
    }

    @Test
    fun `it should not have side-effects`() {
        val nan = Expr.Const(Double.NaN)
        val inf = Expr.Const(Double.POSITIVE_INFINITY)
        val neg = Expr.Const(-2.0)

        val div = Expr.Divide(one, zero).eval()
        assertEquals(Double.POSITIVE_INFINITY, div)

        val add = Expr.Add(one, nan).eval()
        assertEquals(Double.NaN, add)

        val sub = Expr.Subtract(inf, one).eval()
        assertEquals(Double.POSITIVE_INFINITY, sub)

        val sqrt = Expr.Sqrt(neg).eval()
        assertEquals(Double.NaN, sqrt)

        val nans = Expr.Add(nan, nan).eval()
        assertEquals(Double.NaN, nans)

        val wtf = Expr.Add(nan, inf).eval()
        assertEquals(Double.NaN, wtf)
    }

    @Test
    fun `#show should return evaluate an expresion before rendering`() {
        val three = Expr.Add(one, two).show()
        assertEquals("3", three)
    }

    @Test
    fun `#show should return a single digit when the number as no decimals`() {
        val one = Expr.Const(1.0).show()
        assertEquals("1", one)

        val half = Expr.Const(0.5).show()
        assertEquals("0.5", half)

        val pi = Expr.Const(Math.PI).show()
        assertEquals("3.1415926536", pi)
    }
}