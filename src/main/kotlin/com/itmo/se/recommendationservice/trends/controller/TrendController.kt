package com.itmo.se.recommendationservice.trends.controller

import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.api.TrendEvents.TrendCreatedEvent
import com.itmo.se.recommendationservice.trends.api.TrendEvents.TrendingItemCreatedEvent
import com.itmo.se.recommendationservice.trends.logic.Trend
import com.itmo.se.recommendationservice.trends.projections.ItemOrderCounts
import com.itmo.se.recommendationservice.trends.service.TrendService
import org.springframework.web.bind.annotation.*
import ru.quipy.core.EventSourcingService
import java.util.*


@RestController
@RequestMapping("/trending")
class TrendController(
    val trendEsService: EventSourcingService<UUID, TrendAggregate, Trend>,
    val trendService: TrendService
) {
    @PostMapping("")
    fun createTrend(trendId: UUID = UUID.randomUUID()): TrendCreatedEvent {
        return trendEsService.create { it.createNewTrend(id = trendId) }
    }

    @PostMapping("/items/{trendingItemId}")
    fun createTrendingItem(@PathVariable trendingItemId: UUID): TrendingItemCreatedEvent {
        return trendEsService.create { it.createNewTrendingItem(trendingItemId = trendingItemId) }
    }

    @GetMapping("/items")
    fun getAllTrendingItems() {
//        trendEsService.getState()
    }

    @GetMapping("/items/{itemId}")
    fun getTrendingItemsByCategory(
        @PathVariable itemId: UUID
    ): Collection<ItemOrderCounts> {
        return trendService.getTrendingItemsByItemId(itemId)
    }
}
