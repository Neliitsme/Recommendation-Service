package com.itmo.se.recommendationservice.trends.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "trends")
class TrendAggregate : Aggregate