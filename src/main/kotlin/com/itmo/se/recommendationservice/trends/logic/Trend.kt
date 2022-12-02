package com.itmo.se.recommendationservice.trends.logic

import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.api.TrendEvents.*
import ru.quipy.domain.AggregateState
import java.util.*

class Trend : AggregateState<UUID, TrendAggregate> {
    private lateinit var trendId: UUID

    var trendingItems: MutableList<TrendingItem> = arrayListOf()

    override fun getId(): UUID = trendId

    fun createTrend(id: UUID = UUID.randomUUID()): TrendCreatedEvent {
        this.trendId = id
        return TrendCreatedEvent(trendId = id)
    }

    fun createTrendingItem(trendingItemId: UUID = UUID.randomUUID()): TrendingItemCreatedEvent {
        return TrendingItemCreatedEvent(trendId = trendId, trendingItemId = trendingItemId)
    }

    fun getAllTrendingItems(){
        return trendingItems.sortByDescending { it.orderedTimes }
    }

    fun getTrendingItemsByCategory(categoryId: UUID){
        // TODO: discuss whether category field is needed in SeenItem
    }

}

data class TrendingItem(
    val id: UUID,
    internal val itemId: UUID,
    internal val orderedTimes: Int
)