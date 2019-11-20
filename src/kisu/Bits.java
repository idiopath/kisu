package kisu;

public class Bits {

	public Bits() {
		// TODO Auto-generated constructor stub
	}
	public static int[] bitsSet(int bits) {
		int[] ret = new int[Integer.bitCount(bits)];
		int i = 0;
		int pos = Integer.lowestOneBit(bits);
		while (i < ret.length) {
			if ((bits & pos) > 0)
				ret[i++] = pos;
			pos<<= pos;
		};
		return ret;
	}
}
