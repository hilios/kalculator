import CalculatorError.Companion.show
import Expr.Companion.show
import kotlin.system.exitProcess

/**
 * Evaluate the input and execute the side-effects.
 */
tailrec fun runLoop(state: Stack<Expr>) {
    print("> ")
    val input = readLine()?.split("""\s+""".toRegex()) ?: emptyList()
    val result = PostfixCalculator(state).eval(*input.toTypedArray())

    val stack = when(result) {
        is Right -> result.value
        is Left -> when(val left = result.value) {
            is TraceableError -> left.stack
            else -> Stack.Empty
        }
    }

    // print errors
    if(result is Left) {
        println(result.value.show())
    }

    println("stack: ${stack.map { expr -> expr.show() }.mkString(", ")}")
    runLoop(stack)
}

fun main(args: Array<String>) {
    println("""
     _     __   _    __   _    _     __ ________  ___  
    | |_/ / /\ | |  / /` | | || |   / /\ | |/ / \| |_) 
    |_| \/_/--\|_|__\_\_,\_\_/|_|__/_/--\|_|\_\_/|_| \
    
    A command line RPN calculator.
    
    Operations: +, -, *, /, sqrt, clean, undo
    Exit: Ctrl + C

    """.trimIndent())

    runLoop(Stack.Empty)
    exitProcess(0)
}
