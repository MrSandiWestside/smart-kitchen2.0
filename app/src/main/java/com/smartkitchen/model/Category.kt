package com.smartkitchen.model

enum class Category(val displayName: String) {
    VEGETABLES("Vegetables"),
    FRUITS("Fruits"),
    DAIRY("Dairy"),
    MEAT("Meat"),
    GRAINS("Grains"),
    SPICES("Spices");

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): Category {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
                ?: VEGETABLES
        }
    }
}
