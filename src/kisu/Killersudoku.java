/**
 * 
 */
package kisu;

import kisu.Grid.Unit;

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
		
			Show.showKillersudoku(g, sum, new SudokuCandidates(), value);
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
			Show.showKillersudoku(g, sum, new SudokuCandidates(), value);
		} else {
			System.out.println(sums + " von " + sum.length + " Rahmen sind bereits befüllt:");
			Show.showKillersudoku(g, sum, new SudokuCandidates(), value);
			System.out.println("Jetzt wird der Rest generiert...");
			if (generateValues()) {
				setSums();

				Show.showKillersudoku(g, sum, new SudokuCandidates(), value);
			} else  {
				System.out.println("Abbruch nach 10 Millionen Versuchen bei generateValue...");
			}

		}
	}

	public boolean generateValues() {
		SudokuCandidates can = new SudokuCandidates(g);
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
		Show.showKillersudoku(g, sum, can, value);

		return backtracking(can);
	}
	
	public boolean verify() {
		
		SudokuCandidates can = new SudokuCandidates(g, sum);
		
		int[][] solutions = new int [2][];
		
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

		g.setFrames(Frames.stringToArray(frames));
		
	}
	
	private int setSums() {
		sum = new int[g.position.size()];
		int ret = 0;
		for (int i=0;i<sum.length;i++) {
			for (int p : g.getPos(Unit.FRAME, i)) {

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
			frames[i] = g.index.get(i).get(Unit.FRAME);
		}
		return Frames.framesToString(frames);
	}
	
	public void generate() {
		generateValues();
	}
	
	public void print() {
		for (int i=0;i<g.position.size();i++) {
			System.out.println("Pos " + i + ": " + g.position.get(i) + " Sum " + sum[i]);
		}
	}

	public void showKillersudoku() {
		System.out.println(" Kandidaten mit Lösungen                                "
				+ " Kandidaten ohne Lösungen                               "
				+ " Leeres Spielfeld                                       "
				+ " Lösung                                               ");
		for (int y=0;y<9;y++) 
			for (int z=0;z<3;z++) { 
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-1, Unit.FRAME))
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-9, Unit.FRAME)) {
							if (xy == g.getPos(xy, Unit.FRAME)[0]) {
								p+= g.getPos(xy, Unit.FRAME).length + "—";
								p+= (sum[g.getIndex(xy, Unit.FRAME)] > 9 ? "" : "—") + sum[g.getIndex(xy, Unit.FRAME)] + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+="     ";
//						for (int w=4;w>=0;w--)
//							if (can.get(xy).size() == 1) {
//								if (z == 1  && w  == 2)
//									p+= can.get(xy).first();
//								else if (z == 1 && (w == 1 || w == 3))
//										p+= "*";
//								else p+= " ";
//							} else
//								p+= can.get(xy).contains(z*5-w) ? z*5-w : " ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// nur Kandidaten
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-1, Unit.FRAME))
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-9, Unit.FRAME)) {
							if (xy == g.getPos(xy, Unit.FRAME)[0]) {
								p+= g.getPos(xy, Unit.FRAME).length + "—";
								p+= (sum[g.getIndex(xy, Unit.FRAME)] > 9 ? "" : "—") + sum[g.getIndex(xy, Unit.FRAME)] + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+="     ";
//						for (int w=4;w>=0;w--)
//							if (can.get(xy).size() == 1) {
//								p+= " ";
//							} else
//								p+= can.get(xy).contains(z*5-w) ? z*5-w : " ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// leeres Feld
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-1, Unit.FRAME))
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-9, Unit.FRAME)) {
							if (xy == g.getPos(xy, Unit.FRAME)[0]) {
								p+= g.getPos(xy, Unit.FRAME).length + "—";
								p+= (sum[g.getIndex(xy, Unit.FRAME)] > 9 ? "" : "—") + sum[g.getIndex(xy, Unit.FRAME)] + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+="     ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// Lösung
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-1, Unit.FRAME))
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-9, Unit.FRAME)) {
							if (xy == g.getPos(xy, Unit.FRAME)[0]) {
								p+= g.getPos(xy, Unit.FRAME).length + "—";
								p+= (sum[g.getIndex(xy, Unit.FRAME)] > 9 ? "" : "—") + sum[g.getIndex(xy, Unit.FRAME)] + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+= z==1 ? (" *" + value[xy] + "* ") : "     ";  
					}
					System.out.print(p);
				}
				System.out.println(z>0?"|":" ");
			}
		System.out.println(" ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— ————— ");
		int c=0;
		int v=0;
//		for (TreeSet<Integer> z : can) {
//			c+=z.size();
//			if (z.size() == 1) v++;
//		}
		
		System.out.println("Noch sinds " + (c-v) + " Kandidaten, aber " + v + " Werte sind schon gefunden.");
	}

}
