package com.itmo.se.recommendationservice.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "recommendations")
class RecommendationEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    var userId: UUID? = null

    @OneToMany(mappedBy = "recommendation")
    var seenItems: MutableList<SeenItemEntity>? = null

    @OneToMany(mappedBy = "recommendation")
    var seenCategories: MutableList<SeenCategoryEntity>? = null

}