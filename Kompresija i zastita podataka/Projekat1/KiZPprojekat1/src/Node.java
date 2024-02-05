
public class Node{
    double probability;
    String symbol;
    Node left;
    Node right;
    String code;

    public Node(double probability, String symbol, Node left, Node right) {
        this.probability = probability;
        this.symbol = symbol;
        this.left = left;
        this.right = right;
        this.code = "";
    }
    
    @Override
    public String toString() {
    	return "Symbol: " + symbol + " prob: " + probability + "\n";
    }
    
}
