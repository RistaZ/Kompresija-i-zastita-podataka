
public class Symbol {
	Character value;
    double probability;
    String code;

    public Symbol(Character value, double probability) {
        this.value = value;
        this.probability = probability;
        this.code = "";
    }
    
    public Character getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

	public double getProbability() {
		return probability;
	}
    
    
}
