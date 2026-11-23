public class BukuCetak extends Buku implements DapatDikirim {
    private static final int ONGKIR_PER_500_GRAM = 5000;

    private final int beratGram;

    public BukuCetak(String judul, String penulis, int harga, int beratGram) {
        super(judul, penulis, harga);
        this.beratGram = beratGram;
    }

    @Override
    public int getBeratGram() {
        return beratGram;
    }

    private int hitungOngkir() {
        int kelipatan = (beratGram + 499) / 500;
        return kelipatan * ONGKIR_PER_500_GRAM;
    }

    @Override
    public int hargaAkhir() {
        return getHarga() + hitungOngkir();
    }

    @Override
    public String keterangan() {
        return beratGram + " gram, ongkir " + FormatRupiah.format(hitungOngkir());
    }

    @Override
    public void masukKe(Keranjang keranjang) {
        keranjang.tambahProduk(this);
        keranjang.tambahKiriman(this);
    }
}
