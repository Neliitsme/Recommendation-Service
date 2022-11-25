package com.itmo.se.recommendationservice.recommendations.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val RECOMMENDATION_CREATED = "RECOMMENDATION_CREATED_EVENT"

const val USER_SEEN_ITEM_CREATED = "USER_SEEN_ITEM_CREATED_EVENT"
const val USER_SEEN_ITEM_COEFFICIENT_CHANGED = "USER_SEEN_ITEM_COEFFICIENT_CHANGED_EVENT"


@DomainEvent(name = RECOMMENDATION_CREATED)
data class RecommendationCreatedEvent(
    val recommendationId: UUID,
    val ownerId: UUID,
) : Event<RecommendationAggregate>(name = RECOMMENDATION_CREATED)

@DomainEvent(name = USER_SEEN_ITEM_CREATED)
data class UserSeenItemCreatedEvent(
    val seenItemId: UUID,
    val itemId: UUID,
    val coefficient: Int
) : Event<RecommendationAggregate>(name = USER_SEEN_ITEM_CREATED)

@DomainEvent(name = USER_SEEN_ITEM_COEFFICIENT_CHANGED)
data class UserSeenItemCoefficientChangedEvent(
    val seenItemId: UUID,
    val itemId: UUID,
    val newCoefficient: Int
) :
    Event<RecommendationAggregate>(name = USER_SEEN_ITEM_COEFFICIENT_CHANGED)
