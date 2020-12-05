/**
 * A minimal implementation of the Either monad
 */
sealed class Either<out A, out B> {

    fun <C> map(fb: (B) -> C): Either<A, C> =
        flatMap { b -> Right(fb(b)) }

    fun <C> leftMap(fa: (A) -> C): Either<C, B> = when(this) {
        is Right -> Right(this.value)
        is Left -> Left(fa(this.value))
    }

    companion object {
        fun <A> catchExceptions(fn: () -> A): Either<Throwable, A> = try {
            Right(fn())
        } catch (ex: Exception) {
            Left(ex)
        }
    }
}
data class Right<A, out B>(val value: B): Either<A, B>()
data class Left<out A, B>(val value: A): Either<A, B>()

fun <A, B, C> Either<A, B>.flatMap(fn: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Right -> fn(this.value)
    is Left -> Left(this.value)
}

fun <A, B> Either<A, B>.orElse(other: Either<A, B>): Either<A, B> = when(this) {
    is Right -> this
    is Left -> other
}

fun <A, B, C> Either<A, B>.fold(fa: (A) -> C, fb: (B) -> C): C = when (this) {
    is Right -> fb(this.value)
    is Left -> fa(this.value)
}