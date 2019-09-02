package types

import arrow.higherkind

@higherkind
sealed class Perhaps<out T> : PerhapsOf<T> {
    object Mmno: Perhaps<Nothing>()
    data class Duh<T>(val just: T): Perhaps<T>()

    fun <B> map(f: (T) -> B): PerhapsOf<B> = when (this) {
        is Mmno -> Mmno
        is Duh -> Duh(f(just))
    }

    companion object
}

