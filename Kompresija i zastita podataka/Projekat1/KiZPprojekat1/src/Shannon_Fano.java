import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Shannon_Fano {
	
	public static void shannonFanoEncoding(List<Symbol> symbols) {
        if (symbols.size() == 1) {
            return;
        }

        double sumOfProbabilities = symbols.stream().mapToDouble(s -> s.probability).sum();
        double currentSum = 0;
        List<Double> differences = new ArrayList<>();
        
        for (int i = 0; i < symbols.size(); i++) {
            currentSum += symbols.get(i).probability;
            double remainder = sumOfProbabilities - currentSum;
            differences.add(Math.abs(currentSum - remainder));
        }
        
        double minDifference = Collections.min(differences);
        int splitIndex = differences.indexOf(minDifference);
        for (int i = 0; i < symbols.size(); i++) {
            symbols.get(i).code += (i <= splitIndex) ? "0" : "1";
        }

        shannonFanoEncoding(symbols.subList(0, splitIndex + 1));
        shannonFanoEncoding(symbols.subList(splitIndex + 1, symbols.size()));
    }

    public static void displayCodes(List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            System.out.println("Symbol " + symbol.value + ": " + symbol.code);
        }
    }

    public static String encodeString(List<Symbol> symbols, String putanja) {
    	try (BufferedReader reader = Files.newBufferedReader(Path.of(putanja), StandardCharsets.UTF_8)) {
            StringBuilder encodedString = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                for (char ch : line.toCharArray()) {
                    for (Symbol symbol : symbols) {
                        if (symbol.value == ch) {
                            encodedString.append(symbol.code);
                            break;
                        }
                    }
                }
            }

            return encodedString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "greska";
        }
    }
    
    
    public static List<Byte> compressShannonFano(String kodiraniString){
    	List<Byte> bajtovi = new ArrayList<>();
        for (int i = 0; i < kodiraniString.length(); i+=8) {
            String byteString = kodiraniString.substring(i, Math.min(i + 8, kodiraniString.length()));
            byte bajt = (byte) Integer.parseInt(byteString, 2);
            bajtovi.add(bajt);
        }
        return bajtovi;
    }
    
    public static void writeInFile(List<Symbol> symbols, List<Byte> bajtovi) {
    	try (FileOutputStream f = new FileOutputStream("shannon_fano_compress.bin")) {
            for (Symbol symbol : symbols) {
                String infoSymbol = symbol.getValue() + ":" + symbol.getCode();
                f.write(infoSymbol.getBytes("UTF-8"));
                f.write(' ');
            }
            f.write('\n');

            for (byte bajt : bajtovi) {
                f.write(bajt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
