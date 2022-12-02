package com.itmo.se.recommendationservice.trends.controller

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.logic.Recommendation
import com.itmo.se.recommendationservice.trends.api.TrendAggregate
import com.itmo.se.recommendationservice.trends.logic.Trend
import org.springframework.web.bind.annotation.*
import ru.quipy.core.EventSourcingService
import java.util.*


@RestController
@RequestMapping("/trending")
class TrendController( val trendEsService: EventSourcingService<UUID, TrendAggregate, Trend>
) {
    @PostMapping("/trending")
    fun createTrend() {
        return;
    }

    @GetMapping("/trending/items")
    fun getAllTrendingItems() {
        return;
    }

    @GetMapping("/trending/items")
    fun getTrendingItemsByCategory(categoryId: UUID) {
        return;
    }
}