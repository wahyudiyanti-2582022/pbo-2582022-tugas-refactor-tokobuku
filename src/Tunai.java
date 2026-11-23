public class Tunai implements MetodeBayar {
    @Override
    public String getNama() {
        return "Tunai";
    }

    @Override
    public int hitungTotalBayar(int totalBelanja) {
        return totalBelanja;
    }
}
