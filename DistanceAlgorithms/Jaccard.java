package com.anup;

import java.util.ArrayList;

public class Jaccard {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public double jaccardString(String a, String b)
	{
		Cosine co=new Cosine();
		ArrayList<Integer> x=co.convertStringToArrayList(a);
		ArrayList<Integer> y=co.convertStringToArrayList(b);
		return jaccard(x, y);
	}
	
	public double extendedJaccardString(String a, String b)
	{
		Cosine co=new Cosine();
		ArrayList<Integer> x=co.convertStringToArrayList(a);
		ArrayList<Integer> y=co.convertStringToArrayList(b);
		return extendedJaccard(x, y);
	}
	
	public double extendedJaccard(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		double sum=0.0;
		Cosine cos=new Cosine();
		sum= (double)cos.dotProduct(x, y)/(double)(cos.vectorLength(x)*cos.vectorLength(x)+cos.vectorLength(y)*cos.vectorLength(y)-cos.dotProduct(x, y));
		return sum;
	}
	public double jaccard(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		double sum=-1;
		try
		{
		sum = (double)f11(x, y)/(double)(f01(x, y)+f10(x, y)+f11(x, y));
		}
		catch(ArithmeticException ex)
		{
			
		}
		return sum;
	}
	
	public int f00(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		int sum=0;
		if(x.size()!=y.size())
		{
			System.out.println("cannot find distance as length is not equal");
			return -1;
		}
		for(int i=0;i<x.size();i++)
		{
			if(x.get(i) == 0 && y.get(i)==0)
			{
				sum++;
			}
		}
		return sum;
	}
	

	
	public int f01(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		int sum=0;
		if(x.size()!=y.size())
		{
			System.out.println("cannot find distance as length is not equal");
			return -1;
		}
		for(int i=0;i<x.size();i++)
		{
			if(x.get(i) == 0 && y.get(i)==1)
			{
				sum++;
			}
		}
		return sum;
	}
	
	public int f10(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		int sum=0;
		if(x.size()!=y.size())
		{
			System.out.println("cannot find distance as length is not equal");
			return -1;
		}
		for(int i=0;i<x.size();i++)
		{
			if(x.get(i) == 1 && y.get(i)==0)
			{
				sum++;
			}
		}
		return sum;
	}
	public int f11(ArrayList<Integer> x, ArrayList<Integer> y)
	{
		int sum=0;
		if(x.size()!=y.size())
		{
			System.out.println("cannot find distance as length is not equal");
			return -1;
		}
		for(int i=0;i<x.size();i++)
		{
			if(x.get(i)== 1 && y.get(i)==1)
			{
				sum++;
			}
		}
		return sum;
	}
	
	public int bitDifference(String x, String y)
	{
		int sum=0;
		if(x.length()!=y.length())
		{
			System.out.println("cannot find distance as length is not equal");
			return -1;
		}
		for(int i=0;i<x.length();i++)
		{
			if(x.charAt(i)!=y.charAt(i))
			{
				sum++;
			}
		}
		return sum;
	}
	
}
