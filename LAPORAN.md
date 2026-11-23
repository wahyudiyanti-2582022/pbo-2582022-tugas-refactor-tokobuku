# LAPORAN REFACTOR TOKO BUKU

Program kasir `TokoBukuKotor.java` berjalan benar, tetapi melanggar kelima prinsip SOLID. Setelah di-refactor, keluarannya identik karakter demi karakter dengan keluaran awal (sudah dibandingkan dengan `diff`), dan kode akhir tidak lagi memuat field `jenis`, `switch`/`if` pemeriksa jenis buku atau metode bayar, `instanceof`, maupun `UnsupportedOperationException`.

---

## S — Single Responsibility Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();

    int hitungOngkir(Buku b) { ... }
    int hargaAkhir(Buku b) { switch (b.jenis) { ... } }
    String keterangan(Buku b) { switch (b.jenis) { ... } }
    int totalBayar(String metode, int totalBelanja) { ... }
    void checkout(...) { /* cetak struk, hitung berat, simpan riwayat, kirim email */ }
    String formatRupiah(int angka) { ... }
}
```

**(b) Masalah**

Class `Toko` mengerjakan terlalu banyak hal sekaligus: menyimpan isi keranjang, menghitung harga dan ongkir, menghitung biaya metode bayar, mencetak struk, memformat rupiah, mencatat riwayat, dan mengirim notifikasi. Akibatnya ada banyak alasan untuk mengubah class ini: format struk berubah, aturan ongkir berubah, atau cara kirim email berubah. Semuanya menyentuh berkas yang sama dan saling berisiko merusak.

**(c) Perbaikan**

Tiap tanggung jawab dipindah ke class sendiri:

| Tanggung jawab | Class |
|---|---|
| Menampung produk, menjumlahkan total dan berat | `Keranjang` |
| Harga akhir dan keterangan tiap jenis buku | `BukuCetak`, `BukuDigital`, `BukuAudio` |
| Biaya/diskon metode bayar | `Tunai`, `Transfer`, `EWallet` |
| Mencetak struk | `StrukPrinter` |
| Memformat rupiah | `FormatRupiah` |
| Menyimpan riwayat | `FileRiwayat` |
| Mengirim notifikasi | `EmailSender` |
| Mengatur urutan alur checkout | `ProsesCheckout` |

---

## O — Open/Closed Principle

**(a) Kode awal yang melanggar**

```java
int hargaAkhir(Buku b) {
    switch (b.jenis) {
        case "CETAK":   return b.getHarga() + hitungOngkir(b);
        case "DIGITAL": return b.getHarga() * 80 / 100;
        case "AUDIO":   return b.getHarga() * 90 / 100;
        default:        return b.getHarga();
    }
}

int totalBayar(String metode, int totalBelanja) {
    if (metode.equals("TUNAI")) { ... }
    else if (metode.equals("TRANSFER")) { ... }
    else if (metode.equals("EWALLET")) { ... }
    return totalBelanja;
}
```

**(b) Masalah**

Setiap jenis buku baru atau metode bayar baru memaksa kita membuka dan mengedit `Toko`: menambah `case` di `hargaAkhir`, di `keterangan`, di `namaMetode`, di `totalBayar`, dan seterusnya. Pengubahan di banyak tempat seperti ini mudah terlewat satu, dan setiap edit berisiko merusak jenis lain yang sudah benar. Kode seharusnya terbuka untuk ditambah, tertutup untuk diubah.

**(c) Perbaikan**

Perilaku yang berbeda per jenis dipindah ke dalam polimorfisme. `Produk` mendefinisikan `hargaAkhir()` dan `keterangan()`, yang diimplementasikan oleh `BukuCetak`, `BukuDigital`, dan `BukuAudio`. `MetodeBayar` mendefinisikan `getNama()` dan `hitungTotalBayar()`, yang diimplementasikan oleh `Tunai`, `Transfer`, dan `EWallet`. Menambah jenis buku (misalnya `BukuLangka`) atau metode bayar (misalnya `Kartu Kredit`) cukup dengan menulis class baru; `Keranjang`, `StrukPrinter`, dan `ProsesCheckout` tidak perlu disentuh.

---

## L — Liskov Substitution Principle

**(a) Kode awal yang melanggar**

```java
public int getBeratGram() {
    if (!jenis.equals("CETAK")) {
        throw new UnsupportedOperationException(judul + " tidak punya berat");
    }
    return beratGram;
}
public String linkUnduh() { ... throw new UnsupportedOperationException(judul + " tidak bisa diunduh"); }
public int getDurasiMenit() { ... throw new UnsupportedOperationException(judul + " bukan buku audio"); }
```

**(b) Masalah**

`Buku` berjanji lewat `Produk` bahwa semua produk punya berat, link unduh, dan durasi, padahal janji itu dilanggar saat dijalankan: memanggil `getBeratGram()` pada buku digital melempar exception. Kode yang menerima `Produk` tidak bisa memperlakukan semua subtipe secara seragam; ia harus tahu jenisnya lebih dulu (itulah sebabnya ada pemeriksaan `jenis` di mana-mana). Subtipe tidak lagi bisa menggantikan tipe induknya dengan aman.

**(c) Perbaikan**

Kontrak `Produk` dipersempit hanya ke hal yang benar-benar dimiliki semua produk: `getJudul()`, `getPenulis()`, `getHarga()`, `hargaAkhir()`, `keterangan()`, dan `masukKe()`. Tidak ada lagi method yang hanya bisa dipanggil pada sebagian subtipe, sehingga tidak ada `UnsupportedOperationException`. Setiap subclass (`BukuCetak`, `BukuDigital`, `BukuAudio`) bisa dipakai di mana pun `Produk` diharapkan tanpa kejutan.

---

## I — Interface Segregation Principle

**(a) Kode awal yang melanggar**

```java
interface Produk {
    String getJudul();
    String getPenulis();
    int getHarga();
    int getBeratGram();      // hanya masuk akal untuk buku cetak
    String linkUnduh();      // hanya masuk akal untuk buku digital & audio
    int getDurasiMenit();    // hanya masuk akal untuk buku audio
}
```

**(b) Masalah**

Interface `Produk` yang gemuk memaksa setiap implementasi menyediakan method yang tidak relevan baginya. Buku digital dipaksa punya `getBeratGram()`, buku cetak dipaksa punya `linkUnduh()`. Karena tidak mungkin diisi dengan benar, method-method itu diisi dengan `throw`. Pemakai interface juga terpaksa bergantung pada method yang tidak mereka butuhkan.

**(c) Perbaikan**

Interface dipecah menjadi peran-peran kecil:

- `DapatDikirim` (hanya `getBeratGram()`), diimplementasikan oleh `BukuCetak`.
- `DapatDiunduh` (hanya `linkUnduh()`), diimplementasikan oleh `BukuDigital` dan `BukuAudio`.
- `Produk` tinggal berisi yang umum bagi semua produk.

Durasi dan ukuran file sekarang menjadi field privat di `BukuAudio` dan `BukuDigital`, dan hanya muncul lewat `keterangan()`. `Keranjang` menyimpan daftar `DapatDikirim` dan `DapatDiunduh` secara terpisah (diisi lewat `Produk.masukKe(...)`), sehingga total berat dan daftar link unduh dihitung tanpa pemeriksaan tipe.

---

## D — Dependency Inversion Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();
    ...
    void checkout(...) {
        System.out.println("================ STRUK =================");
        ...
        riwayat.simpan(...);
        email.kirim(...);
    }
}
```

**(b) Masalah**

`Toko` (logika tingkat tinggi) membuat sendiri dan bergantung langsung pada class konkret `EmailSender` dan `FileRiwayat`, serta mencetak ke `System.out` secara langsung. Mengganti email dengan SMS, atau menyimpan riwayat ke database, berarti mengedit `Toko`. Pengujian pun sulit karena tidak bisa memasang pengganti palsu (*fake*) untuk email atau file.

**(c) Perbaikan**

Dibuat abstraksi `PenyimpanRiwayat` dan `PengirimNotifikasi` (interface), dengan implementasi `FileRiwayat` dan `EmailSender`. `ProsesCheckout` menerima `StrukPrinter`, `PenyimpanRiwayat`, dan `PengirimNotifikasi` lewat constructor, dengan tipe parameter riwayat dan notifikasi berupa interface. `ProsesCheckout` tidak pernah memanggil `new` untuk dependensinya; seluruh objek dirakit di satu tempat, yaitu `main` di `TokoBukuSolid`. Implementasi bisa diganti tanpa menyentuh `ProsesCheckout`.

---

## Bagian yang sengaja tidak diberi abstraksi (YAGNI/KISS)

`StrukPrinter` sengaja dibiarkan sebagai class konkret dan tidak dibuatkan interface `Printer`, karena belum ada kebutuhan nyata untuk format struk lain (PDF, HTML, dan sebagainya); membuat interface sekarang hanya menambah berkas tanpa manfaat, dan bisa diekstrak kapan pun kebutuhan itu benar-benar muncul. `FormatRupiah` juga cukup sebagai class utilitas dengan satu method statis: format mata uang tidak akan berganti-ganti implementasi, jadi interface dan injeksi hanya akan menambah kerumitan. Begitu pula `Keranjang`, yang tidak perlu menjadi interface karena hanya ada satu cara menyimpan item dalam keranjang. Prinsipnya: abstraksi dibuat di titik yang memang bervariasi (jenis buku, metode bayar, riwayat, notifikasi), bukan di semua tempat.
