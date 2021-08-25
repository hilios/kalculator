package io.hilios.data

import kotlin.test.*

class StackTest {

    @Test
    fun `#from should return empty if the iterable or iterator has no elements`() {
        val list = emptyList<Int>()
        val stack = Stack.from(list)

        assertEquals(Stack.Empty, stack)
    }

    @Test
    fun `it should create an instance from any iterator in the reverse order LIFO`() {
        val list = listOf(1, 2, 3)
        val stack = Stack.from(list.iterator())

        assertEquals(stack, Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty))))
    }

    @Test
    fun `it should create an instance from any iterable in the reverse order LIFO`() {
        val list = listOf(4, 5 ,6)
        val stack = Stack.from(list)

        assertEquals(Stack.Elem(6, Stack.Elem(5, Stack.Elem(4, Stack.Empty))), stack)
    }

    // The Monoid commutative laws
    @Test
    fun `#add should return a new stack with a new elements to the top of the stack`() {
        val stack = Stack.Empty.add(1).add(2).add(3)

        assertEquals(Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty))), stack)
    }

    @Test
    fun `#map should return a new stack`() {
        val ints = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val strs = ints.map { i -> i.toString() }

        assertEquals(Stack.Elem("3", Stack.Elem("2", Stack.Elem("1", Stack.Empty))), strs)
    }

    @Test
    fun `#append should return an stack with the elements from one stack on top of another`() {
        val x = Stack.Elem(2, Stack.Elem(1, Stack.Empty))
        val y = Stack.Elem(4, Stack.Elem(3, Stack.Empty))
        val stack = x.append(y)

        assertEquals(Stack.Elem(4, Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))), stack)
    }

    @Test
    fun `it should return the stack elements when associating with an empty`() {
        val x = Stack.Elem(2, Stack.Elem(1, Stack.Empty))
        val y = Stack.Empty

        assertEquals(x.append(y), y.append(x))
        assertEquals(Stack.Empty, Stack.Empty.append(Stack.Empty))
    }

    @Test
    fun `#fold should return the accumulate result of all elements of the stack`() {
        val ints = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val str = ints.fold("", { acc, i ->
            if (acc.isBlank()) "$i" else "$acc $i"
        })

        assertEquals("3 2 1", str)
    }

    // Last in first out operations
    @Test
    fun `#pop should return a pair with the value of the top element of the stack and the rest of the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val (three, rest) = stack.pop()

        assertEquals(3, three)
        assertEquals(Stack.Elem(2, Stack.Elem(1, Stack.Empty)), rest)
    }

    @Test
    fun `#get should return the value at the given position from the top of the stack or null if out of bounds`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))

        assertEquals(3, stack.get(0))
        assertEquals(2, stack.get(1))
        assertEquals(1, stack.get(2))
        assertEquals(null, stack.get(5))
    }

    @Test
    fun `#take should return the first N elements on the top of the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val take2 = stack.take(2)

        assertEquals(Stack.Elem(3, Stack.Elem(2, Stack.Empty)), take2)
    }

    @Test
    fun `it should return the whole stack if one try to take more elements than is available in the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val take5 = stack.take(5)

        assertEquals(stack, take5)
    }

    @Test
    fun `#drop should remove the first N elements on the top of the stack and return the rest`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val drop1 = stack.drop(1)
        val drop2 = stack.drop(2)

        assertEquals(Stack.Elem(2, Stack.Elem(1, Stack.Empty)), drop1)
        assertEquals(Stack.Elem(1, Stack.Empty), drop2)
    }

    @Test
    fun `it should return empty of you try to drop more elements than it is available in the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val drop5 = stack.drop(5)

        assertEquals(Stack.Empty, drop5)
    }

    @Test
    fun `#length should return the length of the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))

        assertEquals(3, stack.length())
        assertEquals(0, Stack.Empty.length())
    }

    @Test
    fun `#isEmpty should return true if stack as no elements or true otherwise`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))
        val empty = Stack.Empty

        assertFalse(stack.isEmpty())
        assertTrue(empty.isEmpty())
    }

    @Test
    fun `#mkString should return a string in the reverse order of the stack`() {
        val stack = Stack.Elem(3, Stack.Elem(2, Stack.Elem(1, Stack.Empty)))

        println(stack.mkString(" "))

        assertEquals("1 2 3", stack.mkString(" "))
    }

    @Test
    fun `it should return a string in the reverse order of the stack (2)`() {
        val input = "1 2 3 * 5 + * * 6 5"
        val stack = Stack.from(input.split(" "))

        assertEquals(input, stack.mkString(" "))
    }
}