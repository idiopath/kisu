package kisu;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OneRule {
	
	Set<Set<Integer>> todo;
	int[] can;
	Map<Set<Integer>, Integer> sums;
	
	public OneRule() {
		todo = new LinkedHashSet<>();
		sums = new HashMap<>();
	}
	
	public void setSums(int[] sum) {

		for (int i=0;i<sum.length;i++)
			sums.put(Grid.getPosition(i, Unit.FRAME), sum[i]);
	}
	
	public boolean applyOneRule(int xy, int[] can) {
		this.can = can;
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
			if (todo.size() > s)
				System.out.print("\ntodo " + Grid.getPosition(e.getValue(), e.getKey()) + "[" + todo.size() + "] ");
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
				addTodo(p);
				this.can[p] = can[i];
			}
			i++;
		}
		todo.remove(positions);
		System.out.print("{" + todo.size() + "} ");
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


}
