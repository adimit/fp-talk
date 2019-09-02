import arrow.core.getOrElse
import arrow.core.toT
import arrow.optics.extensions.list.cons.cons
import arrow.optics.extensions.list.cons.uncons
import arrow.optics.extensions.list.snoc.snoc

/**
 * @author Aleksandar Dimitrov
 * @since  2019-09-01
 */

sealed class MyList<out T> {
    object Empty: MyList<Nothing>()
    data class Cons<T>(val h: T): MyList<T>()
}

fun MyList<Int>.sum(): Int = this.add(0)

private tailrec fun MyList<Int>.add(base: Int): Int = when (this) {
    is MyList.Empty -> base
    is MyList.Cons -> this.add(base + this.h)
}

sealed class Tree<out T> {
    data class Leaf<T>(val v: T): Tree<T>()
    data class Branch<T>(val l: Tree<T>, val r: Tree<T>): Tree<T>()
}

fun <T> Tree<T>.fold(): List<T> = preorder(listOf(this), listOf())

private tailrec fun <T> preorder(stack: List<Tree<T>>, acc: List<T>): List<T> {
    stack.isEmpty() && return acc
    val (h, t) = stack.uncons().getOrElse { stack[0] toT listOf<Tree<T>>() }
    return when (h) {
        is Tree.Leaf -> preorder(t, acc.snoc(h.v))
        is Tree.Branch -> preorder(h.l.cons(h.r.cons(t)), acc)
    }
}
