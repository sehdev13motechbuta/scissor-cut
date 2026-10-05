package com.example.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object IntentUtils {
    fun callSalon(context: Context, phoneNumber: String = "08054596910") {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer for $phoneNumber", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMaps(
        context: Context,
        query: String = "Scissor Cut unisex salon & academy, SCO 118, Sector 117, Mohali"
    ) {
        try {
            val gmmIntentUri = Uri.parse("geo:30.7046,76.6975?q=" + Uri.encode(query))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query))
                )
                context.startActivity(browserIntent)
            }
        } catch (e: Exception) {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query))
            )
            context.startActivity(browserIntent)
        }
    }

    fun openWhatsApp(context: Context, phoneNumber: String = "918054596910", message: String = "Hello Scissor Cut! I would like to inquire about your salon services and appointments.") {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed. Calling salon instead.", Toast.LENGTH_SHORT).show()
            callSalon(context)
        }
    }

    fun shareSalon(context: Context) {
        try {
            val shareText = "Check out Scissor Cut Unisex Salon & Academy in Sector 117, Airport Rd, Mohali! 4.8★ rating with haircuts, nail extensions, ₹999 makeup & academy courses. Contact: 080545 96910"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Share Scissor Cut"))
        } catch (e: Exception) {
            // ignore
        }
    }
}
