import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Matrica {
	
	public static List<Integer> indexi = new ArrayList<>();

    public static int[][] createMatrix() {
        Random random = new Random(83); //set seed
        int n = 15; // kolone
        int k = 6; //  n - k
        int wr = 5;  //5 kolona po bloku 
        int wc = 3; // 3 reda u bloku
        int[][] H = new int[n - k][n]; // Java initializes int arrays to 0 by default

        for (int i = 0; i < wc; i++) { // pravimo jedinicnu matricu
        	 int pocetak = i * wr;
             int kraj = pocetak + wr;
            for (int j = pocetak; j < kraj; j++) {
                H[i][j] = 1;
            }
        }

        for (int j = 0; j < n; j++) {
            int rbr = random.nextInt(3) + wc; // random number between wc and wc + 2
            for (int i = wc; i < wc * 2; i++) {
                H[i][j] = (rbr == i) ? 1 : 0;
            }
        }

        for (int j = 0; j < n; j++) {
            int rbr = random.nextInt(3) + wc * 2; // random number between wc * 2 and wc * 2 + 2
            for (int i = wc * 2; i < wc * 3; i++) {
                H[i][j] = (rbr == i) ? 1 : 0;
            }
        }
        
        return H;
    }
    
    public static void printMatrix(int H[][])
    {
        for (int i = 0; i < H.length; i++) {
            for (int j = 0; j < H[i].length; j++) {
                System.out.print(H[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static List<int[]> generateBinaryCombinations()
    {
    	int n = 15;
    	List<int[]> binaryCombinations = new ArrayList<>();
    	for (int i = 0; i < (1 << n); i++) {
    	    int[] combination = new int[n];
    	    for (int j = 0; j < n; j++) {
    	        combination[j] = (i >> j) & 1;
    	    }
    	    binaryCombinations.add(combination);
    	}
    	return binaryCombinations;
    }
    
    private static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] result = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }
    
    public static int[][] calculateSyndroms(int[][] H, int[][] e)
    {
    	// H*e^T
    	int[][] e1 = transpose(e);
    	System.out.println("H: " + H.length + " h[0]:" + H[0].length+ " e:"+e1.length +" e[0]: " + e1[0].length);
    	int[][] result = new int[H.length][e1[0].length];
    	
    	for (int i = 0; i < H.length; i++) {
    	    for (int j = 0; j < e1[0].length; j++) {
    	        for (int k = 0; k < H[0].length; k++) {
    	            result[i][j] += H[i][k] * e1[k][j];
    	        }
    	        result[i][j] %= 2;
    	    }
    	}

    	// transponujemo matricu da bi dobili izgled kao na slajdovima
    	int[][] transposedResult = new int[result[0].length][result.length];
    	for (int i = 0; i < result.length; i++) {
    	    for (int j = 0; j < result[0].length; j++) {
    	        transposedResult[j][i] = result[i][j];
    	    }
    	}
    	
    	return transposedResult;
    }
    
    public static List<int[]> minimalWeight(int[][] transposedResult)
    {
    	// Jedinstveni
    	List<int[]> s = new ArrayList<>();
    	

    	for (int i = 0; i < transposedResult.length; i++) {
    	    final int[] currentRow = transposedResult[i];
    	    boolean isUnique = true;
    	    for (int[] uniqueRow : s) {
    	        if (Arrays.equals(uniqueRow, currentRow)) {
    	            isUnique = false;
    	            break;
    	        }
    	    }
    	    if (isUnique) {
    	    	s.add(currentRow);
    	    	indexi.add(i);
    	    }
    	}
    	return s;
    }
    
    
    public static void printResult(List<int[]> jedinstveniS,List<int[]> eMin)
    {
    	System.out.println("E \t\t 	    -> S");
    	for (int i = 0; i < jedinstveniS.size(); i++) {
    	    System.out.println(Arrays.toString(jedinstveniS.get(i)) + " -> " + Arrays.toString(eMin.get(i)));
    	}
    }
    
    
    public static int countOnes(int[] array) {
        int count = 0;
        for (int element : array) {
            if (element == 1) {
                count++;
            }
        }
        return count;
    }
    
    
    public static int kodnoRas(int[][] H,List<int[]> kombinacije) {
        int brojRedova = H.length;
        int kodnoRastojanje = -1;
        
        for (int i = 1; i < kombinacije.size(); i++) {
            int[] sumaReda = new int[brojRedova];
            int brojJedinica = 0;
            int[] trenutnaKombinacija = kombinacije.get(i);

            for (int indeks = 0; indeks < trenutnaKombinacija.length; indeks++) {
                if (trenutnaKombinacija[indeks] == 1) {
                    brojJedinica++;
                    for (int j = 0; j < brojRedova; j++) {
                        sumaReda[j] += H[j][indeks];
                    }
                }
            }
            for (int j = 0; j < brojRedova; j++) {
                if (sumaReda[j] > -1) {
                    sumaReda[j] = sumaReda[j] % 2;
                }
            }
            int pom = 0;
            for (int j = 0; j < brojRedova; j++) {
                if (sumaReda[j] == 0) {
                    pom++;
                }
            }
            if (pom == brojRedova) {
                if (kodnoRastojanje == -1 || kodnoRastojanje > brojJedinica) {
                    kodnoRastojanje = brojJedinica;
                }
            }
        }
        return kodnoRastojanje;
    }
    
    
    public static int[] gallagerBAlgoritam(int[] y, double th0, double th1, int maxIteracija) {
        int n = y.length;
        int[] x = Arrays.copyOf(y, y.length);
        
        for (int iteracija = 0; iteracija < maxIteracija; iteracija++) {
            
            for (int j = 0; j < n; j++) {
                
                int omegaIDoJ = 0;
                for (int i = 0; i < n; i++) {
                    if (i != j) {
                        omegaIDoJ += x[i];
                    }
                }
                
                for (int i = 0; i < n; i++) {
                    if (i != j) {
                        int omegaI = Arrays.stream(x).sum() - x[j];
                        omegaIDoJ = omegaI;
                        
                        int poslataVrednost = omegaIDoJ;
                        
                        int brojNula = 0;
                        int brojJedinica = 0;
                        for (int k = 0; k < n; k++) {
                            if (k != j) {
                                if (poslataVrednost == 0) brojNula++;
                                if (poslataVrednost == 1) brojJedinica++;
                            }
                        }
                        
                        if (brojNula >= th0 * (n - 1)) {
                            x[j] = 0;
                        } else if (brojJedinica >= th1 * (n - 1)) {
                            x[j] = 1;
                        } else {
                            x[j] = y[j];
                        }
                    }
                }
            }
            
            boolean isStationary = true;
            for (int i = 0; i < n; i++) {
                if (x[i] != y[i]) {
                    isStationary = false;
                    break;
                }
            }
            
            if (isStationary) {
                System.out.println("Prosla je cela iteracija " + (iteracija + 1) + ".");
                break;
            }
        }
        return x;
    }

}