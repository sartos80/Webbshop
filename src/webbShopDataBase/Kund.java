package webbShopDataBase;

public class Kund {
    private   int id ;
    private String namn;
    private String adress;
    private String Telefonnummer;
    private String lösenord;


    public Kund(int id, String namn, String lösenord) {
        this.id = id;
        this.namn = namn;

        this.lösenord = lösenord;


    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.namn = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setTelefonnummer(String telefonnummer) {
        Telefonnummer = telefonnummer;
    }

    public void setLösenord(String lösenord) {
        this.lösenord = lösenord;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return namn;
    }

    public String getAdress() {
        return adress;
    }

    public String getTelefonnummer() {
        return Telefonnummer;
    }

    public String getLösenord() {
        return lösenord;
    }
}
