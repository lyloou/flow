package com.lyloou.flow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.ui.home.ScheduleFragment
import com.lyloou.flow.ui.list.ListFragment
import com.lyloou.flow.ui.mine.MineFragment
import kotlinx.android.synthetic.main.activity_main.*

// [Android布局实现-BottomNavigationView+ViewPager+Fragment+整合 | 朋也的博客](https://tomoya92.github.io/2017/04/05/android-bottomnavigationview-viewpager-fragment/)
class MainActivity : BaseCompatActivity(), ViewPager.OnPageChangeListener {
    companion object {
        const val INDEX = "index"
    }

    val menuIdToFragments = arrayListOf(
        R.id.navigation_home to ScheduleFragment(),
        R.id.navigation_list to ListFragment(),
        R.id.navigation_mine to MineFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = menuIdToFragments[position].second
            override fun getCount(): Int = menuIdToFragments.size
        }
        viewPager.currentItem = intent.getIntExtra(INDEX, 0)
        nav_view.setOnNavigationItemSelectedListener { menu ->
            viewPager.setCurrentItem(
                menuIdToFragments.indexOfFirst { it.first == menu.itemId },
                false
            )
            true
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewPager.currentItem = intent?.getIntExtra(INDEX, 0) ?: 0
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        nav_view.menu.getItem(position).isChecked = true
    }

//    override fun onBackPressed() {
//        moveTaskToBack(true)
//    }
}
