package io.hilios.data

/**
 * A minimal implementation of the Either monad
 */
sealed class Either<out A, out B> {
    data class Right<out B>(val value: B): Either<Nothing, B>()
    data class Left<out A>(val value: A): Either<A, Nothing>()

    fun <C> map(f: (B) -> C): Either<A, C> =
        flatMap { b -> Right(f(b)) }

    fun <C> leftMap(f: (A) -> C): Either<C, B> = when(this) {
        is Right -> Right(value)
        is Left -> Left(f(value))
    }

    fun get(): B? = when(this) {
        is Right -> value
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

fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Either.Right -> f(value)
    is Either.Left -> Either.Left(value)
}

fun <A, B> Either<A, B>.orElse(other: Either<A, B>): Either<A, B> = when(this) {
    is Either.Right -> this
    is Either.Left -> other
}

fun <A, B, C> Either<A, B>.fold(fa: (A) -> C, fb: (B) -> C): C = when (this) {
    is Either.Right -> fb(value)
    is Either.Left -> fa(value)
}
