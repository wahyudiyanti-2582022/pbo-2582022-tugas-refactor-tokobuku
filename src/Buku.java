public abstract class Buku implements Produk {
    private final String judul;
    private final String penulis;
    private final int harga;

    protected Buku(String judul, String penulis, int harga) {
        this.judul = judul;
        this.penulis = penulis;
        this.harga = harga;
    }

    @Override
    public String getJudul() {
        return judul;
    }

    @Override
    public String getPenulis() {
        return penulis;
    }

    @Override
    public int getHarga() {
        return harga;
    }

    protected String slug() {
        return judul.toLowerCase().replace(' ', '-');
    }
}
