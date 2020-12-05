tailrec fun runLoop(init: Stack<String>) {
    print("> ")
    val input = readLine()?.split(" ") ?: emptyList()
    val stack = init.append(Stack.from(input))

    println("stack: ${stack.show()}")
    runLoop(stack)
}

fun main(args: Array<String>) {
    runLoop(Stack.Empty)
}
