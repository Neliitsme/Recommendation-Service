package com.itmo.se.recommendationservice.recommendations.logic

import com.itmo.se.recommendationservice.orders.logic.Order
import com.itmo.se.recommendationservice.recommendations.api.*
import com.itmo.se.recommendationservice.trends.logic.Trend
import ru.quipy.domain.AggregateState
import java.util.UUID

class Recommendation : AggregateState<UUID, RecommendationAggregate> {
    private lateinit var recommendationId: UUID
    private lateinit var userId: UUID

    var seenItems: MutableMap<UUID, SeenItem> = mutableMapOf()
    var seenCategories: MutableMap<UUID, SeenCategory> = mutableMapOf()

    override fun getId(): UUID = recommendationId

    fun createRecommendation(id: UUID = UUID.randomUUID(), userId: UUID): RecommendationCreatedEvent {
        this.recommendationId = id
        this.userId = userId
        return RecommendationCreatedEvent(recommendationId = id, userId = userId)
    }

    fun createSeenItem(seenItemId: UUID = UUID.randomUUID(), itemId: UUID): UserSeenItemCreatedEvent {
        val seenItem = SeenItem(
            id = seenItemId,
            itemId = itemId,
            recommendationCoefficient = 50,
            wasBought = false)
        seenItems[seenItemId] = seenItem
        createSeenCategory(itemID = itemId)
        return UserSeenItemCreatedEvent(userId = userId, itemId = itemId, seenItemId = seenItemId)
    }

    private fun createSeenCategory(seenCategoryId: UUID = UUID.randomUUID(), itemID: UUID): UserSeenCategoryCreatedEvent {
        val categoryId = Order().getCategoryIdByItemId(itemId = itemID)
        val seenCategory = SeenCategory(
            id = seenCategoryId,
            categoryId = categoryId,
            recommendationCoefficient = 50)
        seenCategories[seenCategoryId] = seenCategory
        return UserSeenCategoryCreatedEvent(userId = userId, seenCategoryId = seenCategoryId, categoryId = categoryId)
    }

    fun UserSeenItemCoefficientIncreaseEvent(itemId: UUID, seenItemId: UUID, coefficient_delta: Int):
            UserSeenItemCoefficientIncreaseEvent {
        if (!seenItems.containsKey(seenItemId)) {
            createSeenItem(itemId = itemId, seenItemId = seenItemId)
        }
        val item = seenItems[seenItemId]!!
        if (item.recommendationCoefficient + coefficient_delta <= 100){
            item.recommendationCoefficient += coefficient_delta
        }
        seenItems[seenItemId] = item
        return UserSeenItemCoefficientIncreaseEvent(seenItemId = seenItemId,
                                                    newCoefficient = item.recommendationCoefficient)
    }

    fun UserSeenItemCoefficientDecreaseEvent(itemId: UUID, seenItemId: UUID, coefficient_delta: Int):
            UserSeenItemCoefficientDecreaseEvent {
        if (!seenItems.containsKey(itemId)) {
            createSeenItem(itemId = itemId, seenItemId = seenItemId)
        }
        val item = seenItems[itemId]!!
        if (item.recommendationCoefficient - coefficient_delta >= 0){
            item.recommendationCoefficient -= coefficient_delta
        }
        seenItems[itemId] = item
        return UserSeenItemCoefficientDecreaseEvent(seenItemId = itemId,
                                                    newCoefficient = item.recommendationCoefficient)
    }

    fun getTrendingItems(){
        return Trend().getAllTrendingItems()
    }

    fun getRecommendationForItem(itemId: UUID): List<SeenItem> {
        val order = Order()
        val categoryId = order.getCategoryIdByItemId(itemId)
        return seenItems.filter { order.getCategoryIdByItemId(it.value.itemId) == categoryId }
            .values
            .sortedByDescending { it.recommendationCoefficient }
    }

}


data class SeenItem(
    val id: UUID,
    internal val itemId: UUID,
    internal var recommendationCoefficient: Int,
    internal val wasBought: Boolean,
) {}

data class SeenCategory(
    val id: UUID,
    internal val categoryId: UUID,
    internal val recommendationCoefficient: Int,
) {}