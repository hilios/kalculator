import PostfixCalculator.eval
import kotlin.system.exitProcess

//object StackShow<T> : Show<Stack<T>> {
//    override fun Stack<String>.show(): String = {
//        tailrec fun reduce(stack: Stack<T>, accum: String = ""): String = when(stack) {
//            is Stack.Elem -> reduce(stack.tail, "${stack.head} $accum")
//            is Stack.Empty -> accum.dropLast(1)
//        }
//
//        return reduce(this)
//    }
//}

tailrec fun runLoop(state: Stack<Double>) {
    print("> ")
    val input = readLine()?.split(" ") ?: emptyList()
    val stack = input.fold(state, { current, str ->
        str.parse(PostfixCalculator)
            .leftMap { InputError("Invalid input: $str") }
            .flatMap { op -> op.eval(current)  }
            .fold({ e -> println("Failed: $e"); current }, { r -> r })
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
