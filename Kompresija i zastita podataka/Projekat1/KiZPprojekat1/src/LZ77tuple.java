
public class LZ77tuple {
	int bit;
	Character karakter;
	int move;
	int lenght;
	
	public LZ77tuple(int bit,Character karakter)
	{
		this.bit = bit;
		this.karakter = karakter;
	}
	
	public LZ77tuple(int bit,int move,int lenght)
	{
		this.bit = bit;
		this.move = move;
		this.lenght = lenght;
	}
	
	@Override
	public String toString() {
		if(bit == 1)
		{
			return "(1, " + move + ", " + lenght + ") ";
		}
		else
		{
			return "(0, " + karakter + ") ";
		}
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public Character getKarakter() {
		return karakter;
	}

	public void setKarakter(Character karakter) {
		this.karakter = karakter;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}
	
	
}
