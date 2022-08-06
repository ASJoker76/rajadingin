package com.raja.dingin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.raja.dingin.view.fragment.AccountFragment
import com.raja.dingin.view.fragment.CartFragment
import com.raja.dingin.view.fragment.HistoryFragment
import com.raja.dingin.view.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.menu.getItem(0).isCheckable = true
        setFragment(HomeFragment())

        navigation.setOnNavigationItemSelectedListener {menu ->

            when(menu.itemId){

                R.id.home -> {
                    setFragment(HomeFragment())
                    true
                }

                R.id.cart -> {
                    setFragment(CartFragment())
                    true
                }

                R.id.history -> {
                    setFragment(HistoryFragment())
                    true
                }

                R.id.account -> {
                    setFragment(AccountFragment())
                    true
                }

                else -> false
            }
        }

    }

    fun setFragment(fr : Fragment){
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fl_view,fr)
        frag.commit()
    }
}