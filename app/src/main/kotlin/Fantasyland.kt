type interface Eq<A> {
    fun A.eq(b: A): Boolean
}
data class Record(val id: String, val name: String)

instance Eq<Record> {
    override fun Record.eq(b: Record) = this.id == b.id
}

fun <T: Eq> List<T>.filter(like: T): List<T> = TODO()


type interface Functor<F> {
    fun <A, B> F<A>.fmap(f: (A) -> B): F<B>
}

instance Functor<List> {
    override fun <A, B> List<A>
            .fmap(f: (A) -> B): List<B> = this.map(f)
}

fun <F> parseInt(intf: F<Int>): F<String> =
    intf.fmap { it.toString() }