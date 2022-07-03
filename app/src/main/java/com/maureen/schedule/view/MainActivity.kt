package com.maureen.schedule.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.maureen.schedule.R
import com.maureen.schedule.databinding.ActivityMainBinding
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import com.maureen.schedule.TaskViewModel


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<TaskViewModel>()
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment!!.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setNavigation()
        viewBinding.bottomAppBar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.more -> {
                        navController.navigate(R.id.moreActionDialog)
                        true
                    }
                    else -> false
                }
            }
           setNavigationOnClickListener { navController.navigate(R.id.selectChecklistDialog) }
        }

        viewBinding.fbAdd.setOnClickListener {
            val checklistId = viewModel.checklistWithTaskLiveData.value?.checklist?.id ?: -1
            navController.navigate(
                R.id.action_to_edit_task,
                bundleOf(KEY_CHECKLIST_ID to checklistId)
            )
        }
        viewModel.checklistWithTaskLiveData.value
        viewModel.initChecklist()
    }

    private fun setNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.editTaskScreen -> {
                    viewBinding.bottomAppBar.isVisible = false
                    viewBinding.fbAdd.hide()
                }
                else -> {
                    viewBinding.bottomAppBar.isVisible = true
                    viewBinding.fbAdd.show()
                }
            }
        }
    }
}