package com.uti.sqlite.fragment

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.uti.apppm.databinding.FragmentAddBinding
import com.uti.sqlite.MainActivity
import com.uti.sqlite.config.Lite

class AddFragment : Fragment() {
    //    deklarasi variabel SQLite
    lateinit var lite: Lite
    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor

    //    deklarasi variabel binding
    lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//    definisi variabel binding
        binding = FragmentAddBinding.inflate(layoutInflater)

//    definisi variabel lite
        lite = Lite(requireContext())

//    panggil method "setClearComponents"
        setClearComponents()

//      event "btnSimpan"
        binding.btnSimpan.setOnClickListener {
//            jika ada salah satu komponen yang tidak terisi
            if (binding.txtNpm.text.toString() == "" || binding.txtNama.text.toString() == "" || binding.txtJurusan.text.toString() == "") {
//                tampilkan pesan error
                Snackbar.make(binding.root, "Seluruh Data Harus Diisi !", Snackbar.LENGTH_SHORT)
                    .show()
            }
//            jika seluruh komponen diisi
            else {
//                cek apakah data "NPM" yang diisi sudah pernah tersimpan/belum
                db = lite.readableDatabase
                cursor = db.rawQuery(
                    "SELECT npm FROM tb_mahasiswa WHERE npm = '${binding.txtNpm.text}'",
                    null
                )
//                jika "NPM" ditemukan
                if (cursor.moveToFirst()) {
//                    tampilkan pesan error
                    Snackbar.make(
                        binding.root,
                        "NPM Sudah Pernah Tersimpan !",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
//                jika "NPM" tidak ditemukan
                else {
//                    simpan data
                    db = lite.writableDatabase
                    db.execSQL("INSERT INTO tb_mahasiswa VALUES (null,'${binding.txtNpm.text}','${binding.txtNama.text}','${binding.txtJurusan.text}')")

//                    tampilkan pesan
                    Snackbar.make(
                        binding.root,
                        "Data Mahasiswa Berhasil Disimpan",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()

//                    panggil method "setClearComponents"
                    setClearComponents()

                }
                cursor.close()
            }
        }

//        event "btnBatal"
        binding.btnBatal.setOnClickListener {
//            panggil method "setClearComponents"
            setClearComponents()
        }

//        event "imgBack"
        binding.imgBack.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressedDispatcher.onBackPressed()
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    //    method untuk clear component
    fun setClearComponents() {
        binding.txtNpm.setText("")
        binding.txtNama.setText("")
        binding.txtJurusan.setText("")
        binding.txtNpm.requestFocus()
    }
}