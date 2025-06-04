package com.example.tabwidgetsample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentsFragment()
            1 -> TeachersFragment()
            2 -> ClassesFragment()
            else -> Fragment()
        }
    }
}
