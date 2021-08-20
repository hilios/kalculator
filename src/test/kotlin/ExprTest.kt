import Expr.Companion.show
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.*

class ExprTest {

    private val zero = Expr.Const(0.0)
    private val one = Expr.Const(1.0)
    private val two = Expr.Const(2.0)
    private val four = Expr.Const(4.0)

    @Test
    fun `#eval should perform basic algebraic operations`() {
        val sum = Expr.Add(one, two).eval()
        assertEquals(BigDecimal("3.0").toDouble(), sum.toDouble())

        val minus = Expr.Subtract(two, one).eval()
        assertEquals(BigDecimal("1").toDouble(), minus.toDouble())

        val minusOne = Expr.Subtract(one, two).eval()
        assertEquals(BigDecimal("-1").toDouble(), minusOne.toDouble())

        val times = Expr.Multiply(two, two).eval()
        assertEquals(BigDecimal("4").toDouble(), times.toDouble())

        val div = Expr.Divide(one, two).eval()
        assertEquals(BigDecimal("0.5").toDouble(), div.toDouble())

        val sqrt = Expr.Sqrt(four).eval()
        assertEquals(BigDecimal("2.0").toDouble(), sqrt.toDouble())
    }

    @Test
    fun `#eval should have side effects`() {
        val neg = Expr.Const(-2.0)
    
        assertThrows<ArithmeticException> {
            Expr.Divide(one, zero).eval()
        }
    
        assertThrows<ArithmeticException> {
            Expr.Sqrt(neg).eval()
        }
    }
    
    @Test
    fun `#equals should evaluate the expression as strings`() {
        assertEquals(Expr.Const(3.33), Expr.Const("3.33"))
    }

    @Test
    fun `#show should evaluate the expression before rendering`() {
        val three = Expr.Add(one, two).show()
        assertEquals("3", three)
    }

    @Test
    fun `#show should return a single digit when the number as no decimals`() {
        val one = Expr.Const(BigDecimal("1.0")).show()
        assertEquals("1", one)

        val half = Expr.Const(BigDecimal("0.5")).show()
        assertEquals("0,5", half)

        val pi = Expr.Const(BigDecimal(Math.PI)).show()
        assertEquals("3,1415926536", pi)
    }
    
    @Test
    fun `#show should not have side effects`() {
        val neg = Expr.Const(-2.0)
        assertEquals("NaN", Expr.Divide(one, zero).show())
        assertEquals("NaN", Expr.Sqrt(neg).show())
    }
}