/**
*
* @author Furkan KIRAK furkan.kirak@ogr.sakarya.edu.tr
* @since 24.04.2025
* <p>
* Kalıtım için Soyut Sınıf, Uzaydaki temel nesneler (Gezegen, UzayAraci) için ortak özellikler
* </p>
*/
package araclarsimulasyon;

abstract class UzayNesnesi {
    private String ad;
    protected UzayNesnesi(String ad) {
        this.setAd(ad);
    }
    private void setAd(String ad) {
        this.ad = (ad != null && !ad.trim().isEmpty()) ? ad.trim() : "Bilinmeyen Uzay Nesnesi";
    }
    public String getAd() {
        return ad;
    }
}