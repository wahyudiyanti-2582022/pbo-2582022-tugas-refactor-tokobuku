public class EmailSender implements PengirimNotifikasi {
    @Override
    public void kirim(String alamat, String isi) {
        System.out.println("[EMAIL " + alamat + "] " + isi);
    }
}
