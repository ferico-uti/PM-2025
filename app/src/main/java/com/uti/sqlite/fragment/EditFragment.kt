package com.uti.sqlite.fragment

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.uti.apppm.databinding.FragmentEditBinding
import com.uti.sqlite.MainActivity
import com.uti.sqlite.config.Lite

class EditFragment : Fragment() {
    //    deklarasi variabel SQLite
    lateinit var lite: Lite
    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor

    //    deklarasi variabel binding
    lateinit var binding: FragmentEditBinding
    lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //    definisi variabel binding
        binding = FragmentEditBinding.inflate(layoutInflater)

        //    definisi variabel lite
        lite = Lite(requireContext())

//        panggil method "getdata"
        getData()

//        event "btnUbah"
        binding.btnUbah.setOnClickListener {
//            jika ada salah satu komponen yang tidak terisi
            if (binding.txtNpm.text.toString() == "" || binding.txtNama.text.toString() == "" || binding.txtJurusan.text.toString() == "") {
//                tampilkan pesan error
                Snackbar.make(binding.root, "Seluruh Data Harus Diisi !", Snackbar.LENGTH_SHORT)
                    .show()
            }
//            jika seluruh komponen diisi
            else {
//                cek apakah data "NPM" yang diisi sudah pernah tersimpan/belum dan cek data id
                db = lite.readableDatabase
                cursor = db.rawQuery(
                    "SELECT npm FROM tb_mahasiswa WHERE npm = '${binding.txtNpm.text}' AND id != '${id}'",
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
//                    ubah data
                    db = lite.writableDatabase
                    db.execSQL("UPDATE tb_mahasiswa SET npm = '${binding.txtNpm.text}', nama = '${binding.txtNama.text}', jurusan = '${binding.txtJurusan.text}' WHERE id = '${id}'")

//                    tampilkan pesan
                    Snackbar.make(
                        binding.root,
                        "Data Mahasiswa Berhasil Diubah",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()

                }
                cursor.close()

            }
        }

//        event "btnBatal"
        binding.btnBatal.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressedDispatcher.onBackPressed()
        }

//        event "imgBack"
        binding.imgBack.setOnClickListener {
            binding.btnBatal.performClick()
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    fun getData() {
        //   baca key bundle yang dikirim dari "Data.kt"
        id = arguments?.getString("id").toString()
        binding.txtNpm.setText(arguments?.getString("npm"))
        binding.txtNama.setText(arguments?.getString("nama"))
        binding.txtJurusan.setText(arguments?.getString("jurusan"))
    }
}