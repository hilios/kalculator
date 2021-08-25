package io.hilios.data

import io.hilios.data.Either.Companion.catchExceptions
import io.hilios.data.Either.Companion.fromOption
import kotlin.test.*
import io.hilios.data.Either.Right
import io.hilios.data.Either.Left


class EitherTest {
    @Test
    fun `#get should return the right value or null`() {
        val right: Either<Nothing, Int> = Right(123)
        val left: Either<Int, Nothing> = Left(123)

        assertEquals(123, right.get())
        assertEquals(null, left.get())
    }

    @Test
    fun `#map should return the transformation of the right side of the either but not the left (functor, right bias)`() {
        val right123: Either<Nothing, Int> = Right(123)
        val right321 = right123.map { n -> n.toString().reversed() }
        assertEquals(Right("321"), right321)

        // Short-circuit on left
        val left123: Either<Int, Nothing> = Left(123)
        val left321: Either<Int, String> = left123.map { "321" }
        assertEquals(left123, left321)
    }

    @Test
    fun `#leftMap should return the transformation of the left side of the either`() {
        val right123: Either<Nothing, Int> = Right(123)
        val right321 = right123.leftMap  { "321" }
        assertEquals(right123, right321)

        val left123: Either<Int, Nothing> = Left(123)
        val left321: Either<String, Nothing> = left123.leftMap { "321" }
        assertEquals(Left("321"), left321)
    }

    @Test
    fun `#flatMap  should return the monadic transformation of two either (right bias)`() {
        val right123: Either<String, Int> = Right(123)
        val leftString: Either<String, Int> = Left("321")

        val right246 = right123.flatMap { a -> Right(a * 2) }
        val rightBias = leftString.flatMap { a -> Right(a * 2) }

        assertEquals(Right(246), right246)

        // Short-circuit on left
        assertEquals(leftString, rightBias)
        assertEquals(leftString, right246.flatMap { leftString })

        // Binds across Left
        val leftDouble: Either<Double, Int> = Left(12.3)
        assertEquals(leftDouble, right123.flatMap { leftDouble })
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

    // Companion object
    @Test
    fun `#catchExceptions return an either that wrap any exception as the left value`() {
        val fail = catchExceptions { throw Exception() }
        assertTrue(fail is Left)

        val right123 = catchExceptions { 123 }
        assertEquals(Right(123), right123)
    }

    @Test
    fun `#fromOption should lift some optional value into an either`(){
        val ok = fromOption("ok") { NullPointerException() }
        val nok = fromOption(null) { "nok" }
    
        assertEquals(Right("ok"), ok)
        assertEquals(Left("nok"), nok)
    }

}