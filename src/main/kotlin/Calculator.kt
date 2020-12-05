interface Calculator<T> {
    fun <T> T.run(stack: Stack<Double>, input: Input): Stack<Double>
}