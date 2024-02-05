import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Huffman {
	public static Node buildHuffmanTree(String[] characters, List<Double> probabilities) {
        // Kreiranje liste čvorova
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < characters.length; i++) {
            Node n = new Node(probabilities.get(i), characters[i], null, null);
            nodes.add(n);
        }

        while (nodes.size() > 1) {
            // Sortiranje čvorova
            Collections.sort(nodes, Comparator.comparingDouble(node -> node.probability));

            Node left = nodes.remove(0);
            Node right = nodes.remove(0);

            left.code = "0";
            right.code = "1";

            Node newNode = new Node(left.probability + right.probability, left.symbol + right.symbol, left, right);
            nodes.add(newNode);
        }

        return nodes.get(0); // Vracamo koren stabla
    }

    public static void printNodes(Node node, String value) {
        String newValue = value + node.code;
        if (node.left != null) {
            printNodes(node.left, newValue);
        }
        if (node.right != null) {
            printNodes(node.right, newValue);
        }
        if (node.left == null && node.right == null) {
            System.out.println(node.symbol + " -> " + newValue);
        }
    }

    public static String encodeString(String putanja, Node root) {
    	byte[] data;
		try {
			data = Files.readAllBytes(Path.of(putanja));
			String sadrzajFajla = new String(data, StandardCharsets.UTF_8);
			
	        StringBuilder encodedString = new StringBuilder();
	        for (char character : sadrzajFajla.toCharArray()) {
	            encodedString.append(findCode(character, root, ""));
	        }
	        return encodedString.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
    }

    private static String findCode(char character, Node node, String value) {
        if (node == null) {
            return null;
        }
        if (node.left == null && node.right == null && node.symbol.equals(String.valueOf(character))) {
            return value + node.code;
        }
        String leftCode = findCode(character, node.left, value + node.code);
        if (leftCode != null) {
            return leftCode;
        }
        return findCode(character, node.right, value + node.code);
    }
    
    
    public static String cuvajCvoroveUStringu(Node cvor, String vrednost) {
        String novaVrednost = vrednost + cvor.code;
        StringBuilder informacijeOCvorovima = new StringBuilder();
        if (cvor.left != null) {
            informacijeOCvorovima.append(cuvajCvoroveUStringu(cvor.left, novaVrednost));
        }
        if (cvor.right != null) {
            informacijeOCvorovima.append(cuvajCvoroveUStringu(cvor.right, novaVrednost));
        }
        if (cvor.left == null && cvor.right == null) {
            informacijeOCvorovima.append(cvor.symbol).append(":").append(novaVrednost).append(" ");
        }
        return informacijeOCvorovima.toString();
    }
    
    public static List<Byte> compressHuffman(String kodiraniString){
    	List<Byte> bajtovi = new ArrayList<>();
        for (int i = 0; i < kodiraniString.length(); i+=8) {
            String byteString = kodiraniString.substring(i, Math.min(i + 8, kodiraniString.length()));
            byte bajt = (byte) Integer.parseInt(byteString, 2);
            bajtovi.add(bajt);
        }
        return bajtovi;
    }
	
    public static void saveToFile(Node prviRed,List<Byte> kompresovaniPodaci)
    {
    	try (FileOutputStream file = new FileOutputStream("huffman_compress.bin")) {
            String informacijeOCvorovima = cuvajCvoroveUStringu(prviRed, "");
            file.write(informacijeOCvorovima.getBytes("UTF-8"));
            file.write('\n');
            for (byte bajt : kompresovaniPodaci) {
                file.write(bajt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
