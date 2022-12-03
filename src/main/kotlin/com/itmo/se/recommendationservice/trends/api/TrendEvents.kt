package com.itmo.se.recommendationservice.trends.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TREND_CREATED = "TREND_CREATED"
const val TRENDING_ITEM_CREATED = "TRENDING_ITEM_CREATED"
const val TREND_SHIFT = "TREND_SHIFT"


class TrendEvents {

    @DomainEvent(name = TREND_CREATED)
    data class TrendCreatedEvent(
        val trendId: UUID
    ) : Event<TrendAggregate>(name = TREND_CREATED)

    @DomainEvent(name = TRENDING_ITEM_CREATED)
    data class TrendingItemCreatedEvent(
        val trendId: UUID,
        val trendingItemId: UUID
    ) : Event<TrendAggregate>(name = TRENDING_ITEM_CREATED)

    class TrendEvents {
        @DomainEvent(name = TREND_SHIFT)
        data class TrendShiftedEvent(
            val trendShiftedId: UUID
        ) : Event<TrendAggregate>(name = TREND_SHIFT)
    }
}