package com.itmo.se.recommendationservice.recommendations.logic

import com.itmo.se.recommendationservice.orders.logic.Order
import com.itmo.se.recommendationservice.recommendations.api.*
import com.itmo.se.recommendationservice.trends.logic.Trend
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.UUID

class Recommendation : AggregateState<UUID, RecommendationAggregate> {
    private lateinit var recommendationId: UUID
    private lateinit var userId: UUID

    var seenItems: MutableMap<UUID, SeenItem> = mutableMapOf()
    var seenCategories: MutableMap<UUID, SeenCategory> = mutableMapOf()

    override fun getId(): UUID = recommendationId

    fun createNewRecommendation(id: UUID = UUID.randomUUID(), userId: UUID): RecommendationCreatedEvent {
        return RecommendationCreatedEvent(recommendationId = id, userId = userId)
    }

    fun createNewSeenItem(seenItemId: UUID = UUID.randomUUID(), itemId: UUID): UserSeenItemCreatedEvent {
        return UserSeenItemCreatedEvent(userId = userId, itemId = itemId, seenItemId = seenItemId)
    }

    private fun createNewSeenCategory(seenCategoryId: UUID = UUID.randomUUID(), itemID: UUID): UserSeenCategoryCreatedEvent {
        val categoryId = Order().getCategoryIdByItemId(itemId = itemID)
        return UserSeenCategoryCreatedEvent(userId = userId, seenCategoryId = seenCategoryId, categoryId = categoryId)
    }

    fun seenItemCoefficientIncreaseEvent(itemId: UUID, seenItemId: UUID, coefficient_delta: Int):
            UserSeenItemCoefficientIncreaseEvent {
        val seenItem = seenItems[seenItemId]
            ?: throw IllegalArgumentException("No such item to refer to: $seenItemId")
        if (seenItem.recommendationCoefficient + coefficient_delta <= 100){
            return UserSeenItemCoefficientIncreaseEvent(seenItemId = seenItemId,
                coefficientDelta = coefficient_delta)
        }
        return UserSeenItemCoefficientIncreaseEvent(seenItemId = seenItemId,
            coefficientDelta = 0)
    }

    fun seenItemCoefficientDecreaseEvent(itemId: UUID, seenItemId: UUID, coefficient_delta: Int):
            UserSeenItemCoefficientDecreaseEvent {
        val seenItem = seenItems[seenItemId]
            ?: throw IllegalArgumentException("No such item to refer to: $seenItemId")
        if (seenItem.recommendationCoefficient - coefficient_delta >= 0){
            return UserSeenItemCoefficientDecreaseEvent(seenItemId = seenItemId,
                coefficientDelta = coefficient_delta)
        }
        return UserSeenItemCoefficientDecreaseEvent(seenItemId = seenItemId,
            coefficientDelta = 0)
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

    @StateTransitionFunc
    fun createNewRecommendation(event: RecommendationCreatedEvent) {
        recommendationId = event.recommendationId
        userId = event.userId
    }

    @StateTransitionFunc
    fun createNewSeenItem(event: UserSeenItemCreatedEvent){
        val seenItem = SeenItem(
            id = event.seenItemId,
            itemId = event.itemId,
            recommendationCoefficient = 50,
            wasBought = false)
        seenItems[event.seenItemId] = seenItem
        createNewSeenCategory(itemID = event.itemId)
    }

    @StateTransitionFunc
    fun createNewSeenCategory(event: UserSeenCategoryCreatedEvent){
        val seenCategory = SeenCategory(
            id = event.seenCategoryId,
            categoryId = event.categoryId,
            recommendationCoefficient = 50)
        seenCategories[event.seenCategoryId] = seenCategory
    }

    @StateTransitionFunc
    fun UserSeenItemCoefficientIncreaseEvent(event: UserSeenItemCoefficientIncreaseEvent){
        val item = seenItems[event.seenItemId]!!
        item.increaseRecommendationCoefficient(coefficient_delta = event.coefficientDelta)
        // a draft to show that changes in the coefficients of items also affect
        // the change in the coefficient of the category
        seenCategories[Order().getCategoryIdByItemId(
            itemId = seenItems.values.filter { it.id == event.seenItemId }[0].itemId)]!!
            .increaseRecommendationCoefficient(coefficient_delta = event.coefficientDelta/10)
        seenItems[event.seenItemId] = item
    }

    @StateTransitionFunc
    fun UserSeenItemCoefficientDecreaseEvent(event: UserSeenItemCoefficientDecreaseEvent){
        val item = seenItems[event.seenItemId]!!
        item.decreaseRecommendationCoefficient(coefficient_delta = event.coefficientDelta)
        // a draft to show that changes in the coefficients of items also affect
        // the change in the coefficient of the category
        seenCategories[Order().getCategoryIdByItemId(
            itemId = seenItems.values.filter { it.id == event.seenItemId }[0].itemId)]!!
            .decreaseRecommendationCoefficient(coefficient_delta = event.coefficientDelta/10)
        seenItems[event.seenItemId] = item
    }

}


data class SeenItem(
    val id: UUID,
    internal val itemId: UUID,
    internal var recommendationCoefficient: Int,
    internal var wasBought: Boolean,
) {
    fun increaseRecommendationCoefficient(coefficient_delta: Int){
        this.recommendationCoefficient += coefficient_delta
    }

    fun decreaseRecommendationCoefficient(coefficient_delta: Int){
        this.recommendationCoefficient -= coefficient_delta
    }
}

data class SeenCategory(
    val id: UUID,
    internal val categoryId: UUID,
    internal var recommendationCoefficient: Int,
) {

    fun increaseRecommendationCoefficient(coefficient_delta: Int){
        this.recommendationCoefficient += coefficient_delta
    }

    fun decreaseRecommendationCoefficient(coefficient_delta: Int){
        this.recommendationCoefficient -= coefficient_delta
    }
}
