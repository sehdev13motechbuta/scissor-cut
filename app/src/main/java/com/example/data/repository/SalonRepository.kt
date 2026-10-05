package com.example.data.repository

import com.example.data.local.AppointmentDao
import com.example.data.local.ReviewDao
import com.example.data.model.AcademyCourse
import com.example.data.model.Appointment
import com.example.data.model.ReviewItem
import com.example.data.model.SalonService
import com.example.data.model.ServiceCategory
import com.example.data.model.SpecialOffer
import com.example.data.model.Specialist
import com.example.data.model.StyleLook
import kotlinx.coroutines.flow.Flow

class SalonRepository(
    private val appointmentDao: AppointmentDao,
    private val reviewDao: ReviewDao
) {
    val allAppointments: Flow<List<Appointment>> = appointmentDao.getAllAppointments()
    val upcomingAppointments: Flow<List<Appointment>> = appointmentDao.getUpcomingAppointments()
    val allReviews: Flow<List<ReviewItem>> = reviewDao.getAllReviews()

    suspend fun bookAppointment(appointment: Appointment): Long {
        return appointmentDao.insertAppointment(appointment)
    }

    suspend fun cancelAppointment(id: Long) {
        appointmentDao.updateStatus(id, "CANCELLED")
    }

    suspend fun completeAppointment(id: Long) {
        appointmentDao.updateStatus(id, "COMPLETED")
    }

    suspend fun deleteAppointment(id: Long) {
        appointmentDao.deleteById(id)
    }

    suspend fun addReview(review: ReviewItem): Long {
        return reviewDao.insertReview(review)
    }

    fun getSalonServices(): List<SalonService> = listOf(
        // Makeup
        SalonService(
            id = "srv_makeup_party",
            name = "Party Glam Makeup",
            category = ServiceCategory.MAKEUP,
            price = 999,
            originalPrice = 1499,
            durationMinutes = 45,
            description = "Special inaugural offer! High-definition base with eye contouring, lash enhancement, and long-stay lips.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("Popular Deal", "₹999 Special", "Party Ready")
        ),
        SalonService(
            id = "srv_makeup_hd_bridal",
            name = "HD Luxury Bridal Makeup",
            category = ServiceCategory.MAKEUP,
            price = 7999,
            originalPrice = 9999,
            durationMinutes = 150,
            description = "Complete royal bridal package with HD airbrush finish, jewelry setting, dupatta draping, and luxury lenses.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("Bridal", "HD Airbrush", "Full Draping")
        ),
        SalonService(
            id = "srv_makeup_engagement",
            name = "Engagement & Sangeet Glow",
            category = ServiceCategory.MAKEUP,
            price = 3499,
            originalPrice = 4500,
            durationMinutes = 90,
            description = "Dewy radiant look designed for camera lights and evening festivities.",
            targetGender = "Women",
            tags = listOf("Pre-Wedding", "Dewy Glow")
        ),

        // Hair & Styling
        SalonService(
            id = "srv_hair_smooth",
            name = "Hair Smoothening / Rebonding",
            category = ServiceCategory.HAIR,
            price = 3999,
            originalPrice = 5500,
            durationMinutes = 180,
            description = "Deep nourishing Brazilian/Keratin smoothening for sleek, silky, frizz-free hair lasting up to 6 months.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("Bestseller", "Silk Finish", "Frizz Free")
        ),
        SalonService(
            id = "srv_hair_keratin",
            name = "Organic Keratin Hair Spa & Treatment",
            category = ServiceCategory.HAIR,
            price = 2499,
            originalPrice = 3200,
            durationMinutes = 120,
            description = "Intensive protein reconstruction treatment for chemically treated or damaged tresses.",
            targetGender = "Unisex",
            tags = listOf("Damage Repair", "Protein Boost")
        ),
        SalonService(
            id = "srv_haircut_styling_women",
            name = "Advanced Designer Haircut & Blowdry",
            category = ServiceCategory.HAIR,
            price = 599,
            originalPrice = 800,
            durationMinutes = 45,
            description = "Personalized face-contour cut, wash, conditioning, and thermal blow-dry finish.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("Face Shape", "Thermal Finish")
        ),
        SalonService(
            id = "srv_haircut_beard_men",
            name = "Men's Precision Cut & Beard Sculpting",
            category = ServiceCategory.HAIR,
            price = 399,
            originalPrice = 550,
            durationMinutes = 40,
            description = "Classic fade or taper cut paired with hot towel beard shaping, razor lining, and styling pomade.",
            targetGender = "Men",
            isPopular = true,
            tags = listOf("Hot Towel", "Razor Line", "Sharp Look")
        ),
        SalonService(
            id = "srv_hair_color_balayage",
            name = "Global Color & Caramel Balayage",
            category = ServiceCategory.HAIR,
            price = 3499,
            originalPrice = 4800,
            durationMinutes = 150,
            description = "Custom hand-painted French balayage or root touch-up with ammonia-free gloss formula.",
            targetGender = "Unisex",
            tags = listOf("Balayage", "Ammonia-Free")
        ),

        // Nails & Art
        SalonService(
            id = "srv_nail_ext",
            name = "Acrylic / Gel Nail Extensions",
            category = ServiceCategory.NAILS,
            price = 1499,
            originalPrice = 2200,
            durationMinutes = 75,
            description = "Full tip/form extensions with high-grade builder gel, custom shape, and glossy sealant.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("Trendy", "Long-Lasting", "Gel/Acrylic")
        ),
        SalonService(
            id = "srv_nail_art_custom",
            name = "Designer 3D Nail Art & Chrome Accents",
            category = ServiceCategory.NAILS,
            price = 799,
            originalPrice = 1200,
            durationMinutes = 45,
            description = "French ombre, Swarovski crystals, chrome glazed donut, or hand-painted artistic motifs.",
            targetGender = "Women",
            tags = listOf("Chrome", "Swarovski", "Ombre")
        ),
        SalonService(
            id = "srv_nail_mani_pedi",
            name = "Luxury Crystal Spa Manicure & Pedicure",
            category = ServiceCategory.NAILS,
            price = 1199,
            originalPrice = 1600,
            durationMinutes = 60,
            description = "Aromatic foot soak, sugar exfoliation, cuticles care, relaxing massage, and nail buffing.",
            targetGender = "Unisex",
            tags = listOf("Relaxing", "Exfoliation", "Spa")
        ),

        // Skin & Facials
        SalonService(
            id = "srv_skin_hydra",
            name = "Hydra Facial Deep Glow Therapy",
            category = ServiceCategory.SKIN,
            price = 1899,
            originalPrice = 2800,
            durationMinutes = 60,
            description = "Micro-infusion vortex suction that cleanses pores, hydrates deeply, and restores natural luminosity.",
            targetGender = "Unisex",
            isPopular = true,
            tags = listOf("Instant Glow", "Pore Detox")
        ),
        SalonService(
            id = "srv_skin_dtan_cleanse",
            name = "O3+ D-Tan & Brightening Cleanup",
            category = ServiceCategory.SKIN,
            price = 899,
            originalPrice = 1200,
            durationMinutes = 40,
            description = "Reverses sun damage and pollutants with natural mint and active oxygen serum.",
            targetGender = "Unisex",
            tags = listOf("Sun Protection", "Even Tone")
        ),

        // Men & Beard Grooming
        SalonService(
            id = "srv_grooming_royal",
            name = "Royal Grooming Package (Cut, Beard & D-Tan)",
            category = ServiceCategory.GROOMING,
            price = 899,
            originalPrice = 1400,
            durationMinutes = 60,
            description = "Complete package: Executive haircut, beard styling, head massage, and facial de-tan.",
            targetGender = "Men",
            isPopular = true,
            tags = listOf("Combo", "Executive", "Great Value")
        ),

        // Packages & Deals
        SalonService(
            id = "srv_pkg_headtotop",
            name = "Head-to-Toe Luxury Makeover",
            category = ServiceCategory.PACKAGES,
            price = 2999,
            originalPrice = 4500,
            durationMinutes = 120,
            description = "Includes Advanced Haircut + Hair Spa + Gold Cleanup + Gel Nail Polish + Threading/Waxing.",
            targetGender = "Women",
            isPopular = true,
            tags = listOf("All-In-One", "Best Seller", "Save 35%")
        )
    )

    fun getSpecialists(): List<Specialist> = listOf(
        Specialist(
            id = "spec_gurpreet",
            name = "Gurpreet Singh",
            role = "Master Hair Stylist & Director",
            experienceYears = 11,
            rating = 4.9,
            reviewCount = 184,
            specialty = "Hair Smoothening, Precision Fade & Balayage"
        ),
        Specialist(
            id = "spec_simran",
            name = "Simran Sharma",
            role = "Senior Bridal & HD Makeup Artist",
            experienceYears = 8,
            rating = 4.9,
            reviewCount = 142,
            specialty = "HD Bridal, Airbrush Makeup & Draping"
        ),
        Specialist(
            id = "spec_priya",
            name = "Priya Rajput",
            role = "Nail Art & Extension Specialist",
            experienceYears = 6,
            rating = 4.8,
            reviewCount = 98,
            specialty = "Acrylic Extensions, Russian Manicure & 3D Art"
        ),
        Specialist(
            id = "spec_aman",
            name = "Amanpreet Kaur",
            role = "Skin & Hydra Facial Expert",
            experienceYears = 7,
            rating = 4.8,
            reviewCount = 110,
            specialty = "Hydra Facial, Anti-Tan & Skin Rejuvenation"
        )
    )

    fun getAcademyCourses(): List<AcademyCourse> = listOf(
        AcademyCourse(
            id = "crs_cosmetology",
            title = "Diploma in Professional Cosmetology",
            duration = "6 Months (Full-Time)",
            fee = "₹45,000 (Installment Available)",
            level = "Beginner to Advanced",
            certificate = "Govt. Recognized & Salon Certified",
            highlight = "Comprehensive 360° training across Hair, Makeup, Nails & Skin with live client floor practice.",
            syllabus = listOf(
                "Hair Anatomy, Advanced Cutting Techniques & Global Coloring",
                "Chemical Treatments: Rebonding, Smoothening, Keratin & Botox",
                "Skin Consultation, Medical Facials & Machine Treatments",
                "HD & Airbrush Bridal Makeup Masterclasses",
                "Salon Management, Hygiene & Client Retention"
            ),
            batches = "New Batch starts 1st & 15th of Every Month"
        ),
        AcademyCourse(
            id = "crs_hair_master",
            title = "Master Hair Stylist & Barbering Program",
            duration = "3 Months",
            fee = "₹28,000",
            level = "Beginner to Professional",
            certificate = "Professional Hairdresser Certification",
            highlight = "Master scissor work, clipper fades, beard architecture, blow drying, and chemical treatments.",
            syllabus = listOf(
                "Scissor-over-comb and precision texturizing",
                "Men's Fades, Tapers and Razor lines",
                "Women's layered, bob, and curtain fringe cuts",
                "Colorimetry, highlights, balayage & ombre",
                "Hair Spa & Scalp Health diagnosis"
            ),
            batches = "Weekend & Weekday Batches available"
        ),
        AcademyCourse(
            id = "crs_nail_art",
            title = "Certified Nail Art & Extension Masterclass",
            duration = "1 Month (Intensive)",
            fee = "₹18,000",
            level = "All Levels",
            certificate = "Master Nail Technician Diploma",
            highlight = "Hands-on mastery of gel, acrylic, Russian manicure, chrome art, and 3D embellishments.",
            syllabus = listOf(
                "Nail Anatomy and sanitization standards",
                "Form and tip placement for Gel & Acrylic extensions",
                "E-file Russian manicure technique",
                "Freehand nail art, ombre, chrome and stone application",
                "Refills, repairs and safe removal techniques"
            ),
            batches = "Limited to 6 students per batch for 1-on-1 focus"
        ),
        AcademyCourse(
            id = "crs_bridal_makeup",
            title = "Bridal & HD Fashion Makeup Artistry",
            duration = "2 Months",
            fee = "₹32,000",
            level = "Intermediate to Pro",
            certificate = "Scissor Cut Master Makeup Artist",
            highlight = "Learn flawless color theory, high-definition camera makeup, and Punjabi bridal looks.",
            syllabus = listOf(
                "Skin prep, color correction & tone matching",
                "Airbrush technique & HD foundation formulation",
                "Smokey, cut crease & glitter eye arts",
                "Bridal hairstyling, dupatta draping & jewelry setting",
                "Building a high-converting Instagram portfolio"
            ),
            batches = "Flexible morning and evening slots"
        )
    )

    fun getSpecialOffers(): List<SpecialOffer> = listOf(
        SpecialOffer(
            id = "off_999_makeup",
            title = "Glam Makeup Special",
            tag = "HOT DEAL",
            priceText = "Just ₹999",
            validUntil = "Limited Time Offer",
            description = "Get professional party makeup done with premium cosmetics and styling for only ₹999.",
            code = "GLAM999"
        ),
        SpecialOffer(
            id = "off_hair_smoothening",
            title = "Silky Smoothening Fest",
            tag = "POPULAR",
            priceText = "Flat ₹3,999 Any Length",
            validUntil = "Valid This Month",
            description = "Transform unruly curls into glass-smooth hair. Zero hidden charges for hair length.",
            code = "SILK3999"
        ),
        SpecialOffer(
            id = "off_nail_combo",
            title = "Nail Extension + Gel Art",
            tag = "TRENDING",
            priceText = "Combo @ ₹1,799",
            validUntil = "Valid All Weekdays",
            description = "Full set extension with 2 accent custom art nails included.",
            code = "NAILS1799"
        )
    )

    fun getStyleLookbook(): List<StyleLook> = listOf(
        StyleLook(
            id = "look_1",
            title = "Golden Honey Balayage & Beach Waves",
            category = "Hair Color",
            description = "Subtle hand-painted gradient transitioning from rich mocha roots into radiant warm caramel reflections.",
            stylistNote = "Best suited for medium to long hair. Protect with color-safe sulfate free shampoo.",
            recommendedServices = listOf("Global Color & Caramel Balayage", "Organic Keratin Hair Spa")
        ),
        StyleLook(
            id = "look_2",
            title = "Royal Punjabi Bridal HD Glam",
            category = "Bridal Makeup",
            description = "Luminous glass skin base with royal gold cut-crease eyes, sculpted cheeks, and rich crimson lips.",
            stylistNote = "Includes customized false lashes, dupatta draping and tikka setting.",
            recommendedServices = listOf("HD Luxury Bridal Makeup", "Acrylic / Gel Nail Extensions")
        ),
        StyleLook(
            id = "look_3",
            title = "Rose Quartz & Chrome Glazed Nails",
            category = "Nail Art",
            description = "Almond-shaped extensions with soft blush jelly base and reflective pearl chrome powder glaze.",
            stylistNote = "Extremely durable, chip resistant up to 4 weeks.",
            recommendedServices = listOf("Acrylic / Gel Nail Extensions", "Designer 3D Nail Art & Chrome Accents")
        ),
        StyleLook(
            id = "look_4",
            title = "Modern Low Fade & Textured Crop",
            category = "Men's Styling",
            description = "Crisp skin taper on the sides with textured matte volume on top and sharp beard contouring.",
            stylistNote = "Maintained every 2-3 weeks. Finished with matte clay pomade.",
            recommendedServices = listOf("Men's Precision Cut & Beard Sculpting")
        )
    )

    fun getSalonInfo() = SalonInfo(
        name = "Scissor Cut Unisex Salon & Academy",
        rating = 4.8,
        totalReviews = 409,
        justdialRating = "5/5 (413 votes)",
        address = "First Floor, SCO 118, Airport Rd, Sector 117, Sahibzada Ajit Singh Nagar, Punjab 140301",
        landmark = "Sector 117 Airport Road near Mohali",
        phone = "080545 96910",
        formattedPhone = "+91 80545 96910",
        openStatus = "Open · Closes 9:00 PM",
        hours = "Monday – Sunday: 9:00 AM – 9:00 PM",
        busyness = "Live: Busier than usual (Walk-in wait ~20-30 min · Book online for priority chair)"
    )
}

data class SalonInfo(
    val name: String,
    val rating: Double,
    val totalReviews: Int,
    val justdialRating: String,
    val address: String,
    val landmark: String,
    val phone: String,
    val formattedPhone: String,
    val openStatus: String,
    val hours: String,
    val busyness: String
)
