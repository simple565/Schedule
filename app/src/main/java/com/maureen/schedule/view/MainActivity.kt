package com.maureen.schedule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maureen.schedule.R
import com.maureen.schedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        with(viewBinding) {
            mainVpContainer.isUserInputEnabled = false
            mainVpContainer.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> ScheduleFragment()
                        1 -> CourseTableFragment()
                        else -> ScheduleFragment()
                    }
                }

            }
            mainBottomNavView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_schedule -> mainVpContainer.currentItem = 0
                    R.id.nav_timetable -> mainVpContainer.currentItem = 1
                    else -> mainVpContainer.currentItem = 0
                }
                true
            }
        }
    }
}