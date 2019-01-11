package io.tlf.adflyskipper;

/**
 *
 * @author Trevor Flynn <TrevorFlynn@LiquidCrystalStudios.com>
 */

import java.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AdFlySkipper {

    /**
     * Transform adfly url to target url.
     *
     * @param adFlyUrl
     * @return target url
     */

    public static String skipAdFlyUrl(String adFlyUrl) {
        try {
            Document doc = Jsoup.connect(adFlyUrl).get();
            String src = doc.toString();
            src = src.substring(src.indexOf("ysmm"));
            src = src.substring(8);
            src = src.substring(0, src.indexOf('\''));

            //De-encrypt url
            String I = new String();
            String X = new String();
            
            //Sort hash into correct order
            for (int i = 0; i < src.length(); i++) {
                if (i % 2 == 0) {
                    I += src.charAt(i);
                } else {
                    X = src.charAt(i) + X;
                }
            }

            src = I + X;
            byte[] decoded;
            String url;
            
            
            //Fix changes in hash
            for (int i = 0; i < src.length(); i++) {
                if (Character.isDigit(src.charAt(i))) {
                    for (int j = i + 1; j < src.length(); j++) {
                        if (Character.isDigit(src.charAt(j))) {
                            int S = src.charAt(i) ^ src.charAt(j);
                            if (S < 10) { 
                                StringBuilder worker = new StringBuilder(src);
                                worker.setCharAt(i, (char) ((char) S + (char) 48));
                                src = worker.toString();
                            }
                            i = j;
                            j = src.length();
                        }
                    }
                }
            }
            
            decoded = Base64.getDecoder().decode(src);
            url = new String(decoded, "UTF-8");
            url = url.substring(16, url.length() - 16);
            return url;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(skipAdFlyUrl("http://adf.ly/HmtTG")); //Google 
        System.out.println(skipAdFlyUrl("http://adf.ly/IALt1")); //Facebook
        System.out.println(skipAdFlyUrl("http://adf.ly/IF7uW")); //Twitter

    }
}
