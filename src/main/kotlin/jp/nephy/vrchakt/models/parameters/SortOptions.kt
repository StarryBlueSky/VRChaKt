package jp.nephy.vrchakt.models.parameters

enum class SortOptions(val key: String) {
    Popularity("popularity"),
    Created("created"),
    Updated("updated"),
    Order("order"),
    _createdAt("_created_at"),
    _updatedAt("_updated_at")
}
