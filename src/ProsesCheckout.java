public class ProsesCheckout {
    private final StrukPrinter printer;
    private final PenyimpanRiwayat riwayat;
    private final PengirimNotifikasi notifikasi;

    public ProsesCheckout(StrukPrinter printer, PenyimpanRiwayat riwayat,
                          PengirimNotifikasi notifikasi) {
        this.printer = printer;
        this.riwayat = riwayat;
        this.notifikasi = notifikasi;
    }

    public void checkout(String pelanggan, String alamatEmail,
                         Keranjang keranjang, MetodeBayar metode) {
        int totalBelanja = keranjang.totalBelanja();
        int totalBayar = metode.hitungTotalBayar(totalBelanja);

        printer.cetak(pelanggan, keranjang, metode, totalBelanja, totalBayar);

        riwayat.simpan(pelanggan + ";" + keranjang.jumlah() + " buku;"
                + FormatRupiah.format(totalBayar));
        notifikasi.kirim(alamatEmail, "Terima kasih " + pelanggan
                + ", total bayar " + FormatRupiah.format(totalBayar));
    }
}
