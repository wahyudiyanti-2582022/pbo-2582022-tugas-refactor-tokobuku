import java.util.ArrayList;

// =====================================================================
// TokoBukuKotor.java — program ini BERJALAN BENAR, tapi desainnya buruk.
// Tugas Anda: refactor sampai memenuhi SOLID, dengan keluaran yang
// SAMA PERSIS seperti sebelum di-refactor.
// =====================================================================

interface Produk {
    String getJudul();
    String getPenulis();
    int getHarga();
    int getBeratGram();      // hanya masuk akal untuk buku cetak
    String linkUnduh();      // hanya masuk akal untuk buku digital & audio
    int getDurasiMenit();    // hanya masuk akal untuk buku audio
}

class Buku implements Produk {
    String jenis;            // "CETAK", "DIGITAL", atau "AUDIO"
    String judul;
    String penulis;
    int harga;
    int beratGram;
    double ukuranMB;
    int durasiMenit;

    Buku(String jenis, String judul, String penulis, int harga,
         int beratGram, double ukuranMB, int durasiMenit) {
        this.jenis = jenis;
        this.judul = judul;
        this.penulis = penulis;
        this.harga = harga;
        this.beratGram = beratGram;
        this.ukuranMB = ukuranMB;
        this.durasiMenit = durasiMenit;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public int getHarga() {
        return harga;
    }

    public int getBeratGram() {
        if (!jenis.equals("CETAK")) {
            throw new UnsupportedOperationException(judul + " tidak punya berat");
        }
        return beratGram;
    }

    public String linkUnduh() {
        String slug = judul.toLowerCase().replace(' ', '-');
        if (jenis.equals("DIGITAL")) {
            return "https://toko.buku/ebook/" + slug;
        } else if (jenis.equals("AUDIO")) {
            return "https://toko.buku/audio/" + slug;
        }
        throw new UnsupportedOperationException(judul + " tidak bisa diunduh");
    }

    public int getDurasiMenit() {
        if (!jenis.equals("AUDIO")) {
            throw new UnsupportedOperationException(judul + " bukan buku audio");
        }
        return durasiMenit;
    }
}

class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();

    void tambah(Buku buku) {
        keranjang.add(buku);
    }

    int hitungOngkir(Buku b) {
        int kelipatan = (b.getBeratGram() + 499) / 500;
        return kelipatan * 5000;
    }

    int hargaAkhir(Buku b) {
        switch (b.jenis) {
            case "CETAK":
                return b.getHarga() + hitungOngkir(b);
            case "DIGITAL":
                return b.getHarga() * 80 / 100;
            case "AUDIO":
                return b.getHarga() * 90 / 100;
            default:
                return b.getHarga();
        }
    }

    String keterangan(Buku b) {
        switch (b.jenis) {
            case "CETAK":
                return b.getBeratGram() + " gram, ongkir " + FormatRupiah.format(hitungOngkir(b));
            case "DIGITAL":
                return b.ukuranMB + " MB, diskon 20%";
            case "AUDIO":
                return b.getDurasiMenit() + " menit, diskon 10%";
            default:
                return "-";
        }
    }

    String namaMetode(String metode) {
        if (metode.equals("TUNAI")) {
            return "Tunai";
        } else if (metode.equals("TRANSFER")) {
            return "Transfer (+Rp2.500)";
        } else if (metode.equals("EWALLET")) {
            return "E-Wallet (diskon 2%)";
        }
        return "?";
    }

    int totalBayar(String metode, int totalBelanja) {
        if (metode.equals("TUNAI")) {
            return totalBelanja;
        } else if (metode.equals("TRANSFER")) {
            return totalBelanja + 2500;
        } else if (metode.equals("EWALLET")) {
            return totalBelanja * 98 / 100;
        }
        return totalBelanja;
    }

    void checkout(String pelanggan, String alamatEmail, String metode) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        int total = 0;
        for (Buku b : keranjang) {
            System.out.println(b.getJudul() + " (" + b.getPenulis() + ")");
            System.out.println("  Harga       : " + FormatRupiah.format(b.getHarga()));
            System.out.println("  Keterangan  : " + keterangan(b));
            System.out.println("  Harga akhir : " + FormatRupiah.format(hargaAkhir(b)));
            total += hargaAkhir(b);
        }
        int bayar = totalBayar(metode, total);
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + FormatRupiah.format(total));
        System.out.println("Metode bayar  : " + namaMetode(metode));
        System.out.println("Total bayar   : " + FormatRupiah.format(bayar));

        int totalBerat = 0;
        for (Buku b : keranjang) {
            if (b.jenis.equals("CETAK")) {
                totalBerat += b.getBeratGram();
            }
        }
        System.out.println("Berat kiriman : " + totalBerat + " gram");

        System.out.println("-------------- LINK UNDUH ---------------");
        for (Buku b : keranjang) {
            if (!b.jenis.equals("CETAK")) {
                System.out.println(b.linkUnduh());
            }
        }
        System.out.println("-----------------------------------------");

        riwayat.simpan(pelanggan + ";" + keranjang.size() + " buku;" + FormatRupiah.format(bayar));
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + FormatRupiah.format(bayar));
    }
}

public class TokoBukuKotor {
    public static void main(String[] args) {
        Toko toko = new Toko();
        toko.tambah(new Buku("CETAK", "Pemrograman Java", "John Doe", 120000, 750, 0, 0));
        toko.tambah(new Buku("CETAK", "Algoritma Dasar", "Jane Smith", 95000, 400, 0, 0));
        toko.tambah(new Buku("DIGITAL", "Belajar OOP", "Ani Wijaya", 80000, 0, 12.5, 0));
        toko.tambah(new Buku("AUDIO", "Kisah Programmer", "Budi Santoso", 60000, 0, 0, 240));
        toko.checkout("Sari", "sari@mail.com", "EWALLET");
    }
}
