package com.zj.retrieval.master;

import java.util.Iterator;
import java.util.List;


public class Utils {
	public static void cleanList(List<? extends Object>... arrays) {
		for (List<? extends Object> array : arrays) {
			if (array == null)
				continue;
			Iterator<? extends Object> iter = array.iterator();
			while (iter.hasNext()) {
				if (iter.next() == null) 
					iter.remove();
			}
		}
	}
}
