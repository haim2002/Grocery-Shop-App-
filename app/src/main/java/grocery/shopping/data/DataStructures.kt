package grocery.shopping.data

data class ListInfo(
    private var _listName: String?,
   // private var _items: MutableList<GroceryItems>,
    private val _timeCreated: Long = System.currentTimeMillis(),
    private val _creatorName: String,
    private var _userIdLastEdited: String? = null,
    private val _firebaseKey: String? = null
) {
    // Getters and Setters

    var listName: String?
        get() = _listName
        set(value) {
            _listName = value?.ifBlank {
                DEFAULT_LIST_NAME
            }
        }
/*
    var items: MutableList<GroceryItems>
        get() = _items
        set(value) {
            _items = value
        }
*/
    val dateCreatedTime: Long
        get() = _timeCreated

    var firebaseKey: String? = null
        get() = _firebaseKey

    val creatorUserName: String
        get() = _creatorName

    var userIdLastEdited: String?
        get() = _userIdLastEdited
        set(value) {
            _userIdLastEdited = value
        }
}
