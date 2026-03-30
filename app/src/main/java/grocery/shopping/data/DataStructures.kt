package grocery.shopping.data

data class ListInfo(
    var listName: String = UNNAMED_LIST,
    var timeCreated: Long = System.currentTimeMillis(),
    var createdBy: String = DEFAULT_USER_NAME,
    var updatedBy: String = DEFAULT_USER_NAME,
    var firebaseKey: String = "null"
)




