import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {
		//Kreiranje matrice
		int[][] H = Matrica.createMatrix();		
		//Matrica.printMatrix(H);
		
		List<int[]> binarneKombinacije = Matrica.generateBinaryCombinations();
		
		//Collections.sort(binarneKombinacije, Comparator.comparingInt(x -> Arrays.stream(x).sum())); //sortiramo
		//binarneKombinacije.sort(Comparator.comparingInt(a -> Arrays.stream(a).sum()));
		Comparator<int[]> komparator = new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                int jedinice1 = Matrica.countOnes(a);
                int jedinice2 = Matrica.countOnes(b);

                if (jedinice1 == jedinice2) {
                    // Ako su brojevi jedinica jednaki, sortiramo po leksikografskom redosledu
                    return Arrays.compare(a, b);
                }

                return Integer.compare(jedinice1, jedinice2);
            }
        };

        // Sortiramo listu pomocu komparatora
        Collections.sort(binarneKombinacije, komparator);
//        for (int[] niz : binarneKombinacije) {
//            System.out.println(Arrays.toString(niz));
//        }
        
		int[][] matricaBinarnihKombinacija = binarneKombinacije.toArray(new int[0][]); //konvertovanje iz liste u matricu -> e(korektori)
		
		
		int[][] s = Matrica.calculateSyndroms(H, matricaBinarnihKombinacija);
		List<int[]> jedinstveniS = Matrica.minimalWeight(s);
		
		List<int[]> eMin = Matrica.indexi.stream()
                .map(index -> binarneKombinacije.get(index))
                .collect(Collectors.toList());
		
		//resenje za 2.1 
		Matrica.printResult(jedinstveniS, eMin);
		
		
		//2.2 kodno rastojanje
		int kodnoRastojanje = Matrica.kodnoRas(H,binarneKombinacije);
        System.out.println("Kodno rastojanje je: " + kodnoRastojanje);
        
        //2.3 Gallager_b
        //int[] primljeniVektor = {1, 0, 0, 0, 0, 0, 0};
        for(int i=0;i < H.length;i++) {
        	int[] dekodiraniRezultat = Matrica.gallagerBAlgoritam(H[i], 0.5, 0.5, 183);
        	System.out.println("Dekodirani rezultat: " + Arrays.toString(dekodiraniRezultat));
        }
        	
        
	}
	
}
