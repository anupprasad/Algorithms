package com.knn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class KNearestNeighbour {
	public static String pathMovie="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\movie.txt";
	public static String pathTransaction="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\dataPoint.txt";
	public static int K=5;
	public static String movies="2,5,8,9";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		KNearestNeighbour knn=new KNearestNeighbour();
	     ArrayList<String> test=knn.convertMovieIntoTransaction(knn.readDataFile(pathMovie, false, "?", false, "\t",0), movies);
	     ArrayList<ArrayList<String>> train=knn.readDataFile(pathTransaction, true, "?", false, ",",0);
	     TreeMap<String,Double> neighbour=new TreeMap<>();
	     int i=1;
	     for(ArrayList<String> tuple:train)
	     {
	    	 double distance= knn.calED(test, tuple);
	    	 neighbour.put("CDI"+i, distance);
	    	 i++;
	    	
	     }
	     
	     System.out.println("All neighbour "+neighbour);
	     System.out.println("Nearest Neighbour "+knn.findKnearestNeighbour(neighbour, K));
		
		
	}
	
	public ArrayList<String> convertMovieIntoTransaction(ArrayList<ArrayList<String>> data,String movies)
	{
		ArrayList<Integer> movieList=convertStringToArrayList(movies);
		Integer arr[]=new Integer[data.get(0).size()-1];
		for(int i=0;i<arr.length;i++)
		{
			arr[i]=0;
		}
		for(Integer i:movieList)
		{
			ArrayList<String> tuple=data.get(i-1);
			for(int j=1;j<tuple.size();j++)
			{
				if(tuple.get(j).equals("1"))
				{
					if(arr[j-1]==0)
					{
						arr[j-1]=1;
					}else
					{
						int count=0;
						count=arr[j-1];
						count++;
						arr[j-1]=count;
					}
				}
			}
			
		}
		ArrayList<String> out=new ArrayList<>();
		for(int i=0;i<arr.length;i++)
		{
			out.add(""+arr[i]);
		}
		return out;
		
	}
	public ArrayList<ArrayList<String>> readDataFile(String path,boolean isFilter,String valueToSkip,boolean isMissingRequired,String deliminitor,int attPos)
	{
		ArrayList<ArrayList<String>> data=null;
		try {
			FileReader dataPointReader =  new FileReader(path);
			 BufferedReader bufferedReader = new BufferedReader(dataPointReader);
			 String line=null;
			 if(isFilter&&isMissingRequired)
			 {
				 System.out.println("Error ! Both flags Cannot be same");
				 bufferedReader.close();
				 return null;
			 }
			 data = new ArrayList<>();
			 while(( line = bufferedReader.readLine()) != null)
			 {
				 if(isFilter)
				 {
					 if(line.contains(valueToSkip))
					 {
						 continue;
					 }
				 }
				 if(isMissingRequired)
				 {
					 if(!line.contains(valueToSkip))
					 {
						 continue;
					 }
				 }
				 ArrayList<String> tuple=new ArrayList<>();
			   String arr[]=line.split(deliminitor);
			   if(isFilter)
			   {
				   for(int i=0;i<arr.length;i++)
				   {
					   if(i==attPos)
					   {
						   continue;
					   }
					   tuple.add(arr[i].trim());
				   }
			   }
			   else
			   {
				   for(int i=0;i<arr.length;i++)
				   {
					   
					   tuple.add(arr[i].trim());
				   }
			   }
			  
			   data.add(tuple);
			 }
			 bufferedReader.close();   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ArrayList<Integer> convertStringToArrayList(String s)
	{
		if(s.isEmpty())
		{
			System.out.println("Error : String is empty");
			return null;
		}
		ArrayList<Integer> arrList=new ArrayList<Integer>();
		if(s.contains(","))
		{
			String arr[]=s.split(",");
			for(String a: arr)
			{
				arrList.add(new Integer(a));
			}
		}
		else
		{
			for(int i=0;i<s.length();i++)
			{
				arrList.add(new Integer(""+s.charAt(i)));
			}
		}
		
		
		return arrList;
	}

	public double calED(ArrayList<String> a, ArrayList<String> b )
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
			 double temp=new Double(a.get(i))-new Double(b.get(i));
			 sum = sum+(temp*temp);
		 }
		  sum=Math.sqrt(sum);
		  
		  return sum;
		 
	}
	
	public ArrayList<String> findKnearestNeighbour(TreeMap<String,Double> tm,int k)
	{
		
		ArrayList<String> neighbour=new ArrayList<>();
		for(int i=1;i<=k;i++)
		{
			double lowest=999999;
			String temp=null;
			for(String key:tm.keySet())
			{
				if(neighbour.contains(key))
				{
					continue;
				}
				if(tm.get(key)<lowest)
				{
					lowest=tm.get(key);
					temp=key;
				}
			}
			
			neighbour.add(temp);
		}
		return neighbour;
	}
}
