package kisu;

import java.util.ArrayList;
import java.util.TreeSet;

import kisu.Grid.Unit;

public class Show {

	public Show() {
		// TODO Auto-generated constructor stub
	}
	public static void showKillersudoku(Grid g, int[] sum, SudokuCandidates can, int[] value) {
		ArrayList<String> sums = new ArrayList<>();
		for (int i=0;i<g.position.size();i++) {
			String s = new String ();
			s += g.position.get(i).get(Unit.FRAME).size() + "—";
			if (sum.length == 0 || sum[i] == 0) {
				s += "—?"; 
			} else {
				s += (sum[i] > 9 ? "" : "—") + sum[i];
			}
			s += "—";
			sums.add(s);
		}
		String [][] cans = new String[81][2];
		for (int i=0;i<cans.length;i++) {
			TreeSet<Integer> temp = can.get(i);
			String s = new String();
			if (temp == null) {
				s = "     ";
			} else if (temp.size() == 1) {
				s = " +" + temp.first() + "+ ";
			} else for (int j=1;j<6;j++) {
				s += temp.contains(j) ? j : " ";
			}
			cans[i][0] = s;
			s = new String();
			if (temp == null || temp.size() == 1) {
				s = "     ";
			} else for (int j=6;j<11;j++) {
				s += temp.contains(j) ? j : " ";
			}
			cans[i][1] = s;
		}
		String [][] values = new String[81][2];
		for (int i=0;i<values.length;i++) {
			int v = 0;
			TreeSet<Integer> temp = can.get(i);
			if (temp != null && temp.size() == 1)
				v = temp.first();
			if (value.length > i)
				v = value[i];

			values[i][0] = " " + (v>0 ? "*" + v + "*" : "   ") + " ";
			values[i][1] = "     ";
		}
		showKillersudoku(g, sums, cans, values);
	}
	private static void showKillersudoku(Grid g, ArrayList<String> sums, String[][] can, String[][] value) {
		System.out.println(" Kandidaten mit Lösungen                                "
				+ " Kandidaten ohne Lösungen                               "
				+ " Leeres Spielfeld                                       "
				+ " Lösung                                               ");
		for (int y=0;y<9;y++) 
			for (int z=0;z<3;z++) { 
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = x == 0 ? z == 1 ? y < 2 ? " " + y*9 + " " : y*9 + " " : "   " : "";
					if (z>0) {
						if (x==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-1, Unit.FRAME))
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || g.getIndex(xy, Unit.FRAME) != g.getIndex(xy-9, Unit.FRAME)) {
							if (xy == g.getPos(xy, Unit.FRAME)[0]) {
								p += sums.get(g.getIndex(xy, Unit.FRAME));
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p += can[xy][z-1];
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
								p += sums.get(g.getIndex(xy, Unit.FRAME));
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p += can[xy][0].startsWith(" +") ? "     " : can[xy][z-1];
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
								p += sums.get(g.getIndex(xy, Unit.FRAME));
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
								p += sums.get(g.getIndex(xy, Unit.FRAME));
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p += value[xy][z-1].startsWith("  ") && can[xy][z-1].startsWith(" +") ? can[xy][z-1] : value[xy][z-1];
					}
					System.out.print(p);
				}
				System.out.println(z>0?"|":" ");
			}
		System.out.println("    ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— ————— ");
//		int c=0;
//		int v=0;
//		for (TreeSet<Integer> z : can) {
//			c+=z.size();
//			if (z.size() == 1) v++;
//		}
		
//		System.out.println("Noch sinds " + (c-v) + " Kandidaten, aber " + v + " Werte sind schon gefunden.");
	}

}
