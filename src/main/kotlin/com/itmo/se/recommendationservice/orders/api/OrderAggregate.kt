package com.itmo.se.recommendationservice.orders.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

// TODO ???
@AggregateType(aggregateEventsTableName = "orders")
class OrderAggregate : Aggregate