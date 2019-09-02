
import arrow.optics.optics

fun <A, B, C> ((B) -> C)
        .compose(f: (A) -> B): (A) -> C = { this(f(it)) }

fun List<Int>.sum(): Int = TODO()
fun List<String>.parse(): List<Int> = TODO()

val parseAndSum = List<Int>::sum
    .compose<List<String>, List<Int>, Int>(
        List<String>::parse)

@optics data class Street(
    val number: Int, val name: String) {companion object}
@optics data class Address(
    val city: String, val street: Street) {companion object}
@optics data class Company(
    val name: String, val address: Address) {companion object}
@optics data class Employee(
    val name: String, val company: Company?) {companion object}

val e : Employee = TODO()
val f = Employee.company.address.street.name.modify(e) { "$it-Str" }
