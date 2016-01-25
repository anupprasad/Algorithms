package com.dataprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateTransaction {

	public static String pathMovie="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\movie.txt";
	public static String pathTransaction="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\transaction.txt";
	public static String dataPoint="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\dataPoint.txt";
	public static HashMap<String,Integer> generCountMap=new HashMap<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GenerateTransaction gt=new GenerateTransaction();
		gt.readFile(pathMovie,"\t");
		
		gt.generateDataPoints(gt.readFile(pathMovie,"\t"), gt.readFile(pathTransaction," "));
		gt.writeFile(gt.generateDataPoints(gt.readFile(pathMovie,"\t"), gt.readFile(pathTransaction," ")));
	}

	public HashMap<String,ArrayList<String>> readFile(String path,String deliminator)
	{
		HashMap<String,ArrayList<String>> data=null;
		try {
			FileReader dataPointReader =  new FileReader(path);
			 BufferedReader bufferedReader = new BufferedReader(dataPointReader);
			 String line=null;
			
			 data = new HashMap<>();
			 while(( line = bufferedReader.readLine()) != null)
			 {
				
				 ArrayList<String> tuple=new ArrayList<>();
			   String arr[]=line.split(deliminator);
			   for(int i=1;i<arr.length;i++)
			   {
					   tuple.add(arr[i].trim());
				   
			   }
			   data.put(arr[0],tuple);
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
	
	public HashMap<String,HashMap<String,Integer>> generateDataPoints(HashMap<String,ArrayList<String>> movie,HashMap<String,ArrayList<String>> transaction)
	{
		HashMap<String,HashMap<String,Integer>> dataPoint=new HashMap<>();
		
		for(String custID:transaction.keySet())
		{
			HashMap<String,Integer> generCount=new HashMap<>();
			initializeMovieMap(generCount);
			for(String movieID:transaction.get(custID))
			{
				ArrayList<String> temp=movie.get(movieID);
				for(int i=0;i<temp.size();i++)
				{
					if(temp.get(i).equals("1"))
					{
						if(generCount.containsKey(generMapper(i)))
						{
							int count=0;
							count=generCount.get(generMapper(i));
							count++;
							generCount.put(generMapper(i), count);
						}
						
					}
					
				}
			}
			dataPoint.put(custID, generCount);
			
		}
		return dataPoint;
	}
	
	public void initializeMovieMap(HashMap<String,Integer> generCountMap)
	{
		
		generCountMap.put("r", 0);
		generCountMap.put("s", 0);
		generCountMap.put("h", 0);
		generCountMap.put("c", 0);
		generCountMap.put("d", 0);
		generCountMap.put("a", 0);
		generCountMap.put("o", 0);
		generCountMap.put("l", 0);
		//return generCountMap;
		
	}
	public String generMapper(int index)
	{
		if(index==0)
		{
			return "r";
		}
		else if(index==1)
		{
			return "s";
		}
		else if(index==2)
		{
			return "h";
		}
		else if(index==3)
		{
			return "c";
		}
		else if(index==4)
		{
			return "d";
		}
		else if(index==5)
		{
			return "a";
		}
		else if(index==6)
		{
			return "o";
		}
		else if(index==7)
		{
			return "l";
		}
		
		return null;
		
	}
	
	public void writeFile(HashMap<String,HashMap<String,Integer>> datapoints)
	{
		try {
			PrintWriter writer = new PrintWriter(dataPoint, "UTF-8");
			for(String custId:datapoints.keySet())
			{
				HashMap<String,Integer> temp=datapoints.get(custId);
				String tuple[]=new String[9];
						
				for(String gener:temp.keySet())
				{
					tuple[generPosition(gener)]=""+temp.get(gener);
					
				}
				ArrayList<String> tup=new ArrayList<>();
				for(int i=0;i<tuple.length;i++)
				{
					
					if(i==0)
					{
						tup.add(custId);
					}
					
						tup.add(tuple[i]);
					
				}
				writer.println(tup.toString());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int generPosition(String val)
	{
		if(val.equals("r"))
		{
			return 0;
		}
		else if(val.equals("s"))
		{
			return 1;
		}
		else if(val.equals("h"))
		{
			return 2;
		}
		else if(val.equals("c"))
		{
			return 3;
		}
		else if(val.equals("d"))
		{
			return 4;
		}
		else if(val.equals("a"))
		{
			return 5;
		}
		else if(val.equals("o"))
		{
			return 6;
		}
		else if(val.equals("l"))
		{
			return 7;
		}
		
		return -1;
		
	}
	
	
}
