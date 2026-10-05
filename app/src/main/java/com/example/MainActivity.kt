package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BookingBottomSheet
import com.example.ui.components.ReviewDialog
import com.example.ui.components.SalonInfoSheet
import com.example.ui.components.TopSalonBar
import com.example.ui.screens.AcademyScreen
import com.example.ui.screens.AppointmentsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LookbookScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.SalonViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ScissorCutApp()
            }
        }
    }
}

@Composable
fun ScissorCutApp(viewModel: SalonViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val filteredServices by viewModel.filteredServices.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedGender by viewModel.selectedGenderFilter.collectAsStateWithLifecycle()
    val appointments by viewModel.appointments.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val savedLookIds by viewModel.savedLookIds.collectAsStateWithLifecycle()
    val bookingState by viewModel.bookingState.collectAsStateWithLifecycle()
    val reviewDialogState by viewModel.reviewDialogState.collectAsStateWithLifecycle()
    val academyInquiryState by viewModel.academyInquiryState.collectAsStateWithLifecycle()
    val showLocationSheet by viewModel.showLocationSheet.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle back button on secondary screens
    if (currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.setTab(AppTab.HOME)
        }
    }

    LaunchedEffect(bookingState.bookingSuccessMessage) {
        bookingState.bookingSuccessMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopSalonBar(
                onLocationClick = { viewModel.openLocationSheet() }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("salon_bottom_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                // Home
                NavigationBarItem(
                    selected = currentTab == AppTab.HOME,
                    onClick = { viewModel.setTab(AppTab.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF382900),
                        indicatorColor = GoldPrimary,
                        selectedTextColor = GoldPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                // Services
                NavigationBarItem(
                    selected = currentTab == AppTab.SERVICES,
                    onClick = { viewModel.setTab(AppTab.SERVICES) },
                    icon = { Icon(Icons.Default.ContentCut, contentDescription = "Services") },
                    label = { Text("Services") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF382900),
                        indicatorColor = GoldPrimary,
                        selectedTextColor = GoldPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_services")
                )

                // Academy
                NavigationBarItem(
                    selected = currentTab == AppTab.ACADEMY,
                    onClick = { viewModel.setTab(AppTab.ACADEMY) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Academy") },
                    label = { Text("Academy") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF382900),
                        indicatorColor = GoldPrimary,
                        selectedTextColor = GoldPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_academy")
                )

                // Lookbook
                NavigationBarItem(
                    selected = currentTab == AppTab.LOOKBOOK,
                    onClick = { viewModel.setTab(AppTab.LOOKBOOK) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Lookbook") },
                    label = { Text("Lookbook") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF382900),
                        indicatorColor = GoldPrimary,
                        selectedTextColor = GoldPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_lookbook")
                )

                // My Bookings
                NavigationBarItem(
                    selected = currentTab == AppTab.APPOINTMENTS,
                    onClick = { viewModel.setTab(AppTab.APPOINTMENTS) },
                    icon = {
                        val activeCount = appointments.count { it.status == "CONFIRMED" }
                        if (activeCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = GoldPrimary) {
                                        Text("$activeCount", color = Color(0xFF382900))
                                    }
                                }
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "My Bookings")
                            }
                        } else {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "My Bookings")
                        }
                    },
                    label = { Text("Bookings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF382900),
                        indicatorColor = GoldPrimary,
                        selectedTextColor = GoldPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_bookings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> HomeScreen(
                    salonInfo = viewModel.salonInfo,
                    offers = viewModel.specialOffers,
                    specialists = viewModel.specialists,
                    reviews = reviews,
                    popularServices = viewModel.allServices.filter { it.isPopular },
                    onNavigateTab = { viewModel.setTab(it) },
                    onSelectCategory = { viewModel.setCategoryFilter(it) },
                    onBookService = { viewModel.openBooking(service = it) },
                    onOpenBooking = { viewModel.openBooking() },
                    onOpenDirections = { viewModel.openLocationSheet() },
                    onOpenReviewDialog = { viewModel.openReviewDialog() }
                )

                AppTab.SERVICES -> ServicesScreen(
                    services = filteredServices,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    selectedGender = selectedGender,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onCategorySelect = { viewModel.setCategoryFilter(it) },
                    onGenderSelect = { viewModel.setGenderFilter(it) },
                    onBookService = { viewModel.openBooking(service = it) }
                )

                AppTab.ACADEMY -> AcademyScreen(
                    courses = viewModel.academyCourses,
                    inquiryState = academyInquiryState,
                    onOpenInquiry = { viewModel.openAcademyInquiry(it) },
                    onCloseInquiry = { viewModel.closeAcademyInquiry() },
                    onSubmitInquiry = { name, phone, batch ->
                        viewModel.submitAcademyInquiry(name, phone, batch)
                    }
                )

                AppTab.LOOKBOOK -> LookbookScreen(
                    looks = viewModel.lookbookStyles,
                    savedLookIds = savedLookIds,
                    onToggleSave = { viewModel.toggleSaveLook(it) },
                    onBookLook = { look ->
                        val matchingService = viewModel.allServices.firstOrNull {
                            look.recommendedServices.contains(it.name)
                        } ?: viewModel.allServices.first()
                        viewModel.openBooking(service = matchingService)
                    }
                )

                AppTab.APPOINTMENTS -> AppointmentsScreen(
                    appointments = appointments,
                    onOpenBooking = { viewModel.openBooking() },
                    onCancelAppointment = { viewModel.cancelAppointment(it) },
                    onDeleteAppointment = { viewModel.deleteAppointment(it) }
                )
            }
        }
    }

    // Booking Bottom Sheet
    if (bookingState.isOpen) {
        BookingBottomSheet(
            bookingState = bookingState,
            availableDates = viewModel.getAvailableDates(),
            availableTimeSlots = viewModel.getAvailableTimeSlots(),
            specialists = viewModel.specialists,
            allServices = viewModel.allServices,
            onDismiss = { viewModel.closeBooking() },
            onSelectService = { viewModel.updateBookingService(it) },
            onSelectSpecialist = { viewModel.updateBookingSpecialist(it) },
            onSelectDate = { viewModel.updateBookingDate(it) },
            onSelectTime = { viewModel.updateBookingTime(it) },
            onCustomerDetailsChange = { name, phone, notes ->
                viewModel.updateBookingCustomer(name, phone, notes)
            },
            onConfirm = {
                viewModel.confirmBooking {
                    viewModel.setTab(AppTab.APPOINTMENTS)
                }
            }
        )
    }

    // Salon Information & Map Directions Sheet
    if (showLocationSheet) {
        SalonInfoSheet(
            salonInfo = viewModel.salonInfo,
            onDismiss = { viewModel.closeLocationSheet() }
        )
    }

    // Write Customer Review Dialog
    if (reviewDialogState.isOpen) {
        ReviewDialog(
            isOpen = reviewDialogState.isOpen,
            onDismiss = { viewModel.closeReviewDialog() },
            onSubmit = { name, rating, service, comment ->
                viewModel.submitReview(name, rating, service, comment)
                scope.launch {
                    snackbarHostState.showSnackbar("Thank you for reviewing Scissor Cut!")
                }
            }
        )
    }
}
