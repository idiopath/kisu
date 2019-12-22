package kisu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SudokuRules {

	
	Set<Set<Integer>> todo;
	int[] can;
	Map<Set<Integer>, Integer> sums;
	HashSet<Integer> loop;
	boolean debug = false;
//	boolean crossing = true;
	
	public SudokuRules() {
		todo = new LinkedHashSet<>();
		sums = new HashMap<>();
	}
	
	public void setSums(int[] sum) {

		for (int i=0;i<sum.length;i++)
			sums.put(Grid.getPosition(i, Unit.FRAME), sum[i]);
	}
	public boolean applyRules(int xy, int[] can) {
		this.can = can;
		if (isCross(xy))
			return false;
		
		return applyOneRule(xy);
	}
	
	public boolean applyOneRule(int xy) {
		addTodo(xy);
		
		do {
			if (!applyOneRule())
				return false;
		} while (!todo.isEmpty());
		
		return true;
	}
	
	public void addTodo(int xy) {
		for (Entry<Unit, Integer> e:Grid.getIndex(xy).entrySet()) {
			int s = todo.size();
			todo.add(Grid.getPosition(e.getValue(), e.getKey()));
//			if (todo.size() > s)
//				System.out.print("\ntodo " + Grid.getPosition(e.getValue(), e.getKey()) + "[" + todo.size() + "] ");
		}
	}
	
	private boolean applyOneRule() {
		Set<Integer> positions = todo.iterator().next();
		
		int[] can = new int[positions.size()];
		int i = 0;
		for (int p:positions)
			can[i++] = this.can[p];

		can = applyOneRule(can, sums.containsKey(positions) ? sums.get(positions) : 0);

		if (can[0] == 0)
			return false;
		i=0;
		for (int p:positions) {
			if (this.can[p] != can[i]) {
				if (Candidates.count(can[i]) == 1 && isCross(p))
					return false;
				addTodo(p);
				this.can[p] = can[i];
				
			}
			i++;
		}
		todo.remove(positions);
//		System.out.print("{" + todo.size() + "} ");
		return true;
	}
	
	private int[] applyOneRule(int [] can, int sum) {
		int [] ret = new int[can.length];
		
		if (can.length == 1) {
			if (sum > 0)
				ret[0] = (can[0] & (1 << (sum-1))); // wenn Summe als Kandidat enthalten, sonst 0
			else
				ret = can;

			return ret;
		}
		
		int i = Integer.lowestOneBit(can[0]);
		int value = 0;
		
		while (i <= Integer.highestOneBit(can[0])) {
			if ((i & can[0]) > 0) {
				if (sum > 0) {
					do {
						value++;
					} while (i > 1 << (value - 1));
					if (value >= sum)
						break;
				}
				int[] r = new int[can.length - 1];
				for (int j=0;j<r.length;j++) {
					r[j] = can[j+1] - (can[j+1] & i); // Subliste ohne akt. ausgewählten Kandidat
					if (r[j] == 0)
						break;
				}
				if (r[r.length-1] > 0) {
					r = applyOneRule(r, sum - value);
					if (r[0] > 0) {
						ret[0] |= i;
						for (int j=0;j<r.length;j++) {
							ret[j+1] |= r[j];
						}
					}
				}
			}
			i = i << 1;
		}
		return ret;
	}

	public void addTodo(Set<Integer> changed) {
		for (int c:changed)
			addTodo(c);
		
	}
	public boolean isCross(int xy) {
		
		Unit[] u = new Unit[] {Unit.FRAME, Unit.BLOCK};
		
		Set<Integer> set = Grid.intersect(xy, true, u); 
		if (debug) System.out.println("xy " + xy + " set " + set);
//		Show.showKillersudoku(new int[0], can, new int[0], 1);
		loop = new HashSet<>();
		for (int p:set) {
			if (Integer.bitCount(can[p]) == 1) {
//				System.out.print(" p " + p);
				if (Grid.sameUnit(Unit.COLUMN, xy, p)) {
					if (isCross(Unit.ROW, xy, p, Unit.COLUMN, u))
						return true;
				}
				if (Grid.sameUnit(Unit.ROW, xy, p)) {
					if (isCross(Unit.COLUMN, xy, p, Unit.ROW, u))
						return true;
				}
			}
		}
//		System.out.print("\n");
		return false;
	}
	
	private boolean isCross(Unit u0, int posA, int posB, Unit u1, Unit[] u) {
		if (debug) System.out.print(" 1(" + u0 + "," + posA + "," + posB + ")");
		if (!loop.add(posA<posB?posA*100 + posB:posB*100 + posA))
			return false;
		for (int p:Grid.getPosition(Grid.getIndex(posA, u0), u0)) {
			if (can[p] == can[posB]) {
				if (debug) System.out.print(" ab");
				if (isCross(u0, posA, posB, p, u1, u))
					return true;
				break;
			}
		}
		for (int p:Grid.getPosition(Grid.getIndex(posB, u0), u0)) {
			if (can[p] == can[posA]) {
				if (debug) System.out.print(" ba");
				if (isCross(u0, posB, posA, p, u1, u))
					return true;
				break;
			}
		}
		return false;
	}

	private boolean isCross(Unit u0, int posA, int posB, int posC, Unit u1, Unit[] u) {
		int xy = Grid.getPosition(Grid.getIndex(posB, u0), u0).first() + Grid.getPosition(Grid.getIndex(posC, u1), u1).first();
		if (debug) System.out.print(" 2(" + u0 + "," + posA + "," + posB + "," + posC + "xy" + xy + ")");
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

			if (Integer.bitCount(can[xy]) == 1) {
				if (debug) System.out.print(" <a>");
				if (isCross(u0, posA, xy, u1, u))
					return true;
			}
		}
		
		Set<Integer> set = Grid.intersect(posC, true, u);
		set.remove(xy);

		for (int p:set) {
			if (can[p] == can[posA]) {
				if (Grid.sameUnit(u1, p, posC)) {
					if (debug) System.out.print(" <b>");
					return isCross(u0, posB, p, u1, u);
				}
				if (Grid.sameUnit(u0, p, posB)) {
					if (debug) System.out.print(" <c>");
					return isCross(u1, p, posC, u0, u);
				}
			}
		}
		return false;
	}
}
