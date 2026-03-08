package grocery.shopping.data

data class ListInfo(
    var listName: String= UNNAMED_LIST,
    val timeCreated: Long = System.currentTimeMillis(),
    var creatorName: String= "bbb",
    var userIdLastEdited: String = "null",
    var firebaseKey: String = "null"
)




