/**
 * An immutable stack, last in first out, implementation
 */
sealed class Stack<out A> {
    data class Elem<out A>(val head: A, val tail: Stack<A>) : Stack<A>()
    object Empty : Stack<Nothing>()

    companion object {
        fun <A> one(value: A): Stack<A> = Elem(value, Empty)

        fun <A> from(iterator: Iterator<A>): Stack<A> {
            tailrec fun <A> traverse(iterator: Iterator<A>, accumulated: Stack<A> = Empty): Stack<A> = when {
                iterator.hasNext() -> traverse(iterator, Elem(iterator.next(), accumulated))
                else -> accumulated
            }
            return traverse(iterator)
        }

        fun <A> from(iterable: Iterable<A>): Stack<A> = from(iterable.iterator())
    }
}

fun <A, B> Stack<A>.map(f: (A) -> B): Stack<B> {
    fun traverse(stack: Stack<A>): Stack<B> = when(stack) {
        is Stack.Elem -> Stack.Elem(f(stack.head), traverse(stack.tail))
        is Stack.Empty -> Stack.Empty
    }

    return traverse(this)
}

fun <A> Stack<A>.append(other: Stack<A>): Stack<A> {
    fun combine(one: Stack<A>, other: Stack<A>): Stack<A> = when(other) {
        is Stack.Elem -> Stack.Elem(other.head, combine(one, other.tail))
        is Stack.Empty -> one
    }

    return combine(this, other)
}

fun <A, B> Stack<A>.fold(init: B, f: (B, A) -> B): B {
    tailrec fun reduce(stack: Stack<A>, accum: B): B = when(stack) {
        is Stack.Elem -> reduce(stack.tail, f(accum, stack.head))
        is Stack.Empty -> accum
    }

    return reduce(this, init)
}
fun <A> Stack<A>.add(elem: A): Stack<A> = Stack.Elem(elem, this)

fun <A> Stack<A>.pop(): Pair<A?, Stack<A>> = when (this) {
    is Stack.Elem -> Pair(this.head, this.tail)
    is Stack.Empty -> Pair(null, Stack.Empty)
}

fun <A> Stack<A>.get(index: Int): A? {
    tailrec fun traverse(stack: Stack<A>, depth: Int = 0): A? = when(stack) {
        is Stack.Elem -> if (depth == index) stack.head else traverse(stack.tail, depth + 1)
        is Stack.Empty -> null
    }

    return traverse(this)
}

fun <A> Stack<A>.take(n: Int): Stack<A> {
    fun traverse(stack: Stack<A>, depth: Int = 0): Stack<A> = when(stack) {
        is Stack.Elem -> if (depth == n - 1) {
            Stack.Elem(stack.head, Stack.Empty)
        } else {
            Stack.Elem(stack.head, traverse(stack.tail, depth + 1))
        }
        is Stack.Empty -> Stack.Empty
    }

    return traverse(this)
}

fun <A> Stack<A>.drop(n: Int): Stack<A> {
    tailrec fun traverse(stack: Stack<A>, depth: Int = 0): Stack<A> = when(stack) {
        is Stack.Elem -> if (depth == n - 1) stack.tail else traverse(stack.tail, depth + 1)
        is Stack.Empty -> Stack.Empty
    }

    return traverse(this)
}

fun <A> Stack<A>.length(): Int = this.fold(0, { accum, _ -> accum + 1 })

fun <A> Stack<A>.isEmpty(): Boolean = when(this) {
    is Stack.Empty -> true
    else -> false
}