package com.itmo.se.recommendationservice.orders.logic

import com.itmo.se.recommendationservice.recommendations.logic.SeenItem
import java.util.*

class Order {

    var items: MutableMap<UUID, Item> = mutableMapOf()

    fun getAllItems(): MutableMap<UUID, Item> {
        return items
    }

    fun getCategoryIdByItemId(itemId: UUID): UUID{
        if (items.containsKey(itemId)) {
            return items[itemId]!!.categoryId
        } else {
            throw IllegalArgumentException("No such item : $itemId")
        }
    }

    fun getItemsByCategory(categoryId: UUID): List<Item> {
        return items.values.filter { it.categoryId == categoryId }
    }
}

data class Item(
    val id: UUID,
    internal val itemId: UUID,
    internal val categoryId: UUID
)