// A M F A H H

package com.example.islam_shanasi

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.islam_shanasi.databinding.ActivityMainBinding
import com.example.islam_shanasi.databinding.ActivityMainScholarTileClickViewBinding
import com.example.islam_shanasi.home_tiles.ScholarTile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var AMSTCB: ActivityMainScholarTileClickViewBinding
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        AMSTCB = ActivityMainScholarTileClickViewBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        init()
        work()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        val actionBar = supportActionBar
        actionBar?.title = "Home"

        // setting drawer
        drawerLayout = findViewById(R.id.activityMainDrawerLayout)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun work() {
        listeners()
    }

    private fun listeners() {
        activityMainBinding.activityMainScholarTile.setOnClickListener {

            activityMainBinding.activityMainScholarTile.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            runBlocking {
//                delay(1000)
//            }

            // it requires dependency
//            lifecycleScope.launch {
//                delay(50)
                val intent = Intent(this@MainActivity, ScholarTile::class.java)
                startActivity(intent)
                finish()
//            }


        }
        activityMainBinding.activityMainChatTile.setOnClickListener {

        }
        activityMainBinding.activityMainHadithTile.setOnClickListener {

        }
        activityMainBinding.activityMainAyatTile.setOnClickListener {

        }
        activityMainBinding.activityMainPrayerTile.setOnClickListener {

        }
        activityMainBinding.activityMainIslamicTile.setOnClickListener {

        }
        activityMainBinding.activityMainQiblaTile.setOnClickListener {

        }
        activityMainBinding.activityMainMosqueTile.setOnClickListener {

        }
        activityMainBinding.activityMainFaqTile.setOnClickListener {

        }
        activityMainBinding.activityMainLogoutTile.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

//        if (AMSTCB.AMSTCBConstraintLay.visibility == View.VISIBLE) {
//            println("##### Line 136 MainActivity.kt")
//            setContentView(activityMainBinding.root)
//            AMSTCB.AMSTCBConstraintLay.visibility = View.GONE
//        } else {
//            println("##### Line 139 MainActivity.kt")

            AlertDialog.Builder(this).setMessage("Exit the App ?")
                .setPositiveButton("Yes", { dialog, btnPos ->
                    super.onBackPressed()
                })
                .setNegativeButton("No", null)
                .show()
//        }
    }
}