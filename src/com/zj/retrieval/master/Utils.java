package com.zj.retrieval.master;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
	
	public static String null2Empty(String value) {
		return (value == null ? StringUtils.EMPTY : value);
	}
}
