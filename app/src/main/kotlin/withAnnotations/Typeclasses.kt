package withAnnotations

import arrow.Kind
import arrow.extension
import types.ForPerhaps
import types.Perhaps
import types.fix
import withAnnotations.perhaps.functor.functor

interface Functor<F> {
    fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B>
}

@extension
interface PerhapsFunctorInstance : Functor<ForPerhaps> {
    override fun <A, B> Kind<ForPerhaps, A>.map(f: (A) -> B): Kind<ForPerhaps, B> {
        return fix().map { f(it) }
    }
}

val foo = Perhaps.functor()
