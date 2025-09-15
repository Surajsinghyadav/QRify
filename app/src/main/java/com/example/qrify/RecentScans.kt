package com.example.qrify

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

class ScanHistory() {

    fun addToScans(
        input: String?,
        context: Context,
        deleteAll: Boolean = false
    ): Map<String, Any?> {
        val recentValues = context.getSharedPreferences("Recently Scanned QR", MODE_PRIVATE)
        val countPref = context.getSharedPreferences("Counter", MODE_PRIVATE)
        var countValue = countPref.getInt("Curr_Count", 0)

        fun sortedMap(recentValues: SharedPreferences): Map<String, Any?> {
            val scanMapValues = recentValues.all
            return scanMapValues.toList()
                .sortedBy { it.first.removePrefix("count").toIntOrNull() ?: 0 }
                .toMap()
        }



        if (input.isNullOrEmpty() && !deleteAll) {
            return sortedMap(recentValues)
        }

        fun deleteHistory() {
            val counteditor = countPref.edit()
            counteditor.clear()
            counteditor.apply()
            val editor = recentValues.edit()
            editor.clear()
            editor.apply()
        }

        if (deleteAll) {
            deleteHistory()
            return sortedMap(recentValues)
        }

        if (countValue < 8) {
            val counteditor = countPref.edit()
            counteditor.putInt("Curr_Count", ++countValue)
            counteditor.apply()

            recentValues.edit {
                putString("count${countValue}", input)
            }
        } else {
            for (k in 1..7) {
                val nextValue = recentValues.getString("count${k + 1}", "")
                recentValues.edit {
                    putString("count${k}", nextValue ?: "")
                }
            }

            recentValues.edit {
                putString("count8", input)
            }
        }

        return sortedMap(recentValues)
    }
}