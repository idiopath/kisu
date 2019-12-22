package kisu;

import java.util.Set;
import java.util.TreeSet;

public class Crossing {

	int[] can;
	Set<Integer> changed;

	public Crossing() {

	}

//	public boolean eliminateCrosses0(int xy, int[] can) {
//		
//		TreeSet<Integer> set = testSet(xy, can);		
//		
//		for (int s : set) {
//			boolean b = true;
//			if (Grid.getIndex(xy, Unit.ROW) != Grid.getIndex(s, Unit.ROW)) {
//				b = isCross(Unit.ROW, xy, s, can);
//			}
//			if (Grid.getIndex(xy, Unit.COLUMN) != Grid.getIndex(s, Unit.COLUMN)) {
//					if (b && !isCross(Unit.COLUMN, xy, s, can))
//						b = false;
//			}
//			if (b) {
//				System.out.println(xy + ": " + set + " " + s + " " + b + ", ");
//				return false;
//			}
//		}
//		if (xy > 80) {
//			printExit(can);
//		}
//		
//		return true;
//	}
//	public boolean eliminateCrosses1(int xy, int[] can) {
//		this.can = can;
//		value = new TreeSet<>();
//		for (int i=0;i<can.length;i++)
//			if (Integer.bitCount(can[i]) == 1)
//				value.add(i);
//		
//		Set<Integer> set = Grid.intersect(xy, true, Unit.FRAME, Unit.BLOCK); 
//		set.retainAll(value); 
//		System.out.print(xy + " ");
//		for (int s : set) {
//			if (Grid.getIndex(xy, Unit.COLUMN) == Grid.getIndex(s, Unit.COLUMN)) {
//				if (isCross(can[xy], Grid.getIndex(xy, Unit.ROW), can[s], Grid.getIndex(s, Unit.ROW), Unit.ROW, Unit.COLUMN, Unit.FRAME, Unit.BLOCK))
//					return false;
//				if (isCross(can[s], Grid.getIndex(s, Unit.ROW), can[xy], Grid.getIndex(xy, Unit.ROW), Unit.ROW, Unit.COLUMN, Unit.FRAME, Unit.BLOCK))
//					return false;
//			}
//			if (Grid.getIndex(xy, Unit.ROW) == Grid.getIndex(s, Unit.ROW)) {
//				if (isCross(can[xy], Grid.getIndex(xy, Unit.COLUMN), can[s], Grid.getIndex(s, Unit.COLUMN), Unit.COLUMN, Unit.ROW, Unit.FRAME, Unit.BLOCK))
//					return false;
//				if (isCross(can[s], Grid.getIndex(s, Unit.COLUMN), can[xy], Grid.getIndex(xy, Unit.COLUMN), Unit.COLUMN, Unit.ROW, Unit.FRAME, Unit.BLOCK))
//					return false;
//			}
//		}
//		if (xy > 80) {
//			printExit(can);
//		}
//		
//		return true;
//	}
	
	public Set<Integer> eliminateCrosses(int xy, int[] can) {
		this.can = can;
		changed = new TreeSet<Integer>();
		
		Unit[] u = new Unit[] {Unit.FRAME, Unit.BLOCK};
		
		Set<Integer> set = Grid.intersect(xy, true, u); 
		System.out.println("xy " + xy + " set " + set);
		Show.showKillersudoku(new int[0], can, new int[0], 1);
		for (int p:set) {
			if (Integer.bitCount(can[p]) == 1) {
				System.out.print(" p " + p);
				if (Grid.sameUnit(Unit.COLUMN, xy, p)) {
					if (isCross(Unit.ROW, xy, p, true, Unit.COLUMN, u))
						return null;
				}
				if (Grid.sameUnit(Unit.ROW, xy, p)) {
					if (isCross(Unit.COLUMN, xy, p, true, Unit.ROW, u))
						return null;
				}
			}
		}
		System.out.print("\n");
		return changed;
	}
	
	private boolean isCross(Unit u0, int posA, int posB, boolean edge, Unit u1, Unit[] u) {
		System.out.print(" 1(" + u0 + "," + posA + "," + posB + ")");
		for (int p:Grid.getPosition(Grid.getIndex(posA, u0), u0)) {
			if (can[p] == can[posB]) {
				if (isCross(u0, posA, posB, p, edge, u1, u))
					return true;
				break;
			}
		}
		for (int p:Grid.getPosition(Grid.getIndex(posB, u0), u0)) {
			if (can[p] == can[posA]) {
				if (isCross(u0, posB, posA, p, edge, u1, u))
					return true;
				break;
			}
		}
		return false;
	}

	private boolean isCross(Unit u0, int posA, int posB, int posC, boolean edge, Unit u1, Unit[] u) {
		int xy = Grid.getPosition(Grid.getIndex(posB, u0), u0).first() + Grid.getPosition(Grid.getIndex(posC, u1), u1).first();
		System.out.print(" 2(" + u0 + "," + posA + "," + posB + "," + posC + "xy" + xy + ")");
		if 	(posA==posC) {
			System.out.println();
			for (int i:Grid.getPos(posA, u0))
				System.out.print(Integer.toBinaryString(can[i]) + " ");
			System.out.println();
			for (int i:Grid.getPos(posB, u0))
				System.out.print(Integer.toBinaryString(can[i]) + " ");
			System.out.println();
			Show.showKillersudoku(new int[0], can, new int[0], 1);
			System.exit(0);
		}
		if (Grid.sameUnit(Unit.FRAME, posC, xy)) {
			if (can[xy] == can[posA])
				return true;
			
			if (edge && (can[xy] & can[posA]) > 0) {
				can[xy] -= can[posA]; // und nun prüfe, obs den letzten verbliebenen Kandidaten schon gibt
				if (Candidates.count(can[xy]) == 1)
					for (int i:Grid.getPos(xy, u0))
						if (xy != i && can[xy] == can[i])
							return true;
				changed.add(xy);
			}
	
			if (Integer.bitCount(can[xy]) == 1)
				if (isCross(u0, posA, xy, edge, u1, u))
					return true;
		}
		
		Set<Integer> set = Grid.intersect(posC, true, u);
		set.remove(xy);

		for (int p:set) {
			if (can[p] == can[posA]) {
				if (Grid.sameUnit(u1, p, posC))
					return isCross(u0, posB, p, edge, u1, u);
				if (Grid.sameUnit(u0, p, posB))
					return isCross(u1, p, posC, edge, u0, u);
			}
		}
		return false;
	}

//	private boolean isCross(int av, int al, int bv, int bl, Unit u0, Unit u1, Unit... u) {
//		System.out.print(u0 + " (" + al + "," + bl + ": ");
//		System.out.print((Integer.numberOfTrailingZeros(av)+1) + "," + (Integer.numberOfTrailingZeros(bv)+1) + ") ");
//		for (int p:Grid.getPosition(al, u0)) {
//			if (can[p] == bv) {
//				int xy = bl * (u0 == Unit.ROW ? 9 : 1) + Grid.getIndex(p, u1) * (u1 == Unit.ROW ? 9 : 1);
//				Set<Integer> set = Grid.intersect(p, true, u);
//				System.out.print("p " + p +" xy " + xy + " " + set + "" + ":" + Integer.toBinaryString(can[xy]) +  " ");
//				if (set.contains(xy)) {
//					if (!value.contains(xy)) {
//						can[xy] -= av;
//						
//						if (Integer.bitCount(can[xy]) == 1)
//							value.add(xy);
//						System.out.print(Integer.toBinaryString(can[xy]) + " ");
//					}
//					
//					if (value.contains(xy)) {
//						if (can[xy] == av)
//							return true;
//						if (isCross(av, al, can[xy], bl, u0, u1, u))
//							return true;
//					}
//					set.remove(xy);
//					System.out.print(set + "\n");
//				}
//				
//				for (int s:set) {
////					if (can[s] == av)
////						if (g.position.get(g.getIndex(p, u1)).get(u1).contains(s)) {
////							return isCross(av, g.getIndex(s, u0), bv, al, u0, u1, u);
////						}
////						if (g.position.get(bl).get(u0).contains(s)) {
////							if (isCross(av, g.getIndex(s, u1), bv, g.getIndex(p, u1), u1, u0, u))
////								return true;
////							return isCross(bv, g.getIndex(p, u1), av, g.getIndex(s, u1), u1, u0, u);
////						}
//				}
//				break;
//			}
//		}
//		return false;
//	}
	
//	private boolean isCross(Unit u, int a0, int b0, int[] can) {
//		int a1 = find(u, a0, can[b0], can);
//		
//		if (a1 > -1) {
//			if (Grid.getIndex(Grid.getPos(a0, u)[a1], Unit.FRAME) == Grid.getIndex(Grid.getPos(b0, u)[a1], Unit.FRAME)
//					&& can[Grid.getPos(b0, u)[a1]] == can[a0]) {
//				
//				return true;
//			}
//		}
//		return false;
//	}
//	private int find (Unit u, int p, int v, int [] can) {
//		int[] pos = Grid.getPos(p, u);
//		int ret = 8;
//		do {
//			if (can[pos[ret]] == v)
//				break;
//		} while (--ret > -1);
//		return ret;
//	}
//	
//	private void printExit(int [] can) {
//		int [] frames = new int[81];
//		for (int i=0;i<81;i++) {
//			frames[i] = Grid.getIndex(i, Unit.FRAME);
//		}
//		String f = Frames.framesToString(frames);
//
//		for (int y=0;y<9;y++) {
//			System.out.println();
//			for (int x=0;x<9;x++) {
//				System.out.print(Integer.bitCount(can[y*9+x]) == 1 ? Integer.numberOfTrailingZeros(can[y*9+x]) + 1 : " ");
//				if (x%3 == 2)
//					System.out.print(" ");
//			}
//			System.out.print("      ");
//			for (int x=0;x<9;x++) {
//				System.out.print(f.charAt(y*9+x));
//				if (x%3 == 2)
//					System.out.print(" ");
//			}
//			if (y%3 == 2)
//			System.out.println();
//		}
//		System.exit(0);
//		
//	}
//	
//	private TreeSet<Integer> testSet(int xy, int[] can) {
//		
//		TreeSet<Integer> ret = new TreeSet<>();
//		for (int i : Grid.getPos(xy, Unit.BLOCK)) {
//			if (i != xy)
//				if (Grid.getPosition(Grid.getIndex(xy, Unit.FRAME), Unit.FRAME).contains(i))
//					if (Integer.bitCount(can[i]) == 1)
//						ret.add(i);
//		}
//		return ret;
//	}
}
