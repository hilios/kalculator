import io.hilios.calculator.CalculatorError.Companion.show
import io.hilios.calculator.Expr
import io.hilios.calculator.Expr.Companion.show
import io.hilios.PostfixCalculator
import io.hilios.data.Either.Left
import io.hilios.data.Stack
import io.hilios.data.map
import io.hilios.data.mkString
import kotlin.system.exitProcess

/**
 * Evaluate the input and execute the side effects.
 */
tailrec fun runCalculator(state: Stack<Expr>) {
    print("> ")
    val input = readLine()
        ?.split("""\s+""".toRegex())
        ?.filterNot { it.isEmpty() }
        ?.toTypedArray()
        ?: exitProcess(1)

    val (stack, result) = PostfixCalculator.parse(*input).get(state)

    if (result is Left) println("error: ${result.value.show()}")

    val humanized = stack.map { expr -> expr.show() }.mkString(" ")
    println("stack: $humanized")

    runCalculator(stack)
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

    runCalculator(Stack.Empty)
    exitProcess(0)
}
