package com.itmo.se.recommendationservice.recommendations.controller

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import com.itmo.se.recommendationservice.recommendations.api.RecommendationCreatedEvent
import com.itmo.se.recommendationservice.recommendations.api.UserSeenItemCreatedEvent
import com.itmo.se.recommendationservice.recommendations.logic.Recommendation
import org.springframework.web.bind.annotation.*
import ru.quipy.core.EventSourcingService
import java.util.*

@RestController
@RequestMapping("/recommendations")
class RecommendationController(
    val recommendationEsService: EventSourcingService<UUID, RecommendationAggregate, Recommendation>,
) {
    // TODO: Probably remove later, due to existence of `UsersSubscriber`
    // It's fine to have it for now for testing
    @PostMapping("/personal/{ownerId}")
    fun createPersonalRecommendation(@PathVariable ownerId: UUID): RecommendationCreatedEvent {
        return recommendationEsService.create { it.createNewRecommendation(userId = ownerId) }
    }

    @PostMapping("/personal/{ownerId}/items/{itemId}")
    fun createSeenItem(@PathVariable ownerId: UUID, @PathVariable itemId: UUID): UserSeenItemCreatedEvent {
        return recommendationEsService.create { it.createNewSeenItem(ownerId = ownerId, itemId = itemId) }
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
        @RequestBody coefficientDelta: Int,
    ) {
        return
    }

    // TODO: Get from the view
    @GetMapping("/trending/items")
    fun getTrendingItems() {
        return
    }

    // TODO: Get from the view
    @GetMapping("/trending/items/{itemId}")
    fun getRecommendationForItem(@PathVariable itemId: String) {
        return
    }
}