package com.maureen.schedule.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.maureen.schedule.R
import com.maureen.schedule.TaskViewModel
import com.maureen.schedule.databinding.ActivityMainBinding
import com.maureen.schedule.utils.KEY_CHECKLIST_ID


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<TaskViewModel>()
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}