package withAnnotations.livecoding.either

import arrow.core.Either
import arrow.core.extensions.fx
import withAnnotations.example.EitherApi

typealias Result<T> = Either<Error, T>

data class Tag(val id: String, val name: String)
data class Record(val name: String, val tags: List<Tag>)

abstract class EitherApi {
    abstract fun getAllTags(): Result<List<Tag>>
    abstract fun createTag(name: String): Result<Tag>
    abstract fun recordTag(recordId: String, tagId: String): Result<Unit>
    abstract fun getRecord(name: String): Result<Record>
}

fun updateEitherTagList(recordId: String, tags: Array<String>, api: EitherApi): Result<Unit> {
    TODO()
}

fun updateComprehensionTagList(recordId: String, tags: Array<String>, api: EitherApi): Result<withAnnotations.example.Record> = Either.fx {
    TODO()
}

