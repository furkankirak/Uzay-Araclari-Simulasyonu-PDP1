/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 23.04.2025
* <p>
* Zaman Sınıfı, Zamanı kontrol eder,Aylarin gün sayısı belirlenerek Takvimi oluşturur,Zamanin karşışaltırılması ve Tarih saat atamalarını yapar yönetir.
* </p>
*/
package araclarsimulasyon;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Zaman implements Comparable<Zaman> {
    private int gun;
    private int ay;
    private int yil;
    private int saat;
    private final int gezegenGunSaatSayisi;


    //Kurucu Metod
    public Zaman(String tarihStr, int gezegenGunSaatSayisi) {
        this.gezegenGunSaatSayisi = Math.max(gezegenGunSaatSayisi, 1);
        setTarihFromString(tarihStr);
        this.saat = 0; // Başlangıç saati 00:00
    }

    //Kopyalama constructorı
    public Zaman(Zaman diger) {
        this.gun = diger.gun; this.ay = diger.ay; this.yil = diger.yil;
        this.saat = diger.saat; this.gezegenGunSaatSayisi = diger.gezegenGunSaatSayisi;
    }
    //Public Get Metodları
    public int getGun() { return gun; }
    public int getAy() { return ay; }
    public int getYil() { return yil; }
    public int getSaat() { return saat; }
    public int getGezegenGunSaatSayisi() { return gezegenGunSaatSayisi; }
    
    //Takvimi oluşturmak için her aydaki gün sayıları belirlenir
    private static final Map<Integer, Integer> TAKVIM = new HashMap<>();
    static {
    	TAKVIM.put(1, 31); TAKVIM.put(2, 28); TAKVIM.put(3, 31);
        TAKVIM.put(4, 30); TAKVIM.put(5, 31); TAKVIM.put(6, 30);
        TAKVIM.put(7, 31); TAKVIM.put(8, 31); TAKVIM.put(9, 30);
        TAKVIM.put(10, 31); TAKVIM.put(11, 30); TAKVIM.put(12, 31);
    }

    //String'den tarihi ayrıştırır.
    private void setTarihFromString(String tarihStr) {
        if (tarihStr == null || tarihStr.trim().isEmpty()) { assignDefaultDate(); return; }
        String[] parcalar = tarihStr.trim().split("\\.");
        if (parcalar.length == 3) {
            try {
                int pGun = Integer.parseInt(parcalar[0]), pAy = Integer.parseInt(parcalar[1]), pYil = Integer.parseInt(parcalar[2]);
                //Basit doğrulama, artık yıl kontrolü yok
                if (pYil < 0 || pAy < 1 || pAy > 12 || pGun < 1 || pGun > TAKVIM.getOrDefault(pAy, 31) ) {
                    System.err.println("Uyarı: Geçersiz tarih formatı veya değerler: " + tarihStr + ". Varsayılan kullanıldı.");
                    assignDefaultDate();
                } else {
                    this.gun = pGun; this.ay = pAy; this.yil = pYil;
                }
            } catch (NumberFormatException e) {
                 System.err.println("Uyarı: Tarih parse edilirken sayı formatı hatası: " + tarihStr + ". Varsayılan kullanıldı.");
                 assignDefaultDate();
            }
        } else {
            System.err.println("Uyarı: Geçersiz tarih formatı (gg.aa.yyyy bekleniyor): " + tarihStr + ". Varsayılan kullanıldı.");
            assignDefaultDate();
        }
    }

    //Geçersiz tarih durumunda varsayılan tarihi atar.
    private void assignDefaultDate() {
        this.gun = 1; this.ay = 1; this.yil = 2000;
    }

    //Zamana saat ekler, gün/ay/yıl atlamalarını yönetir.
    public void saatEkle(int eklenecekSaat) {
        if (eklenecekSaat < 0) {
             System.err.println("Uyarı: Negatif saat ekleme desteklenmiyor.");
            return;
        }
        long toplamSaat = (long)this.saat + eklenecekSaat;
        long eklenecekGun = toplamSaat / this.gezegenGunSaatSayisi;
        this.saat = (int)(toplamSaat % this.gezegenGunSaatSayisi);

        if (eklenecekGun > 0) {
             //EklenecekGun çok büyükse kontrol eder
            if (eklenecekGun > Integer.MAX_VALUE) {
                 System.err.println("Hata: Eklenen gün sayısı limit aşıyor.");
                 return;
            }
            gunEkle((int)eklenecekGun);
        }
    }

    //Tarihe gün ekler.
    private void gunEkle(int eklenecekGun) {
        if (eklenecekGun <= 0) return;
        this.gun += eklenecekGun;
        int ayinGunSayisi = TAKVIM.getOrDefault(this.ay, 30);
        ayinGunSayisi = (this.ay == 2) ? 28 : TAKVIM.getOrDefault(this.ay, 31);

        while (this.gun > ayinGunSayisi) {
            this.gun -= ayinGunSayisi;
            ayEkle(1);
            //Ay değiştikçe gün sayısını tekrar al
             ayinGunSayisi = (this.ay == 2) ? 28 : TAKVIM.getOrDefault(this.ay, 31);
        }
    }

    //Tarihe ay ekler.
    private void ayEkle(int eklenecekAy) {
        if (eklenecekAy <= 0) return;
        this.ay += eklenecekAy;
        while (this.ay > 12) {
            this.ay -= 12;
            this.yil++;
        }
    }

    //Sadece gün/ay/yıl olarak tarihleri karşılaştırır.
    public boolean tarihEsitMi(Zaman diger) {
        return diger != null && this.gun == diger.gun && this.ay == diger.ay && this.yil == diger.yil;
    }

    //Tarih ve saati karşılaştırır.
    public boolean tarihSaatEsitMi(Zaman diger) { // YENİ METOT
        return tarihEsitMi(diger) && this.saat == diger.saat;
    }
    //Simülasyon başlangıcında varacağ tarihi hesaplamak için kullanılan fonksiyon
    public int saatFarkiHesapla(Zaman hedefZaman) { 
        if (hedefZaman == null) return -1;

        //Hedef zamanın bu zamandan önce olup olmadığını kontrol ediyoruz
        if (this.compareTo(hedefZaman) > 0) {
            System.err.println("Uyarı: saatFarkiHesapla - hedefZaman ("+hedefZaman.tarihToString()+" "+hedefZaman.saatToString()
                +") başlangıç zamanından ("+this.tarihToString()+" "+this.saatToString()+") önce. Fark hesaplanamıyor.");
            return -1; // Hedef geçmişte
        }
        //Başlangıç zamanının kopyası
        Zaman anlikZaman = new Zaman(this); 
        long saatFarki = 0;
        //Sonsuz döngüyü engellemek için
        long güvenlikSayaci = 0; 
        //Örnek bir limit (yaklaşık 2280 yıl * 24 saat)
        long maksIterasyon = 20_000_000; 

        //Anlık zaman hedef zamana eşit olana kadar saat ekle
        while (!anlikZaman.tarihSaatEsitMi(hedefZaman)) {
            anlikZaman.saatEkle(1);
            saatFarki++;
            güvenlikSayaci++;
            if (güvenlikSayaci > maksIterasyon) {
                 System.err.println("Hata: saatFarkiHesapla güvenlik sayacını aştı. Olası mantık hatası veya çok büyük fark.");
                 return -1; // Hesaplama başarısız
            }
        }

        // Fark int sınırlarını aşıyorsa
        if (saatFarki > Integer.MAX_VALUE) {
            System.err.println("Uyarı: Hesaplanan saat farkı Integer sınırlarını aşıyor.");
            return Integer.MAX_VALUE; // Mümkün olan en büyük int değerini döndür
        }
        return (int) saatFarki;
    }


    // Tarihi gün.ay.yıl formatında döndürür.
    public String tarihToString() { return String.format("%02d.%02d.%04d", gun, ay, yil); }
    // Saati HH:00 formatında döndürür.
    public String saatToString() { return String.format("%02d:00", saat); }


    //Eşitliği kontrol etmek için yardımcı metotları ovverride ediyoruz
    @Override
    public boolean equals(Object o) { // equals metodu eklendi
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zaman zaman = (Zaman) o;
        return gun == zaman.gun &&
               ay == zaman.ay &&
               yil == zaman.yil &&
               saat == zaman.saat &&
               gezegenGunSaatSayisi == zaman.gezegenGunSaatSayisi;
    }
    //hashCode metodu override edildi
    @Override
    public int hashCode() { 
        return Objects.hash(gun, ay, yil, saat, gezegenGunSaatSayisi);
    }
    //compareTo metodu eklendi. Comparable için
    @Override
    public int compareTo(Zaman diger) { 
        if (this.yil != diger.yil) return Integer.compare(this.yil, diger.yil);
        if (this.ay != diger.ay) return Integer.compare(this.ay, diger.ay);
        if (this.gun != diger.gun) return Integer.compare(this.gun, diger.gun);
        if (this.saat != diger.saat) return Integer.compare(this.saat, diger.saat);
        return 0; 
    }
}