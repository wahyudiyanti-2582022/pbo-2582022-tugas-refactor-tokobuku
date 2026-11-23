import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Keranjang {
    private final List<Produk> semua = new ArrayList<>();
    private final List<DapatDikirim> kiriman = new ArrayList<>();
    private final List<DapatDiunduh> unduhan = new ArrayList<>();

    public void tambah(Produk produk) {
        produk.masukKe(this);
    }

    // Dipanggil oleh Produk.masukKe(...)
    public void tambahProduk(Produk produk) {
        semua.add(produk);
    }

    public void tambahKiriman(DapatDikirim item) {
        kiriman.add(item);
    }

    public void tambahUnduhan(DapatDiunduh item) {
        unduhan.add(item);
    }

    public List<Produk> getProduk() {
        return Collections.unmodifiableList(semua);
    }

    public List<DapatDiunduh> getUnduhan() {
        return Collections.unmodifiableList(unduhan);
    }

    public int jumlah() {
        return semua.size();
    }

    public int totalBelanja() {
        int total = 0;
        for (Produk p : semua) {
            total += p.hargaAkhir();
        }
        return total;
    }

    public int totalBeratGram() {
        int total = 0;
        for (DapatDikirim d : kiriman) {
            total += d.getBeratGram();
        }
        return total;
    }
}
