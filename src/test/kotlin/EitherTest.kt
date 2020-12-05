import kotlin.test.*

class EitherTest {
    @Test
    fun `#map should return the transformation of the right side of the either but not the left (functor, right bias)`() {
        val right123: Either<Nothing, Int> = Right(123)
        val right321 = right123.map { n -> n.toString().reversed() }
        assertEquals(Right("321"), right321)

        // Short-circuit on left
        val left123: Either<Int, Nothing> = Left(123)
        val left321: Either<Int, String> = left123.map { n -> n.toString().reversed() }
        assertEquals(left123, left321)
    }

    @Test
    fun `#leftMap should return the transformation of the left side of the either`() {
        val right123: Either<Nothing, Int> = Right(123)
        val right321 = right123.leftMap  { n -> n.toString().reversed() }
        assertEquals(right123, right321)

        val left123: Either<Int, Nothing> = Left(123)
        val left321: Either<String, Nothing> = left123.leftMap { n -> n.toString().reversed() }
        assertEquals(Left("321"), left321)
    }

    @Test
    fun `#flatMap  should return the monadic transformation of two either (right bias)`() {
        val right123: Either<String, Int> = Right(123)
        val left321: Either<String, Int> = Left("321")

        val right246 = right123.flatMap { a -> Right(a * 2) }
        val rightBias = left321.flatMap { a -> Right(a * 2) }

        assertEquals(Right(246), right246)

        // Short-circuit on left
        assertEquals(left321, rightBias)
        assertEquals(left321, right246.flatMap { left321 })
    }

    @Test
    fun `#orElse should return the first right among the either chain or the last left`() {
        val right123: Either<String, Int> = Right(123)
        val left123: Either<String, Int> = Left("123")
        val left321: Either<String, Int> = Left("321")

        assertEquals(right123, right123.orElse(left123))
        assertEquals(right123, left123.orElse(right123))
        // Commutative on right but not on left
        assertEquals(right123.orElse(left123), left123.orElse(right123))
        assertEquals(left321, left123.orElse(left321))
        assertEquals(left123, left321.orElse(left123))
    }

    @Test
    fun `#fold should return a single value by transforming either side of the value (bifunctor)`() {
        val right123: Either<Int, Double> = Right(123.0)
        val left321: Either<Int, Double> = Left(321)

        assertEquals("123.0", right123.fold({ i -> i.toString() }, { d -> d.toString()}))
        assertEquals("321", left321.fold({ i -> i.toString() }, { d -> d.toString()}))
    }

    @Test
    fun `#catchExceptions return an either that wrap any exception as the left value`() {
        val fail = Either.catchExceptions { throw Exception() }
        assertTrue(fail is Left<*, *>)

        val right123 = Either.catchExceptions { 123 }
        assertEquals(Right(123), right123)
    }

}