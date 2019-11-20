package kisu;

public class BitSet {

	int bits;
	
	public BitSet() {
		// TODO Auto-generated constructor stub
	}
	public boolean contains(int bit) {
		return (bits & bit) > 0;
	}
	public int getFirst() {
		return Integer.lowestOneBit(bits);
	}
	public int count() {
		return Integer.bitCount(bits);
	}

}
