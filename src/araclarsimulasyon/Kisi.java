/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 22.04.2025
* <p>
* Kisi Sınıfı, Uzaydaki kişileri temsil eder. yaşam süresi, yaşı ve bulunduğu uzay aracını belirtir
* </p>
*/
package araclarsimulasyon;

class Kisi {
 private String isim;
 private int yas;
 private int kalanOmur;
 private String bulunduguUzayAraciAdi;
 private boolean hayatta;
//Kurucu metod
 public Kisi(String isim, int yas, int kalanOmur, String bulunduguUzayAraciAdi) {
     setIsim(isim);
     setYas(yas);
     setKalanOmur(kalanOmur);
     setBulunduguUzayAraciAdi(bulunduguUzayAraciAdi);
     this.hayatta = (this.kalanOmur > 0); // Kalan ömre göre hayatta durumu belirlenir
 }
 //Private Set Metodları
 private void setIsim(String isim) { this.isim = (isim != null && !isim.trim().isEmpty()) ? isim.trim() : "Isimsiz"; }
 private void setYas(int yas) { this.yas = Math.max(yas, 0); } // Negatif yaş olmasın kontrolü
 private void setKalanOmur(int kalanOmur) { this.kalanOmur = Math.max(kalanOmur, 0); } // Negatif ömür olmasın kontrolü
 private void setBulunduguUzayAraciAdi(String bulunduguUzayAraciAdi) { this.bulunduguUzayAraciAdi = (bulunduguUzayAraciAdi != null) ? bulunduguUzayAraciAdi.trim() : "Yok"; }

 //Public Get Metodları
 public String getIsim() { return isim; }
 public int getYas() { return yas; }
 public int getKalanOmur() { return kalanOmur; }
 public String getBulunduguUzayAraciAdi() { return bulunduguUzayAraciAdi; }
 public boolean isHayatta() { return hayatta; }
 
 //Kişinin kalan ömrünü azaltır ve gerekirse 'hayatta' durumunu günceller.
 public void zamanGecir(int saat) {
     if (hayatta && saat > 0) {
         kalanOmur -= saat;
         if (kalanOmur <= 0) {
             kalanOmur = 0;
             hayatta = false; // Ömrü bittiğinde durumu güncelle
         }
     }
 }
}