package com.sudoware.aqua.reminder

import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sudoware.aqua.reminder.helpers.SqliteHelper
import com.sudoware.aqua.reminder.utils.AppUtils
import com.sudoware.aqua.reminder.utils.ChartXValueFormatter
import kotlinx.android.synthetic.main.activity_stats.*
import kotlinx.android.synthetic.main.activity_stats.main_activity_parent


class StatsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private var totalPercentage: Float = 0f
    private var totalGlasses: Float = 0f
    private var isThemeDark: Boolean = false


    private fun setDarkTheme(){


        btnBack.setImageDrawable(ContextCompat.getDrawable(applicationContext,
            R.drawable.ic_arrow_circle_left_solid_dark
        ))
        btnInfo.setImageDrawable(ContextCompat.getDrawable(applicationContext,
            R.drawable.ic_info_black_24dp
        ))
        remainingIntake.setTextColor(ContextCompat.getColor(applicationContext,
            R.color.colorWhite
        ))
        targetIntake.setTextColor(ContextCompat.getColor(applicationContext,
            R.color.colorWhite
        ))

        main_activity_parent.background = ContextCompat.getDrawable(applicationContext,
            R.drawable.ic_app_bg_dark
        )
        summaryHeadingTextView.setTextColor(ContextCompat.getColor(applicationContext,
            R.color.colorWhite
        ))
        titleTextView.setTextColor(ContextCompat.getColor(applicationContext,
            R.color.colorBlack
        ))

        chart.axisLeft.textColor = Color.WHITE
        chart.axisRight.textColor = Color.WHITE


        waveConstrantLayout.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.blackishGrey))
    }

    private fun setChartlineToLight(){

        waveConstrantLayout.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.colorWhite))
        chart.axisLeft.textColor = Color.BLACK
        chart.axisRight.textColor = Color.BLACK
        chart.legend.textColor = Color.BLACK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)

        isThemeDark = sharedPref.getBoolean(AppUtils.THEME_SELECTION,true)

        if(isThemeDark){
            setTheme(R.style.MainThemeDark)
        }else{
            setTheme(R.style.MainTheme)
        }

        setContentView(R.layout.activity_stats)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        sqliteHelper = SqliteHelper(this)


        btnBack.setOnClickListener {
            finish()
        }


        btnInfo.setOnClickListener {

            Toast.makeText(applicationContext, "Info Clicked", Toast.LENGTH_SHORT).show();

        }


        if(isThemeDark){
            setDarkTheme()
        }else{
            setChartlineToLight()
        }


        val entries = ArrayList<Entry>()
        val dateArray = ArrayList<String>()

        val cursor: Cursor = sqliteHelper.getAllStats()

        if (cursor.moveToFirst()) {

            for (i in 0 until cursor.count) {
                dateArray.add(cursor.getString(1))
                val percent = cursor.getInt(2) / cursor.getInt(3).toFloat() * 100
                totalPercentage += percent
                totalGlasses += cursor.getInt(2)
                entries.add(Entry(i.toFloat(), percent))
                cursor.moveToNext()
            }

        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show()
        }

        if (entries.isNotEmpty()) {

            chart.description.isEnabled = false
            chart.animateY(1000, Easing.Linear)
            chart.viewPortHandler.setMaximumScaleX(1.5f)
            chart.xAxis.setDrawGridLines(false)
            chart.xAxis.position = XAxis.XAxisPosition.TOP
            chart.xAxis.isGranularityEnabled = true
            chart.legend.isEnabled = false
            chart.fitScreen()
            chart.isAutoScaleMinMaxEnabled = true
            chart.scaleX = 1f
            chart.setPinchZoom(true)
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false
            chart.axisLeft.setDrawAxisLine(false)
            chart.xAxis.setDrawAxisLine(false)
            chart.setDrawMarkers(false)
            chart.xAxis.labelCount = 5
            val rightAxix = chart.axisRight
            rightAxix.setDrawGridLines(false)
            rightAxix.setDrawZeroLine(false)
            rightAxix.setDrawAxisLine(false)
            rightAxix.setDrawLabels(false)

            val dataSet = LineDataSet(entries, "Label")
            dataSet.setDrawCircles(false)
            dataSet.lineWidth = 2.5f
            dataSet.color = ContextCompat.getColor(this, R.color.colorWhite)
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = getDrawable(R.drawable.graph_fill_gradiant_dark)
            dataSet.setDrawValues(false)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER


            if(isThemeDark){
                dataSet.fillDrawable = getDrawable(R.drawable.graph_fill_gradiant_dark)

            }else{
                dataSet.fillDrawable = getDrawable(R.drawable.graph_fill_gradiant)

            }

            val lineData = LineData(dataSet)
            chart.xAxis.valueFormatter = (ChartXValueFormatter(dateArray))
            chart.data = lineData
            chart.invalidate()

            val remaining = sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            ) - sqliteHelper.getIntook(AppUtils.getCurrentDate()!!)

            if (remaining > 0) {
                remainingIntake.text = "$remaining ml"
            } else {
                remainingIntake.text = "0 ml"
            }

            targetIntake.text = "${sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            )
            } ml"

            val percentage = sqliteHelper.getIntook(AppUtils.getCurrentDate()!!) * 100 / sharedPref.getInt(
                AppUtils.TOTAL_INTAKE,
                0
            )
            waterLevelView.centerTitle = "$percentage%"
            waterLevelView.progressValue = percentage

        }
    }
}
