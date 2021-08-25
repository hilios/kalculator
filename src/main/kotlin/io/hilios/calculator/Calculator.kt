package io.hilios.calculator

import io.hilios.data.Either
import io.hilios.data.Either.Right
import io.hilios.data.Either.Left
import io.hilios.data.Stack
import io.hilios.data.State
import io.hilios.data.append
import io.hilios.data.drop
import io.hilios.data.flatMap
import io.hilios.data.get

/**
 * The memory is just a function from the current [Stack] to [Either] a [CalculatorError] or the updated [Stack], thus,
 * a simple type alias of the [State] monad.
 */
typealias Memory<A> = State<Stack<A>, Either<CalculatorError, Stack<A>>>

/**
 * The base structure of a calculator, which is, in essence, a string parser and a transformation function that
 * manipulate the inner memory and can either succeed or fail.
 *
 * The [Memory] is defined as a [State] monad that transforms a [Stack] of elements and returns either an
 * [CalculatorError] or the modified part of the stack.
 *
 * On the companion object are provides helper function such as that enables the calculator implementation to create
 * complex operations that interacts with the calculator [Memory] directly.
 */
fun interface Calculator<A> {
    fun parse(input: String): Memory<A>

    /**
     * Evaluate a series of inputs, short-circuiting on the first error found.
     * It returns the last state that succeeded in the chain and wrap the original error into a traceable data type.
     * @see <a href="https://impurepics.com/posts/2020-10-03-always-traverse.html">The answer is always traverse</a>
     */
    fun parse(vararg inputs: String): Memory<A> = Memory { stack ->
        // Get the stack from the memory to populate the initial state
        val init: Memory<A> = State { Pair(stack, Right(Stack.Empty)) }
        inputs.foldIndexed(init) { index, memory, input ->
            memory.flatMap { lastResult ->
                // Parse the input and combine the results into the memory
                parse(input).map { result ->
                    val wrapped = result.leftMap { CalculatorError.TraceableError(input, index, it) }
                    // Either's flatMap encodes the short circuit functionality, so we show the first error
                    lastResult.flatMap { wrapped }
                }
            }
        }.get(stack)
    }

    companion object {
        /**
         * Manipulates the current [Stack] in [Memory] without any side effects (pure function)
         */
        fun <A> pure(f: (Stack<A>) -> Stack<A>): Memory<A> = Memory {
            val stack = f(it)
            Pair(stack, Right(stack))
        }

        /**
         * Apply a transformation with side effects to the stack and append all elements to the [Memory] and if it fails
         * just returns the original stack
         */
        fun <A> fromEither(f: (Stack<A>) -> Either<CalculatorError, Stack<A>>): Memory<A> = Memory { stack ->
            when (val result = f(stack)) {
                is Right -> Pair(stack.append(result.value), Right(result.value))
                is Left -> Pair(stack, Left(result.value))
            }
        }

        /**
         * Apply the transformation function on the first element of the [Stack] or fail if there is no element
         */
        fun <A> requires1(f: (A) -> Stack<A>): Memory<A> = Memory { stack ->
            val x = stack.get(0) ?: return@Memory Pair(stack, Left(CalculatorError.InsufficientParametersError))
            val expr = f(x)
            Pair(stack.drop(1).append(expr), Right(expr))
        }

        /**
         * Apply the transformation function on the first two elements of the [Stack] or fail if there are not enough
         * elements in it
         */
        fun <A> requires2(f: (A, A) -> Stack<A>): Memory<A> = Memory { stack ->
            val x = stack.get(1) ?: return@Memory Pair(stack, Left(CalculatorError.InsufficientParametersError))
            val y = stack.get(0) ?: return@Memory Pair(stack, Left(CalculatorError.InsufficientParametersError))
            val expr = f(x, y)
            Pair(stack.drop(2).append(expr), Right(expr))
        }
    }
}
