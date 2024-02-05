import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {
	public static List<LZ77tuple> LZ77Compression(String putanja, int windowSize) {
		
		List<LZ77tuple> compressedOutput = new ArrayList<>();
		try (BufferedReader reader = Files.newBufferedReader(Path.of(putanja), StandardCharsets.UTF_8)) {
		    String line;
		    while ((line = reader.readLine()) != null) {
		        String inputString = new String(line);
		        int i = 0;
		        while (i < inputString.length()) {
		            int maxMatchLength = 0;
		            int maxMatchIndex = 0;

		            for (int j = 1; j <= Math.min(windowSize, i); j++) {
		                int matchLength = 0;
		                while (i + matchLength < inputString.length() &&
		                        inputString.charAt(i + matchLength) == inputString.charAt(i - j + matchLength)) {
		                    matchLength++;
		                }
		                if (matchLength > maxMatchLength) {
		                    maxMatchLength = matchLength;
		                    maxMatchIndex = i - j;
		                }
		            }
		            if (maxMatchLength > 0) {
		                compressedOutput.add(new LZ77tuple(1, i - maxMatchIndex, maxMatchLength));
		                i += maxMatchLength;
		            } else {
		                compressedOutput.add(new LZ77tuple(0, inputString.charAt(i)));
		                i++;
		            }
		        }
		    }
		    return compressedOutput;
		} catch (IOException e) {
		    e.printStackTrace();
		    return null;
		}
    }
	
	public static void writeLZ77ToBinaryFile(List<LZ77tuple> compressedOutput) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream("LZ77_compress.bin")) {
            for (LZ77tuple element : compressedOutput) {
                if (element.getBit() == 0) {
                    fileOutputStream.write(0x00);
                    fileOutputStream.write((element.getKarakter()).toString().getBytes("UTF-8"));
                } else if (element.getBit() == 1) {
                    fileOutputStream.write(0x01);
                    fileOutputStream.write(ByteBuffer.allocate(2).putShort((short) (element.getMove())).array());
                    fileOutputStream.write(ByteBuffer.allocate(2).putShort((short) (element.getLenght())).array());
                }
            }
        }
    }
	
	public static String LZ77Decode() {
		List<LZ77tuple> kodiraniPodaci = LZ77.ReadFile();
        StringBuilder dekodiranNiz = new StringBuilder();
        StringBuilder bafer = new StringBuilder();
        for (LZ77tuple unos : kodiraniPodaci) {
            if (unos.getBit() == 0) {
                dekodiranNiz.append(unos.getKarakter());
                bafer.append((unos.getKarakter()));
            } else {
                int indeksPocetka = bafer.length() - unos.getMove();
                for (int i = 0; i < unos.getLenght(); i++) {
                    dekodiranNiz.append(bafer.charAt(indeksPocetka + i));
                    bafer.append(bafer.charAt(indeksPocetka + i));
                }
            }
        }
        return dekodiranNiz.toString();
    }
	
	public static List<LZ77tuple> ReadFile()
	{
		 List<LZ77tuple> exit = new ArrayList<>();
		 try (FileInputStream fajl = new FileInputStream("LZ77_compress.bin")) {
	            while (true) {
	                int marker = fajl.read();
	                if (marker == -1) {
	                    break;
	                }
	                if (marker == 0) {
	                    char karakter = (char) fajl.read();
	                    exit.add(new LZ77tuple(0, karakter));
	                } else if (marker == 1) {
	                    int move = (fajl.read() << 8) | fajl.read();
	                    int lenght = (fajl.read() << 8) | fajl.read();
	                    exit.add(new LZ77tuple(1, move, lenght));
	                }
	            }
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 return exit;
	}
	
}
