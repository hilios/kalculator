/**
 * A minimal implementation of the Either monad
 */
sealed class Either<out A, out B> {

    fun <C> map(f: (B) -> C): Either<A, C> =
        flatMap { b -> Right(f(b)) }

    fun <C> leftMap(f: (A) -> C): Either<C, B> = when(this) {
        is Right -> Right(this.value)
        is Left -> Left(f(this.value))
    }

    fun get(): B? = when(this) {
        is Right -> this.value
        is Left -> null
    }

    companion object {
        fun <A> catchExceptions(f: () -> A): Either<Throwable, A> = try {
            Right(f())
        } catch (ex: Exception) {
            Left(ex)
        }

        fun <A, B> fromOption(b: B?, ifNull: () -> A): Either<A, B> =
            b?.let { v -> Right(v) } ?: Left(ifNull())
    }
}
data class Right<A, out B>(val value: B): Either<A, B>()
data class Left<out A, B>(val value: A): Either<A, B>()

fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Right -> f(this.value)
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