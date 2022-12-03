package com.itmo.se.recommendationservice.recommendations.logic

import com.itmo.se.recommendationservice.orders.logic.Order
import com.itmo.se.recommendationservice.recommendations.api.*
import com.itmo.se.recommendationservice.trends.logic.Trend
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class Recommendation : AggregateState<UUID, RecommendationAggregate> {
    private lateinit var recommendationId: UUID
    private lateinit var userId: UUID

    var seenItems: MutableMap<UUID, SeenItem> = mutableMapOf()
    var seenCategories: MutableMap<UUID, SeenCategory> = mutableMapOf()

    override fun getId(): UUID = recommendationId

    fun createNewRecommendation(id: UUID = UUID.randomUUID(), userId: UUID): RecommendationCreatedEvent {
        return RecommendationCreatedEvent(recommendationId = id, userId = userId)
    }

    fun createNewSeenItem(seenItemId: UUID = UUID.randomUUID(), ownerId: UUID, itemId: UUID):
            UserSeenItemCreatedEvent {
        return UserSeenItemCreatedEvent(userId = ownerId, itemId = itemId, seenItemId = seenItemId)
    }

    private fun createNewSeenCategory(seenCategoryId: UUID = UUID.randomUUID(), ownerId: UUID, itemID: UUID):
            UserSeenCategoryCreatedEvent {
        val categoryId = Order().getCategoryIdByItemId(itemId = itemID)
        return UserSeenCategoryCreatedEvent(userId = ownerId, seenCategoryId = seenCategoryId, categoryId = categoryId)
    }

    fun seenItemCoefficientIncreaseEvent(itemId: UUID, seenItemId: UUID, coefficientDelta: Int):
            UserSeenItemCoefficientIncreaseEvent {
        val seenItem = seenItems[seenItemId]
            ?: throw IllegalArgumentException("No such item to refer to: $seenItemId")
        if (seenItem.recommendationCoefficient + coefficientDelta <= 100) {
            return UserSeenItemCoefficientIncreaseEvent(
                seenItemId = seenItemId,
                coefficientDelta = coefficientDelta
            )
        }
        return UserSeenItemCoefficientIncreaseEvent(
            seenItemId = seenItemId,
            coefficientDelta = 0
        )
    }

    fun seenItemCoefficientDecreaseEvent(itemId: UUID, seenItemId: UUID, coefficientDelta: Int):
            UserSeenItemCoefficientDecreaseEvent {
        val seenItem = seenItems[seenItemId]
            ?: throw IllegalArgumentException("No such item to refer to: $seenItemId")
        if (seenItem.recommendationCoefficient - coefficientDelta >= 0) {
            return UserSeenItemCoefficientDecreaseEvent(
                seenItemId = seenItemId,
                coefficientDelta = coefficientDelta
            )
        }
        return UserSeenItemCoefficientDecreaseEvent(
            seenItemId = seenItemId,
            coefficientDelta = 0
        )
    }

    fun getTrendingItems() {
        return Trend().getAllTrendingItems()
    }

    fun getRecommendationForItem(itemId: UUID): List<SeenItem> {
        val order = Order()
        val categoryId = order.getCategoryIdByItemId(itemId)
        return seenItems.filter { order.getCategoryIdByItemId(it.value.itemId) == categoryId }
            .values
            .sortedByDescending { it.recommendationCoefficient }
    }

    @StateTransitionFunc
    fun createNewRecommendation(event: RecommendationCreatedEvent) {
        recommendationId = event.recommendationId
        userId = event.userId
    }

    @StateTransitionFunc
    fun createNewSeenItem(event: UserSeenItemCreatedEvent) {
        val seenItem = SeenItem(
            id = event.seenItemId,
            itemId = event.itemId,
            recommendationCoefficient = 50,
            wasBought = false
        )
        seenItems[event.seenItemId] = seenItem
        createNewSeenCategory(ownerId = event.userId, itemID = event.itemId)
    }

    @StateTransitionFunc
    fun createNewSeenCategory(event: UserSeenCategoryCreatedEvent) {
        val seenCategory = SeenCategory(
            id = event.seenCategoryId,
            categoryId = event.categoryId,
            recommendationCoefficient = 50
        )
        seenCategories[event.seenCategoryId] = seenCategory
    }

    @StateTransitionFunc
    fun userSeenItemCoefficientIncreaseEvent(event: UserSeenItemCoefficientIncreaseEvent) {
        val item = seenItems[event.seenItemId]!!
        item.increaseRecommendationCoefficient(coefficientDelta = event.coefficientDelta)
        // a draft to show that changes in the coefficients of items also affect
        // the change in the coefficient of the category
        seenCategories[Order().getCategoryIdByItemId(
            itemId = seenItems.values.filter { it.id == event.seenItemId }[0].itemId
        )]!!
            .increaseRecommendationCoefficient(coefficientDelta = event.coefficientDelta / 10)
        seenItems[event.seenItemId] = item
    }

    @StateTransitionFunc
    fun userSeenItemCoefficientDecreaseEvent(event: UserSeenItemCoefficientDecreaseEvent) {
        val item = seenItems[event.seenItemId]!!
        item.decreaseRecommendationCoefficient(coefficientDelta = event.coefficientDelta)
        // a draft to show that changes in the coefficients of items also affect
        // the change in the coefficient of the category
        seenCategories[Order().getCategoryIdByItemId(
            itemId = seenItems.values.filter { it.id == event.seenItemId }[0].itemId
        )]!!
            .decreaseRecommendationCoefficient(coefficientDelta = event.coefficientDelta / 10)
        seenItems[event.seenItemId] = item
    }

}


data class SeenItem(
    val id: UUID,
    internal val itemId: UUID,
    internal var recommendationCoefficient: Int,
    internal var wasBought: Boolean,
) {
    fun increaseRecommendationCoefficient(coefficientDelta: Int) {
        this.recommendationCoefficient += coefficientDelta
    }

    fun decreaseRecommendationCoefficient(coefficientDelta: Int) {
        this.recommendationCoefficient -= coefficientDelta
    }
}

data class SeenCategory(
    val id: UUID,
    internal val categoryId: UUID,
    internal var recommendationCoefficient: Int,
) {

    fun increaseRecommendationCoefficient(coefficientDelta: Int) {
        this.recommendationCoefficient += coefficientDelta
    }

    fun decreaseRecommendationCoefficient(coefficientDelta: Int) {
        this.recommendationCoefficient -= coefficientDelta
    }
}
