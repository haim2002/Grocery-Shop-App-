package grocery.shopping.data

data class ListInfo(
    var listName: String = UNNAMED_LIST,
    var timeCreated: Long = System.currentTimeMillis(),
    var createdBy: String = "bbb",
    var updatedBy: String = "null",
    var firebaseKey: String = "null"
)




