package edu.jiangxin.mess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Array和List相互转换
 *
 */
public class Array2List3 {
	
	public static void main(String[] args) {
		
		{
			/**
			 * Array转List
			 */
			String[] strArray = {"aa","bb","cc"};
			List<String> list = new ArrayList<String>();
			Collections.addAll(list, strArray);
			System.out.println(list.toString());
		}
		
		{
			/**
			 * Array转List
			 */
			String[] strArray = {"aa","bb","cc"};
			List<String> list = Arrays.asList(strArray); // 返回一个受指定数组支持的固定大小的列表,不能做add、remove等操作
			System.out.println(list.toString());
		}
		
		{
			/**
			 * Array转List
			 */
			String[] strArray = {"aa","bb","cc"};
			List<String> list = new ArrayList<String>(Arrays.asList(strArray)); // 可以进行add remove操作
			System.out.println(list.toString());
		}
		
		
		{
			/**
			 * Array转List
			 */
			String[] strArray = {"aa","bb","cc"};
			
			List<String> list = new ArrayList<String>(strArray.length);
			for(String str: strArray) {
				list.add(str);
			}
			System.out.println(list.toString());
		}
		
		
		{
			/**
			 * List转Array
			 */
			List<String> list = new ArrayList<String>();
			list.add("aa");
			list.add("bb");
			list.add("cc");
			
			//不要使用强制转换
			//String[] strArray2 = (String[]) list2.toArray();
			
			Object[] objArray = list.toArray(); // 不指定大小
			
			for(Object obj : objArray) {
				System.out.print(obj.toString() + " ");
			}
			System.out.println();
			
		}
		
		{
			/**
			 * List转Array
			 */
			List<String> list = new ArrayList<String>();
			list.add("aa");
			list.add("bb");
			list.add("cc");
			
			String[] strArray = (String[])list.toArray(new String[list.size()]); // 指定大小
			
			for(String str : strArray) {
				System.out.print(str.toString() + " ");
			}
			System.out.println();
			
		}
		
		{
			/**
			 * List转Array
			 */
			List<String> list = new ArrayList<String>();
			list.add("aa");
			list.add("bb");
			list.add("cc");
			
			String[] strArray = new String[list.size()];
			for(int i=0;i<list.size();i++) {
				strArray[i] = list.get(i);
			}
			
			for(String str : strArray) {
				System.out.print(str.toString() + " ");
			}
			System.out.println();
		}
		
	}

}
