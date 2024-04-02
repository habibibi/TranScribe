package com.ikp.transcribe.ui.setting

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.ikp.transcribe.MainViewModel
import com.ikp.transcribe.R
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ikp.transcribe.auth.data.AuthService
import com.ikp.transcribe.auth.ui.LoginActivity
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
    private val mainViewModel : MainViewModel by activityViewModels()

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

        val emailnow = mainViewModel.getEmail()
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val databasetransac = context?.let { AppDatabase.getInstance(it) }
        val simpantombol = view.findViewById<Button>(R.id.simpandaftartransaksi)
        val kirimtombol = view.findViewById<Button>(R.id.kirimdaftartransaksi)
        val keluar = view.findViewById<Button>(R.id.keluar)
        val randomize = view.findViewById<Button>(R.id.randomize)

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
                        val direktori = File(requireContext().getExternalFilesDir(null),"TransCribe")
                        val timestampget = getTimestamp()
                        val namafile = "Data_Transaksi_$timestampget"
                        val filepath = File(direktori,"$namafile.xlsx").absolutePath
                        CoroutineScope(Dispatchers.IO).launch {
                            val dataambil = databasetransac?.TransactionDao()?.getTransaction(emailnow)
                            if (dataambil != null) {
                                createFileExcel(dataambil,filepath)
                            }
                            else{
                                println("Gagal")
                            }
                        }

                    } else if (which == 1) {
                        val direktori = File(requireContext().getExternalFilesDir(null),"TransCribe")
                        val timestampget = getTimestamp()
                        val namafile = "Data_Transaksi_$timestampget"
                        val filepath = File(direktori,"$namafile.xls").absolutePath
                        CoroutineScope(Dispatchers.IO).launch {
                            val dataambil = databasetransac?.TransactionDao()?.getTransaction(emailnow)
                            if (dataambil != null) {
                                createFileExcel(dataambil,filepath)
                            }
                            else{
                                println("Gagal")
                            }
                        }
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
                    val direktori = File(requireContext().getExternalFilesDir(null),"TransCribe")
                    val timestampget = getTimestamp()
                    val namafile = "Data_Transaksi_$timestampget"
                    val filepath = File(direktori,"$namafile.xlsx").absolutePath
                    CoroutineScope(Dispatchers.IO).launch {
                        val dataambil = databasetransac?.TransactionDao()?.getTransaction(emailnow)
                        if (dataambil != null) {
                            createFileExcel(dataambil,filepath)
                            val uriText = "mailto:$emailnow" +
                                    "?subject=" + Uri.encode("Data Transaksi TransCribe") +
                                    "&body=" + Uri.encode("Terlampir adalah data transaksi")

                            val uri = Uri.parse(uriText)
                            val emailIntent = Intent(Intent.ACTION_SENDTO,uri).apply {
                                putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$filepath"))
                            }
                            context?.startActivity(emailIntent)
                        }
                        else{
                            println("Gagal")
                        }
                    }
                }
                else if(which==1){
                    val direktori = File(requireContext().getExternalFilesDir(null),"TransCribe")
                    val timestampget = getTimestamp()
                    val namafile = "Data_Transaksi_$timestampget"
                    val filepath = File(direktori,"$namafile.xls").absolutePath
                    CoroutineScope(Dispatchers.IO).launch {
                        val dataambil = databasetransac?.TransactionDao()?.getTransaction(emailnow)
                        if (dataambil != null) {
                            createFileExcel(dataambil,filepath)
                            val uriText = "mailto:$emailnow" +
                                    "?subject=" + Uri.encode("Data Transaksi TransCribe") +
                                    "&body=" + Uri.encode("Terlampir adalah data transaksi")

                            val uri = Uri.parse(uriText)
                            val emailIntent = Intent(Intent.ACTION_SENDTO,uri).apply {
                                putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$filepath"))
                            }
                            context?.startActivity(emailIntent)
                        }
                        else{
                            println("Gagal")
                        }
                    }
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

        randomize.setOnClickListener {
            val intentsend = Intent().setAction("com.ikp.broadcastSendMessage")
            context?.sendBroadcast(intentsend)
        }

        return view
    }
    private fun createFileExcel(transactions: List<Transaction>, filePath: String){
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Transactions")

        // Styling for the header row
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
        }

        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).apply {
            setCellValue("ID")
            cellStyle = headerStyle
        }
        headerRow.createCell(1).apply {
            setCellValue("Email")
            cellStyle = headerStyle
        }
        headerRow.createCell(2).apply {
            setCellValue("Judul")
            cellStyle = headerStyle
        }
        headerRow.createCell(3).apply {
            setCellValue("Kategori")
            cellStyle = headerStyle
        }
        headerRow.createCell(4).apply {
            setCellValue("Nominal")
            cellStyle = headerStyle
        }
        headerRow.createCell(5).apply {
            setCellValue("Tanggal")
            cellStyle = headerStyle
        }
        headerRow.createCell(6).apply {
            setCellValue("Lokasi")
            cellStyle = headerStyle
        }

        // Styling for data rows
        val dataStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.LEFT
        }

        // Populate data rows
        var rowNum = 1
        for (transaction in transactions) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).apply {
                setCellValue(transaction.id?.toDouble() ?: 0.0)
                cellStyle = dataStyle
            }
            row.createCell(1).apply {
                setCellValue(transaction.email ?: "")
                cellStyle = dataStyle
            }
            row.createCell(2).apply {
                setCellValue(transaction.judul ?: "")
                cellStyle = dataStyle
            }
            row.createCell(3).apply {
                setCellValue(transaction.kategori ?: "")
                cellStyle = dataStyle
            }
            row.createCell(4).apply {
                setCellValue(transaction.nominal?.toDouble() ?: 0.0)
                cellStyle = dataStyle
            }
            row.createCell(5).apply {
                setCellValue(transaction.tanggal ?: "")
                cellStyle = dataStyle
            }
            row.createCell(6).apply {
                setCellValue(transaction.lokasi ?: "")
                cellStyle = dataStyle
            }
        }

        // Write to file
        val fileOut = FileOutputStream(filePath)
        workbook.write(fileOut)
        fileOut.close()
        workbook.close()
    }
    private fun getTimestamp(): String {
        val timeStampFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        return timeStampFormat.format(currentTime)
    }


    private fun createFolder(){
        val folder = File(requireContext().getExternalFilesDir(null),"TransCribe")
        if(!folder.exists()){
            val created = folder.mkdir()
            if(created) {
                println("Berhasil")
            }
            else{
                println("Gagal")
            }
        }
        else{
            println("Dah ada")
        }
    }

    private fun logout() {
        val authServiceIntent = Intent(requireContext(), AuthService::class.java)
        requireContext().stopService(authServiceIntent)

        val sharedPreferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("email").remove("token").apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        val msg = getString(R.string.logout)
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
        requireActivity().finish()
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