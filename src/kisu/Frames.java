/**
 * 
 */
package kisu;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class Frames {

	enum Direction {
		RIGHT (1), DOWN (2), LEFT (4), UP (8);
		private final int bit;
		Direction(int bit) {
			this.bit = bit;
		}
	}
	
	static TreeMap<Integer, Integer> pos;
	/**
	 * 
	 */
	public Frames() {
	}
	static String generate() {
		pos = new TreeMap<Integer, Integer>();
		
		int frame = 0;
		int xy = 0;
		int free;
		int counter = 0;
		
		do {
			int r = (int)(Math.random() * 8) + 2;
			TreeSet<Integer> dontGoBack = new TreeSet<>();
			for (int i=0;i<r;i++) {
				
				free = 0;
				for (Direction d : Direction.values())
					free |= isFree(dir(d, xy), frame) ? d.bit : 0;
				
				if (Integer.bitCount(free) == 1) {
					pos.put(xy, frame + 40);
					dontGoBack.add(xy);
				} else
					pos.put(xy, frame);
				
				if (free == 0) {
					if (i == 0)
						frame = remove(xy);
					break;
				}
				

				xy = move(xy, free);
			}
			while (dontGoBack.size() > 0) {
				int i = dontGoBack.pollFirst();
				pos.put(i, pos.get(i) - 40);
			}
			
			frame++;
			xy = nextFree();
			counter++;
		} while (xy < 81);
		
		String ret = new String();
		int maxFrame = 0;
		for (int i : pos.values()) {
			ret +=  numberToLetter(i);
			if (i > maxFrame)
				maxFrame =  i; 
		}
		System.out.println("Durchgänge für " + (maxFrame+1) +" Rahmen: " + counter);
		return ret;
	}
	
	static int[] stringToArray(String frames) {
		int [] ret = new int[frames.length()];
		char [] c = frames.toCharArray();
		for (int i = 0;i<ret.length;i++) {
			ret[i] = letterToNumber(c[i]);
			
		}
		return ret;
	}
	
	private static int letterToNumber(char c) {
		switch (c) {
		case 'ä' : return 36;
		case 'ö' : return 37;
		case 'ü' : return 38;
		case 'ß' : return 39;
		}
		return c <= '9' ? 26 + c - '0' : c - 'a';
	}
	private static char numberToLetter(int n) {
		switch (n) {
		case 36 : return 'ä';
		case 37 : return 'ö';
		case 38 : return 'ü';
		case 39 : return 'ß';
		}
		return (char) (n >= 26 ? '0' + (n - 26) : 'a' + n);
	}
	private static int nextFree() {
		int xy = 0;
		while (pos.containsKey(xy)) {
			xy++;
		}
		return xy;
	}

	private static boolean isFree(int xy, int c) {

		if (xy > -1) {
			Integer temp = pos.get(xy);
			if (temp == null || temp == c)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param dir direction
	 * @param xy fromPosition
	 * @return new position or -1 if there is none
	 */
	private static int dir(Direction dir, int xy) {
		switch (dir) {
		case RIGHT : if (xy%9 < 8)
			return xy + 1;
		case DOWN : if (xy/9 < 8)
			return xy + 9;
		case LEFT : if (xy%9 > 0)
			return xy - 1;
		case UP : if (xy/9 > 0)
			return xy - 9;
		}
		return -1;
	}
	
	private static int move(int xy, int directions) {
		int nr = (int) (Math.random() * Integer.bitCount(directions));
		Direction dir = Direction.RIGHT;

		for (Direction d : Direction.values()) {
				dir = d;
			if ((dir.bit & directions) > 0)
				if (nr == 0)
					break;
				else nr--;
		}
		
		return dir(dir, xy);
	}

	private static int remove(int xy) {
		int ab = 0;
		for (Direction d : Direction.values())
				ab = maxFrame(dir(d, xy), ab);
		
		for (int i=0;i<=81;i++)
			if (pos.getOrDefault(i, -1) >= ab)
				pos.remove(i);

		return --ab;
	}
	
	private static int maxFrame(int xy, int max) {
		if (xy > -1) {
			int max2 = pos.getOrDefault(xy, -1);
			if (max2 > max)
				max = max2;
		}
		return max;
	}
	public static String framesToString(int[] frames) {
		String ret = new String();
		for (int f : frames) {
			ret += numberToLetter(f);
		}
		return ret;
	}
}
