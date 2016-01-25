package com.aglomerative;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgloUtil {
	
	public static String pathGenre="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\genre.txt";
	public static HashMap<String,ArrayList<String>> genre=new HashMap<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AgloUtil obj=new AgloUtil();
		//System.out.println("----> Matrix"+obj.readDataFile(path, true, "?", false, ",", 0));
		//System.out.println("----> Transpose"+obj.transposeMatrix(obj.readDataFile(path, true, "?", false, ",", 0)));
		//System.out.println("genre ---> "+genre.toString());
		obj.mergeNeighbour();
		
	}

	public ArrayList<ArrayList<String>> transposeMatrix(ArrayList<ArrayList<String>> data)
	{
		ArrayList<ArrayList<String>> transpose=new ArrayList<>();
		
		int row=data.size();
		int col=data.get(0).size();
		for(int j=0;j<col;j++)
		{
			ArrayList<String> tuple=new ArrayList<>();
			for(int i=0;i<row;i++)
			{
				tuple.add(data.get(i).get(j));
			}
			transpose.add(tuple);
			genre.put(generMapper(j), tuple);
		}
		return transpose;
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
	
	
	public double calED(List<String> a, List<String> b )
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
	public void mergeNeighbour()
	{
		ArrayList<ArrayList<String>> genre1=readDataFile(pathGenre, false, "?", false,",", 0);
		findTwoNearestPoint(genre1);
		/*ArrayList<ArrayList<String>> nearestNeighbours=findTwoNearestPoint(genre1);
		ArrayList<ArrayList<String>> newNeighbours=new ArrayList<>();
		ArrayList<String> genreUsed=new ArrayList<>();
		HashMap<String,ArrayList<String>> arr=new HashMap<>();
		for(ArrayList<String> genre:nearestNeighbours)
		{
			ArrayList<List<String>> nearestNeighVal=new ArrayList<>();
			for(String p:genre)
			{
				List<String> temp=new ArrayList<>();
				for(ArrayList<String> s:genre1)
				{
					if(s.get(0).contains(p))
					{
						
						temp.addAll(s.subList(1, s.size()));
						break;
					}
				}
				nearestNeighVal.add(temp);
				
			}
			ArrayList<String> arlist=calMean(nearestNeighVal);
			arlist.add(0, genre.toString());
			newNeighbours.add(arlist);
			genreUsed.addAll(genre);
			arr.put(genre.toString(), calMean(nearestNeighVal));
			
		}
		
		System.out.println(arr);
		for(ArrayList<String> tup:genre1)
		{
			if(!genreUsed.contains(tup.get(0)))
			{
				newNeighbours.add(tup);
			}
		}*/
		
		
		
	}
	public void findTwoNearestPoint(ArrayList<ArrayList<String>> genre1)
	{
		/*ArrayList<ArrayList<String>> genre1=readDataFile(path, false, "?", false,",", 0);*/
		//ArrayList<ArrayList<String>> genre2=readDataFile(pathGenre, false, "?", false,",", 0);
		int i=0;
		int j=0;
		if(genre1.size()==1)
		{
			return;
		}
		String genres=null;
		HashMap<String,Double> hm=new HashMap<>();
		for(ArrayList<String> tup1:genre1)
		{
			j=0;
			String gen1=tup1.get(0);
			double minDistance=9999999;
			for(ArrayList<String> tup2:genre1)
			{
				if(i==j)
				{
					j++;
					continue;
				}
				String gen2=tup2.get(0);
				double distance=calED(tup1.subList(1, tup1.size()), tup2.subList(1,tup2.size()));
				if(distance<minDistance)
				{
					minDistance=distance;
					genres=gen1+","+gen2;
					
				}
				
				j++;
			}
			i++;
			hm.put(genres, minDistance);
		}
		
		
		ArrayList<ArrayList<String>> newNeighbours=new ArrayList<>();
		ArrayList<String> genreUsed=new ArrayList<>();
		HashMap<String,ArrayList<String>> arr=new HashMap<>();
		ArrayList<ArrayList<String>> nearestNeighbours=selectNearestGenre(hm);
		for(ArrayList<String> genre:nearestNeighbours)
		{
			ArrayList<List<String>> nearestNeighVal=new ArrayList<>();
			StringBuilder sb=new StringBuilder();
			for(String p:genre)
			{
				
				List<String> temp=new ArrayList<>();
				for(ArrayList<String> s:genre1)
				{
					if(s.get(0).contains(p))
					{
						sb.append(p);
						temp.addAll(s.subList(1, s.size()));
						break;
					}
				}
				nearestNeighVal.add(temp);
				
			}
			ArrayList<String> arlist=calMean(nearestNeighVal);
			arlist.add(0, sb.toString());
			sb.delete(0, sb.length()-1);
			newNeighbours.add(arlist);
			genreUsed.addAll(genre);
			arr.put(genre.toString(), calMean(nearestNeighVal));
			
		}
		
		System.out.println(arr);
		for(ArrayList<String> tup:genre1)
		{
			if(!genreUsed.contains(tup.get(0)))
			{
				newNeighbours.add(tup);
			}
		}
		findTwoNearestPoint(newNeighbours);
		//return selectNearestGenre(hm);
		
		
	}
	
	public ArrayList<ArrayList<String>> selectNearestGenre(HashMap<String,Double> hm)
	{
		double dist=9999;
		
		for(String genre:hm.keySet())
		{
			if(hm.get(genre)<dist)
			{
				dist=hm.get(genre);
				
			}
		}
		ArrayList<String> bingo=new ArrayList<>();
		ArrayList<ArrayList<String>> pingo=new ArrayList<>();
		for(String genre:hm.keySet())
		{
			if(hm.get(genre)==dist)
			{
				String arr[]=genre.split(",");
				ArrayList<String> unique=new ArrayList<>();
				for(int i=0;i<arr.length;i++)
				{
					unique.add(arr[i]);
				}
				unique.sort(null);
				if(!bingo.contains(unique.toString()))
				{
					bingo.add(unique.toString());
					pingo.add(unique);
				}
				
			}
		}
		return pingo;
	}
	
	public ArrayList<String> calMean(ArrayList<List<String>> tuples)
	{
		int sizeOuter=tuples.size();
		int sizeInner=tuples.get(0).size();
		ArrayList<String> meanArr=new ArrayList<>();
		int mean=0;
		for(int i=0;i<sizeInner;i++)
		{
			/*i = 1 to skip record number . Do not consider it in calculating mean
			 * will consider only integer part of mean . we will not consider decimal
			 *  part of mean. so sum and mean is int instead of decimal .
			 */
			int sum=0;
			for(int j=0;j<sizeOuter;j++)
			{
				sum=sum+(new Integer(tuples.get(j).get(i)));
						
			}
			mean=sum/sizeOuter;
			meanArr.add(""+mean);
		}
		return meanArr;
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
}
