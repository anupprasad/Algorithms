package com.anup;

import java.util.ArrayList;

public class EculidianDistance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EculidianDistance ob=new EculidianDistance();
		ArrayList<Integer> c1=new ArrayList<>();
		c1.add(10);
		c1.add(7);
		
		ArrayList<Integer> c2=new ArrayList<>();
		c2.add(16);
		c2.add(9);
		
		ArrayList<Integer> d1=new ArrayList<>();
		d1.add(2);
		d1.add(5);
		
		ArrayList<Integer> d2=new ArrayList<>();
		d2.add(26);
		d2.add(28);
		double sum=ob.calculate(d1, c1)+ob.calculate(d2, c1)+ob.calculate(d1, c2)+ob.calculate(d2, c2);
		System.out.println("Euclidian distance "+sum);
		
	}

	public double calculate(ArrayList<Integer> a, ArrayList<Integer> b )
	{
		 if(a.size()!=b.size())
		 {
			 System.err.println("cannot calculate eculidian distance !");
			 return 0.0;
		 }
		 
		 int length=a.size();
		 double sum=0;
		 for(int i=0;i<length;i++)
		 {
			 double temp=a.get(i)-b.get(i);
			 sum = sum+(temp*temp);
		 }
		  sum=Math.sqrt(sum);
		  
		  return sum;
		 
	}
}
