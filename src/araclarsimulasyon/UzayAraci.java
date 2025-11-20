/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>
* Uzay Aracı Sınıfı, Gezegenler arası seyahat eden uzay aracını temsil eder.Durumunu, konumunu, yolcularını, rotasını ve zamanlamasını takip eder.
* </p>
*/
package araclarsimulasyon;
import java.util.ArrayList;
import java.util.List;

class UzayAraci extends UzayNesnesi {
 private String durum;
 private String cikisGezegeniAdi;
 private String varisGezegeniAdi;
 private final Zaman cikisTarihi; 
 private final int mesafe;      
 private int hedefeKalanSaat; 
 private Zaman hedefeVaracagiTarih;
 private final List<Kisi> yolcular;
 private String konumGezegenAdi;

 //Kurucu metod
 public UzayAraci(String ad, String cikisGezegeniAdi, String varisGezegeniAdi,
                    String cikisTarihiStr, int mesafe, int cikisGezegeniGunSaatSayisi) {
     super(ad);
     this.yolcular = new ArrayList<>();
     setCikisGezegeniAdi(cikisGezegeniAdi);
     setVarisGezegeniAdi(varisGezegeniAdi);
     this.cikisTarihi = new Zaman(cikisTarihiStr, cikisGezegeniGunSaatSayisi);
     this.mesafe = Math.max(mesafe, 0);
     setHedefeKalanSaat(this.mesafe);
     setDurum("Bekliyor");
     setKonumGezegenAdi(this.cikisGezegeniAdi);
     this.hedefeVaracagiTarih = null; // Kurulumda hesaplanacak
 }
 //Private Set Metodları
 private void setDurum(String durum) { if (durum != null && List.of("Bekliyor", "Yolda", "Vardı", "IMHA").contains(durum)) this.durum = durum; }
 private void setCikisGezegeniAdi(String ad) { this.cikisGezegeniAdi = (ad != null) ? ad.trim() : "?"; }
 private void setVarisGezegeniAdi(String ad) { this.varisGezegeniAdi = (ad != null) ? ad.trim() : "?"; }
 private void setHedefeKalanSaat(int saat) { this.hedefeKalanSaat = Math.max(0, saat); }
 private void setKonumGezegenAdi(String ad) { this.konumGezegenAdi = ad; }

 //Public Set Metodu hedefeVaracağıtarihi simülasyon başlangıcında ayarlamak için kullanır
 public void setHedefeVaracagiTarih(Zaman tarih) { this.hedefeVaracagiTarih = tarih; }

 //Public Get Metodları
 public String getDurum() { return durum; }
 public String getCikisGezegeniAdi() { return cikisGezegeniAdi; }
 public String getVarisGezegeniAdi() { return varisGezegeniAdi; }
 public Zaman getCikisTarihi() { return cikisTarihi; }
 public int getMesafe() { return mesafe; }
 public int getHedefeKalanSaat() { return hedefeKalanSaat; }
 public Zaman getHedefeVaracagiTarih() { return hedefeVaracagiTarih; }
 public List<Kisi> getYolcular() { return yolcular; }
 public String getKonumGezegenAdi() { return konumGezegenAdi; }
 public long getHayattakiYolcuSayisi() { return this.yolcular.stream().filter(Kisi::isHayatta).count(); }
 
 //Eğer bir aracın kalkış tarihi gezegenin başlangıç tarihinden önceyse anında imha etmek için gerekli fonksiyon
 public void anindaImhaEt() {
	 	//Durumu imha yap
	 	setDurum("IMHA");
	 	//Konumu null yap
	    setKonumGezegenAdi(null); 
	    //Tahmini varışı null yap
	    setHedefeVaracagiTarih(null); 
	    //Kalan saatı 0la
	    setHedefeKalanSaat(0);    
	}

 //Araca bir yolcu ekler.
 public void yolcuEkle(Kisi kisi) {
     if (kisi != null && this.getAd().equals(kisi.getBulunduguUzayAraciAdi())) {
          this.yolcular.add(kisi);
     }
 }

 //Aracı "Yolda" durumuna geçirir.
 public void hareketEt() {
     if (this.durum.equals("Bekliyor")) {
         setDurum("Yolda");
         setKonumGezegenAdi(null);
     }
 }
 
 //Aracın ve yolcularının durumunu 1 saatlik zaman dilimi için günceller.
 public void saatlikGuncelleme(Gezegen varisGezegeni) {
     if (this.durum.equals("IMHA")) return; // İmha ise işlem yapma
     //Yolcuların ömrü similasyon sonuna kadar azalır.
     for (Kisi kisi : this.yolcular) {
         kisi.zamanGecir(1);
     }

     //Yoldaysa ilerle ve varışı kontrol et
     if (this.durum.equals("Yolda")) {
         if (hedefeKalanSaat > 0) hedefeKalanSaat--;
         if (hedefeKalanSaat <= 0) {
             setDurum("Vardı");
             hedefeKalanSaat = 0;
             setKonumGezegenAdi(this.varisGezegeniAdi);
         }
     }

     //İmha kontrolü (Yolda veya Bekliyor iken) Eğer hedef vardıysa imha etmeye gerek yok zaten uçuşunu tamamlamış.
     if (!this.durum.equals("IMHA") && !this.durum.equals("Vardı") && getHayattakiYolcuSayisi() == 0) {
          setDurum("IMHA");
          setKonumGezegenAdi(null);
          setHedefeVaracagiTarih(null);
          setHedefeKalanSaat(0);
     }
 }
}