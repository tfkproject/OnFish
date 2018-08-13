package ta.pratiwi.onfish.model;

public class Dagangan {
    private String id_dagangan;
    private String id_petani;
    private String nama_petani;
    private String id_kategori_ikan;
    private String nama_ikan;
    private String link_foto;
    private String harga_per_kg;
    private String deskripsi;
    private String nohp;

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

    public String getHarga_per_kg() {
        return harga_per_kg;
    }

    public void setHarga_per_kg(String harga_per_kg) {
        this.harga_per_kg = harga_per_kg;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
