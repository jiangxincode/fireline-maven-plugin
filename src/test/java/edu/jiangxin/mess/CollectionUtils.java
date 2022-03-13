package edu.jiangxin.mess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

	public static <T> boolean isEleTypeOf(Collection<T> coll, Class<?> obj) {
		if(coll == null || coll.size() == 0) {
			return true;
		}

		for(T ele : coll) {
			if(!ele.getClass().equals(obj)) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		List<Object> list = new ArrayList<Object>();
		list.add(new Integer(3));
		list.add(new Integer(4));
		list.add(new Integer(5));
		System.out.println(isEleTypeOf(list, Integer.class));
		list.add(new Long(3));
		System.out.println(isEleTypeOf(list, Integer.class));
	}

}
