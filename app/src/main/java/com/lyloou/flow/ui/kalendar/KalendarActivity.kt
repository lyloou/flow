package com.lyloou.flow.ui.kalendar

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity

class KalendarActivity : BaseCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalendar)

        navController = Navigation.findNavController(findViewById(R.id.fragment))
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}
