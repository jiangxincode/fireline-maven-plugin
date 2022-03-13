package edu.jiangxin.mess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ArrayList遍历的4种方法 
 *
 */
public class ArrayListDemo {

	public static void main(String args[]) {

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");

		{
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + " ");
			}
			System.out.println();
		}
		
		{
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				System.out.print(it.next() + " ");
			}
			System.out.println();
		}
		

		{
			for (String str : list) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
		
		{
			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i) + " ");
			}
			System.out.println();
		}
	}

}
