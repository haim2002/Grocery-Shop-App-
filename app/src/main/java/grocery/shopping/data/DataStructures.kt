package grocery.shopping.data

data class CompleteShoppingList(
        private var _listName: String,
        private var _items: MutableList<GroceryItems> = mutableListOf(),
        private val _timeCreated: Long = System.currentTimeMillis(),
        private var _userIdLastEdited: String? = null
    ) {
        // Getters and Setters

        var listName: String
            get() = _listName
            set(value) {
                _listName = value.ifBlank {
                    DEFAULT_LIST_NAME
                }
            }

        var items: MutableList<GroceryItems>
            get() = _items
            set(value) { _items = value }

        val dateCreated: Long
            get() = _timeCreated

        var userIdLastEdited: String?
            get() = _userIdLastEdited
            set(value) { _userIdLastEdited = value }
    }
