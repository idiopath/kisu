/**
 * 
 */
package kisu;

/**
 * @author Idiopath
 *
 */
public class Killersudoku extends Sudoku {

	private int[] sum;
	/**
	 * @throws Exception 
	 * 
	 */
	public Killersudoku() {

		this(Frames.generate());
		
	}
	
	public Killersudoku(String frames) {
		super();
		
		setFrames(frames);

		if (super.generateValues()) {
			setSums();
		
			Show.showKillersudoku(sum, new int[0], value, 3, 4);
		} else  {
			System.out.println("Abbruch nach 10 Millionen Versuchen bei generateValue...");
			return;
		}
		
	}
	
	public Killersudoku(String frames, String values) {
		super(values);
		
		setFrames(frames);
		
		int sums = setSums();
		if (sums == sum.length) {
			System.out.println("Das Killersudoku ist komplett.");
			Show.showKillersudoku(sum, new int[0], value, 3, 4);
		} else {
			System.out.println(sums + " von " + sum.length + " Rahmen sind bereits befüllt:");
			Show.showKillersudoku(sum, new int[0], value, 3, 4);
			System.out.println("Jetzt wird der Rest generiert...");
			if (generateValues()) {
				setSums();

				Show.showKillersudoku(sum, new int[0], value, 3, 4);
			} else  {
				System.out.println("Abbruch nach 10 Millionen Versuchen bei generateValue...");
			}

		}
	}

	public boolean generateValues() {
		SudokuCandidates can = new SudokuCandidates();
		int count = 0;
		for (int i=0;i<value.length;i++) {
			if (value[i] > 0) {
				if (!can.setCandidate(i, value[i])) {
					throw new IllegalArgumentException("value at position " + i + " is not valid!");
				}
				count++;
			}
		}
		can.commit();
		System.out.println(count + " feste Kandidaten wurden gesetzt.");
		Show.showKillersudoku(sum, can.get(), value, 1, 2, 3, 4);
		int[][] solutions = new int [1][];
		
		if (can.backtracking(solutions) > 0) {
			value = solutions[0];
			return true;
		}
		
		return false;

//		return backtracking(can);
	}
	
	public boolean verify() {
		
		SudokuCandidates can = new SudokuCandidates(sum);
		
		int[][] solutions = new int [3][];
		
		int sol = can.backtracking(solutions);
		
		if (sol > 1) {
			System.out.print("Mist! Mehr als 1 Lösung, check die Unterschiede:\nValue: ");
			for (int i : value)
				System.out.print(i);
			System.out.print("\nSol 1: ");
			for (int i : solutions[0])
				System.out.print(i);
			System.out.print("\nSol 2: ");
			for (int i : solutions[1])
				System.out.print(i);
			System.out.print("\nDiff: ");
			for (int i=0;i<81;i++) {
				if (solutions[0][i] != solutions[1][i])
					System.out.print(i +" ");
			}
			System.out.print("\n");
			return false;

		} else if (sol == 1) {
			System.out.println("Perfekt! Genau 1 Lösung :-)");
			return true;
		} else {
			System.out.println("Upps! Keine Lösung :-O");
			return false;
		}
		
	}
	
	private void setFrames(String frames) {
		if (frames.length() != 81)
			throw new IllegalArgumentException("Killersudoku/frames: you need 81 values!");

		Grid.setFrames(Frames.stringToArray(frames));
		
	}
	
	private int setSums() {
		sum = new int[Grid.countFrames()];
		int ret = 0;
		System.out.println(Grid.countFrames());
		for (int i=0;i<sum.length;i++) {
			for (int p : Grid.getPosition(i, Unit.FRAME)) {

				if (value[p] == 0) {
					sum[i] = 0;
					break;
				}
				sum[i] += value[p];
			}
			if (sum[i] > 0)
				ret++;
		}
		return ret;
	}
	
	public String getFrames() {
		int [] frames = new int[81];
		for (int i=0;i<81;i++) {
			frames[i] = Grid.getIndex(i, Unit.FRAME);
		}
		return Frames.framesToString(frames);
	}
	
	public void generate() {
		generateValues();
	}

}
