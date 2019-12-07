package ba.unsa.etf.rpr;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Preduzece {

    private int osnovica;
    private List<RadnoMjesto> radnaMjesta = new ArrayList<>();

    public Preduzece(int osnovica) throws NeispravnaOsnovica {
        if (osnovica <= 0) throw new NeispravnaOsnovica("Neispravna osnovica " + osnovica);
        this.osnovica = osnovica;
    }

    public int dajOsnovicu() { return osnovica; }

    public void postaviOsnovicu(int osnovica) throws NeispravnaOsnovica {
        if (osnovica <= 0) throw new NeispravnaOsnovica("Neispravna osnovica " + osnovica);
        this.osnovica = osnovica;
    }

    public void novoRadnoMjesto(RadnoMjesto workplace) { radnaMjesta.add(workplace); }

    public void zaposli(Radnik radnik, String naziv) {
        List<RadnoMjesto> filtrirani = radnaMjesta.stream().filter(radnoMjesto -> radnoMjesto.getNaziv().equals(naziv) && radnoMjesto.getRadnik() == null).collect(Collectors.toList());
        // filter iz liste IZBACUJE sve one elemente koje zadovoljavaju kriterij da postoji naziv tog radnog mjesta i da ako postoji da je prazan i to ce ubaciti u kolekciju
        // drugi ishod moze biti da equals uvijek vrati false, a razlog za to samo moze biti cinjenica da NE POSTOJI radno mjesto sa tim nazivom, i u tom slucaju ce biti empty
        // a collect skuplja ono sto izbacuje filter
        if (filtrirani.isEmpty()) throw new IllegalStateException("Nijedno radno mjesto tog tipa nije slobodno");
        filtrirani.get(0).setRadnik(radnik); // i ako je sve OK, filtriraj bi trebao imati broj elemenata 0 i njega ubacimo u skup
    }

    public void obracunajPlatu() {
        radnaMjesta.stream().filter(radnoMjesto -> radnoMjesto.getRadnik() != null).forEach(radnoMjesto -> radnoMjesto.getRadnik().dodajPlatu(osnovica*radnoMjesto.getKoeficijent()));
        // za sve radnike koji su evidentirani na radnim mjestima u firmi treba izracunati platu po formuli..,
        // te dodati platu radniku pozivacjuci metodu dodajPlatu.
        // filter nam prolazi kroz svako radno mjesto pojedinacno u Listi i onda od tog radnog mjesta poziva da uzmemo radnika
        // dalje, foreach prolazi kroz to radno mjesto i za svakog radnika doda mu na platu proizvod

        // sa filter smo postavili uslov, sa forEach smo prosli kroz lisu randih mjesta, i sa get radnik.dodajplatu smo svakom radniku na tom radnom mjestu uvecali platu
    }

    double iznosPlate() {
        double suma = 0;
        for (RadnoMjesto radnoMjesto : radnaMjesta) {   // za svako radno mjesto koje ima radnika, uzme koeficijent tog radnog mjesta i pomnozi sa osnovicom
            if (radnoMjesto.getRadnik() != null) suma += radnoMjesto.getKoeficijent()*osnovica; // radno mjesto uvijek ima samo jednog radnika
        }
        return suma;
    }

    Set<Radnik> radnici() {
        Set<Radnik> radnici = new TreeSet();

        for (RadnoMjesto radnoMjesto : radnaMjesta) {
            if (radnoMjesto.getRadnik() != null) radnici.add(radnoMjesto.getRadnik());
        }
        return radnici; // treeset automatski sortira po plati
    }

    public Map<RadnoMjesto, Integer> sistematizacija() {
        Map<RadnoMjesto, Integer> result = new HashMap<>();

        for (RadnoMjesto radnoMjesto : radnaMjesta) {
            if (!result.containsKey(radnoMjesto)) result.put(radnoMjesto, 1);   // ako se prvi put javlja to radno mjesto prilikom iteriranja
            else result.put(radnoMjesto, result.get(radnoMjesto)+1);    // a za svaki sljedeci put stavimo to radno, ali povecamo broj za 1
        }
        return result;
    }


    public List<Radnik> filterRadnici(Predicate<Radnik> function) {
//        List<Radnik> radnici = new ArrayList<>();
//        for(RadnoMjesto radnoMjesto : radnaMjesta){
//            if(function.apply(radnoMjesto.getRadnik())){
//                radnici.add(radnoMjesto.getRadnik());
//            }
//        }

        return radnaMjesta.stream().filter(radnoMjesto -> radnoMjesto.getRadnik() != null).map(radnoMjesto -> radnoMjesto.getRadnik()).filter(function).collect(Collectors.toList());
        // prvo filtriramo po radnim mjestima tako sto mora postojati radnik na tom radnom mjestu
        // zatim mapiramo tj. sacuvamo sve one radnike koje filter(function) vrati da true
        // collect samo sluzi da smjesti to sto vraca u mapu
    }


    public List<Radnik> vecaProsjecnaPlata(double plata) {
        return filterRadnici(radnik -> radnik.prosjecnaPlata() > plata);
    }// jer lambda podrazumijeva da ce biti Radnik radnik
}

