import kotlin.system.exitProcess

tailrec fun runLoop(state: Stack<Expr>) {
    print("> ")
    val input = readLine()?.split("""\s+""".toRegex()) ?: emptyList()
    val stack = input.fold(state, { current, str ->
        PostfixCalculator.eval(current, str)
            .fold({ e ->
                println("operator $str (position: 0): $e")
                current
            }, { r -> r})
    })

    println("stack: ${stack.map { expr -> expr.eval() }.mkString(" ")}")
    runLoop(stack)
}

// 1 2 3 * 5 + * * 6 5

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
