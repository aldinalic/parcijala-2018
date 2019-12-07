package ba.unsa.etf.rpr;

import java.util.Arrays;

public class Radnik implements Comparable{
    private String ime_prezime, jmbg;
    private double[] plate = new double[1000];
    private int br_plata;

    public Radnik(String ime_prezime, String jmbg) {
        this.ime_prezime = ime_prezime;
        this.jmbg = jmbg;
        br_plata = 0;
    }

    public String getImePrezime() {
        return ime_prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setImePrezime(String ime_prezime) {
        this.ime_prezime = ime_prezime;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public void dodajPlatu(double plata) {
        if (br_plata == 1000) throw new IllegalArgumentException("Ne mozete registrovati vise od 1000 plata za radnika " + ime_prezime);
        plate[br_plata] = plata;
        br_plata++;
    }

    public double prosjecnaPlata() {
        double suma = 0;
        for(double plata : plate){
            suma+= plata;
        }
        return  br_plata != 0 ? suma / br_plata : 0;
    }

    @Override   // ja definisem kako ce porediti dva radnika
    public int compareTo(Object o) {
        if(o instanceof Radnik){
            Radnik radnik = (Radnik) o;
            int compare = Double.compare(prosjecnaPlata(),radnik.prosjecnaPlata());
            if(compare == 0) return ime_prezime.compareTo(radnik.ime_prezime);
            return -compare;
        }
        return 0;
    }
}

