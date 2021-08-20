import CalculatorError.Companion.show
import Expr.Companion.show
import kotlin.system.exitProcess

/**
 * Evaluate the input and execute the side effects.
 */
tailrec fun runLoop(state: Stack<Expr>) {
    print("> ")
    val input = readLine()
        ?.split("""\s+""".toRegex())
        ?.filterNot { s -> s.isEmpty() }
        ?: exitProcess(1)
    
    val (stack, result) = PostfixCalculator.parse(*input.toTypedArray()).get(state)
    
    if (result is Left) println("error: ${result.value.show()}")
    println("stack: ${stack.map { expr -> expr.show() }.mkString(" ")}")
    
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
