/**
 * 
 */
package kisu;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;


/**
 * @author Idiopath
 *
 */
public class SudokuCandidates {

	private SudokuRules rules;
	private int[] can;
	private int[] save;

	/**
	 * 
	 */
	public SudokuCandidates() {
		can = new int[81];
		Arrays.fill(can, Candidates.all);
		rules = new SudokuRules();
		commit();
	}

	public SudokuCandidates(int[] sums) {
		this();
		rules.setSums(sums);
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
	
	public Set<Integer> get(int xy) {
		return Candidates.values(save[xy]);
	}
	
	public boolean remove(int xy, int val) {
		return rem(xy, Candidates.candidate(val));
	}
	
	public boolean remove(int xy, Set<Integer> val) {
		return rem(xy, Candidates.candidates(val));
	}
	
	private boolean rem(int xy, int bits) {
		can[xy] &= Candidates.not(bits);
		
		return can[xy] != 0;
	}
	
	public boolean changed() {
		return !can.equals(save);
	}
	
	public int countSure() {
		int count = 0;
		for (int i : save) {
			if (Candidates.count(i) == 1) {
				count++;
			}
		}
		return count;
	}
	
	public int[] posWithCandidates() {
		TreeSet<Integer> pos = new TreeSet<Integer>();
		for (int i=0;i<can.length;i++) {
			if (Candidates.count(can[i]) > 1) {
				pos.add(i);
			}
		}
		int[] ret = new int[pos.size()];
		int i=0;
		for (int p : pos)
			ret[i++] = p;
			
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
//			if (pos[p] == 41) {
//				Show.showKillersudoku(grid, sums, this, new int[0]);
//				System.exit(0);
//			}
			long start = System.currentTimeMillis();
//			if (p == 57) rules.debug = true;
			int rest = findNextCandidate(pos[p]);
			s.setCharAt(p, (char)('0' + Candidates.count(rest)));
			System.out.println(count + ". p=" + p + " pos[p]=" + pos[p] + ", " + (System.currentTimeMillis() - start) + "ms");
//			Show.showKillersudoku(new int[0], can, new int[0], 1);

//			if (p == 57) System.exit(0);
			
			if (Candidates.first(can[pos[p]]) > 0) {
				if (p + 1 == pos.length) {
					System.out.println(solutions+1 + ". Lösung gefunden, von gesuchten " + solution.length + " nach " + count + " Durchgängen...");
//					Show.showKillersudoku(sums, this, new int[0]);
					int [] sol = can.clone();
					Arrays.setAll(sol, i -> Candidates.value(sol[i]));
					Show.showKillersudoku(new int[0], new int[0], sol, 3, 4);
					solution[solutions] = sol;
					if (++solutions == solution.length)
						break;
				} else {
					save[p] = get();
					save[p][pos[p]] = rest;
					commit();
					
					p++;
					max = pos[p] > max ? pos[p] : max;
//					if (pos[p] == 1) {
//						Show.showKillersudoku(new int[0], can, new int[0], 1, 2, 3, 4);
//						System.exit(0);
//					}
						
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
				can[pos[p]] = rest;
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
//		Crossing c = new Crossing();
		while ((test = Candidates.first(ret)) > 0) {
			can[xy] = test;
			ret -= test;

			if (rules.applyRules(xy, can))
				break;
//			Set<Integer> changed = c.eliminateCrosses(xy, can); 
//			if (changed != null) {
//				one.addTodo(changed);
//				if (one.applyOneRule(xy, can)) {
//					break;
//				}
//			}
			rollback();
			can[xy] = 0;
		}
		return ret;
	}

	
	public boolean setCandidate(int xy, int value) {
		can[xy] = Candidates.candidate(value);

		return rules.applyRules(xy, can);
	}

}
