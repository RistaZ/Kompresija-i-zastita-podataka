import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Decode {
	public static List<UcitaniSimbol> firstRow(String putanja) throws IOException {
        List<UcitaniSimbol> simboli = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(putanja), "UTF-8"))) {
            String prviRedBin = br.readLine().trim();
            String[] simboliRaw = prviRedBin.split("\\s+");            
            for (String simbolRaw : simboliRaw) {
            	//System.out.println("RAW: " + simbolRaw);
                String[] parts = simbolRaw.split(":");
                UcitaniSimbol simbol = new UcitaniSimbol(parts[0], parts[1]);
                simboli.add(simbol);
            }
        }
        return simboli;
    }
    
    public static String getRest(String putanja) throws IOException {
        Path path = Paths.get(putanja);
        byte[] data = Files.readAllBytes(path);

        // Pronađi indeks kraja prvog reda (do kraja prvog reda)
        int endIdx = 0;
        while (endIdx < data.length && data[endIdx] != '\n') {
            endIdx++;
        }

        // Ako ima novih redova, počni sa i = endIdx + 1, inače sa i = 0
        int startIdx = (endIdx < data.length) ? endIdx + 1 : 0;

        StringBuilder bitniString = new StringBuilder();
        for (int i = startIdx; i < data.length; i++) {
            bitniString.append(String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(' ', '0'));
        }
        return bitniString.toString();
    }
    
    public static String ukloniCifre(String binarniNiz, int brojCifaraZaUklanjanje) {
        // Uzimanje poslednjih 8 cifara
        String poslednjih8 = binarniNiz.substring(binarniNiz.length() - 8);
        poslednjih8 = poslednjih8.substring(brojCifaraZaUklanjanje,8);
        // Uzimanje ostalog dela niza
        String ostatak = binarniNiz.substring(0, binarniNiz.length() - 8);
        
        // Formiranje krajnjeg niza
        String krajnjiNiz = ostatak + poslednjih8;
        
        return krajnjiNiz;
    } 
    
    
    public static String decode(String niz, List<UcitaniSimbol> simboli) {
        StringBuilder dekodiraniNiz = new StringBuilder();
        StringBuilder trenutniKod = new StringBuilder();
        for (char bit : niz.toCharArray()) {
            trenutniKod.append(bit);
            for (UcitaniSimbol simbol : simboli) {
                if (trenutniKod.toString().equals(simbol.kod)) {
                    dekodiraniNiz.append(simbol.vrednost);
                    trenutniKod.setLength(0);
                    break;
                }
            }
        }
        return dekodiraniNiz.toString();
    }
    
    
    public static String compare(String output)
    {
    	String currentDirectory = System.getProperty("user.dir");
        String input = Paths.get(currentDirectory, "Sample-text-file.txt").toString();
           

        try (BufferedReader reader = Files.newBufferedReader(Path.of(input), StandardCharsets.UTF_8)) {
            StringBuilder encodedString = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                for (char ch : line.toCharArray()) {
                    encodedString.append(ch);
                }
            }
            
            if(encodedString.toString().equals(output)) {
            	return " je uspela.";
            }
            return " nije uspela.";
        }
         catch (Exception e) {
			// TODO: handle exception
        	 return "GRESKA";
		}   
        
        
    }
}
