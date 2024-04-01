package com.ikp.transcribe

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.data.dao.TransactionDao
import com.ikp.transcribe.data.table.Transaction
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: TransactionDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.TransactionDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertData() = runBlocking {
        val transaction = Transaction(
                email = "test@email",
                judul = "Ayam",
                kategori = "Pembelian",
                nominal = 10.0,
                lokasi = "Bandung",
                tanggal = "01/01/2000")
        dao.insertData(transaction.email!!,transaction.judul!!,transaction.kategori!!,transaction.nominal!!,transaction.lokasi!!,transaction.tanggal!!)
        val list = dao.getTransaction("test@email")
        assert(list.isNotEmpty())
        transaction.id = list[0].id
        assertEquals(list[0], transaction)
    }

    @Test
    fun deleteData() = runBlocking {
        val transaction = Transaction(
            email = "test@email",
            judul = "Ayam",
            kategori = "Pembelian",
            nominal = 10.0,
            lokasi = "Bandung",
            tanggal = "01/01/2000")
        dao.insertData(transaction.email!!,transaction.judul!!,transaction.kategori!!,transaction.nominal!!,transaction.lokasi!!,transaction.tanggal!!)
        var list = dao.getTransaction("test@email")
        assert(list.isNotEmpty())
        dao.deleteData(list[0].id!!)
        list = dao.getTransaction("test@email")
        assert(list.isEmpty())
    }

    @Test
    fun updateData() = runBlocking {
        val transaction = Transaction(
            email = "test@email",
            judul = "Ayam",
            kategori = "Pembelian",
            nominal = 10.0,
            lokasi = "Bandung",
            tanggal = "01/01/2000")
        dao.insertData(transaction.email!!,transaction.judul!!,transaction.kategori!!,transaction.nominal!!,transaction.lokasi!!, transaction.tanggal!!)
        var list = dao.getTransaction("test@email")
        assert(list.isNotEmpty())
        dao.updateData(list[0].id!!,
            "Bebek",
            transaction.nominal!!,
            transaction.lokasi!!)
        list = dao.getTransaction("test@email")
        assertEquals(list[0].judul, "Bebek")
    }

}