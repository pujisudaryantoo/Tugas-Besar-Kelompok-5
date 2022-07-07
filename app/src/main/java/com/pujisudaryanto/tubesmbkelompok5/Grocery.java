package com.pujisudaryanto.tubesmbkelompok5;

public class Grocery {
    private String id_pengeluaran, title, fee, tanggal;

    public Grocery(String id_pengeluaran, String title, String fee, String tanggal) {
        this.id_pengeluaran = id_pengeluaran;
        this.title = title;
        this.fee = fee;
        this.tanggal = tanggal;
    }

    public String getId_pengeluaran() {
        return id_pengeluaran;
    }

    public void setId_pengeluaran(String id_pengeluaran) {
        this.id_pengeluaran = id_pengeluaran;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}