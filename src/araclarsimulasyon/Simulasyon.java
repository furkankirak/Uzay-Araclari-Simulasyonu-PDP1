/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>
* Simulasyon Sınıfı, Simülasyon Süreçlerinin bulunduğu sınıf tüm süreç burda yönetilir.Uzay araçlarının gezegenlerin ve kişilerin durumlarını gösteren ve ekrana yazdıran sınıf.
* </p>
*/
package araclarsimulasyon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Simulasyon {
    private List<Gezegen> gezegenler;
    private List<UzayAraci> araclar;
    private boolean simulasyonBitti = false;
    private int saatAdimi = 0;
    //Kurucu metod
    public Simulasyon() {
        this.gezegenler = new ArrayList<>();
        this.araclar = new ArrayList<>();
    }
    //Normal baslatma modu
    public void baslat() throws IOException {
        if (!kurulumYap()) return;
        calistirSimulasyonDongusu(false);
    }
    //Hızlı mod debug işlemleri ve süreci atlayip direkt sonuçları gösteren mod
    public void hizliBaslat() throws IOException {
        if (!kurulumYap()) return;
        calistirSimulasyonDongusu(true);
    }

    //Kurulumu yapar ve başarılı olup olmadığını döndürür
    private boolean kurulumYap() throws IOException {
        try {
            gezegenler = DosyaOkuma.gezegenleriOku("Gezegenler.txt");
            if (gezegenler.isEmpty()) { System.err.println("Kurulum Hatası: Hiç gezegen okunamadı."); return false; }
            //System.out.println("Gezegenler okundu: " + gezegenler.size() + " adet.");

            List<Kisi> tumKisiler = DosyaOkuma.kisileriOku("Kisiler.txt");
            //System.out.println("Kişiler okundu: " + tumKisiler.size() + " adet.");
            
            araclar = DosyaOkuma.araclariOku("Araclar.txt", gezegenler);
            if (araclar.isEmpty()) { System.err.println("Uyarı: Hiç araç okunamadı."); }
            else { 
            	//System.out.println("Araçlar okundu: " + araclar.size() + " adet."); 
            	}
            
            //Yolculari araclara dağıtır.
            yolculariAraclaraDagit(tumKisiler);
            //System.out.println("Yolcular araçlara dağıtıldı.");
            
            //Hedefe varış tarihlerini simülasyon başlangıcında hesaplar
            hedefeVaracagiTarihiHesapla();

            return true;

        } catch (IOException e) {
            System.err.println("Kurulum sırasında dosya okuma hatası: " + e.getMessage());
            throw e;
        } catch (Exception e) {
             System.err.println("Kurulum sırasında beklenmedik hata: " + e.getMessage());
             e.printStackTrace(); 
             return false;
        }
    }
    //Hedefe varış tarihlerini simülasyon başlangıcında hesaplamak için geliştirilen fonksiyon
    private void hedefeVaracagiTarihiHesapla() {
        if (araclar.isEmpty()) {
             System.out.println("Hesaplanacak araç yok.");
             return;
        }

        for (UzayAraci arac : araclar) {
            Gezegen cikisGezegeni = gezegenBul(arac.getCikisGezegeniAdi());
            Gezegen varisGezegeni = gezegenBul(arac.getVarisGezegeniAdi());

            //Gerekli gezegen bilgileri kontrolü
            if (cikisGezegeni != null && varisGezegeni != null) {
                Zaman cikisGezegeniBaslangicZamani = cikisGezegeni.getTarih();
                Zaman aracKalkisZamani = arac.getCikisTarihi();

                //Kalkış zamanı, gezegenin başlangıç zamanından önceyse imha et
                if (aracKalkisZamani.compareTo(cikisGezegeniBaslangicZamani) < 0) {
                    // Evet, kalkış zamanı geçmişte kalmış!
                    System.err.printf("!!! HATA: Araç [%s] kalkış zamanı (%s) kalkış gezegeni [%s] başlangıç zamanından (%s) önce! Araç imha ediliyor.%n",
                                      arac.getAd(),
                                      aracKalkisZamani.tarihToString(),
                                      cikisGezegeni.getAd(),
                                      cikisGezegeniBaslangicZamani.tarihToString());
                    //Aracı doğrudan imha et
                    arac.anindaImhaEt(); 
                    //Bu araç için hesaplamanın geri kalanını atla, sonraki araca geç
                    continue; 
                }

                Zaman varisGezegeniBaslangicZamani = varisGezegeni.getTarih();
                int ucusSuresi_Saat = arac.getMesafe();

                //Kalkışa kadar geçecek süreyi hesapla
                int kalkisaKadarGecenSure_Saat = cikisGezegeniBaslangicZamani.saatFarkiHesapla(aracKalkisZamani);

                if (kalkisaKadarGecenSure_Saat < 0) {
                    System.err.printf("Uyarı: Araç [%s] için kalkışa kadar saat farkı hesaplanamadı (%d). Varış tahmini yapılamıyor.%n", arac.getAd(), kalkisaKadarGecenSure_Saat);
                    arac.setHedefeVaracagiTarih(null);
                    continue;
                }
                //Toplam eklenecek saati hesapla
                int toplamEklenecekSaat = kalkisaKadarGecenSure_Saat + ucusSuresi_Saat;

                //Varış Gezegeninin başlangıç zamanına toplam saati ekle
                Zaman tahminiVarisZamani = new Zaman(varisGezegeniBaslangicZamani);
                tahminiVarisZamani.saatEkle(toplamEklenecekSaat);

                //Hesaplanan tahmini varış zamanını araca ata
                arac.setHedefeVaracagiTarih(tahminiVarisZamani);


            } else {
                // Gerekli gezegen bilgisi eksikse hatayı göster
                 String hataMesaj = "Uyarı: Araç [" + arac.getAd() + "] için ";
                 if (cikisGezegeni == null) hataMesaj += "kalkış gezegeni [" + arac.getCikisGezegeniAdi() + "] ";
                 if (varisGezegeni == null) hataMesaj += "varış gezegeni [" + arac.getVarisGezegeniAdi() + "] ";
                 hataMesaj += "bulunamadığı için tahmini varış tarihi hesaplanamıyor.";
                 System.err.println(hataMesaj);
                 arac.setHedefeVaracagiTarih(null);
            }
        }
    }

    //Ana simülasyon döngüsünü çalıştırır
    private void calistirSimulasyonDongusu(boolean hizliMod) {
    	//Hesaplamalar yapıldıktan sonra ilk durumu göster 
    	if (!hizliMod) { 
             ekraniTemizle();
             durumuYazdir();
        }

        while (!simulasyonBitti) {
            saatAdimi++;

            //Gezegenleri güncelle
            gezegenler.forEach(Gezegen::saatlikGuncelleme);

            //Araçları güncelle
            for (UzayAraci arac : araclar) {
                Gezegen varisGezegeni = gezegenBul(arac.getVarisGezegeniAdi());
                arac.saatlikGuncelleme(varisGezegeni); // Yolcu ömrü, kalan süre, durum güncellemesi

                //Kalkış kontrolü
                if (arac.getDurum().equals("Bekliyor")) {
                    Gezegen cikisGezegeni = gezegenBul(arac.getCikisGezegeniAdi());
                    if (cikisGezegeni != null && cikisGezegeni.getTarih().tarihEsitMi(arac.getCikisTarihi())) {
                        arac.hareketEt();
                    }
                }
            }

            //Durumu yazdır
            if (!hizliMod) {
                 ekraniTemizle();
                 durumuYazdir();
            }

            //Bitiş kontrolü
            simulasyonBitti = tumAraclarHedefteMi();

            //Yavaşlatma durumu simülasyonu yönetmek için(default olarak 10 belirledim hızlı sonuç için ,isteğe bağlı değiştirilebilir)
            if (!hizliMod) {
                try { Thread.sleep(50); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); simulasyonBitti = true; }
            }
        }
        //Son durumu her zaman yazdır
        if (hizliMod) ekraniTemizle();
        durumuYazdir();
        System.out.println("\n------------------------------------------------");
        System.out.println("Simülasyonu bitirmek için Toplam " + saatAdimi + " saat geçti");
        System.out.println("------------------------------------------------");
        
    }
    //Yolcuları ağaçlara dağıtan fonksiyon
     private void yolculariAraclaraDagit(List<Kisi> tumKisiler) {
        for (Kisi kisi : tumKisiler) {
            boolean atandi = aracBul(kisi.getBulunduguUzayAraciAdi()).map(arac -> {
                arac.yolcuEkle(kisi);
                return true;
            }).orElse(false); 

            if (!atandi) {
                System.err.println("Uyarı: Kişi [" + kisi.getIsim() + "] için belirtilen araç [" + kisi.getBulunduguUzayAraciAdi() + "] bulunamadı.");
            } else {}
        }
    }
    //Tüm aracların hedefte olup olmadığını kontrol eder
    private boolean tumAraclarHedefteMi() { return araclar.isEmpty() || araclar.stream().allMatch(a -> a.getDurum().equals("Vardı") || a.getDurum().equals("IMHA")); }
    //Gezegen eşleşmesi için gerekli fonksiyon
    private Gezegen gezegenBul(String gezegenAdi) { if (gezegenAdi == null) return null; String tAd = gezegenAdi.trim(); for (Gezegen g : gezegenler) { if (g.getAd().equalsIgnoreCase(tAd)) return g; } return null; }
    //Uzay aracı durumunu kontrol eder nulldan daha güvenli kontrol için optional kullanılmıştır
    private Optional<UzayAraci> aracBul(String aracAdi) { if (aracAdi == null) return Optional.empty(); String tAd = aracAdi.trim(); for (UzayAraci a : araclar) { if (a.getAd().equalsIgnoreCase(tAd)) return Optional.of(a); } return Optional.empty(); }
    //Ekranı sürekli güncelleyerek interaktif bir konsol uygulamasını sağlamak için ekranıtemizle fonksiyonu
    private void ekraniTemizle() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J"); System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            for(int i = 0; i < 50; i++) System.out.println(); // Fallback
        }
    }
    //Tüm durumu yazdırır programın ön yüzü denebilir ekranda görünecek durumların bulunduğu fonksiyon
    private void durumuYazdir() {
	    System.out.println("\n\n<<Uzay Yolculuğu Simülasyonu>>\n\n");
        System.out.println("Gezegenler:");
        System.out.print("           ");
        if (!gezegenler.isEmpty()) {
            List<String> adlar = new ArrayList<>(), tarihler = new ArrayList<>(), nufuslar = new ArrayList<>();
            String formatGezegenBaslik = "%-20s";
            String formatGezegenVeri = "%-20s";

            for (Gezegen gezegen : gezegenler) {
                long anlikNufus = 0;
                for (UzayAraci arac : araclar) {
                     if (gezegen.getAd().equalsIgnoreCase(arac.getKonumGezegenAdi()) && (arac.getDurum().equals("Bekliyor") || arac.getDurum().equals("Vardı"))) {
                          anlikNufus += arac.getHayattakiYolcuSayisi();
                     }
                }
                adlar.add("--- " + gezegen.getAd() + " ---");
                tarihler.add(gezegen.getTarih().tarihToString());
                nufuslar.add(String.valueOf(anlikNufus));
            }

            for (String ad : adlar) System.out.printf(formatGezegenBaslik, ad); System.out.println();
            System.out.printf("%-10s ", "Tarih");
            for (String tarih : tarihler) System.out.printf(formatGezegenVeri, tarih); System.out.println();
            System.out.printf("%-10s ", "Nüfus");
            
            for (String nufus : nufuslar) {
            	System.out.printf("%-5s", "");
            	System.out.printf("%-10s", nufus);
            	System.out.printf("%-5s", "");

            }
        	System.out.println();
        } else { System.out.println("Gösterilecek gezegen yok."); }

        System.out.println("\nUzay Araçları:");
        String formatAracBaslik = "%-20s %-15s %-15s %-15s %-25s %-25s%n";
        String formatAracVeri   = "%-20s %-15s %-15s %-15s %-25s %-25s%n";
        System.out.printf(formatAracBaslik, "Araç Adı", "Durum", "Çıkış", "Varış", "Hedefe Kalan Saat", "Hedefe Varacağı Tarih");

        if (!araclar.isEmpty()) {
            for (UzayAraci arac : araclar) {
                String kalanSaatStr = "--";
                String varisTarihStr = "--";

                 //Hesaplanan hedefe varacağı tarihin yazdırılması
                if (arac.getHedefeVaracagiTarih() != null && !arac.getDurum().equals("IMHA")) {
                    varisTarihStr = arac.getHedefeVaracagiTarih().tarihToString();
                }

                if (arac.getDurum().equals("IMHA")) {
                    kalanSaatStr = "--";
                    varisTarihStr = "--";
                } else if (arac.getDurum().equals("Bekliyor")) {
                     kalanSaatStr = String.valueOf(arac.getMesafe());
                } else if (arac.getDurum().equals("Yolda")) {
                    kalanSaatStr = String.valueOf(arac.getHedefeKalanSaat());
                } else if (arac.getDurum().equals("Vardı")) {
                    kalanSaatStr = "0";
                }

                System.out.printf(formatAracVeri,
                    arac.getAd(),
                    arac.getDurum(),
                    arac.getCikisGezegeniAdi(),
                    arac.getVarisGezegeniAdi(),
                    kalanSaatStr,
                    varisTarihStr,
                    arac.getHayattakiYolcuSayisi());
            }
        } else { System.out.println("Gösterilecek uzay aracı yok."); }
        System.out.println("\n" +"Simülasyon Saati: " + this.saatAdimi );
    }

}