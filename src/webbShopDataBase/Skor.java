package webbShopDataBase;

public class Skor {
    private int id;
    private int storlek ;
    private String färg;
    private int pris;
    private String märke;
    private  int skor_antal;

    public Skor(int id, int storlek, String färg,  String märke, int skor_antal) {
        this.id = id;
        this.storlek = storlek;
        this.färg = färg;

        this.märke = märke;
        this.skor_antal = skor_antal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStorlek(int storlek) {
        this.storlek = storlek;
    }

    public void setFärg(String färg) {
        this.färg = färg;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }

    public void setMärke(String märke) {
        this.märke = märke;
    }

    public void setSkor_antal(int skor_antal) {
        this.skor_antal = skor_antal;
    }

    public int getId() {
        return id;
    }

    public int getStorlek() {
        return storlek;
    }

    public String getFärg() {
        return färg;
    }

    public int getPris() {
        return pris;
    }

    public String getMärke() {
        return märke;
    }

    public int getSkor_antal() {
        return skor_antal;
    }
}
