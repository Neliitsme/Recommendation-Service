package com.itmo.se.recommendationservice.recommendations.logic

import com.itmo.se.recommendationservice.recommendations.api.RecommendationAggregate
import ru.quipy.domain.AggregateState
import java.util.UUID

class Recommendation : AggregateState<UUID, RecommendationAggregate> {
    private lateinit var recommendationId: UUID
    private lateinit var ownerId: UUID

    var seenItems: MutableMap<UUID, SeenItem> = mutableMapOf()
    var seenCategories: MutableMap<UUID, SeenCategory> = mutableMapOf()

    override fun getId(): UUID = recommendationId
}

data class SeenItem(
    val id: UUID,
    internal val itemId: UUID,
    internal val recommendationCoefficient: Int,
    internal val wasBought: Boolean,
) {}

data class SeenCategory(
    val id: UUID,
    internal val categoryId: UUID,
    internal val recommendationCoefficient: Int,
) {}