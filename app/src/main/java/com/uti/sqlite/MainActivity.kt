package com.uti.sqlite

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uti.apppm.R
import com.uti.sqlite.config.Lite
import com.uti.sqlite.fragment.ViewFragment


class MainActivity : AppCompatActivity() {
    //    deklarasi variabel "menu"
    lateinit var menu: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//      panggil class "Lite" (file "Lite.kt")
        val lite = Lite(this)

//        akses database ()
        lite.writableDatabase

//        panggil method "openViewFragment"
        openViewFragment()

//        event tombol "back"
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (menu == "add" || menu == "edit") {
//                    panggil method "openViewFragment"
                    openViewFragment()
                } else {
//                    tutup aplikasi
                    finish()
                }
            }

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun openViewFragment() {
//        buka "ViewFragment"
        menu = "view"
        supportFragmentManager.beginTransaction().replace(R.id.frm_content, ViewFragment()).commit()
    }
}