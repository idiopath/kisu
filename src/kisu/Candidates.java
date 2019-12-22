package kisu;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class Candidates {

	static int all = 0b111111111;
	
	
	/**
	 * 
	 * @param can as bits
	 * @return count of candidates left
	 */
	static int count(int can) {
		return Integer.bitCount(can);
	}
	
	/**
	 * @param can as bits
	 * @return the first candidate as bit
	 */
	static int first(int can) {
		return Integer.lowestOneBit(can);
	}
	
	
	/**
	 * 
	 * @param can as bits
	 * @return the value if only one candidate left, 0 if non left else 10
	 */
	static int value(int can) {
		return count(can) == 1 ? Integer.numberOfTrailingZeros(can) + 1 : count(can) > 1 ? 10 : 0;
	}
	
	/**
	 * @param can as bits
	 * @return a set of candidates
	 */
	static Set<Integer> values(int can) {
		Set<Integer> ret = new TreeSet<Integer>();
		int c;
		while ((c = first(can)) > 0) {
			int v = value(c);
			ret.add(v);
			can -= c;
		}
		return ret;
	}
	
	/**
	 * @param value the value
	 * @return this value as candidate as bit 
	 */
	static int candidate(int value) {
		return  1 << (value - 1);
	}
	
	/**
	 * @param values set of values
	 * @return this values as candidates as bits
	 */
	static int candidates(Set<Integer> values) {
		int ret = 0;
		for (int v:values)
			ret += candidate(v);
		return ret;
	}
	
	static int not(int can) {
		return all ^ can;
	}
}