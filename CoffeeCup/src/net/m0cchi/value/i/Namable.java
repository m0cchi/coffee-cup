package net.m0cchi.value.i;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface Namable {
	public static final String DEF_STRING = "def";
	public static final int A = 65;
	public static final int Z = 90;
	public static final int SUB = 32; // a - A

	default String getName() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<Integer> points = new ArrayList<>();
		String orignString = this.getClass().getSimpleName();
		byte[] origin = orignString.getBytes();
		for (int i = 0; i < origin.length; i++) {
			byte ch = origin[i];
			if (ch >= A && ch <= Z) {
				points.add(i);
				origin[i] = (byte) (origin[i] + SUB);
			}
		}

		if (points.size() == 0 || points.size() == 1) {
			return new String(origin);
		}

		points.remove(0);

		if (DEF_STRING.equalsIgnoreCase(new String(origin, 0, points.get(0)))) {
			points.remove(0);
			if (points.size() == 0) {
				return new String(origin);
			}
		}

		int before = 0;
		Iterator<Integer> it = points.iterator();
		while (it.hasNext()) {
			baos.write(origin, before, (-before + (before += it.next())));
			if (it.hasNext()) {
				baos.write('-');
			} else {
				baos.write('-');
				baos.write(origin, before, origin.length - before);
			}
		}

		String ret = baos.toString();
		try {
			baos.close();
		} catch (IOException e) {
		}

		return ret;
	}
}
