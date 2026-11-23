class Toko {
    Keranjang keranjang = new Keranjang();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();

    void tambah(Produk produk) {
        keranjang.tambah(produk);
    }

    void checkout(String pelanggan, String alamatEmail, MetodeBayar metode) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        for (Produk p : keranjang.getProduk()) {
            System.out.println(p.getJudul() + " (" + p.getPenulis() + ")");
            System.out.println("  Harga       : " + FormatRupiah.format(p.getHarga()));
            System.out.println("  Keterangan  : " + p.keterangan());
            System.out.println("  Harga akhir : " + FormatRupiah.format(p.hargaAkhir()));
        }
        int total = keranjang.totalBelanja();
        int bayar = metode.hitungTotalBayar(total);
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + FormatRupiah.format(total));
        System.out.println("Metode bayar  : " + metode.getNama());
        System.out.println("Total bayar   : " + FormatRupiah.format(bayar));
        System.out.println("Berat kiriman : " + keranjang.totalBeratGram() + " gram");

        System.out.println("-------------- LINK UNDUH ---------------");
        for (DapatDiunduh d : keranjang.getUnduhan()) {
            System.out.println(d.linkUnduh());
        }
        System.out.println("-----------------------------------------");

        riwayat.simpan(pelanggan + ";" + keranjang.jumlah() + " buku;" + FormatRupiah.format(bayar));
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + FormatRupiah.format(bayar));
    }
}

public class TokoBukuKotor {
    public static void main(String[] args) {
        Toko toko = new Toko();
        toko.tambah(new BukuCetak("Pemrograman Java", "John Doe", 120000, 750));
        toko.tambah(new BukuCetak("Algoritma Dasar", "Jane Smith", 95000, 400));
        toko.tambah(new BukuDigital("Belajar OOP", "Ani Wijaya", 80000, 12.5));
        toko.tambah(new BukuAudio("Kisah Programmer", "Budi Santoso", 60000, 240));
        toko.checkout("Sari", "sari@mail.com", new EWallet());
    }
}
