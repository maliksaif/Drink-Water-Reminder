package com.sudoware.aqua.reminder

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import com.sudoware.aqua.reminder.fragments.BottomSheetFragment
import com.sudoware.aqua.reminder.helpers.AlarmHelper
import com.sudoware.aqua.reminder.helpers.SqliteHelper
import com.sudoware.aqua.reminder.utils.AppUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private var totalIntake: Int = 0
    private var inTook: Int = 0
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var dateNow: String
    private var notificStatus: Boolean = false
    private var selectedOption: Int? = null
    private var selectedOptionName: String? = null
    private var snackbar: Snackbar? = null
    private var doubleBackToExitPressedOnce = false

    private var isThemeDark: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        isThemeDark = sharedPref.getBoolean(AppUtils.THEME_SELECTION, true)

        if (isThemeDark) {
            setTheme(R.style.MainThemeDark)
        } else {
            setTheme(R.style.MainTheme)
        }

        setContentView(R.layout.activity_main)

        sqliteHelper = SqliteHelper(this)


        totalIntake = sharedPref.getInt(AppUtils.TOTAL_INTAKE, 0)

        if (sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true)) {
            startActivity(Intent(this, WalkThroughActivity::class.java))
            finish()
        } else if (totalIntake <= 0) {
            startActivity(Intent(this, InitUserInfoActivity::class.java))
            finish()
        }

        dateNow = AppUtils.getCurrentDate()!!

//        setFabColors()
    }

    override fun onResume() {
        super.onResume()
        println("on Resume Called")

    }

    private fun setDarkTheme() {

        val dailyDrinkTextView: TextView = findViewById(R.id.dailyDrinkTargetTextView)
        dailyDrinkTextView.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorBlack
            )
        )
        tvIntook.setTextColor(resources.getColor(R.color.colorBlack))
        tvTotalIntake.setTextColor(resources.getColor(R.color.colorBlack))

        intakeProgress.progressColor = Color.BLACK
        intakeProgress.markerColor = Color.BLACK
        intakeProgress.textColorMarker = Color.BLACK
        btnMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_settings_black_24dp))

        main_activity_parent.background = resources.getDrawable(R.drawable.ic_app_bg_dark)
        constraintLayout2.setBackgroundColor(resources.getColor(R.color.blackishGrey))
        constraintLayout3.setBackgroundColor(resources.getColor(R.color.blackishGrey))

        btnNotific.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blackishGrey))
        btnStats.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blackishGrey))
        fabAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_plus_solid_dark))


    }

    private fun setLightTheme() {


        val dailyDrinkTextView: TextView = findViewById(R.id.dailyDrinkTargetTextView)
        dailyDrinkTextView.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorWhite
            )
        )
        tvIntook.setTextColor(resources.getColor(R.color.colorWhite))
        tvTotalIntake.setTextColor(resources.getColor(R.color.colorWhite))

        intakeProgress.progressColor = Color.WHITE
        intakeProgress.markerColor = Color.WHITE
        intakeProgress.textColorMarker = Color.WHITE
        btnMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_settings_white_24dp))

        main_activity_parent.background = resources.getDrawable(R.drawable.ic_app_bg)
        constraintLayout2.setBackgroundColor(resources.getColor(R.color.colorWhite))
        constraintLayout3.setBackgroundColor(resources.getColor(R.color.colorWhite))

        btnNotific.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorWhite))
        btnStats.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorWhite))
        fabAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_plus_solid))


    }


    override fun onStart() {
        super.onStart()

        val outValue = TypedValue()
        applicationContext.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

        notificStatus = sharedPref.getBoolean(AppUtils.NOTIFICATION_STATUS_KEY, true)
        val alarm = AlarmHelper()
        if (!alarm.checkAlarm(this) && notificStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell))
            alarm.setAlarm(this, sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong())
        }

        if (notificStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell))
        } else {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell_disabled))
        }

        sqliteHelper.addAll(dateNow, 0, totalIntake)

        inTook = sqliteHelper.getIntook(dateNow)

        setWaterLevel(inTook, totalIntake)

        btnMenu.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        changeThemeSwitch.setOnClickListener {

            isThemeDark = if (!isThemeDark) {

                Toast.makeText(applicationContext, "Dark mode activated", Toast.LENGTH_SHORT).show()
                setDarkTheme()
                application.setTheme(R.style.MainThemeDark)
                sharedPref.edit().putBoolean(AppUtils.THEME_SELECTION, true).apply()
                recreate()
                true

            } else {
                Toast.makeText(applicationContext, "Light mode activated", Toast.LENGTH_SHORT).show()

                setLightTheme()
                application.setTheme(R.style.MainTheme)
                sharedPref.edit().putBoolean(AppUtils.THEME_SELECTION, false).apply()
                recreate()
                false
            }

        }


        if (isThemeDark) {
            changeThemeSwitch.isChecked = true
            setDarkTheme()
        } else {
            changeThemeSwitch.isChecked = false
            setLightTheme()
        }

        fabAdd.setOnClickListener {
            if (selectedOption != null && selectedOptionName != null) {
                if ((inTook * 100 / totalIntake) <= 140) {
                    if (sqliteHelper.addIntook(dateNow, selectedOption!!) > 0) {
                        inTook += selectedOption!!
                        setWaterLevel(inTook, totalIntake)

                        Snackbar.make(it, "Your water intake was saved...!!", Snackbar.LENGTH_SHORT).show()

                    }
                } else {
                    Snackbar.make(it, "You have already achieved the goal", Snackbar.LENGTH_SHORT).show()
                }
                selectedOption = null
                selectedOptionName = null
                opAqua.background = getDrawable(outValue.resourceId)
                opCoffee.background = getDrawable(outValue.resourceId)
                opTea.background = getDrawable(outValue.resourceId)
                opSoftDrink.background = getDrawable(outValue.resourceId)
                opJuice.background = getDrawable(outValue.resourceId)
                opMilk.background = getDrawable(outValue.resourceId)
            } else {
                YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(cardView)
                Snackbar.make(it, "Please select an option", Snackbar.LENGTH_SHORT).show()
            }
        }



        btnNotific.setOnClickListener {
            notificStatus = !notificStatus
            sharedPref.edit().putBoolean(AppUtils.NOTIFICATION_STATUS_KEY, notificStatus).apply()
            if (notificStatus) {
                btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell))
                Snackbar.make(it, "Notification Enabled..", Snackbar.LENGTH_SHORT).show()
                alarm.setAlarm(this, sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong())
            } else {
                btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell_disabled))
                Snackbar.make(it, "Notification Disabled..", Snackbar.LENGTH_SHORT).show()
                alarm.cancelAlarm(this)
            }
        }

        btnStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }


        opAqua.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 200
            selectedOptionName = "Aqua"
            opAqua.background = getDrawable(R.drawable.option_select_bg)
            opCoffee.background = getDrawable(outValue.resourceId)
            opTea.background = getDrawable(outValue.resourceId)
            opSoftDrink.background = getDrawable(outValue.resourceId)
            opJuice.background = getDrawable(outValue.resourceId)
            opMilk.background = getDrawable(outValue.resourceId)

        }

        opCoffee.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 150
            selectedOptionName = "Coffee"
            opAqua.background = getDrawable(outValue.resourceId)
            opCoffee.background = getDrawable(R.drawable.option_select_bg)
            opTea.background = getDrawable(outValue.resourceId)
            opSoftDrink.background = getDrawable(outValue.resourceId)
            opJuice.background = getDrawable(outValue.resourceId)
            opMilk.background = getDrawable(outValue.resourceId)

        }

        opTea.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 150
            selectedOptionName = "Tea"
            opAqua.background = getDrawable(outValue.resourceId)
            opCoffee.background = getDrawable(outValue.resourceId)
            opTea.background = getDrawable(R.drawable.option_select_bg)
            opSoftDrink.background = getDrawable(outValue.resourceId)
            opJuice.background = getDrawable(outValue.resourceId)
            opMilk.background = getDrawable(outValue.resourceId)

        }

        opSoftDrink.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 200
            selectedOptionName = "Soft Drink"
            opAqua.background = getDrawable(outValue.resourceId)
            opCoffee.background = getDrawable(outValue.resourceId)
            opTea.background = getDrawable(outValue.resourceId)
            opSoftDrink.background = getDrawable(R.drawable.option_select_bg)
            opJuice.background = getDrawable(outValue.resourceId)
            opMilk.background = getDrawable(outValue.resourceId)

        }

        opJuice.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 200
            selectedOptionName = "Juice"
            opAqua.background = getDrawable(outValue.resourceId)
            opCoffee.background = getDrawable(outValue.resourceId)
            opTea.background = getDrawable(outValue.resourceId)
            opSoftDrink.background = getDrawable(outValue.resourceId)
            opJuice.background = getDrawable(R.drawable.option_select_bg)
            opMilk.background = getDrawable(outValue.resourceId)

        }

        opMilk.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 50
            selectedOptionName = "Milk"
            opAqua.background = getDrawable(outValue.resourceId)
            opCoffee.background = getDrawable(outValue.resourceId)
            opTea.background = getDrawable(outValue.resourceId)
            opSoftDrink.background = getDrawable(outValue.resourceId)
            opJuice.background = getDrawable(outValue.resourceId)
            opMilk.background = getDrawable(R.drawable.option_select_bg)

        }

    }


    private fun setWaterLevel(inTook: Int, totalIntake: Int) {

        YoYo.with(Techniques.SlideInDown)
            .duration(500)
            .playOn(tvIntook)
        tvIntook.text = "$inTook"
        tvTotalIntake.text = "/$totalIntake ml"
        val progress = ((inTook / totalIntake.toFloat()) * 100).toInt()
        YoYo.with(Techniques.Pulse)
            .duration(500)
            .playOn(intakeProgress)
        intakeProgress.currentProgress = progress
        if ((inTook * 100 / totalIntake) > 140) {
            Snackbar.make(main_activity_parent, "You achieved the goal", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Snackbar.make(
            this.window.decorView.findViewById(android.R.id.content),
            "Please click BACK again to exit",
            Snackbar.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }

}
