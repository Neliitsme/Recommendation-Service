package com.itmo.se.recommendationservice.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "seen_items")
class SeenItemEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @ManyToOne
    @JoinColumn(name="recommendation", unique = true)
    var recommendation: RecommendationEntity? = null

    @Column(name = "itemId", unique = true)
    var itemId: UUID? = null

    var recommendationCoef: Int = 0

    var wasBought: Boolean = false
}