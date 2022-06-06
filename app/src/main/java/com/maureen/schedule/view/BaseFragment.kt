package com.maureen.schedule.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.maureen.schedule.R


/**
 * Function:
 * @author lianml
 * Create 2021-10-16
 */
open class BaseFragment : Fragment() {

    protected fun hideInputMethod(view: View) {
        view.clearFocus()
        val imm = (view.context).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}