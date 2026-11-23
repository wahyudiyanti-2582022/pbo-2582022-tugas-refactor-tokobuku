public interface Produk {
    String getJudul();

    String getPenulis();

    int getHarga();

    int hargaAkhir();

    String keterangan();

    // Double dispatch: produk sendiri yang tahu "peran" apa saja yang dimilikinya,
    // sehingga Keranjang tidak perlu memeriksa tipe konkret.
    void masukKe(Keranjang keranjang);
}
