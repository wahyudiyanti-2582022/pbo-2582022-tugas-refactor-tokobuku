public class Transfer implements MetodeBayar {
    private static final int BIAYA_ADMIN = 2500;

    @Override
    public String getNama() {
        return "Transfer (+Rp2.500)";
    }

    @Override
    public int hitungTotalBayar(int totalBelanja) {
        return totalBelanja + BIAYA_ADMIN;
    }
}
