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
import java.util.Calendar
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var fused : FusedLocationProviderClient

//    ----TODO Ganti Email-----
    private val emailnow = "13521159@std.stei.itb.ac.id"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        fused = LocationServices.getFusedLocationProviderClient(this)

        val databasetransac = AppDatabase.getInstance(applicationContext)

        val back = findViewById<ImageView>(R.id.backicon)

        back.setOnClickListener{
            finish()
        }
        val simpan = findViewById<Button>(R.id.tombolsimpan)
        getConnection()

        val refresh = findViewById<FloatingActionButton>(R.id.tombolrefreshlokasi)
        refresh.setOnClickListener{
            getConnection()
        }
        simpan.setOnClickListener{
            val judul = findViewById<EditText>(R.id.judul)
            val lokasi = findViewById<EditText>(R.id.lokasi)
            val nominal = findViewById<EditText>(R.id.nominal)
            val pembelian = findViewById<RadioButton>(R.id.pemasukan)
            val penjualan = findViewById<RadioButton>(R.id.pengeluaran)
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

                println(judul.text)
                println(ubahnumber)
                println(kategori)
                println(lokasi.text)
                println(tanggal)
                CoroutineScope(Dispatchers.IO).launch {
                    databasetransac.TransactionDao().insertData(
                        emailnow, judul.text.toString(),
                        kategori, ubahnumber, lokasi.text.toString(), tanggal
                    )
                    val dataambil = databasetransac.TransactionDao().getTransaction(emailnow)
                    finish()
                    Log.d("Simpan", "Datanya: $dataambil")
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
//                var test = "${it.latitude} ${it.longitude}"
                val ambiladress = Geocoder(this, Locale.getDefault())
                val ambilsatu = ambiladress.getFromLocation(it.latitude,it.longitude,1)
                val alamatasli = ambilsatu?.get(0)?.getAddressLine(0)
                lokasi.setText(alamatasli)
            }
        }

    }
}