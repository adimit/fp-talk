package withAnnotations.livecoding.plain

abstract class Api {
    abstract fun getAllTags(): List<Tag>
    abstract fun createTag(name: String): Tag
    abstract fun recordTag(recordId: String, tagId: String): Unit
    abstract fun getRecord(recordId: String): Record
}

data class Tag(val id: String, val name: String)
data class Record(val name: String, val tags: List<Tag>)

fun updateTagList(recordId: String, tags: Array<String>, api: Api): Record {
    val existingTags = api.getAllTags()
    val newTagNames = tags.filter {
        existingTags.any { tag -> tag.name == it }
    }

    val allTags = newTagNames.map { api.createTag(it) }.plus(existingTags)

    tags.map { allTags.find { tag -> tag.name == it } }.map {
        api.recordTag(recordId, it!!.id)
    }
    return api.getRecord(recordId)
}

