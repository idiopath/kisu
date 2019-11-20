/**
 * 
 */
package kisu;

import java.util.Arrays;
import java.util.TreeSet;

import kisu.Grid.Unit;

/**
 * @author Idiopath
 *
 */
public class SudokuCandidates {

	private Grid grid;
	private int[] sums;
	private int[] can;
	private int[] save;

	/**
	 * 
	 */
	public SudokuCandidates() {
		can = new int[81];
		Arrays.fill(can, 0b111111111);
		sums = new int[81];
		commit();
	}
	
	public SudokuCandidates(Grid grid) {
		this();
		this.grid = grid;
	}
	
	public SudokuCandidates(Grid grid, int[] sums) {
		this(grid);
		this.sums = sums;
	}
	
	private SudokuCandidates(int x) {
		
	}
	
	public void set(int [] candidates) {
		can = candidates.clone();
		commit();
	}
	
	public int[] get() {
		return save.clone();
	}
	
	public void commit() {
		save = can.clone();
	}
	
	public void rollback() {
		can = save.clone();
	}
	
	public TreeSet<Integer> get(int xy) {
		if (can.length == 0)
			return null;
		TreeSet<Integer> ret = new TreeSet<Integer>();
		int n = 1;
		for (int i=1;i<10;i++) {
			if ((save[xy] & n) > 0) {
				ret.add(i);
			}
			n = n<<1;
		}
		return ret;
	}
	
	public int getFirst(int xy) {
		return Integer.lowestOneBit(can[xy]);
	}
	
	public boolean remove(int xy, int val) {
		return rem(xy, 1 << (val-1));
	}
	
	public boolean remove(int xy, TreeSet<Integer> val) {
		int bits = 0;
		for (int i : val) {
			bits+= 1 << (i - 1);
		}
		return rem(xy, bits);
	}
	
	private boolean rem(int xy, int bits) {
		if (can[xy] == bits) {
			return false;
		}

		if ((can[xy] & bits) == 0) { // nicht enhalten = keine Änderung
			return true;
		}

		can[xy] = can[xy] & (511 ^ (bits));
		
		return true;
	}
	
	public boolean changed() {
		return !can.equals(save);
	}
	
	public int countSure() {
		int count = 0;
		for (int i : save) {
			if (Integer.bitCount(i) == 1) {
				count++;
			}
		}
		return count;
	}
	
	public int[] posWithCandidates() {
		TreeSet<Integer> pos = new TreeSet<Integer>();
		for (int i=0;i<can.length;i++) {
			if (Integer.bitCount(can[i]) > 1) {
				pos.add(i);
			}
		}
		int[] ret = new int[pos.size()];
		int i=0;
		for (int p : pos)
			ret[i++] = p;
			
		return ret;
	}
	
	public SudokuCandidates clone() {
		SudokuCandidates ret = new SudokuCandidates(0);
		ret.can = can.clone();
		ret.save = save.clone();
		ret.grid = grid;
		ret.sums = sums.clone();
		
		return ret;
	}
	
	public int setNext(int xy) {
		int ret;
		if ((ret = Integer.lowestOneBit(can[xy])) > 0) {
			can[xy] -= ret;
		}
		return ret;
	}
	
	public int backtracking(int[][] solution) {
		int [] pos = posWithCandidates();
		int [][] save = new int[pos.length][81];

		int solutions = 0;
		int p = 0;
		int count = 0;
		StringBuilder s = new StringBuilder();
		for (int i=0;i<pos.length;i++) s.append('9');
		int min = 81;
		int max = 0;
		do {
			if (++count % 1000000 == 0)
				System.out.println(s + " " + count + " min " + min + " max " + max);

			int rest = findNextCandidate(pos[p]);
			s.setCharAt(p, (char)('0' + Integer.bitCount(rest)));
			
			if (getFirst(pos[p]) > 0) {
				if (p + 1 == pos.length) {
					System.out.println(solutions+1 + ". Lösung gefunden, von gesuchten " + solution.length + " nach " + count + " Durchgängen...");
					Show.showKillersudoku(grid, sums, this, new int[0]);
					int [] sol = can.clone();
					Arrays.setAll(sol, i -> Integer.numberOfTrailingZeros(sol[i]) + 1);
					solution[solutions] = sol;
					if (++solutions == solution.length)
						break;
				} else {
					save[p] = get();
					save[p][pos[p]] = rest;
					commit();
					
					p++;
					max = pos[p] > max ? pos[p] : max;
					continue;
				}
			} 
			if (rest == 0) {
				p--;
				if (p < 0)
					return solutions;

				min = pos[p] < min ? pos[p] : min;
				set(save[p]);
			} else {
//				can.rollback();
				setCandidates(pos[p], rest);
			}
			
		} while (count < 10000000);
		System.out.print("Durchgänge für " + pos.length + " Werte: " + (count) + "\n");
//		if (count == 10000000)
//			for (int i=0;i<min;i++)
//				System.out.println(i + ": " + Integer.toBinaryString(save[i][i]));
//		if (p == pos.length) {
//			Show.showKillersudoku(g, new int[0], can, new int[0]);
//			return false;
//		}
//		for (int i=0;i<81;i++) {
//			TreeSet<Integer> t = can.get(i);
//			if (t.size() == 1)
//				value[i] = t.first();
//		}
		return solutions;
	}
	
	public int findNextCandidate(int xy) {
		int ret = can[xy];
		int test;
		while ((test = Integer.lowestOneBit(ret)) > 0) {
			can[xy] = test;
			ret -= test;
			boolean success = applyOneRule(xy);
			if (success) {
				break;
			} else {
				rollback();
				can[xy] = 0;
			}
		}
		return ret;
	}
	
	public void setCandidates(int xy, int candidates) {
		can[xy] = candidates;
	}
	
	public boolean setCandidate(int xy, int value) {
		can[xy] = 1 << (value - 1);

		return applyOneRule(xy);
	}
	
	private boolean applyOneRule(int xy) {

		for (Unit u : Unit.values()) {
			if (!applyOneRule(xy, u)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean applyOneRule(int xy, Unit u) {
		int [] positions = grid.getPos(xy, u);
		if (positions.length > 0) {
			int [] can = new int[positions.length]; 
			for (int i=0;i<positions.length;i++) {
				can[i] = this.can[positions[i]];
			}
			can = applyOneRule(can, u == Unit.FRAME ? sums[grid.getIndex(xy, u)] : 0);

			if (can[0] == 0)
				return false;
			for (int i=0;i<positions.length;i++) {
				if (this.can[positions[i]] == can[i])
					continue;
				this.can[positions[i]] = can[i];
			}
		}
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

}
