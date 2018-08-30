package ta.pratiwi.onfish.model;

public class Dagangan {
    private String id_dagangan;
    private String id_petani;
    private String nama_penjual;
    private String id_kategori_ikan;
    private String nama_ikan;
    private String link_foto;
    private String berat_tersedia;
    private String harga_per_kg;
    private String deskripsi;
    private String nohp;
    private String lat;
    private String lon;

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

    public String getNama_penjual() {
        return nama_penjual;
    }

    public void setNama_penjual(String nama_penjual) {
        this.nama_penjual = nama_penjual;
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

    public String getBerat_tersedia() {
        return berat_tersedia;
    }

    public void setBerat_tersedia(String berat_tersedia) {
        this.berat_tersedia = berat_tersedia;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
