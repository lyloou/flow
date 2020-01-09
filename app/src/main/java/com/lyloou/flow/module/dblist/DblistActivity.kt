package com.lyloou.flow.module.dblist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.lyloou.flow.R


class DblistActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dblist)

        navController = Navigation.findNavController(findViewById(R.id.fragment_db))
        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(R.id.fragment_db).windowToken, 0)
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}
