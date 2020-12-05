/**
 * An immutable stack, last in first out, implementation
 */
sealed class Stack<out T> {
    data class Elem<out T>(val head: T, val tail: Stack<T>) : Stack<T>()
    object Empty : Stack<Nothing>()

    companion object {
        fun <T> from(iterator: Iterator<T>): Stack<T> {
            tailrec fun <T> traverse(iterator: Iterator<T>, accumulated: Stack<T> = Empty): Stack<T> = when {
                iterator.hasNext() -> traverse(iterator, Elem(iterator.next(), accumulated))
                else -> accumulated
            }
            return traverse(iterator)
        }

        fun <T> from(iterable: Iterable<T>): Stack<T> = from(iterable.iterator())
    }
}

fun <T> Stack<T>.append(other: Stack<T>): Stack<T> {
    fun combine(one: Stack<T>, other: Stack<T>): Stack<T> = when(other) {
        is Stack.Elem -> Stack.Elem(other.head, combine(one, other.tail))
        is Stack.Empty -> one
    }

    return combine(this, other)
}

fun <T> Stack<T>.add(elem: T): Stack<T> = Stack.Elem(elem, this)

fun <T> Stack<T>.pop(): Pair<T?, Stack<T>> = when (this) {
    is Stack.Elem -> Pair(this.head, this.tail)
    is Stack.Empty -> Pair(null, Stack.Empty)
}

fun <T> Stack<T>.take(n: Int): Stack<T> {
    fun traverse(stack: Stack<T>, depth: Int = 0): Stack<T> = when(stack) {
        is Stack.Elem -> if (depth == n - 1) {
            Stack.Elem(stack.head, Stack.Empty)
        } else {
            Stack.Elem(stack.head, traverse(stack.tail, depth + 1))
        }
        is Stack.Empty -> Stack.Empty
    }

    return traverse(this)
}

fun <T> Stack<T>.drop(n: Int): Stack<T> {
    tailrec fun traverse(stack: Stack<T>, depth: Int = 0): Stack<T> = when(stack) {
        is Stack.Elem -> if (depth == n - 1) stack.tail else traverse(stack.tail, depth + 1)
        is Stack.Empty -> Stack.Empty
    }

    return traverse(this)
}

fun <T> Stack<T>.length(): Int {
    tailrec fun reduce(accumulated: Stack<T>, depth: Int = 0): Int = when(accumulated) {
        is Stack.Elem -> reduce(accumulated.tail, depth + 1)
        is Stack.Empty -> depth
    }

    return reduce(this)
}

fun <T> Stack<T>.show(): String {
    tailrec fun reduce(stack: Stack<T>, accum: String = ""): String = when(stack) {
        is Stack.Elem -> reduce(stack.tail, "${stack.head} $accum")
        is Stack.Empty -> accum.dropLast(1)
    }

    return reduce(this)
}