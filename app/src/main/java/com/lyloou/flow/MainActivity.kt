package com.lyloou.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lyloou.flow.ui.home.HomeFragment
import com.lyloou.flow.ui.list.ListFragment
import com.lyloou.flow.ui.mine.MineFragment
import kotlinx.android.synthetic.main.activity_main.*

// [Android布局实现-BottomNavigationView+ViewPager+Fragment+整合 | 朋也的博客](https://tomoya92.github.io/2017/04/05/android-bottomnavigationview-viewpager-fragment/)
class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    val menuIdToFragments = arrayListOf(
        R.id.navigation_home to HomeFragment(),
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        nav_view.menu.getItem(position).isChecked = true
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}
