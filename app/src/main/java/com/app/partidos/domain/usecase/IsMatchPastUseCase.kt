package com.app.partidos.domain.usecase

import java.util.Calendar
import javax.inject.Inject

class IsMatchPastUseCase @Inject constructor() {
    operator fun invoke(fecha: String, hora: String): Boolean {
        return try {
            val fechaParts = fecha.take(10).split("-")
            val year = fechaParts[0].toInt()
            val month = fechaParts[1].toInt() - 1
            val day = fechaParts[2].toInt()

            val timeParts = hora.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            val cal = Calendar.getInstance()
            cal.set(year, month, day, hour, minute, 0)
            
            val now = Calendar.getInstance()
            cal.timeInMillis <= now.timeInMillis
        } catch (e: Exception) {
            false
        }
    }
}
