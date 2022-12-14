package com.example.bitfitv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class MainHabitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(FeedFragment())
        handleNewEntry()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationMain)
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.feedTab -> fragment = FeedFragment()
                R.id.overviewTab -> fragment = OverviewFragment()
            }
            replaceFragment(fragment)
            handleNewEntry()
            true
        }

    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, newFragment)
        fragmentTransaction.commit()

    }

    private fun handleNewEntry() {
        val habit = intent.getSerializableExtra("ENTRY_EXTRA")
        if (habit != null){
            Log.d("AddActivity", "got an extra")
            Log.d("AddActivity", (habit as Habit).toString())
            lifecycleScope.launch(Dispatchers.IO) {
                (application as BitFitApplication).db.habitDao().insert(
                    HabitEntity(
                        date = habit.date,
                        hour = habit.hour,
                        comment = habit.comment,
                        rate = habit.rate
                    )
                )
            }
            intent.removeExtra("ENTRY_EXTRA")
        } else {
            Log.d("AddActivity", "no extra")
        }

        var addButtonView = findViewById<Button>(R.id.btnNewEntry)
        addButtonView.setOnClickListener {
            val intentX = Intent(this, AddHabitActivity::class.java)
            this.startActivity(intentX)
        }

    }


}