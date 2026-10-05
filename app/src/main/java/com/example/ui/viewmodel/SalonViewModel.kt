package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AcademyCourse
import com.example.data.model.Appointment
import com.example.data.model.ReviewItem
import com.example.data.model.SalonService
import com.example.data.model.ServiceCategory
import com.example.data.model.SpecialOffer
import com.example.data.model.Specialist
import com.example.data.model.StyleLook
import com.example.data.repository.SalonInfo
import com.example.data.repository.SalonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class AppTab(val title: String) {
    HOME("Home"),
    SERVICES("Services"),
    ACADEMY("Academy"),
    LOOKBOOK("Lookbook"),
    APPOINTMENTS("My Bookings")
}

data class BookingUiState(
    val isOpen: Boolean = false,
    val selectedService: SalonService? = null,
    val selectedSpecialist: Specialist? = null,
    val selectedDate: String = "",
    val selectedTime: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val notes: String = "",
    val isSubmitting: Boolean = false,
    val bookingSuccessMessage: String? = null
)

data class ReviewDialogState(
    val isOpen: Boolean = false,
    val authorName: String = "",
    val rating: Int = 5,
    val serviceUsed: String = "Haircut & Styling",
    val comment: String = "",
    val isSubmitting: Boolean = false
)

data class AcademyInquiryState(
    val isOpen: Boolean = false,
    val selectedCourse: AcademyCourse? = null,
    val studentName: String = "",
    val studentPhone: String = "",
    val preferredBatch: String = "Weekday Morning",
    val submittedMessage: String? = null
)

class SalonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SalonRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SalonRepository(database.appointmentDao(), database.reviewDao())
    }

    val salonInfo: SalonInfo = repository.getSalonInfo()
    val allServices: List<SalonService> = repository.getSalonServices()
    val specialists: List<Specialist> = repository.getSpecialists()
    val academyCourses: List<AcademyCourse> = repository.getAcademyCourses()
    val specialOffers: List<SpecialOffer> = repository.getSpecialOffers()
    val lookbookStyles: List<StyleLook> = repository.getStyleLookbook()

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Service Filtering
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val selectedCategory: StateFlow<ServiceCategory?> = _selectedCategory.asStateFlow()

    private val _selectedGenderFilter = MutableStateFlow("All")
    val selectedGenderFilter: StateFlow<String> = _selectedGenderFilter.asStateFlow()

    // Filtered Services Flow
    val filteredServices: StateFlow<List<SalonService>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedGenderFilter
    ) { query, category, gender ->
        allServices.filter { service ->
            val matchesQuery = query.isBlank() ||
                    service.name.contains(query, ignoreCase = true) ||
                    service.description.contains(query, ignoreCase = true) ||
                    service.tags.any { it.contains(query, ignoreCase = true) }

            val matchesCategory = category == null || service.category == category

            val matchesGender = when (gender) {
                "Women" -> service.targetGender == "Women" || service.targetGender == "Unisex"
                "Men" -> service.targetGender == "Men" || service.targetGender == "Unisex"
                else -> true
            }

            matchesQuery && matchesCategory && matchesGender
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allServices)

    // Room Appointments & Reviews Flows
    val appointments: StateFlow<List<Appointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<ReviewItem>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Saved Looks
    private val _savedLookIds = MutableStateFlow<Set<String>>(setOf("look_1"))
    val savedLookIds: StateFlow<Set<String>> = _savedLookIds.asStateFlow()

    // Dialog & Sheet States
    private val _bookingState = MutableStateFlow(BookingUiState(
        selectedDate = getAvailableDates().firstOrNull() ?: "Today"
    ))
    val bookingState: StateFlow<BookingUiState> = _bookingState.asStateFlow()

    private val _reviewDialogState = MutableStateFlow(ReviewDialogState())
    val reviewDialogState: StateFlow<ReviewDialogState> = _reviewDialogState.asStateFlow()

    private val _academyInquiryState = MutableStateFlow(AcademyInquiryState())
    val academyInquiryState: StateFlow<AcademyInquiryState> = _academyInquiryState.asStateFlow()

    private val _showLocationSheet = MutableStateFlow(false)
    val showLocationSheet: StateFlow<Boolean> = _showLocationSheet.asStateFlow()

    // Actions
    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: ServiceCategory?) {
        _selectedCategory.value = category
    }

    fun setGenderFilter(gender: String) {
        _selectedGenderFilter.value = gender
    }

    fun toggleSaveLook(lookId: String) {
        val current = _savedLookIds.value.toMutableSet()
        if (current.contains(lookId)) {
            current.remove(lookId)
        } else {
            current.add(lookId)
        }
        _savedLookIds.value = current
    }

    fun openLocationSheet() {
        _showLocationSheet.value = true
    }

    fun closeLocationSheet() {
        _showLocationSheet.value = false
    }

    // Booking actions
    fun openBooking(service: SalonService? = null, specialist: Specialist? = null) {
        val defaultDate = getAvailableDates().firstOrNull() ?: "Today"
        val defaultTime = "11:30 AM"
        _bookingState.value = BookingUiState(
            isOpen = true,
            selectedService = service ?: allServices.first(),
            selectedSpecialist = specialist,
            selectedDate = defaultDate,
            selectedTime = defaultTime,
            customerName = _bookingState.value.customerName,
            customerPhone = _bookingState.value.customerPhone
        )
    }

    fun closeBooking() {
        _bookingState.value = _bookingState.value.copy(isOpen = false, bookingSuccessMessage = null)
    }

    fun updateBookingService(service: SalonService) {
        _bookingState.value = _bookingState.value.copy(selectedService = service)
    }

    fun updateBookingSpecialist(specialist: Specialist?) {
        _bookingState.value = _bookingState.value.copy(selectedSpecialist = specialist)
    }

    fun updateBookingDate(date: String) {
        _bookingState.value = _bookingState.value.copy(selectedDate = date)
    }

    fun updateBookingTime(time: String) {
        _bookingState.value = _bookingState.value.copy(selectedTime = time)
    }

    fun updateBookingCustomer(name: String, phone: String, notes: String) {
        _bookingState.value = _bookingState.value.copy(
            customerName = name,
            customerPhone = phone,
            notes = notes
        )
    }

    fun confirmBooking(onSuccess: (Long) -> Unit) {
        val state = _bookingState.value
        val service = state.selectedService ?: return
        val name = state.customerName.ifBlank { "Guest Client" }
        val phone = state.customerPhone.ifBlank { "9876543210" }

        val specialistName = state.selectedSpecialist?.name ?: "Any Senior Stylist (First Available)"
        val specialistId = state.selectedSpecialist?.id ?: "any"

        viewModelScope.launch {
            _bookingState.value = state.copy(isSubmitting = true)
            val appointment = Appointment(
                customerName = name,
                customerPhone = phone,
                serviceId = service.id,
                serviceName = service.name,
                serviceCategory = service.category.displayName,
                specialistId = specialistId,
                specialistName = specialistName,
                appointmentDate = state.selectedDate.ifBlank { "Today" },
                appointmentTime = state.selectedTime.ifBlank { "12:00 PM" },
                price = service.price,
                status = "CONFIRMED",
                notes = state.notes
            )
            val id = repository.bookAppointment(appointment)
            _bookingState.value = state.copy(
                isSubmitting = false,
                isOpen = false,
                bookingSuccessMessage = "Appointment confirmed for ${service.name} at ${state.selectedTime}!"
            )
            onSuccess(id)
        }
    }

    fun cancelAppointment(id: Long) {
        viewModelScope.launch {
            repository.cancelAppointment(id)
        }
    }

    fun deleteAppointment(id: Long) {
        viewModelScope.launch {
            repository.deleteAppointment(id)
        }
    }

    // Reviews
    fun openReviewDialog() {
        _reviewDialogState.value = ReviewDialogState(isOpen = true)
    }

    fun closeReviewDialog() {
        _reviewDialogState.value = ReviewDialogState(isOpen = false)
    }

    fun submitReview(name: String, rating: Int, service: String, comment: String) {
        if (comment.isBlank()) return
        viewModelScope.launch {
            _reviewDialogState.value = _reviewDialogState.value.copy(isSubmitting = true)
            val review = ReviewItem(
                authorName = name.ifBlank { "Verified Mohali Client" },
                rating = rating.coerceIn(1, 5),
                timeAgo = "Just now",
                comment = comment,
                serviceUsed = service.ifBlank { "Salon Service" },
                source = "Verified Customer"
            )
            repository.addReview(review)
            _reviewDialogState.value = ReviewDialogState(isOpen = false)
        }
    }

    // Academy Inquiry
    fun openAcademyInquiry(course: AcademyCourse) {
        _academyInquiryState.value = AcademyInquiryState(
            isOpen = true,
            selectedCourse = course
        )
    }

    fun closeAcademyInquiry() {
        _academyInquiryState.value = AcademyInquiryState(isOpen = false)
    }

    fun submitAcademyInquiry(name: String, phone: String, batch: String) {
        val course = _academyInquiryState.value.selectedCourse ?: return
        _academyInquiryState.value = _academyInquiryState.value.copy(
            submittedMessage = "Thank you $name! Scissor Cut Academy counselor will call you on $phone regarding ${course.title}."
        )
    }

    fun getAvailableDates(): List<String> {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("EEE, d MMM", Locale.ENGLISH)
        val list = mutableListOf<String>()
        for (i in 0..6) {
            val dateStr = if (i == 0) "Today (${format.format(calendar.time)})"
            else if (i == 1) "Tomorrow (${format.format(calendar.time)})"
            else format.format(calendar.time)
            list.add(dateStr)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    fun getAvailableTimeSlots(): List<String> = listOf(
        "09:30 AM", "10:15 AM", "11:00 AM", "11:45 AM",
        "01:00 PM", "02:00 PM", "03:00 PM", "04:15 PM",
        "05:30 PM", "06:45 PM", "07:30 PM", "08:15 PM"
    )
}
