package com.itmo.se.recommendationservice.recommendations.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val RECOMMENDATION_CREATED = "RECOMMENDATION_CREATED_EVENT"
const val USER_SEEN_ITEM_CREATED = "USER_SEEN_ITEM_CREATED_EVENT"
const val USER_SEEN_CATEGORY_CREATED = "USER_SEEN_CATEGORY_CREATED_EVENT"
const val USER_SEEN_ITEM_COEFFICIENT_INCREASED = "USER_SEEN_ITEM_COEFFICIENT_INCREASED_EVENT"
const val USER_SEEN_ITEM_COEFFICIENT_DECREASED = "USER_SEEN_ITEM_COEFFICIENT_DECREASED_EVENT"


@DomainEvent(name = RECOMMENDATION_CREATED)
data class RecommendationCreatedEvent(
    val recommendationId: UUID,
    val userId: UUID,
) : Event<RecommendationAggregate>(name = RECOMMENDATION_CREATED)

@DomainEvent(name = USER_SEEN_ITEM_CREATED)
data class UserSeenItemCreatedEvent(
    val userId: UUID,
    val seenItemId: UUID,
    val itemId: UUID
) : Event<RecommendationAggregate>(name = USER_SEEN_ITEM_CREATED)


@DomainEvent(name = USER_SEEN_CATEGORY_CREATED)
data class UserSeenCategoryCreatedEvent(
    val userId: UUID,
    val seenCategoryId: UUID,
    val categoryId: UUID
) : Event<RecommendationAggregate>(name = USER_SEEN_CATEGORY_CREATED)


@DomainEvent(name = USER_SEEN_ITEM_COEFFICIENT_INCREASED)
data class UserSeenItemCoefficientIncreaseEvent(
    val seenItemId: UUID,
    val coefficientDelta: Int
) :
    Event<RecommendationAggregate>(name = USER_SEEN_ITEM_COEFFICIENT_INCREASED)


@DomainEvent(name = USER_SEEN_ITEM_COEFFICIENT_DECREASED)
data class UserSeenItemCoefficientDecreaseEvent(
    val seenItemId: UUID,
    val coefficientDelta: Int
) :
    Event<RecommendationAggregate>(name = USER_SEEN_ITEM_COEFFICIENT_DECREASED)
