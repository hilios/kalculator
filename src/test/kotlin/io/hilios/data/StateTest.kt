package io.hilios.data

import kotlin.test.Test
import kotlin.test.assertEquals

class StateTest {
    
    private val initial = "abc"
    private val nextChar: State<String, Char> = State {
        val lastChar = it.toCharArray().last().inc()
        Pair("$it$lastChar", lastChar)
    }
    
    @Test
    fun `#run should return the final value after a given initial state`() {
        val d = nextChar.run(initial)
        assertEquals('d', d)
    }
    
    @Test
    fun `#get should return the final value and next state after a given initial state`() {
        val (str, char) = nextChar.get(initial)
        assertEquals('d', char)
        assertEquals("abcd", str)
    }
    
    @Test
    fun `#map should do something`() {
        val bool = nextChar.map { it == 'd' }.run(initial)
        assertEquals(true, bool)
    }
    
    @Test
    fun `#flatMap should keep accumulating the state at each iteration`() {
        val (str, _) = nextChar
            .flatMap { nextChar }
            .flatMap { nextChar }
            .flatMap { nextChar }
            .get(initial)
        assertEquals("abcdefg", str)
    }
    
    @Test
    fun `#flatMap should keep accumulating the state at each iteration (2)`() {
        val addOne: State<Stack<Int>, Int> = State {
            val int = when(it) {
                is Stack.Empty -> 0
                is Stack.Elem -> it.head + 1
            }
            Pair(it.add(int), int)
        }
        val (stack, int) = addOne
            .flatMap { addOne }
            .flatMap { addOne }
            .flatMap { addOne }
            .get(Stack.Empty)
        assertEquals(3, int)
        assertEquals(Stack.one(0).add(1).add(2).add(3), stack)
    }
}