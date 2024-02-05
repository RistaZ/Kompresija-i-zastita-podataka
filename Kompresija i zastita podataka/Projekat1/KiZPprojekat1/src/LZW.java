import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LZW {
	private static List<Character> uniqueSymbols = new ArrayList<>();
	private static List<String> uniqueSymbolsFromFIle = new ArrayList<>();
	
	 public List<Integer> lzwEncoder(String input) throws IOException {
	        List<Integer> output = new ArrayList<>();
	        Map<String, Integer> dictionary = new HashMap<>();

	        try (BufferedReader reader = Files.newBufferedReader(Path.of(input), StandardCharsets.UTF_8)) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String inputString = line;

	                for (char x : inputString.toCharArray()) {
	                    if (!uniqueSymbols.contains(x)) {
	                        uniqueSymbols.add(x);
	                    }
	                }

	                for (char x : inputString.toCharArray()) {
	                    String key = String.valueOf(x);
	                    if (!dictionary.containsKey(key)) {
	                        dictionary.put(key, dictionary.size() + 1);
	                    }
	                }

	                String currentSequence = String.valueOf(inputString.charAt(0));
	                for (int i = 1; i < inputString.length(); i++) {
	                    char x = inputString.charAt(i);
	                    if (dictionary.containsKey(currentSequence + x)) {
	                        currentSequence += x;
	                    } else {
	                        output.add(dictionary.get(currentSequence));
	                        dictionary.put(currentSequence + x, dictionary.size() + 1);
	                        currentSequence = String.valueOf(x);
	                    }
	                }

	                output.add(dictionary.get(currentSequence));
	            }
	        }

	        return output;
	    }
	
	public static List<Character> getUniqueSymbols() {
        return uniqueSymbols;
    }
	
	public void saveToFile(List<Integer> kodiraniIzlazLZW)
	{
		String uniqueSymbolsString = uniqueSymbols.stream()
	               .map(String::valueOf)
	               .collect(Collectors.joining(","));
		
		String kod = kodiraniIzlazLZW.stream()
	               .map(String::valueOf)
	               .collect(Collectors.joining(","));
		
		try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("LZW_compress.bin"))) {
            writer.write(uniqueSymbolsString);
            writer.newLine();
            writer.write(kod);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
	}
	
	public List<Integer> readFromFile()
	{
		List<String> lines = null;
		List<Integer> indeksiLZW = null;
        try {
            lines = Files.readAllLines(Paths.get("LZW_compress.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines != null && lines.size() >= 2) {
            String prviRedString = new String(lines.get(0).getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            String drugiRedString = new String(lines.get(1).getBytes(), java.nio.charset.StandardCharsets.UTF_8);

            uniqueSymbolsFromFIle = Stream.of(prviRedString.split(",")).collect(Collectors.toList());
            indeksiLZW = Stream.of(drugiRedString.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }       
        return indeksiLZW;
	}
	
	public String lzwDekoder(List<Integer> kodiraniLZWFromFile) {
        HashMap<Integer, String> recnik = new HashMap<>();
        for (int i = 0; i < uniqueSymbolsFromFIle.size(); i++) { // Pakujemo prvi red
            recnik.put(i + 1, uniqueSymbolsFromFIle.get(i));
        }
        
        int trenutniIndeks = recnik.size() + 1;
        StringBuilder rezultat = new StringBuilder(recnik.get(kodiraniLZWFromFile.get(0)));
        String trenutnaSekvencija = recnik.get(kodiraniLZWFromFile.get(0));

        for (int indeks : kodiraniLZWFromFile.subList(1, kodiraniLZWFromFile.size())) {
            String novaSekvencija;
            if (recnik.containsKey(indeks)) {
                novaSekvencija = recnik.get(indeks);
            } else if (indeks == trenutniIndeks) {
                novaSekvencija = trenutnaSekvencija + trenutnaSekvencija.charAt(0);
            } else {
                throw new IllegalArgumentException("Neispravan niz indeksa.");
            }
            rezultat.append(novaSekvencija);

            recnik.put(trenutniIndeks, trenutnaSekvencija + novaSekvencija.charAt(0));
            trenutniIndeks++;

            trenutnaSekvencija = novaSekvencija;
        }
        return rezultat.toString();
    }
	
	
}
