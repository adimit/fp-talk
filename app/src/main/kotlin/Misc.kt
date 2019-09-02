fun <A, B, C> ((B) -> C)
        .compose(f: (A) -> B): (A) -> C = { this(f(it)) }

fun List<Int>.sum(): Int = TODO()
fun List<String>.parse(): List<Int> = TODO()

val parseAndSum = List<Int>::sum
    .compose<List<String>, List<Int>, Int>(
        List<String>::parse)
