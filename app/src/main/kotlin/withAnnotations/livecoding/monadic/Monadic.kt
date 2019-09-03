package withAnnotations.livecoding.monadic

import arrow.Kind
import arrow.typeclasses.Applicative
import arrow.typeclasses.MonadFx
import withAnnotations.example.MonadicApi

data class Tag(val id: String, val name: String)
data class Record(val name: String, val tags: List<Tag>)

abstract class MonadicApi<G> {
    abstract fun getAllTags(m: MonadFx<G>): Kind<G, List<Tag>>
    abstract fun createTag(m: MonadFx<G>, name: String): Kind<G, Tag>
    abstract fun recordTag(m: MonadFx<G>, recordId: String, tagId: String): Kind<G, Unit>
    abstract fun getRecord(m: MonadFx<G>, name: String): Kind<G, Record>
}

fun <G> updateComprehensionMonadicTagList(m: MonadFx<G>, a: Applicative<G>, recordId: String, tagNames: List<String>, api: MonadicApi<G>) = m.monad {

}
