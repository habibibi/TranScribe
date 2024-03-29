package com.ikp.transcribe.ui.setting

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ikp.transcribe.MainActivity
import com.ikp.transcribe.R
import com.ikp.transcribe.auth.data.CryptoConstants
import com.ikp.transcribe.auth.data.CryptoUtils
import com.ikp.transcribe.auth.ui.LoginActivity
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val simpantombol = view.findViewById<Button>(R.id.simpandaftartransaksi)
        val kirimtombol = view.findViewById<Button>(R.id.kirimdaftartransaksi)
        val keluar = view.findViewById<Button>(R.id.keluar)

        simpantombol.setOnClickListener {
            if(context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),101)
            }
            else {
                createFolder()
                var dialog = AlertDialog.Builder(context)
                dialog.setTitle("Pilih tipe data")
                dialog.setItems(R.array.tipe, DialogInterface.OnClickListener { dialog, which ->
                    if (which == 0) {
//                    Todo simpan time xlsx
                        println("simpan xlsx")
                    } else if (which == 1) {
//                    Todo simpan time xlsx
                        println("simpan xls")
                    } else {
                        dialog.dismiss()
                    }

                })
                val dialogtampil = dialog.create()
                dialogtampil.show()
            }
        }

        kirimtombol.setOnClickListener {
            var dialog = AlertDialog.Builder(context)
            dialog.setTitle("Pilih tipe data")
            dialog.setItems(R.array.tipe,DialogInterface.OnClickListener{
                    dialog, which ->
                if(which==0){
//                    Todo simpan time xlsx
                    println("kirim xlsx")
                }
                else if(which==1){
//                    Todo simpan time xlsx
                    println("kirim xls")
                }
                else{
                    dialog.dismiss()
                }

            })
            val dialogtampil = dialog.create()
            dialogtampil.show()
        }

        keluar.setOnClickListener {
            logout()
        }

        return view
    }

    private fun createFileXLS(){
        var workbook = HSSFWorkbook()
        var sheet = workbook.createSheet("Transaction")




    }
    private fun createFileXLSX(){




    }

    private fun createFolder(){
        val folder = File(requireContext().getExternalFilesDir(null),"TransCribe")
        if(!folder.exists()){
            val created = folder.mkdir()
            if(created) {
                Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(context, "Dah ada",Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("email").remove("token").apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()

        Toast.makeText(
            requireContext(),
            getString(R.string.logout),
            Toast.LENGTH_LONG
        ).show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}