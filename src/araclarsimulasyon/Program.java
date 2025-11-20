/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 22.04.2025
* <p>
* Program Sınıfı, Main sınıfı, Programın başlatıldığı sınıftır.
* </p>
*/
package araclarsimulasyon;

import java.io.File;
import java.io.IOException;

public class Program {
	
	 public static void main(String[] args) {
	   //  System.out.println("\n<<Uzay Yolculuğu Simülasyonu>>\n");

	     String[] dosyalar = {"Kisiler.txt", "Araclar.txt", "Gezegenler.txt"};
	     if (!dosyalarVarMi(dosyalar)) { System.err.println("HATA: Gerekli dosyalar bulunamadı. Program sonlandırılıyor."); return; }

	     Simulasyon simulasyon = new Simulasyon();
	     try {
	         //Çalıştırma Modu
	          simulasyon.baslat();      //Normal Mod: Adım adım, bekleyerek.
	          //simulasyon.hizliBaslat(); //Hızlı Mod: Direkt son durum, beklemeden.
	     } catch (IOException e) { System.err.println("\nKritik Dosya Hatası: " + e.getMessage()); }
	       catch (Exception e) { System.err.println("\nKritik Hata: " + e.getMessage()); e.printStackTrace(); }
	     System.out.println("Simülasyon Çalışması Tamamlandı.");
	     System.out.println("------------------------------------------------");
	 }

	 // Gerekli dosyaların varlığını kontrol eder.
	 private static boolean dosyalarVarMi(String[] dosyaAdlari) {
	     boolean hepsiVar = true;
	     for (String dosyaAdi : dosyaAdlari) {
	         File dosya = new File(dosyaAdi);
	         if (!dosya.exists() || dosya.isDirectory()) { System.err.println(" - EKSIK: " + dosyaAdi); hepsiVar = false; }
	     }
	     if (!hepsiVar) System.err.println("Lütfen eksik dosyaları programın çalıştığı dizine yerleştirin.");
	     return hepsiVar;
	 }
}
