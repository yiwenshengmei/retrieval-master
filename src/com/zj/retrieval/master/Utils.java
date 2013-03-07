package com.zj.retrieval.master;

import java.util.Iterator;
import java.util.List;


public class Utils {
	public static void cleanList(List... arrays) {
		for (List array : arrays) {
			if (array == null)
				continue;
			Iterator iter = array.iterator();
			while (iter.hasNext()) {
				if (iter.next() == null) 
					iter.remove();
			}
		}
	}
}
