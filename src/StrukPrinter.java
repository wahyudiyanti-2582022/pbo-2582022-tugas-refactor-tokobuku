public class StrukPrinter {
    public void cetak(String pelanggan, Keranjang keranjang, MetodeBayar metode,
                      int totalBelanja, int totalBayar) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        for (Produk p : keranjang.getProduk()) {
            System.out.println(p.getJudul() + " (" + p.getPenulis() + ")");
            System.out.println("  Harga       : " + FormatRupiah.format(p.getHarga()));
            System.out.println("  Keterangan  : " + p.keterangan());
            System.out.println("  Harga akhir : " + FormatRupiah.format(p.hargaAkhir()));
        }
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + FormatRupiah.format(totalBelanja));
        System.out.println("Metode bayar  : " + metode.getNama());
        System.out.println("Total bayar   : " + FormatRupiah.format(totalBayar));
        System.out.println("Berat kiriman : " + keranjang.totalBeratGram() + " gram");
        System.out.println("-------------- LINK UNDUH ---------------");
        for (DapatDiunduh d : keranjang.getUnduhan()) {
            System.out.println(d.linkUnduh());
        }
        System.out.println("-----------------------------------------");
    }
}
