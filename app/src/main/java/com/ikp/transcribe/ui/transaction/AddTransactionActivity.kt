package com.ikp.transcribe.ui.transaction

import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ikp.transcribe.R
import com.ikp.transcribe.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var fused : FusedLocationProviderClient

//    ----TODO Ganti Email-----
    private val emailnow = "test@gmail.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        fused = LocationServices.getFusedLocationProviderClient(this)
        val judul = findViewById<EditText>(R.id.judul)
        val lokasi = findViewById<EditText>(R.id.lokasi)
        val nominal = findViewById<EditText>(R.id.nominal)
        val pembelian = findViewById<RadioButton>(R.id.pemasukan)
        val penjualan = findViewById<RadioButton>(R.id.pengeluaran)
        val radiogrupkategori = findViewById<RadioGroup>(R.id.radiogrupkategori)
        val buatkategoriedit = findViewById<TextView>(R.id.buateditkategori)
        val tombolhapus = findViewById<Button>(R.id.tombolhapus)

        val databasetransac = AppDatabase.getInstance(applicationContext)


        val extraid = intent.extras
        if(extraid!=null){
            val id = extraid.getInt("id",0)
            println(id)
            CoroutineScope(Dispatchers.IO).launch {
                val user = databasetransac.TransactionDao().getTransacID(id)
                println(user)
                withContext(Dispatchers.Main){
                    judul.setText(user.judul)
                    lokasi.setText(user.lokasi)
                    nominal.setText(user.nominal.toString())
                    radiogrupkategori.visibility = RadioGroup.GONE
                    buatkategoriedit.visibility = TextView.VISIBLE
                    tombolhapus.visibility = Button.VISIBLE
                    buatkategoriedit.text = user.kategori
                }
            }
        }
        else{
            getConnection()
        }

        val back = findViewById<ImageView>(R.id.backicon)

        back.setOnClickListener{
            finish()
        }
        val simpan = findViewById<Button>(R.id.tombolsimpan)

        tombolhapus.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val idubah  = extraid?.getInt("id",0)
                if (idubah != null) {
                    databasetransac.TransactionDao().deleteData(idubah)
                }
                finish()
            }
        }
        val refresh = findViewById<FloatingActionButton>(R.id.tombolrefreshlokasi)
        refresh.setOnClickListener{
            getConnection()
        }
        simpan.setOnClickListener{
            if(extraid!=null){
                if(judul.text.isEmpty() || nominal.text.isEmpty() || lokasi.text.isEmpty()){
                    Toast.makeText(applicationContext,"Silahkan isi semua data",Toast.LENGTH_SHORT).show()
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        val ubahnumber = nominal.text.toString().toInt()
                        val idubah  = extraid.getInt("id",0)
                        databasetransac.TransactionDao().updateData(idubah,judul.text.toString(),ubahnumber,lokasi.text.toString())
                        finish()
                    }
                }
            }
            else{
                var kategori = "kosong"
                if(pembelian.isChecked){
                    kategori = "Pemasukan"
                }
                else if(penjualan.isChecked){
                    kategori = "Pengeluaran"
                }
                if(kategori==="kosong" || judul.text.isEmpty() || nominal.text.isEmpty() || lokasi.text.isEmpty()){
                    Toast.makeText(applicationContext,"Silahkan isi semua data",Toast.LENGTH_SHORT).show()
                }
                else{
                    val ubahnumber = nominal.text.toString().toInt()
                    val calender = Calendar.getInstance()
                    val year = calender.get(Calendar.YEAR)
                    val month = calender.get(Calendar.MONTH) + 1
                    val day = calender.get(Calendar.DAY_OF_MONTH)
                    val tanggal = "$day/$month/$year"
                    CoroutineScope(Dispatchers.IO).launch {
                        databasetransac.TransactionDao().insertData(
                            emailnow, judul.text.toString(),
                            kategori, ubahnumber, lokasi.text.toString(), tanggal
                        )
                        finish()
                    }
                }
            }
        }
    }
    private fun getConnection(){
        val task = fused.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
        }
        task.addOnSuccessListener{
            if(it!=null){
                val lokasi = findViewById<EditText>(R.id.lokasi)
                val ambiladress = Geocoder(this, Locale.getDefault())
                val ambilsatu = ambiladress.getFromLocation(it.latitude,it.longitude,1)
                val alamatasli = ambilsatu?.get(0)?.getAddressLine(0)
                lokasi.setText(alamatasli)
            }
        }

    }
}