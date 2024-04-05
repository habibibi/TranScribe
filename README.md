# IF3210-2024-Android-IKP

K2 - Kelompok IKP

- 13521118 Ahmad Ghulam Ilham
- 13521159 Sulthan Dzaky Alfaro
- 13521169 Muhammad Habibi Husni

## Deskripsi aplikasi

Aplikasi yang kami buat merupakan aplikasi manajemen keuangan yang dibuat untuk melakukan pengelolaan transaksi. Fitur yang ada pada aplikasi ini adalah penambahan, pengubahan, dan penghapusan transaksi, daftar transaksi lengkap, scan nota, grafik rangkuman transaksi, ekspor transaksi ke spreadsheet, serta pengiriman file transaksi melalui email. Selain itu terdapat fitur keamanan, seperti deteksi sinyal internet dan service latar belakang untuk memeriksa kevalidan token login.

## Library yang digunakan

Aplikasi diimplementasikan dengan bahasa Kotlin.
Berikut adalah library yang digunakan

    "androidx.core:core-ktx:1.12.0"
    "androidx.appcompat:appcompat:1.6.1"
    "com.google.android.material:material:1.11.0"
    "androidx.constraintlayout:constraintlayout:2.1.4"
    "androidx.navigation:navigation-ui-ktx:2.7.7"
    "androidx.navigation:navigation-fragment-ktx:2.7.7"
    "androidx.navigation:navigation-dynamic-features-fragment:2.7.7"
    "androidx.cardview:cardview:1.0.0"
    "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
    "com.github.PhilJay:MPAndroidChart:v3.1.0"
    "com.squareup.retrofit2:retrofit:2.9.0"
    "com.squareup.retrofit2:converter-moshi:2.9.0"
    "com.squareup.moshi:moshi:1.12.0"
    "com.squareup.moshi:moshi-kotlin:1.12.0"
    "androidx.annotation:annotation:1.7.1"
    "androidx.core:core-splashscreen:1.0.1"
    "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"
    "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
    "androidx.room:room-runtime:2.6.1"
    "com.google.android.gms:play-services-location:21.2.0"
    "org.apache.poi:poi:5.2.5"
    "org.apache.poi:poi-ooxml:5.2.5"
    "androidx.room:room-ktx:2.6.1"
    "androidx.fragment:fragment-ktx:1.6.2"
    "junit:junit:4.13.2"
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
    "androidx.test.ext:junit:1.1.5"
    "androidx.test.espresso:espresso-core:3.5.1"
    "androidx.navigation:navigation-testing:2.7.7"
    "androidx.room:room-compiler:2.6.1"
    "androidx.room:room-compiler:2.6.1"
    "androidx.camera:camera-camera2:1.4.0-alpha04"
    "androidx.camera:camera-view:1.4.0-alpha04"

## Screenshot aplikasi (dimasukkan dalam folder screenshot)

### Login

[](url)

### Transaksi

![Transaksi](screenshot/transaksi.jpg)

![Tambah Transaksi](screenshot/transaksi-tambah.jpg)

![Edit Transaksi](screenshot/transaksi-edit.jpg)

### Graf

![Graf](screenshot/graph.jpg)

![Graf Landscape](screenshot/graph-landscape.jpg)

### Pengaturan

![Pengaturan](screenshot/setting.jpg)

## Pembagian kerja anggota kelompok

| NIM       | Nama                          | Pekerjaan           |
|-----------|-------------------------------|---------------------|
| 13521118  | Ahmad Ghulam Ilham        |  Login, Logout, JWT Service, Graph|
| 13521159  | Sulthan Dzaky Alfaro  | CRUD Transaksi, Intent Lokasi, Broadcast Receiver, Simpan Transaksi,Intent Gmail |
| 13521169  | Muhammad Habibi Husni  | Header-Navbar,Daftar Transaksi,Scanner, Network Sensing|

## Jumlah jam persiapan dan pengerjaan untuk masing-masing anggota

- 13521118 Ahmad Ghulam Ilham
    - Persiapan : 12 jam
    - Pengerjaan : 24 jam
- 13521159 Sulthan Dzaky Alfaro
    - Persiapan : 12 jam
    - Pengerjaan : 24 jam
- 13521169 Muhammad Habibi Husni
    - Persiapan : 14 jam
    - Pengerjaan : 24 jam