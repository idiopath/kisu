/**
 * 
 */
package kisu;

import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public abstract class Sudoku implements ISudoku {

	protected int[] value;
	protected Grid g;
	
	/**
	 * 
	 */
	public Sudoku() {
		value = new int[81];
		g = new Grid();
	}
	
	public Sudoku(String values) {
		this();

		for (int i=0;i<values.length() && i<values.length();i++)
			value[i] = values.charAt(i) - '0';
	}

	public boolean generateValues() {
		SudokuCandidates can = new SudokuCandidates(g);
		
		int[][] solutions = new int [1][];
		
		if (can.backtracking(solutions) > 0) {
			value = solutions[0];
			return true;
		}
		
		return false;
	}
	
	public boolean backtracking(SudokuCandidates can) {
		int [] pos = can.posWithCandidates();
		int [][] save = new int[pos.length][81];

		int solution = 0;
		int p = 0;
		int count = 0;
		StringBuilder s = new StringBuilder();
		for (int i=0;i<pos.length;i++) s.append('9');
		int min = 81;
		int max = 0;
		do {
			if (++count % 1000000 == 0)
				System.out.println(s + " " + count + " min " + min + " max " + max);

			int rest = can.findNextCandidate(pos[p]);
			s.setCharAt(p, (char)('0' + Integer.bitCount(rest)));

			if (can.getFirst(pos[p]) > 0) {
				if (p + 1 == pos.length) {
					Show.showKillersudoku(g, new int[0], can, new int[0]);
					if (solution++ > 3)
						break;
				} else {
					save[p] = can.get();
					save[p][pos[p]] = rest;
					can.commit();
					
					p++;
					max = pos[p] > max ? pos[p] : max;
					continue;
				}
			} 
			if (rest == 0) {
				p--;
				if (p < 0)
					return false;

				min = pos[p] < min ? pos[p] : min;
				can.set(save[p]);
			} else {
//				can.rollback();
				can.setCandidates(pos[p], rest);
			}
			
		} while (count < 10000000);
		System.out.print("Durchgänge für " + pos.length + " Werte: " + (count) + "\n");
//		if (count == 10000000)
//			for (int i=0;i<min;i++)
//				System.out.println(i + ": " + Integer.toBinaryString(save[i][i]));
		if (p == pos.length) {
			Show.showKillersudoku(g, new int[0], can, new int[0]);
			return false;
		}
		for (int i=0;i<81;i++) {
			TreeSet<Integer> t = can.get(i);
			if (t.size() == 1)
				value[i] = t.first();
		}
		return true;
	}
	
	public String getValues() {
		String ret = new String();
		for (int i : value)
			ret+= i;
		
		return ret;
	}
	
	abstract public void generate();
//	abstract boolean extraRule(int xy);
	
	abstract public void print();
}
