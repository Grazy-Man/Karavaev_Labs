package com.example.tabwidgetsample



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapter = TabPagerAdapter(this)
        viewPager.adapter = adapter

        val tabTitles = listOf(
            getString(R.string.tab1_indicator),
            getString(R.string.tab2_indicator),
            getString(R.string.tab3_indicator)
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.contentDescription = tabTitles[position]
        }.attach()
    }
}

