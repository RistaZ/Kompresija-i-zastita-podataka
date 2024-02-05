
public class UcitaniSimbol {
	String vrednost;
    String kod;

    public UcitaniSimbol(String vrednost, String kod) {
        this.vrednost = vrednost;
        this.kod = kod;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "Vrednost: " + vrednost + " kod: " + kod;
    }
}
