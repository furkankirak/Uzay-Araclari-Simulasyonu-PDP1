/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 24.04.2025
* <p>
* Dosya Okuma Sınıfı, Dosyalardaki verileri okuyup nesnelerin oluşmasına yardımcı olan sınıf. Metotları statiktir, çünkü genel amaçlıdırlar.
* </p>
*/
package araclarsimulasyon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class DosyaOkuma {

 //Kisiler.txt dosyasını okur ve Kisi listesi döndürür.
 public static List<Kisi> kisileriOku(String dosyaYolu) throws IOException {
     List<Kisi> kisiler = new ArrayList<>();
     try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
         String line; int satirNo = 0;
         while ((line = br.readLine()) != null) {
              satirNo++; line = line.trim();
              if (line.isEmpty() || line.startsWith("//")) continue;
              String[] p = line.split("#");
              if (p.length == 4) {
                  try { kisiler.add(new Kisi(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]), p[3])); }
                  catch (NumberFormatException e) { logHata("Kisi", satirNo, "Sayı formatı", line); }
              } else { logHata("Kisi", satirNo, "Bölüm sayısı (4 bekleniyor)", line); }
         }
     } catch (IOException e) { System.err.println("HATA: Kişi dosyası okunamadı: " + dosyaYolu); throw e; }
     return kisiler;
 }

 //Araclar.txt dosyasını okur ve UzayAraci listesi döndürür. 
 public static List<UzayAraci> araclariOku(String dosyaYolu, List<Gezegen> mevcutGezegenler) throws IOException {
     List<UzayAraci> araclar = new ArrayList<>();
     if (mevcutGezegenler == null || mevcutGezegenler.isEmpty()) { System.err.println("Uyarı: Araçları okumak için gezegen bilgisi yok."); return araclar; }
     try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
         String line; int satirNo = 0;
         while ((line = br.readLine()) != null) {
              satirNo++; line = line.trim();
              if (line.isEmpty() || line.startsWith("//")) continue;
              String[] p = line.split("#");
              if (p.length == 5) {
                  String cikisGezegenAdi = p[1];
                  Optional<Gezegen> cikisOpt = mevcutGezegenler.stream().filter(g -> g.getAd().equalsIgnoreCase(cikisGezegenAdi)).findFirst();
                  if (cikisOpt.isPresent()) {
                      try { araclar.add(new UzayAraci(p[0], cikisGezegenAdi, p[2], p[3], Integer.parseInt(p[4]), cikisOpt.get().getGunSaatSayisi())); }
                      catch (NumberFormatException e) { logHata("Araç", satirNo, "Mesafe formatı", line); }
                  } else { logHata("Araç", satirNo, "Çıkış gezegeni bulunamadı: "+cikisGezegenAdi, line); }
              } else { logHata("Araç", satirNo, "Bölüm sayısı (5 bekleniyor)", line); }
         }
     } catch (IOException e) { System.err.println("HATA: Araç dosyası okunamadı: " + dosyaYolu); throw e; }
     return araclar;
 }

 //Gezegenler.txt dosyasını okur ve Gezegen listesi döndürür.
 public static List<Gezegen> gezegenleriOku(String dosyaYolu) throws IOException {
     List<Gezegen> gezegenler = new ArrayList<>();
     try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
         String line; int satirNo = 0;
         while ((line = br.readLine()) != null) {
              satirNo++; line = line.trim();
              if (line.isEmpty() || line.startsWith("//")) continue;
              String[] p = line.split("#");
              if (p.length == 3) {
                  try { gezegenler.add(new Gezegen(p[0], Integer.parseInt(p[1]), p[2])); }
                  catch (NumberFormatException e) { logHata("Gezegen", satirNo, "Gün saat sayısı formatı", line); }
              } else { logHata("Gezegen", satirNo, "Bölüm sayısı (3 bekleniyor)", line); }
         }
     } catch (IOException e) { System.err.println("HATA: Gezegen dosyası okunamadı: " + dosyaYolu); throw e; }
     return gezegenler;
 }

 //Dosya okuma hatalarını loglamak için private yardımcı metot.
 private static void logHata(String tip, int satirNo, String hataMesajı, String satir) {
     System.err.println("Dosya Okuma Uyarısı (" + tip + " - Satır " + satirNo + "): " + hataMesajı + " -> \"" + satir + "\"");
 }
}