package me.tuple.lily.adapter

import android.support.annotation.IntRange
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import me.tuple.lily.core.Contexter
import java.util.*


/**  * Created by goku on 19/5/16.   */
class CustomPagerAdapter : FragmentPagerAdapter, ViewPager.OnPageChangeListener {
    private lateinit var titles: MutableList<String>
    private lateinit var fragments: MutableList<Fragment>

    constructor(fm: FragmentManager) : super(fm) {
        fragments = ArrayList(3)
        titles = ArrayList(3)
    }

    constructor(fm: FragmentManager, @IntRange(from = 1) count: Int) : super(fm) {
        fragments = ArrayList(count)
        titles = ArrayList(count)
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun addFragment(title: String, fragment: Fragment) {
        titles.add(title)
        fragments.add(fragment)
    }

    fun addFragment(@StringRes title: Int, fragment: Fragment) {
        titles.add(Contexter.getString(title))
        fragments.add(fragment)
    }

    fun removeAll(fm: FragmentManager) {
        for (i in fragments.indices) {
            titles.removeAt(i)
            fm.beginTransaction().remove(fragments[i]).commit()
            fragments.removeAt(i)
        }
    }

    fun getFragment(position: Int): Fragment? {
        return if (fragments.size > position) {
            fragments[position]
        } else null
    }

    fun setVisibleFragment(position: Int) {
        if (fragments.size > position) {
            for (i in fragments.indices) {
                fragments[i].userVisibleHint = position == i
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        setVisibleFragment(position)
    }

    override fun onPageScrollStateChanged(state: Int) {}
}

