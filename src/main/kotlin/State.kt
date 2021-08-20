/**
 * A minimal implementation of the State monad.
 */
data class State<S, out A>(private val fa: (S) -> Pair<S, A>) {
    fun <B> map(f: (A) -> B): State<S, B> = State {
        val (state, b) = fa(it)
        Pair(state, f(b))
    }
    
    fun get(initial: S): Pair<S, A> = fa(initial)
    
    fun run(initial: S): A = fa(initial).second
}

fun <S, B, C> State<S, B>.flatMap(f: (B) -> State<S, C>): State<S, C> = State {
    val (s, b) = this.get(it)
    f(b).get(s)
}