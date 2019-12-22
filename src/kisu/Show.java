package kisu;

import java.util.Set;
import java.util.TreeSet;

public class Show {

	public Show() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Prints the Killersudoku in variants
	 * @param sum the sums, if array is empty sums will calculated by method itself
	 * @param can the cans or empty array
	 * @param value the solution values or empty array
	 * @param var the variants to be printed:
	 * 1 - solutions else candidates
	 * 2 - just candidates
	 * 3 - no solutions, no candidates
	 * 4 - only solutions 
	 */
	public static void showKillersudoku(int[] sum, int[] can, int[] value, int... var) {
		if (can.length == 0)
			can = new int[81];
		if (value.length == 0)
			value = new int[81];
		// Summen falls keine da
		if (sum.length == 0) {
			sum = new int[Grid.countFrames()];
			for (int i=0;i<sum.length;i++) {
				for (int p:Grid.getPosition(i, Unit.FRAME)) {
					if (value[p] > 0)
						sum[i] += value[p];
					else if (Candidates.count(can[p]) == 1)
						sum[i] += Candidates.value(can[p]);
					else {
						sum[i] = 0;
						break;
					}
				}
			}
		}
		
		String[] sums = new String[81];
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++)
				sums[y*9 + x] = (y == 0 || !Grid.sameUnit(Unit.FRAME, y*9 + x, y*9 + x - 9)) ? "—————" : y%3 == 0 ? "- - -" : "     ";

		for (int i=0;i<Grid.countFrames();i++) {
			sums[Grid.getPosition(i, Unit.FRAME).first()] = Grid.getPosition(i, Unit.FRAME).size() + "—"
					+ (sum.length == 0 || sum[i] == 0  ? "—?" : (sum[i] < 10 ? "—" : "") + sum[i]) + "—"; 
		}
		
		String [][] cans = new String[81][];
		String [][] values = new String[81][];

		for (int i=0;i<81;i++) {
			Set<Integer> c = Candidates.values(can[i]);  
			
			if (c.size() == 1) {
				cans[i] = new String[]{" +" + c.iterator().next() + "+ ", "     "};
			} else {
				cans[i] = new String[]{"", ""};
				for (int j=1;j<11;j++)
					cans[i][(j-1)/5] += c.contains(j) ? j : " ";
			}
			values[i] = new String[] {" " + (value[i] > 0 ? "*" + value[i] + "*" : "   ") + " ", "     "};
			
		}
		showKillersudoku(sums, cans, values, var);
	}

	private static void showKillersudoku(String[] sums, String[][] can, String[][] value, int... var) {
		System.out.print("    ");
		for (int i : var) {
			switch (i) {
			case 1:
				System.out.print(" Kandidaten mit Lösungen                                ");
				break;
			case 2:
				System.out.print(" Kandidaten ohne Lösungen                               ");
				break;
			case 3:
				System.out.print(" Leeres Spielfeld                                       ");
				break;
			case 4:
				System.out.print(" Lösung                                                 ");
				break;

			default:
				System.out.print("                                                        ");
				break;
			}

		}
		System.out.println();

		for (int y=0;y<9;y++) 
			for (int z=0;z<3;z++) {
				System.out.print(z == 1 ? y < 2 ? " " + y*9 + " " : y*9 + " " : "   ");
				for (int v:var)
					showKillersudoku(sums, can, value, v, y, z);
				System.out.println();
			}
		System.out.print("   ");
		for (int i=0;i<var.length;i++) {
			System.out.print(" ————— ————— ————— ————— ————— ————— ————— ————— —————  ");
		}
		System.out.println();
//		int c=0;
//		int v=0;
//		for (TreeSet<Integer> z : can) {
//			c+=z.size();
//			if (z.size() == 1) v++;
//		}
		
//		System.out.println("Noch sinds " + (c-v) + " Kandidaten, aber " + v + " Werte sind schon gefunden.");
	}
	private static void showKillersudoku(String[] sums, String[][] can, String[][] value, int var, int y, int z) {
		for (int x=0;x<9;x++) {
			int xy = y*9+x;
			String p = z > 0 ? (x==0 || !Grid.sameUnit(Unit.FRAME, xy, xy-1)) ? "|" : x%3==0 ? "¦" : " " : " ";
			if (z==0) {
				p += sums[xy];
			} else {
				switch (var) {
				case 1:
					p += can[xy][z-1];	
					break;
				case 2:
					p += can[xy][0].startsWith(" +") ? "     " : can[xy][z-1];	
					break;
				case 3:
					p+="     ";	
					break;
				case 4:
					p += value[xy][z-1].startsWith("  ") && can[xy][z-1].startsWith(" +") ? can[xy][z-1] : value[xy][z-1];	
					break;

				default:
					break;
				}
				
			}
			System.out.print(p);
		}
		System.out.print(z > 0 ? "| ":"  ");
		
	}

}
