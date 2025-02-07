package webbShopDataBase;

public class Beställning_Skor {
    private int antal;
    private int beställningid;
    private   int skorid ;

    public Beställning_Skor(int antal, int beställningid, int skorid) {
        this.antal = antal;
        this.beställningid = beställningid;
        this.skorid = skorid;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public void setBeställningid(int beställningid) {
        this.beställningid = beställningid;
    }

    public void setSkorid(int skorid) {
        this.skorid = skorid;
    }

    public int getAntal() {
        return antal;
    }

    public int getBeställningid() {
        return beställningid;
    }

    public int getSkorid() {
        return skorid;
    }
}
