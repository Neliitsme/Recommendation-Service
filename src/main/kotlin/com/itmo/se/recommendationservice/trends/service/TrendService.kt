package com.itmo.se.recommendationservice.trends.service

import com.itmo.se.recommendationservice.trends.projections.ItemOrderCounts
import com.itmo.se.recommendationservice.trends.projections.ItemOrderCountsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TrendService(
    private val itemOrderCountsRepository: ItemOrderCountsRepository
) {
    fun getTrendingItemsByItemId(itemId: UUID): Collection<ItemOrderCounts> {
        return itemOrderCountsRepository.findAllByItemId(itemId)
    }

    fun getAllTrendingItems(): Collection<ItemOrderCounts> {
        return itemOrderCountsRepository.findAll()
    }
}