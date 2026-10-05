package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ServiceCategory(val displayName: String, val icon: String) {
    HAIR("Hair & Styling", "ContentCut"),
    NAILS("Nails & Art", "Palette"),
    MAKEUP("Makeup & Bridal", "Face"),
    SKIN("Skin & Facials", "Spa"),
    GROOMING("Men & Beard", "SelfImprovement"),
    PACKAGES("Combos & Deals", "LocalOffer")
}

data class SalonService(
    val id: String,
    val name: String,
    val category: ServiceCategory,
    val price: Int,
    val originalPrice: Int? = null,
    val durationMinutes: Int,
    val description: String,
    val targetGender: String = "Unisex", // "Unisex", "Women", "Men"
    val isPopular: Boolean = false,
    val tags: List<String> = emptyList()
)

data class Specialist(
    val id: String,
    val name: String,
    val role: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewCount: Int,
    val specialty: String,
    val availableDays: String = "All Days (Except Tuesday)"
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val customerPhone: String,
    val serviceId: String,
    val serviceName: String,
    val serviceCategory: String,
    val specialistId: String,
    val specialistName: String,
    val appointmentDate: String, // e.g. "Mon, Oct 12"
    val appointmentTime: String, // e.g. "02:30 PM"
    val price: Int,
    val status: String = "CONFIRMED", // "CONFIRMED", "COMPLETED", "CANCELLED"
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorName: String,
    val rating: Int,
    val timeAgo: String,
    val comment: String,
    val serviceUsed: String = "General Salon Service",
    val source: String = "Google Review",
    val isVerified: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class AcademyCourse(
    val id: String,
    val title: String,
    val duration: String,
    val fee: String,
    val level: String,
    val certificate: String,
    val highlight: String,
    val syllabus: List<String>,
    val batches: String
)

data class SpecialOffer(
    val id: String,
    val title: String,
    val tag: String,
    val priceText: String,
    val validUntil: String,
    val description: String,
    val code: String
)

data class StyleLook(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val stylistNote: String,
    val recommendedServices: List<String>
)
