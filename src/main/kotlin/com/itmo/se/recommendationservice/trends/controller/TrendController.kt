package com.itmo.se.recommendationservice.trends.controller

import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.api.TrendEvents.*
import com.itmo.se.recommendationservice.trends.logic.Trend
import org.springframework.web.bind.annotation.*
import ru.quipy.core.EventSourcingService
import java.util.*


@RestController
@RequestMapping("/trending")
class TrendController(
    val trendEsService: EventSourcingService<UUID, TrendAggregate, Trend>
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

    @GetMapping("/items")
    fun getTrendingItemsByCategory(categoryId: UUID): Trend? {
        return trendEsService.getState(categoryId)
    }
}
