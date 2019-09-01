/**
 * @author Aleksandar Dimitrov
 * @since  2019-09-01
 */

interface Eqty<T> {
    fun eq(a: T, b: T): Boolean
}

object StringEqtyInstance : Eqty<String> {
    override fun eq(a: String, b: String): Boolean
            = a.toLowerCase() == b.toLowerCase()
}

fun <T> List<T>.find(t: T, eqty: Eqty<T>): List<T>
        = flatMap { eqty.run {
    if (eq(t, it)) listOf(it) else listOf() }
}

fun fooList(l: List<String>) = l.find("FOO", StringEqtyInstance)

interface Functor<F> {
    fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B>
}

interface Kind<out F, out A>
class ForMaybe private constructor()
typealias MaybeOf<T> = Kind<ForMaybe, T>

sealed class Maybe<out T> : MaybeOf<T> {
    object No: Maybe<Nothing>()
    data class Yes<T>(val just: T): Maybe<T>()

    fun <B> map(f: (T) -> B): MaybeOf<B> = when (this) {
        is No -> No
        is Yes -> Yes(f(just))
    }

    companion object
}

fun <A> MaybeOf<A>.fix(): Maybe<A> = this as Maybe<A> // 😱
interface MaybeFunctorInstance : Functor<ForMaybe> {
    override fun <A, B> Kind<ForMaybe, A>.map(f: (A) -> B): Kind<ForMaybe, B> {
        return fix().map { f(it) }
    }
}

fun Maybe.Companion.functor(): MaybeFunctorInstance = object : MaybeFunctorInstance { }

fun <F> incrementAll(f: Functor<F>, a: Kind<F, Int>): Kind<F, Int> = f.run {
    a.map { it + 1 }
}

val foo: Maybe<Int> = incrementAll(Maybe.functor(), Maybe.Yes(1)).fix() // just 2
val bar: Maybe<Int> = incrementAll(Maybe.functor(), Maybe.No).fix() // No
