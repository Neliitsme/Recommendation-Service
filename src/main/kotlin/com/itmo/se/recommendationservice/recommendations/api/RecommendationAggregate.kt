package com.itmo.se.recommendationservice.recommendations.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "recommendations")
class RecommendationAggregate : Aggregate