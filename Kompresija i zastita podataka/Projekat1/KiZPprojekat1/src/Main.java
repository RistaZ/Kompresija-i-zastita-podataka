import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		//Uzimamo putanju od File-a
		String putanja = Upis.getFilePath("Sample-text-file.txt");
		
		//1.Radimo entropiju
		System.out.println("----------------------------");
		System.out.println("Entropija:");
		Entropija ent = new Entropija();
		double ukupnaEntropija = ent.racunanjeBajtEntropije(putanja);
		System.out.printf("Ukupna entropija fajla je: %.4f\n",ukupnaEntropija);
		
		
		
		
		
		//2.1 Shannon-Fano 
		//char -> vrednost se nalazi u symbolu
		System.out.println("----------------------------");
		System.out.println("Shannon-Fano:");
		List<Symbol> simboli = new ArrayList<Symbol>();	
		
		for(int i=0; i < Entropija.karakteriNiz.size(); i++) {
			Symbol s = new Symbol(Entropija.karakteriNiz.get(i), Entropija.verovatnocaSvakogKaraktera.get(i));
			simboli.add(s);
		}
		
		simboli.sort((s1, s2) -> Double.compare(s2.probability, s1.probability)); //sortiramo u opadajucem poretku
		 
		Shannon_Fano.shannonFanoEncoding(simboli); // dodeljujemo svakom simbolu njegov code
		Shannon_Fano.displayCodes(simboli);
		
		//Kompresija shannon_fana
		String kodiraniIzlazBit = Shannon_Fano.encodeString(simboli, putanja); // String nula-jedinica
		List<Byte> bajtovi = Shannon_Fano.compressShannonFano(kodiraniIzlazBit);
		Shannon_Fano.writeInFile(simboli, bajtovi); // upisivanje u file
		
		//Dekompresija shannon_fana
		String currentDirectory = System.getProperty("user.dir");
		String pathShannonFano = Paths.get(currentDirectory, "shannon_fano_compress.bin").toString();
		List<UcitaniSimbol> prviRedKodiranogFilea = Decode.firstRow(pathShannonFano);

		String stringBezIzbacenihEL = Decode.getRest(pathShannonFano);
		
		int ukupnaDuzina = kodiraniIzlazBit.length(); //kodiraniIzlazBit.length();
		int poModulu8 = ukupnaDuzina % 8; 
		String stringSaIzbacenimEl = null;
		if(poModulu8 != 0)
		{
			stringSaIzbacenimEl = Decode.ukloniCifre(stringBezIzbacenihEL,8 - poModulu8);
		}
		else 
		{
			stringSaIzbacenimEl = stringBezIzbacenihEL;	
		}
		
		//System.out.println(stringSaIzbacenimEl);
		
		String decompressedSF = Decode.decode(stringSaIzbacenimEl, prviRedKodiranogFilea);
		//System.out.println("SHANNON_FANO:" + decompressedSF); // GOTOV SHANNON-FANO
		
		
		
		//2.2HUFFMAN
		System.out.println("----------------------------");
		System.out.println("Huffman:");
		String[] karakteriNizStr = new String[Entropija.karakteriNiz.size()];
		for (int i = 0; i < Entropija.karakteriNiz.size(); i++) {
			karakteriNizStr[i] = Character.toString(Entropija.karakteriNiz.get(i));
		}
		Node koren = Huffman.buildHuffmanTree(karakteriNizStr, Entropija.verovatnocaSvakogKaraktera);
		Huffman.printNodes(koren, "");  // -> ispisivanje cvorova
		
		//Kodiranje Huffman-a
		String stringHuffman = Huffman.encodeString(putanja, koren); //Pravljenje Stringa od 0 i 1 -> kao kod Shannon-Fana
		//System.out.println("Huffman kodiranje: " + stringHuffman);
		List<Byte> huffmanByte = Huffman.compressHuffman(stringHuffman);
		Huffman.saveToFile(koren, huffmanByte);
		
		//Dekompresija huffman-a
		String pathHuffman = Paths.get(currentDirectory, "huffman_compress.bin").toString();
		List<UcitaniSimbol> prviRedKodiranogFileaHuffman = Decode.firstRow(pathHuffman);
		
		String stringBezIzbacenihELHuffman = Decode.getRest(pathHuffman);
		
		ukupnaDuzina = stringHuffman.length();
		poModulu8 = ukupnaDuzina % 8; 
		String stringSaIzbacenimElHuffman = null;
		if(poModulu8 != 0)
		{
			stringSaIzbacenimElHuffman = Decode.ukloniCifre(stringBezIzbacenihELHuffman,8 - poModulu8);
		}
		else 
		{
			stringSaIzbacenimElHuffman = stringBezIzbacenihELHuffman;	
		}
		
		
		String decompressedHuff = Decode.decode(stringSaIzbacenimElHuffman, prviRedKodiranogFileaHuffman);
		//System.out.println("HUFF: " + decompressedHuff.substring(0, Math.min(decompressedHuff.length(), 15))); // GOTOV HUFFMAN
			
	
		//3.1 LZ77
		System.out.println("----------------------------");
		System.out.println("LZ77:");
		String putanjaLZ77 = Upis.getFilePath("Sample-text-file.txt");
		
		int windowSize = 5000;
        List<LZ77tuple> compressedOutput = LZ77.LZ77Compression(putanjaLZ77, windowSize);
        //System.out.println("Kompresovani LZ77 fajl: " + compressedOutput);
        LZ77.writeLZ77ToBinaryFile(compressedOutput);
		
        String decompressedLZ77 = LZ77.LZ77Decode();
        System.out.println("decompresija: " + decompressedLZ77.substring(0, Math.min(decompressedLZ77.length(), 15))); // GOTOV LZ77
       
       
       
       
       //3.2 LZW
       System.out.println("----------------------------");
       System.out.println("LZW:");
       String putanjaLZW = Upis.getFilePath("Sample-text-file.txt"); 
       
       LZW lzw = new LZW();
       List<Integer> kodiraniIzlazLZW = lzw.lzwEncoder(putanjaLZW);
       lzw.saveToFile(kodiraniIzlazLZW); // Pakovanje u file
          
       List<Integer> kodiraniLZWFromFile = lzw.readFromFile(); // Citanje iz file-a
       
       String decompresseedLZW = lzw.lzwDekoder(kodiraniLZWFromFile);
       System.out.println("Prvih 15 karaktera file-a:LZW " + decompresseedLZW.substring(0, Math.min(decompresseedLZW.length(), 15))); // GOTOV LZW
       
       
       
       //4. Racunanje stepena kompresija
       System.out.println("----------------------------");
       System.out.println("Stepen kompresije:");
       StepenKompresije stepen = new StepenKompresije();
       //Stepen kompresije za ShannonFano
       double stepenKompresijeShannonFano = stepen.stepenKompresijeShannonFano();
       System.out.println("Stepen kompresije ShannonFano: " + stepenKompresijeShannonFano);
       
       //Stepen kompresije za Huffman-a
       double stepenKompresijeHuffman = stepen.stepenKompresijeHuffman();
       System.out.println("Stepen kompresije Huffman: " + stepenKompresijeHuffman);
       
       //Stepen kompresije za LZ77
       double stepenKompresijeLZ77 = stepen.stepenKompresijeLZ77();
       System.out.println("Stepen kompresije LZ77: " + stepenKompresijeLZ77);
       
       //Stepen kompresije za LZW
       double stepenKompresijeLZW = stepen.stepenKompresijeLZW();
       System.out.println("Stepen kompresije LZW: " + stepenKompresijeLZW);
		
       
       
       //5. Provera da li radi kompresija
       System.out.println("Shannon-Fano kompresija " + Decode.compare(decompressedSF));
       System.out.println("Huffman-ova kompresija " +Decode.compare(decompressedHuff));
       System.out.println("LZ77 kompresija " +Decode.compare(decompressedLZ77));
       System.out.println("LZW kompresija " +Decode.compare(decompresseedLZW));
       
       
	   //System.out.println("KRAJ!");
	}

}
