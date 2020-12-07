/**
 * The show type class allows the delegation of how to render objects to string into the local scope.
 */
interface Show<T> {
    fun T.show(): String
}