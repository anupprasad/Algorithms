package com.anup;

import java.util.ArrayList;

public class Cosine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public double cosinString(String a,String b)
	{
		ArrayList<Integer> x=convertStringToArrayList(a);
		ArrayList<Integer> y=convertStringToArrayList(b);
		double result = cosine(x,y);
    	return result;
	}
    public double cosine(ArrayList<Integer> x, ArrayList<Integer> y)
    {
    	double result = (double)dotProduct(x, y)/(double)(vectorLength(x)*vectorLength(y));
    	return result;
    }
	public int dotProduct(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		if(x.size()!=y.size())
		{
			System.out.println("Size not equal cannot calculate !");
			return 0;
		}
		int sum=0;
		for(int i=0;i<x.size();i++)
		{
			sum = sum+(x.get(i)*y.get(i));
		}
		
		return sum;
	}
	
	public double vectorLength(ArrayList<Integer> x)
	{
		int sum=0;
		
		for(Integer i:x)
		{
			sum =sum +(i*i);
		}
		return Math.sqrt(sum);
	}
	
	public ArrayList<Integer> convertStringToArrayList(String s)
	{
		if(s.isEmpty())
		{
			System.out.println("Error : String is empty");
			return null;
		}
		ArrayList<Integer> arrList=new ArrayList<Integer>();
		for(int i=0;i<s.length();i++)
		{
			arrList.add(new Integer(""+s.charAt(i)));
		}
		return arrList;
	}
}
