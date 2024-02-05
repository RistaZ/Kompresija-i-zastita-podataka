import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Entropija {
	public static List<Character> karakteriNiz = new ArrayList<>(); // Pojedinacni karaktiri: a b c d e
    public static List<Double> verovatnocaSvakogKaraktera = new ArrayList<>(); // Verovatnoca za svaki od karaktera

    public double racunanjeBajtEntropije(String putanja) throws IOException {
       
        try (BufferedReader reader = new BufferedReader(new FileReader(putanja))) { 
        	long N = 0; // Ukupan broj karakera
            int charRead;
            long[] characterCount = new long[256]; 
            
            while ((charRead = reader.read()) != -1) {
                N++;
                characterCount[charRead & 0xFF]++;
            }
            
            double[] p = new double[256];
            for (int i = 0; i < characterCount.length; i++) {
                p[i] = (double) characterCount[i] / N;
            }
            
            for (int byteValue = 0; byteValue < characterCount.length; byteValue++) {
                if (characterCount[byteValue] > 0) {
                    char karakter = (char) byteValue;
                    double p_i = p[byteValue];
                    karakteriNiz.add(karakter);
                    verovatnocaSvakogKaraktera.add(p_i);
                   // System.out.printf("%c: Pojavljuje se %d puta, Verovatnoca: %.2f%n", karakter, bajtBrojac[byteValue], p_i);
                }
            }
            
            double entropija = 0;
            for (double p_i : p) {
                if (p_i != 0) {
                    entropija -= p_i * (Math.log(p_i) / Math.log(2));
                }
            }
            return entropija;
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
       
    }
}
