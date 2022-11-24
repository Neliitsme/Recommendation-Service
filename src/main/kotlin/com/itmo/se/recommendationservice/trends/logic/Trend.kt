package com.itmo.se.recommendationservice.trends.logic

import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import ru.quipy.domain.AggregateState
import java.util.*

class Trend : AggregateState<UUID, TrendAggregate> {
    private lateinit var trendId: UUID

    var trendingItems: MutableMap<UUID, TrendingItem> = mutableMapOf()

    override fun getId(): UUID = trendId
}

data class TrendingItem(
    val id: UUID,
    internal val itemId: UUID,
    internal val orderedTimes: Int
)