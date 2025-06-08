package com.uti.sqlite.adapter

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.uti.apppm.R
import com.uti.sqlite.MainActivity
import com.uti.sqlite.config.Lite
import com.uti.sqlite.fragment.EditFragment

class Data(
    val id_list: ArrayList<String>,
    val npm_list: ArrayList<String>,
    val nama_list: ArrayList<String>,
    val jurusan_list: ArrayList<String>,
    val fragmentManager: FragmentManager
) : RecyclerView.Adapter<Data.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //    definisikan variabel komponen dari file "layout_data.xml"
        val npm: TextView = itemView.findViewById(R.id.txt_npm)
        val nama: TextView = itemView.findViewById(R.id.txt_nama)
        val jurusan: TextView = itemView.findViewById(R.id.txt_jurusan)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val edit: ImageView = itemView.findViewById(R.id.img_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // isi setiap komponen berdasarkan data
        holder.npm.text = npm_list[position]
        holder.nama.text = nama_list[position]
        holder.jurusan.text = jurusan_list[position]

        // definisi SQLite
        val lite = Lite(holder.itemView.context)

        // event untuk "imgHapus" setiap baris
        holder.delete.setOnClickListener {
//            buat dialog konfirmasi
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Informasi")
                .setMessage("Data Mahasiswa: \"${holder.nama.text}\"\nIngin Dihapus ?")
                .setPositiveButton("Ya") { dialog, _ ->
                    //            cek apakah "NPM" ada/tidak
                    val db = lite.readableDatabase
                    val cursor = db.rawQuery(
                        "SELECT npm FROM tb_mahasiswa WHERE npm = ?",
                        arrayOf(holder.npm.text.toString())
                    )

//            jika "NPM" ditemukan
                    if (cursor.moveToFirst()) {
                        cursor.close()

                        // hapus dari database
                        val db = lite.writableDatabase
                        db.execSQL(
                            "DELETE FROM tb_mahasiswa WHERE npm = ?",
                            arrayOf(holder.npm.text.toString())
                        )

                        // tampilkan pesan
                        Snackbar.make(
                            holder.itemView,
                            "Data Mahasiswa: \"${holder.nama.text}\"\nBerhasil Dihapus",
                            Snackbar.LENGTH_SHORT
                        ).show()

                        // hapus dari list dan update UI
                        if (position < npm_list.size) {
                            npm_list.removeAt(position)
                            nama_list.removeAt(position)
                            jurusan_list.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, npm_list.size)
                        }
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

//        event untuk "imgEdit" setiap baris
        holder.edit.setOnClickListener {
//            cek apakah "NPM" ada/tidak
            val db = lite.readableDatabase
            val cursor = db.rawQuery(
                "SELECT npm FROM tb_mahasiswa WHERE npm = ?", arrayOf(holder.npm.text.toString())
            )
//            jika "NPM" ditemukan
            if (cursor.moveToFirst()) {
                val editFragment = EditFragment()
//            definisi variabel "Bundle"
                val bundle = Bundle()
//                kirim key "bundle" ke FragmentEdit
                bundle.putString("id", id_list[position])
                bundle.putString("npm", holder.npm.text.toString())
                bundle.putString("nama", holder.nama.text.toString())
                bundle.putString("jurusan", holder.jurusan.text.toString())

                editFragment.arguments = bundle

//                buka FragmentEdit
                (holder.itemView.context as MainActivity).menu = "edit"
                fragmentManager.beginTransaction()
                    .replace(R.id.frm_content, editFragment)
                    .commit()
            }

//            jika "NPM" tidak ditemukan
            else {
                cursor.close()
                Snackbar.make(
                    holder.itemView, "Data Mahasiswa Tidak Ditemukan !", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun getItemCount(): Int {
//        hitung jumlah data
        return npm_list.size
    }
}
