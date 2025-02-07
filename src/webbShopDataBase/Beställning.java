package webbShopDataBase;

import java.util.Date;

public class Beställning {
    private int id ;
    private Date datum;
    private int pris;
    private int kundid;
    private Status status ;

    public Beställning(int id, Date datum, int pris, int kundid, Status status) {
        this.id = id;
        this.datum = datum;
        this.pris = pris;
        this.kundid = kundid;
        this.status = status;
    }

    public Beställning() {

    }



    public void setId(int id) {
        this.id = id;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }

    public void setKundid(int kundid) {
        this.kundid = kundid;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Date getDatum() {
        return datum;
    }

    public int getPris() {
        return pris;
    }

    public int getKundid() {
        return kundid;
    }

    public Status getStatus() {
        return status;
    }
}
