public class BukuAudio extends Buku implements DapatDiunduh {
    private final int durasiMenit;

    public BukuAudio(String judul, String penulis, int harga, int durasiMenit) {
        super(judul, penulis, harga);
        this.durasiMenit = durasiMenit;
    }

    @Override
    public int hargaAkhir() {
        return getHarga() * 90 / 100;
    }

    @Override
    public String keterangan() {
        return durasiMenit + " menit, diskon 10%";
    }

    @Override
    public String linkUnduh() {
        return "https://toko.buku/audio/" + slug();
    }

    @Override
    public void masukKe(Keranjang keranjang) {
        keranjang.tambahProduk(this);
        keranjang.tambahUnduhan(this);
    }
}
