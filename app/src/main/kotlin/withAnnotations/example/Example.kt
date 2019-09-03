package withAnnotations.example
import arrow.Kind
import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.fx
import arrow.core.extensions.list.functorFilter.flattenOption
import arrow.core.extensions.list.traverse.traverse
import arrow.core.fix
import arrow.core.flatMap
import arrow.core.toOption
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.applicative.applicative
import arrow.fx.extensions.io.monad.monad
import arrow.typeclasses.Applicative
import arrow.typeclasses.MonadFx

abstract class Api {
    abstract fun getAllTags(): List<Tag>
    abstract fun createTag(name: String): Tag
    abstract fun recordTag(recordId: String, tagId: String): Unit
    abstract fun getRecord(recordId: String): Record
}

data class Tag(val id: String, val name: String)

fun updateTagList(recordId: String, tags: Array<String>, api: Api): Unit {
    val existingTags = api.getAllTags()
    val newTagNames = tags.filter {
        existingTags.any { tag -> tag.name == it }
    }

    val allTags = newTagNames.map { api.createTag(it) }.plus(existingTags)

    tags.map { allTags.find { tag -> tag.name == it } }.map {
        api.recordTag(recordId, it!!.id)
    }
}

sealed class Error {
    data class SomeError(val message: String): Error()
}

typealias Result<T> = Either<Error, T>

data class Record(val name: String, val tags: List<Tag>)

abstract class EitherApi {
    abstract fun getAllTags(): Result<List<Tag>>
    abstract fun createTag(name: String): Result<Tag>
    abstract fun recordTag(recordId: String, tagId: String): Result<Unit>
    abstract fun getRecord(name: String): Result<Record>
}

fun updateEitherTagList(recordId: String, tags: Array<String>, api: EitherApi): Result<Unit> {
    api.getAllTags().map { existingTags ->
        tags.filter { existingTags.any { tag -> tag.name == it }}
    }.flatMap {newTagNames ->
        newTagNames.traverse(Either.applicative()) { api.createTag(it) }.fix().map { it.fix() }
    }.map {

    }

    TODO()
}

fun updateComprehensionTagList(recordId: String, tags: Array<String>, api: EitherApi): Result<Record> = Either.fx {
    val (existingTags) = api.getAllTags()
    val newTagNames = tags.filter { existingTags.any { tag -> tag.name == it }}
    val (createdTags) = newTagNames.traverse(Either.applicative()) { api.createTag(it) }.fix().map { it.fix() }
    val allTags = createdTags.plus(existingTags)
    val newTags = tags.map { allTags.find { tag -> tag.name == it }.toOption() }.flattenOption()
    newTags.traverse(Either.applicative()) { api.recordTag(recordId, it.id )}
    api.getRecord(recordId).component1()
}

abstract class MonadicApi<G> {
    abstract fun getAllTags(m: MonadFx<G>): Kind<G, List<Tag>>
    abstract fun createTag(m: MonadFx<G>, name: String): Kind<G, Tag>
    abstract fun recordTag(m: MonadFx<G>, recordId: String, tagId: String): Kind<G, Unit>
    abstract fun getRecord(m: MonadFx<G>, name: String): Kind<G, Record>
}

fun <G> updateComprehensionMonadicTagList(m: MonadFx<G>, a: Applicative<G>, recordId: String, tagNames: List<String>, api: MonadicApi<G>) = m.monad {
    val (existingTags) = api.getAllTags(m)
    val newTagNames = tagNames.filter { existingTags.any { tag -> tag.name == it }}
    val (createdTags) = newTagNames.traverse(a) { api.createTag(m, it) }
    val allTags = createdTags.fix().plus(existingTags)
    val newTags = tagNames.map { allTags.find { tag -> tag.name == it }.toOption() }.flattenOption()
    newTags.traverse(a) { api.recordTag(m, recordId, it.id) }
    api.getRecord(m, recordId).bind()
}

val foo = updateComprehensionMonadicTagList(IO.monad().fx, IO.applicative(), "some record id", listOf(), object : MonadicApi<ForIO>() {
    override fun getAllTags(m: MonadFx<ForIO>): Kind<ForIO, List<Tag>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createTag(m: MonadFx<ForIO>, name: String): Kind<ForIO, Tag> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun recordTag(
        m: MonadFx<ForIO>,
        recordId: String,
        tagId: String
    ): Kind<ForIO, Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRecord(m: MonadFx<ForIO>, name: String): Kind<ForIO, Record> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
})


