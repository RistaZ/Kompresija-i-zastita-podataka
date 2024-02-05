import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StepenKompresije {
	static Path currentDirectory = Paths.get(System.getProperty("user.dir"));
	
	public double stepenKompresijeShannonFano()
	{
		Path inputFile = currentDirectory.resolve("Sample-text-file.txt"); 
		Path exitFile = currentDirectory.resolve("shannon_fano_compress.bin"); 
		
		long sizeInputFile = 0;
		long sizeShannonFano = 0;
        try {
        	sizeInputFile = Files.size(inputFile);
			sizeShannonFano = Files.size(exitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return getStepenKompresije(sizeInputFile, sizeShannonFano);
	}
	
	public double stepenKompresijeHuffman()
	{
		Path inputFile = currentDirectory.resolve("Sample-text-file.txt"); 
		Path exitFile = currentDirectory.resolve("huffman_compress.bin"); 
		
		long sizeInputFile = 0;
		long sizeHuffman = 0;
        try {
        	sizeInputFile = Files.size(inputFile);
        	sizeHuffman = Files.size(exitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return getStepenKompresije(sizeInputFile, sizeHuffman);
	}
	
	
	public double stepenKompresijeLZ77()
	{
		Path inputFile = currentDirectory.resolve("Sample-text-file.txt"); 
		Path exitFile = currentDirectory.resolve("LZ77_compress.bin"); 
		
		long sizeInputFile = 0;
		long sizeLZ77 = 0;
        try {
        	sizeInputFile = Files.size(inputFile);
        	sizeLZ77 = Files.size(exitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return getStepenKompresije(sizeInputFile, sizeLZ77);
	}
	
	public double stepenKompresijeLZW()
	{
		Path inputFile = currentDirectory.resolve("Sample-text-file.txt");  
		Path exitFile = currentDirectory.resolve("LZW_compress.bin"); 
		
		long sizeInputFile = 0;
		long sizeLZW = 0;
        try {
        	sizeInputFile = Files.size(inputFile);
        	sizeLZW = Files.size(exitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return getStepenKompresije(sizeInputFile, sizeLZW);
	}
	
	public double getStepenKompresije(long inputNumber,long outputNumber)
	{
		double compressionRatio = (double) inputNumber / outputNumber;
        compressionRatio = Math.round(compressionRatio * 10000) / 10000.0;
        return compressionRatio;
	}
}
