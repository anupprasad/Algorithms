package com.anup;

import java.util.ArrayList;


public class NumberOfPartition {
	ArrayList<ArrayList<Integer>> allPossibleSets=new ArrayList<ArrayList<Integer>>();
	static int setSize=9;
	static int numberOfpartition=4;
	public static void main(String args[])
	{
		NumberOfPartition partObject=new NumberOfPartition();
		
		partObject.calculate(setSize, numberOfpartition,new ArrayList<Integer>(),true);
		
		System.out.println(partObject.allPossibleSets);
		partObject.filterDuplicate(partObject.allPossibleSets);
	}
	
	public void calculate(int setLength,int partitionLength,ArrayList<Integer> vector,boolean isFirst)
	{
		int maxElement=0;
		if(isFirst)
		{
			maxElement=setLength-(partitionLength-1);
			 vector=new ArrayList<Integer>();
			vector.add(maxElement);
			int tempPartitionLength=partitionLength-1;
			int tempSetLength=setLength-maxElement;
			while((tempPartitionLength)>=1)
			{
				maxElement=tempSetLength-(tempPartitionLength-1);
				vector.add(maxElement);
				tempSetLength=tempSetLength-maxElement;
				tempPartitionLength--;

			}
		}
		else
		{
			while((partitionLength)>=1)
			{
				maxElement=setLength-(partitionLength-1);
				vector.add(maxElement);
				setLength=setLength-maxElement;
				partitionLength--;

			}
		}
		allPossibleSets.add(vector);
		analyzeVector(vector,setSize,numberOfpartition);
		
	}
	
	public void analyzeVector(ArrayList<Integer> vector,int setLength,int partitionLength)
	{
		int lastElement=vector.size()-1;
		int startOfCompare=lastElement-1;
		ArrayList<Integer> tempVector=new ArrayList<>();
		int tempSetlength=0;
		int tempPartition=0;
		int sum=0;
		while(startOfCompare>=0)
		{
			if((vector.get(startOfCompare)-vector.get(lastElement))>1)
			{
				break;
			}
			else
			{
				startOfCompare--;
			}
		}
		
		if(startOfCompare==-1)
		{
			return;
		}
		else
		{
			tempVector=new ArrayList<Integer>();
			for(int i=0;i<=startOfCompare;i++)
			{
				
				if(i==startOfCompare)
				{
					tempVector.add(vector.get(i)-1);
				}
				else
				{
					tempVector.add(vector.get(i));
				}
			}
				
			//tempVector.add(startOfCompare, vector.get(startOfCompare)-1);
			for(int i=0;i<=tempVector.size()-1;i++)
			{
				sum=sum+tempVector.get(i);
			}
			tempSetlength=setLength-sum;
			tempPartition=partitionLength-tempVector.size();
			calculate(tempSetlength,tempPartition,tempVector,false);
		}
	}
	
	public void filterDuplicate(ArrayList<ArrayList<Integer>> original)
	{
		ArrayList<String> arr=new ArrayList<>();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<=original.size()-1;i++)
		{
			original.get(i).sort(null);
			for(int j=0;j<=original.get(i).size()-1;j++)
			{
				sb.append(original.get(i).get(j));
			}
			
			if(!arr.contains(sb.toString()))
			{
				arr.add(sb.toString());
			}
			
			sb.delete(0, sb.length());
		}
		System.out.println(arr);
	}
}
