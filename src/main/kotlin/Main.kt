import kotlin.system.exitProcess

tailrec fun runLoop(state: Stack<Expr>) {
    print("> ")
    val input = readLine()?.split("""\s+""".toRegex()) ?: emptyList()
    val stack = input.fold(state, { current, str ->
        PostfixCalculator(current)
            .eval(str)
            .fold({ e ->
                println("operator $str (position: 0): $e")
                current
            }, { r -> r})
    })

    println("stack: $stack")
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
