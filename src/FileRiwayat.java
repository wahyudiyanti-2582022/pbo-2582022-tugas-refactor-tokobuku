public class FileRiwayat implements PenyimpanRiwayat {
    @Override
    public void simpan(String baris) {
        System.out.println("[FILE riwayat.txt] " + baris);
    }
}
