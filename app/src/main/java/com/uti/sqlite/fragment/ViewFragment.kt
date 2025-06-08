package com.uti.sqlite.fragment


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.uti.apppm.R
import com.uti.apppm.databinding.FragmentViewBinding
import com.uti.sqlite.MainActivity
import com.uti.sqlite.adapter.Data
import com.uti.sqlite.config.Lite

class ViewFragment : Fragment() {
    //    deklarasi variabel SQLite
    lateinit var lite: Lite
    lateinit var db: SQLiteDatabase
    lateinit var cursor: Cursor

    //    deklarasi variabel binding
    lateinit var binding: FragmentViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        definisi variabel "binding"
        binding = FragmentViewBinding.inflate(layoutInflater)

//        definisi variabel lite
        lite = Lite(requireContext())

//        definisi variabel untuk list data
        val id_list = ArrayList<String>()
        val npm_list = ArrayList<String>()
        val nama_list = ArrayList<String>()
        val jurusan_list = ArrayList<String>()

        val layout = LinearLayoutManager(context)
        val adapter = Data(id_list, npm_list, nama_list, jurusan_list, parentFragmentManager)

        binding.rcvData.layoutManager = layout
        binding.rcvData.setHasFixedSize(true)
        binding.rcvData.adapter = adapter


//        tampilkan data
        db = lite.readableDatabase
        cursor = db.rawQuery(
            "SELECT * FROM tb_mahasiswa ORDER BY id",
            null
        )
//        jika data ditemukan
        if (cursor.moveToFirst()) {
            //        looping data
            for (data in 0 until cursor.count) {
                cursor.moveToPosition(data)

                id_list.add(cursor.getString(0)) // kolom 0 = id
                npm_list.add(cursor.getString(1))  // Kolom 1 = npm
                nama_list.add(cursor.getString(2)) // Kolom 2 = nama
                jurusan_list.add(cursor.getString(3)) // Kolom 3 = jurusan
            }
        }

//        event "fabAdd"
        binding.fabAdd.setOnClickListener {
//            tampilkan fragment "AddFragment"
            (requireActivity() as MainActivity).menu = "add"
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.frm_content,
                AddFragment()
            ).commit()
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}