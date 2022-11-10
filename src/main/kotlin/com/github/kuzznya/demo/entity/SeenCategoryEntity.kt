package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "seen_categories")
class SeenCategoryEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @ManyToOne
    @JoinColumn(unique = true)
    var recommendation: RecommendationEntity? = null

    @Column(unique = true)
    var categoryId: UUID? = null

    var recommendationCoef: Int = 0

    var wasBought: Boolean = false
}