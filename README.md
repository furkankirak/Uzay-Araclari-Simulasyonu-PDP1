# Uzay-Araclari-Simulasyonu-PDP1

## Proje Hakkında

Bu proje, Java ile geliştirilmiş, gezegenler arası uzay yolculuğunu simüle eden bir konsol uygulamasıdır. Simülasyon, uzay araçlarını, yolcuları ve kendi zaman akışlarına sahip gezegenleri içerir ve saatlik adımlarla ilerler. Proje, nesneye yönelik programlama prensiplerini kullanarak modüler ve genişletilebilir bir yapıda tasarlanmıştır.

Simülasyonun tüm durumu (`Gezegenler`, `Uzay Araçları`, `Kişiler`) harici metin dosyalarından okunarak yapılandırılır.

## Temel Bileşenler

-   **`Gezegen`**: Simülasyondaki bir gezegeni temsil eder. Her gezegenin kendi adı, bir günün kaç saat sürdüğü ve simülasyon başlangıcındaki kendi takvim tarihi vardır.
-   **`UzayAraci`**: Gezegenler arasında seyahat eden araçları temsil eder. Bir kalkış ve varış gezegeni, kalkış tarihi, yolculuk süresi (saat cinsinden) ve taşıdığı yolcular bulunur. Aracın durumu "Bekliyor", "Yolda", "Vardı" veya "IMHA" olabilir.
-   **`Kisi`**: Bir uzay aracındaki yolcuyu temsil eder. Her yolcunun bir adı, yaşı ve saat cinsinden kalan bir yaşam süresi vardır. Yolcuların ömrü, simülasyon ilerledikçe azalır.
-   **`Zaman`**: Farklı gezegen takvimlerindeki olayları senkronize etmek için kullanılan özel bir zaman yönetim sınıfıdır. Tarihleri, saat ilerlemesini ve ayların gün sayılarını içeren özel bir takvim mantığını yönetir.
-   **`Simulasyon`**: Tüm süreci yöneten ana motordur. Dosyalardan verileri okur, nesneleri başlatır, ana döngüyü çalıştırarak her bir saatlik adımda tüm nesnelerin durumunu günceller ve mevcut durumu konsola yazdırır.

## Nasıl Çalışır?

1.  **Başlatma**: `Program.java` çalıştırıldığında, `Simulasyon` sınıfı `Gezegenler.txt`, `Araclar.txt` ve `Kisiler.txt` dosyalarından verileri okuyarak simülasyonun başlangıç durumunu oluşturur. Kişiler ilgili uzay araçlarına atanır.
2.  **Ön Hesaplama**: Simülasyon döngüsü başlamadan önce, her uzay aracının tahmini varış zamanı, kalkış gezegeninin takvimi ve yolculuk süresi baz alınarak hesaplanır.
3.  **Simülasyon Döngüsü**: Simülasyon, tüm araçlar hedeflerine varana veya imha olana kadar devam eden saatlik adımlarla ilerler. Her bir saatlik adımda:
    -   Tüm gezegenlerin yerel saati bir saat ilerletilir.
    -   Tüm yolcuların kalan ömrü bir saat azaltılır.
    -   Uzay araçlarının durumu güncellenir:
        -   **Bekliyor**: Kalkış zamanı geldiyse durumu "Yolda" olarak değişir.
        -   **Yolda**: Hedefe kalan yolculuk süresi bir saat azalır. Süre sıfırlandığında durumu "Vardı" olur.
        -   **Vardı**: Araç görevini tamamlamış sayılır.
    -   Bir uzay aracındaki tüm yolcular hedefe varmadan ölürse, aracın durumu "IMHA" olarak değiştirilir.
4.  **Arayüz**: Her saatlik adımdan sonra konsol ekranı temizlenir ve gezegenlerin, uzay araçlarının ve simülasyonun genel durumu güncel verilerle yeniden yazdırılır. Bu, konsolda dinamik bir takip imkanı sunar.
5.  **Bitiş**: Tüm uzay araçları "Vardı" veya "IMHA" durumuna geçtiğinde simülasyon döngüsü sona erer ve toplam geçen süre ekrana yazdırılır.

## Özellikler

-   **Dosya Tabanlı Yapılandırma**: Simülasyonun tüm başlangıç verileri (`.txt` dosyaları) harici olarak tanımlanır, bu da farklı senaryoları test etmeyi kolaylaştırır.
-   **Dinamik Zaman Yönetimi**: Her gezegenin bir günü farklı sayıda saatten oluşabilir ve `Zaman` sınıfı bu karmaşıklığı yönetir.
-   **İki Çalışma Modu**:
    -   **Normal Mod (`simulasyon.baslat()`):** Süreci adım adım gözlemlemeye olanak tanıyan, adımlar arasında küçük bir bekleme süresi olan moddur.
    -   **Hızlı Mod (`simulasyon.hizliBaslat()`):** Adım adım gösterimi atlayıp doğrudan simülasyonun nihai sonucunu gösteren moddur. Hızlı test ve hata ayıklama için kullanışlıdır.
-   **Platform Bağımsız Konsol Temizleme**: Hem Windows hem de Unix tabanlı sistemlerde (Linux, macOS) konsol ekranını temizleyerek akıcı bir kullanıcı deneyimi sunar.

## Kurulum ve Çalıştırma

1.  Projeyi klonlayın veya zip olarak indirin.
2.  Bir Java Geliştirme Kiti (JDK) yüklü olduğundan emin olun.
3.  Projenin ana giriş noktası `src/araclarsimulasyon/Program.java` dosyasıdır.
4.  Projeyi bir IDE (Eclipse, IntelliJ IDEA, vb.) üzerinden veya terminal/komut istemi kullanarak derleyip çalıştırabilirsiniz.
5.  **Önemli:** `Araclar.txt`, `Gezegenler.txt` ve `Kisiler.txt` dosyaları, programın çalıştırıldığı kök dizinde bulunmalıdır.

**Çalıştırma Modunu Değiştirme:**
`Program.java` dosyasındaki `main` metodu içinde, hangi modda çalıştırmak istediğinize göre ilgili satırı yorumdan çıkarıp diğerini yorum satırı yapabilirsiniz:

```java
// Normal Mod: Adım adım, bekleyerek.
simulasyon.baslat();

// Hızlı Mod: Direkt son durum, beklemeden.
// simulasyon.hizliBaslat();
```

## Girdi Dosya Formatları

-   **`Gezegenler.txt`**
    -   Format: `GezegenAdı#GünSaatSayısı#BaşlangıçTarihi(gg.aa.yyyy)`
    -   Örnek: `A0#16#7.4.2025`

-   **`Araclar.txt`**
    -   Format: `AraçAdı#KalkışGezegeni#VarışGezegeni#KalkışTarihi(gg.aa.yyyy)#Mesafe(Saat)`
    -   Örnek: `Arac 00#A0#B0#18.4.2025#100`

-   **`Kisiler.txt`**
    -   Format: `KişiAdı#Yaş#KalanÖmür(Saat)#BineceğiAraç`
    -   Örnek: `Kisi1#21#1470#Arac 00`

## Tasarım Notları ve Varsayımlar

Bu projenin geliştirilmesi sırasında bazı varsayımlar yapılmıştır:

-   **Özel Takvim**: Proje, artık yılları hesaba katmayan, ayların gün sayılarının sabit olduğu (`Zaman` sınıfı içindeki `TAKVIM` haritası) özel bir takvim sistemi kullanır.
-   **Yaşam Süresi**: Bir kişinin kalan ömrü, simülasyonun başından sonuna kadar, yolculuk tamamlandıktan sonra bile gezegende beklerken azalmaya devam eder.
-   **Araç İmhası**: Bir uzay aracı, yalnızca "Bekliyor" veya "Yolda" durumundayken içindeki tüm yolcular ölürse "IMHA" olur. Görevini tamamlayıp "Vardı" durumuna geçen bir araç, yolcuları daha sonra ölse bile imha edilmez.
-   **Geçersiz Kalkış Tarihi**: Bir aracın `Araclar.txt` dosyasında belirtilen kalkış tarihi, kalkış yapacağı gezegenin `Gezegenler.txt` dosyasındaki başlangıç tarihinden önceyse, bu araç simülasyonun en başında doğrudan "IMHA" edilir.