package com.itmo.se.recommendationservice.recommendations.controller

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.api.RecommendationCreatedEvent
import com.itmo.se.recommendationservice.recommendations.api.UserSeenItemCreatedEvent
import com.itmo.se.recommendationservice.recommendations.logic.Recommendation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.core.EventSourcingService
import java.util.*

@RestController
@RequestMapping("/recommendations")
class RecommendationController(
    val recommendationEsService: EventSourcingService<UUID, RecommendationAggregate, Recommendation>
) {
    @PostMapping("/personal/{ownerId}")
    fun createPersonalRecommendation(@PathVariable ownerId: UUID): RecommendationCreatedEvent {
        return recommendationEsService.create { it.createNewRecommendation(userId = ownerId) }
    }

    @PostMapping("/personal/{ownerId}/items/{itemId}")
    fun createSeenItem(@PathVariable ownerId: UUID, @PathVariable itemId: UUID): UserSeenItemCreatedEvent {
        return recommendationEsService.create { it.createNewSeenItem(owner_id = ownerId, itemId = itemId) }
    }

//    TODO: Probably implement
//    @GetMapping("/personal/{ownerId}/items/{itemId}")
//    fun getPersonalRecommendation(@PathVariable ownerId: UUID, @PathVariable itemId: UUID) {
//        return;
//    }


    @PutMapping("/personal/{ownerId}/items/{itemId}")
    fun changeItemRecommendationCoefficient(
        @PathVariable itemId: UUID,
        @PathVariable ownerId: UUID,
        @RequestBody coefficient_delta: Int
    ) {
        return;
    }

    @GetMapping("/trending/items")
    fun getTrendingItems() {
        return;
    }

    @GetMapping("/trending/items/{itemId}")
    fun getRecommendationForItem(@PathVariable itemId: String) {
        return;
    }
}