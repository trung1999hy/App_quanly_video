package com.example.valut.ui.base


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding(layoutInflater)
        setContentView(binding.root)
        init()
    }

    abstract fun getLayout(): Int
    abstract fun getViewBinding(inflater: LayoutInflater): VB
    abstract fun init()

    protected fun addFragment(
        toFragment: Fragment? = null,
        fromFragment: Fragment,
        addBackstack: Boolean = false
    ) {
        if (toFragment != null) {
            supportFragmentManager.beginTransaction()


                .add(android.R.id.content, fromFragment).addToBackStack(fromFragment.tag)
                .hide(toFragment).commit()
        } else {
            supportFragmentManager.beginTransaction().apply {
                add(android.R.id.content, fromFragment)
                if (addBackstack) addToBackStack(fromFragment.tag)

            }.commit()
        }


    }


}