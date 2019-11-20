package kisu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.TreeSet;

public class Grid {
	enum Unit {ROW, COLUMN, BLOCK, FRAME};

	ArrayList<EnumMap<Unit, TreeSet<Integer>>> position;
	ArrayList<EnumMap<Unit, Integer>> index;
	
	boolean existFrames = false;

	public Grid() {
		position = new ArrayList<EnumMap<Unit, TreeSet<Integer>>>();
		index = new ArrayList<EnumMap<Unit, Integer>>();

		for (int i=0;i<9;i++) { 
			position.add(new EnumMap<Unit, TreeSet<Integer>>(Unit.class));
			for (Unit u : Unit.values())
				position.get(i).put(u, new TreeSet<Integer>());
			
			for (int j=0;j<9;j++){
				position.get(i).get(Unit.ROW).add(i*9 + j);
				position.get(i).get(Unit.COLUMN).add(i + j*9);
				position.get(i).get(Unit.BLOCK).add((i/3) * 27 + i%3*3 + j%3 + (j/3) * 9);

				index.add(new EnumMap<>(Unit.class));
				
			}
		}
		for (int i=0;i<9;i++) {
			for (Unit u : Unit.values()) {
				for (int j : position.get(i).get(u)) {
					index.get(j).put(u, i);
				}
			}
		}
	}
	void setFrames (int[] frames) {
		for (int i=0;i<frames.length;i++) {
			index.get(i).put(Unit.FRAME, frames[i]);
			if (position.size() == frames[i]) {
				position.add(new EnumMap<>(Unit.class));
			}
			if (!position.get(frames[i]).containsKey(Unit.FRAME)) {
				position.get(frames[i]).put(Unit.FRAME, new TreeSet<>());
			}
			position.get(frames[i]).get(Unit.FRAME).add(i);
		}
		existFrames =true;
	}
	int getIndex(int pos, Unit u) {
		if (!existFrames && u == Unit.FRAME)
			return -1;
		return index.get(pos).get(u);
	}
	int [] getPos(Unit u, int index) {
		if (!existFrames && u == Unit.FRAME)
			return new int[0];
		return toArray(position.get(index).get(u));
	}
	int [] getPos(int pos, Unit u) {
		if (!existFrames && u == Unit.FRAME)
			return new int[0];
		return toArray(position.get(getIndex(pos, u)).get(u));
	}
	int[] intersect(int a, Unit ua, int b, Unit ub) {
		if (!existFrames && (ua == Unit.FRAME || ub == Unit.FRAME))
			return new int[0];
		TreeSet<Integer> temp = new TreeSet<Integer>();
		temp.addAll(position.get(a).get(ua));
		temp.retainAll(position.get(b).get(ub));
		return toArray(temp);
	}
	
	void print() {
		for (int i=0;i<position.size();i++) {
			System.out.println("Pos " + i + ": " + position.get(i));
		}
		System.out.println("Index " + index);
	}
	private int[] toArray(Collection<Integer> collection) {
		int[] ret = new int[collection.size()];
		int i = 0;
		for (int c : collection) {
			ret[i++] = c;
		}
		
		return ret;
	}

}
