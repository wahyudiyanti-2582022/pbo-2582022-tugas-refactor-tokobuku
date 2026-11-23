public class TokoBukuKotor {
    public static void main(String[] args) {
        Keranjang keranjang = new Keranjang();
        keranjang.tambah(new BukuCetak("Pemrograman Java", "John Doe", 120000, 750));
        keranjang.tambah(new BukuCetak("Algoritma Dasar", "Jane Smith", 95000, 400));
        keranjang.tambah(new BukuDigital("Belajar OOP", "Ani Wijaya", 80000, 12.5));
        keranjang.tambah(new BukuAudio("Kisah Programmer", "Budi Santoso", 60000, 240));

        ProsesCheckout checkout = new ProsesCheckout(
                new StrukPrinter(),
                new FileRiwayat(),
                new EmailSender());

        checkout.checkout("Sari", "sari@mail.com", keranjang, new EWallet());
    }
}
