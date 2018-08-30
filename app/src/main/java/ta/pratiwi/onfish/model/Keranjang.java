package ta.pratiwi.onfish.model;

public class Keranjang {
    private String id_item_beli;
    private String id_keranjang;
    private String id_dagangan;
    private String id_petani;
    private String nama_petani;
    private String id_kategori_ikan;
    private String nama_ikan;
    private String link_foto;
    private String jum_kg;
    private String harga_total;
    private String berat_tersedia;

    public String getId_item_beli() {
        return id_item_beli;
    }

    public void setId_item_beli(String id_item_beli) {
        this.id_item_beli = id_item_beli;
    }

    public String getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(String id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public String getId_dagangan() {
        return id_dagangan;
    }

    public void setId_dagangan(String id_dagangan) {
        this.id_dagangan = id_dagangan;
    }

    public String getId_petani() {
        return id_petani;
    }

    public void setId_petani(String id_petani) {
        this.id_petani = id_petani;
    }

    public String getNama_petani() {
        return nama_petani;
    }

    public void setNama_petani(String nama_petani) {
        this.nama_petani = nama_petani;
    }

    public String getId_kategori_ikan() {
        return id_kategori_ikan;
    }

    public void setId_kategori_ikan(String id_kategori_ikan) {
        this.id_kategori_ikan = id_kategori_ikan;
    }

    public String getNama_ikan() {
        return nama_ikan;
    }

    public void setNama_ikan(String nama_ikan) {
        this.nama_ikan = nama_ikan;
    }

    public String getLink_foto() {
        return link_foto;
    }

    public void setLink_foto(String link_foto) {
        this.link_foto = link_foto;
    }

    public String getJum_kg() {
        return jum_kg;
    }

    public void setJum_kg(String jum_kg) {
        this.jum_kg = jum_kg;
    }

    public String getHarga_total() {
        return harga_total;
    }

    public void setHarga_total(String harga_total) {
        this.harga_total = harga_total;
    }

    public String getBerat_tersedia() {
        return berat_tersedia;
    }

    public void setBerat_tersedia(String berat_tersedia) {
        this.berat_tersedia = berat_tersedia;
    }
}
