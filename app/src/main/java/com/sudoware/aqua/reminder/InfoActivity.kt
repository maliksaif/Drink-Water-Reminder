package com.sudoware.aqua.reminder

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sudoware.aqua.reminder.adapter.InformationAdapter
import com.sudoware.aqua.reminder.helpers.InfoHelper
import com.sudoware.aqua.reminder.utils.AppUtils
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.btnBack
import kotlinx.android.synthetic.main.activity_stats.*

class InfoActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    private lateinit var sharedPref: SharedPreferences
    private var isThemeDark: Boolean = false


    private val mInformations = listOf(
        InfoHelper(
            "It lubricates the joints",
            "Cartilage, found in joints and the disks of the spine, contains around 80 percent water. Long-term dehydration can reduce the joints' shock-absorbing ability, leading to joint pain.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/knee.png?alt=media&token=7956964b-cc2f-415f-8975-fe0af0aa7629"
        ),
        InfoHelper(
            "It forms saliva and mucus",
            "Saliva helps us digest our food and keeps the mouth, nose, and eyes moist. This prevents friction and damage. Drinking water also keeps the mouth clean. Consumed instead of sweetened beverages, it can also reduce tooth decay.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/cold.png?alt=media&token=1c3b538c-dd16-446c-8e8a-4cc9df68ac29"

        ),
        InfoHelper(
            "It delivers oxygen throughout the body",
            "Blood is more than 90 percent water, and blood carries oxygen to different parts of the body.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/oxygen-tank.png?alt=media&token=64a58b42-d453-4ad7-abbd-5c3bf2a1353b"

        ),
        InfoHelper(
            "It boosts skin health and beauty",
            "With dehydration, the skin can become more vulnerable to skin disorders and premature wrinkling",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/facial-treatment.png?alt=media&token=5bbaf37c-0c33-4b75-b925-1d4a7b49bcbe"

        ),
        InfoHelper(
            "It cushions the brain, spinal cord, and other sensitive tissues",
            "Dehydration can affect brain structure and function. It is also involved in the production of hormones and neurotransmitters. Prolonged dehydration can lead to problems with thinking and reasoning.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/brain.png?alt=media&token=1cdb71d8-1cf5-4727-8aa5-98a922264c00"

        ),
        InfoHelper(
            "It regulates body temperature",
            "Water that is stored in the middle layers of the skin comes to the skin's surface as sweat when the body heats up. As it evaporates, it cools the body. In sport.\n" +
                    "\n" +
                    "Some scientists have suggested that when there is too little water in the body, heat storage increases and the individual is less able to tolerate heat strain.\n" +
                    "\n" +
                    "Having a lot of water in the body may reduce physical strain if heat stress occurs during exercise. However, more research is needed into these effects.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/cold%20(1).png?alt=media&token=9e61b0da-7ffa-4d67-a8a1-4701b2c7eeb5"

        ),
        InfoHelper(
            "The digestive system depends on it",
            "The bowel needs water to work properly. Dehydration can lead to digestive problems, constipation, and an overly acidic stomach. This increases the risk of heartburn and stomach ulcers.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/esophagus.png?alt=media&token=1c8846cd-ddb5-4d0c-81a2-a538d6513934"

        ),
        InfoHelper(
            "It flushes body waste",
            "Water is needed in the processes of sweating and removal of urine and feces.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/sweat.png?alt=media&token=dad0c15e-13a7-4d93-95ca-c1784daf2e66"

        ),
        InfoHelper(
            "It helps maintain blood pressure",
            "A lack of water can cause blood to become thicker, increasing blood pressure.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/pressure.png?alt=media&token=1dc6e2fb-b8b6-48ac-82a3-651515939a4f"

        ),
        InfoHelper(
            "The airways need it",
            "When dehydrated, airways are restricted by the body in an effort to minimize water loss. This can make asthma and allergies worse.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/lungs.png?alt=media&token=92a37e5e-9028-403b-a3f0-2f25b028be6b"

        ),
        InfoHelper(
            "It makes minerals and nutrients accessible",
            "These dissolve in water, which makes it possible for them to reach different parts of the body.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/bottle.png?alt=media&token=0b18c307-c31c-41eb-8ab1-1bcee85ee578"

        ),
        InfoHelper(
            "It prevents kidney damage",
            "The kidneys regulate fluid in the body. Insufficient water can lead to kidney stones and other problems.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/kidney.png?alt=media&token=1b9467b7-8895-4e60-be13-6e2f60241701"

        ),
        InfoHelper(
            "It boosts performance during exercise",
            "Some scientists have proposed that consuming more water might enhance performance during strenuous activity.\n" +
                    "\n" +
                    "More research is needed to confirm this, but one review found that dehydration reduces performance in activities lasting longer than 30 minutes.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/gymnastics.png?alt=media&token=9d74a2ad-76e8-4394-a05f-8c0d4915dc95"

        ),
        InfoHelper(
            "Weight loss",
            "Water may also help with weight loss, if it is consumed instead of sweetened juices and sodas. \"Pre loading\" with water before meals can help prevent overeating by creating a sense of fullness.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/calories.png?alt=media&token=be951cb8-a1fb-4df6-8b51-322660942cf9"

        ),
        InfoHelper(
            "It reduces the chance of a hangover",
            "When partying, unsweetened soda water with ice and lemon alternated with alcoholic drinks can help prevent over consumption of alcohol.",
            "https://firebasestorage.googleapis.com/v0/b/ai-food-7aacc.appspot.com/o/drunk.png?alt=media&token=509289ce-6437-4599-adda-986f78f8e023"

        )


    )

    lateinit var infoAdapter: InformationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        isThemeDark = sharedPref.getBoolean(AppUtils.THEME_SELECTION, true)

        recyclerView = findViewById(R.id.recyclerView)


        infoAdapter = InformationAdapter(this,mInformations, isThemeDark)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = infoAdapter

        //###################################################
        //################ Setting Theme ####################
        //###################################################
        if (isThemeDark) {
            btnBack.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_arrow_circle_left_solid_dark
                )
            )

            main_constraintLayout.background = ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_app_bg_dark
            )

        } else {
            btnBack.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_arrow_circle_left_solid
                )
            )

            main_constraintLayout.background = ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_app_bg
            )
        }

        btnBack.setOnClickListener {
            finish()
        }

    }


}
