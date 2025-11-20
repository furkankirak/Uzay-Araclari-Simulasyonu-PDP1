/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>
* Gezegen Sınıfı, Simülasyondaki bir gezegeni temsil eder. UzayNesnesi'nden türer.Kendi zamanını ve gün uzunluğunu tutar.
* </p>
*/
package araclarsimulasyon;

class Gezegen extends UzayNesnesi {
 private Zaman tarih;
 private final int gunSaatSayisi;

 public Gezegen(String ad, int gunSaatSayisi, String tarihStr) {
     super(ad);
     this.gunSaatSayisi = Math.max(gunSaatSayisi, 1);
     this.tarih = new Zaman(tarihStr, this.gunSaatSayisi);
 }
 
 //Public Get Metodları
 public Zaman getTarih() { return tarih; }
 public int getGunSaatSayisi() { return gunSaatSayisi; }
 
 // Gezegenin zamanını 1 saat ilerletir. 
 public void saatlikGuncelleme() {
     tarih.saatEkle(1);
 }

}
