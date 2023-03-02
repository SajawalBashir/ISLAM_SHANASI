package com.example.islam_shanasi

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.time.LocalDate
import java.time.ZoneId
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*

class TempActivity : AppCompatActivity() {
    lateinit var tvMonth: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
        val cal = findViewById<CompactCalendarView>(R.id.calendar)
        val islDate = findViewById<TextView>(R.id.tvIslamicDate)
        val tvEvent = findViewById<TextView>(R.id.tvEvent)
        tvMonth = findViewById(R.id.tvMonthTitle)

        val localDate = LocalDate.now()
        val dateFormated = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val calendar = Calendar.getInstance()
        calendar.time = date

        setMonthTitle(calendar)

        println("##### Line 39 TempActivity.kt calendar.get(Calendar.MONTH) = ${calendar.get(Calendar.MONTH)}")

        val islamicDate = convertToIslamicDate(dateFormated)
        islDate.text = islamicDate

        val event = Event(Color.parseColor("#686868"), 1677974400000L, "Some extra data that I want to store.")

        cal.addEvent(event)

        cal.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {

//                val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//                val date = LocalDate.parse(dateClicked?.toLocaleString(), dateFormater)

                val cal2 = Calendar.getInstance()
                cal2.time = dateClicked as Date

                if(cal2.get(Calendar.DATE).equals(5) && cal2.get(Calendar.MONTH).equals(2) && cal2.get(Calendar.YEAR).equals(2023)){
                    Toast.makeText(this@TempActivity, "it is holiday", Toast.LENGTH_SHORT).show()
                }

                println("##### Line 36 day TempActivity.kt date = ${cal2.get(Calendar.DATE)} month = ${cal2.get(Calendar.MONTH)} year= ${cal2.get(Calendar.YEAR)} ")
                Toast.makeText(this@TempActivity, "it is $dateClicked", Toast.LENGTH_SHORT).show()

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val cal2 = Calendar.getInstance()
                cal2.time = firstDayOfNewMonth as Date
                setMonthTitle(cal2)
            }
        })
    }

    fun setMonthTitle(cal2: Calendar) {
        when (cal2.get(Calendar.MONTH)) {
            0 -> {
                tvMonth.text = "January ${cal2.get(Calendar.YEAR)}"
            }
            1 -> {
                tvMonth.text = "February ${cal2.get(Calendar.YEAR)}"
            }
            2 -> {
                tvMonth.text = "March ${cal2.get(Calendar.YEAR)}"
            }
            3 -> {
                tvMonth.text = "April ${cal2.get(Calendar.YEAR)}"
            }
            4 -> {
                tvMonth.text = "May ${cal2.get(Calendar.YEAR)}"
            }
            5 -> {
                tvMonth.text = "June ${cal2.get(Calendar.YEAR)}"
            }
            6 -> {
                tvMonth.text = "July ${cal2.get(Calendar.YEAR)}"
            }
            7 -> {
                tvMonth.text = "August ${cal2.get(Calendar.YEAR)}"
            }
            8 -> {
                tvMonth.text = "September ${cal2.get(Calendar.YEAR)}"
            }
            9 -> {
                tvMonth.text = "October ${cal2.get(Calendar.YEAR)}"
            }
            10 -> {
                tvMonth.text = "November ${cal2.get(Calendar.YEAR)}"
            }
            11 -> {
                tvMonth.text = "December ${cal2.get(Calendar.YEAR)}"
            }
        }
    }

    fun convertToIslamicDate(englishDate: String): String {
        val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(englishDate, dateFormater)
        val islamicMonth = HijrahDate.from(date).withVariant(HijrahChronology.INSTANCE)
            .get(ChronoField.MONTH_OF_YEAR).toString()
        val islamicYear =
            HijrahDate.from(date).withVariant(HijrahChronology.INSTANCE).get(ChronoField.YEAR)
                .toString()
        val islamicDay = HijrahDate.from(date).withVariant(HijrahChronology.INSTANCE)
            .get(ChronoField.DAY_OF_MONTH).toString()
        println("##### Line 40 TempActivity.kt month = $islamicMonth year = $islamicYear day = $islamicDay")

        var islamicDate = ""

        when (islamicMonth.toInt()) {
            1 -> islamicDate = "Muharram $islamicDay, $islamicYear"
            2 -> islamicDate = "Safar $islamicDay, $islamicYear"
            3 -> islamicDate = "Rabi Al-Awwal $islamicDay, $islamicYear"
            4 -> islamicDate = "Rabi Al-Thani $islamicDay, $islamicYear"
            5 -> islamicDate = "Jamada Al-Awwal $islamicDay, $islamicYear"
            6 -> islamicDate = "Jamada Al-Thani $islamicDay, $islamicYear"
            7 -> islamicDate = "Rajab $islamicDay, $islamicYear"
            8 -> islamicDate = "Shaban $islamicDay, $islamicYear"
            9 -> islamicDate = "Ramadan $islamicDay, $islamicYear"
            10 -> islamicDate = "Shawwal $islamicDay, $islamicYear"
            11 -> islamicDate = "Dhul-Qadah $islamicDay, $islamicYear"
            12 -> islamicDate = "Dhul-Hijjah $islamicDay, $islamicYear"
        }

        return islamicDate
    }
}















