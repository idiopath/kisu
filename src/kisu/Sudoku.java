/**
 * 
 */
package kisu;

/**
 * @author Idiopath
 *
 */
public abstract class Sudoku implements ISudoku {

	protected int[] value;
	
	/**
	 * 
	 */
	public Sudoku() {
		value = new int[81];
	}
	
	public Sudoku(String values) {
		this();

		for (int i=0;i<values.length();i++)
			value[i] = values.charAt(i) - '0';
	}

	public boolean generateValues() {
		SudokuCandidates can = new SudokuCandidates();
		
		int[][] solutions = new int [1][];
		
		if (can.backtracking(solutions) > 0) {
			value = solutions[0];
			return true;
		}
		
		return false;
	}
	
	
	public String getValues() {
		String ret = new String();
		for (int i : value)
			ret+= i;
		
		return ret;
	}
	
	abstract public void generate();
	
}
