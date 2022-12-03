package com.itmo.se.recommendationservice.trends.logic

import com.itmo.se.recommendationservice.orders.logic.Order
import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.api.TrendEvents.TrendCreatedEvent
import com.itmo.se.recommendationservice.trends.api.TrendEvents.TrendingItemCreatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class Trend : AggregateState<UUID, TrendAggregate> {
    private lateinit var trendId: UUID

    var trendingItems: MutableList<TrendingItem> = arrayListOf()

    override fun getId(): UUID = trendId

    fun createNewTrend(id: UUID = UUID.randomUUID()): TrendCreatedEvent {
        return TrendCreatedEvent(trendId = id)
    }

    fun createNewTrendingItem(trendingItemId: UUID = UUID.randomUUID()): TrendingItemCreatedEvent {
        return TrendingItemCreatedEvent(trendId = trendId, trendingItemId = trendingItemId)
    }

    fun getAllTrendingItems() {
        return trendingItems.sortByDescending { it.orderedTimes }
    }

    fun getTrendingItemsByCategory(categoryId: UUID): List<TrendingItem> {
        return trendingItems.filter { Order().getCategoryIdByItemId(it.trendingItemId) == categoryId }
    }

    @StateTransitionFunc
    fun createNewTrend(event: TrendCreatedEvent) {
        this.trendId = event.trendId
    }

    @StateTransitionFunc
    fun createNewTrendingItem(event: TrendingItemCreatedEvent) {
        val trendingItem = TrendingItem(
            id = event.id,
            trendingItemId = event.trendingItemId,
            orderedTimes = 0
        )
        trendingItems.add(trendingItem)
    }


}

data class TrendingItem(
    val id: UUID,
    internal val trendingItemId: UUID,
    internal val orderedTimes: Int
)