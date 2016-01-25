package com.NieveBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NieveBayes{

	HashMap<String,Integer> count=new HashMap<>();
	HashMap<String,Integer> frequencyDestribution=new HashMap<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path="C:\\Users\\Anup\\Desktop\\DM\\BrestCancerData.txt";
		String testPath="C:\\Users\\Anup\\Desktop\\DM\\test.txt";
		NieveBayes util=new NieveBayes();
		util.missingValueAnalysis(util.readDataFile(path,false,"?",false),true);
		util.missingValueAverage(util.readDataFile(path,true,"?",false));
		
		ArrayList<String> attributes =new ArrayList<>();
		attributes.add("SCN");
		attributes.add("CT");
		attributes.add("UCSize");
		attributes.add("UCShape");
		attributes.add("MA");
		attributes.add("SECS");
		attributes.add("BN");
		attributes.add("BC");
		attributes.add("NN");
		attributes.add("Mitoses");
		attributes.add("Class");
		util.initializeCountMap(attributes);
		
		/*ArrayList<String> attriValue =new ArrayList<>();
		attriValue.add("1057013");
		attriValue.add("8");
		attriValue.add("4");
		attriValue.add("5");
		attriValue.add("1");
		attriValue.add("2");
		attriValue.add("?");
		attriValue.add("7");
		attriValue.add("3");
		attriValue.add("1");
		attriValue.add("4");*/
		
		//System.out.println("Class Conditional prob "+util.classConditionalProbabilities(attriValue, "1", util.readDataFile(path,true,"?",false), 6,attributes));
		util.calMaximumPosteriorProbability(util.posteriorProbability(util.readDataFile(path,false,"?",true), util.readDataFile(path,true,"?",false),attributes));
	}

	public ArrayList<ArrayList<String>> readDataFile(String path,boolean isFilter,String valueToSkip,boolean isMissingRequired)
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
			   String arr[]=line.split(",");
			   for(int i=0;i<arr.length;i++)
			   {
				   tuple.add(arr[i]);
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
	
	public void missingValueAnalysis(ArrayList<ArrayList<String>> data,boolean isWrite)
	{
		HashMap<String,String> hm=new HashMap<>();
		ArrayList<String> ar=new ArrayList<>();
		try {
			PrintWriter writer = new PrintWriter("C:\\Users\\Anup\\Desktop\\DM\\filteredRecords.txt", "UTF-8");
			for(ArrayList<String> tuple: data)
			{
				if(tuple.contains("?"))
				{
					//ar.add(tuple.get(0));
					//hm.put(tuple.get(0)," ( "+ tuple.indexOf("?")+" , "+tuple.get(10)+" )");
					System.out.println(tuple.get(0)+" = "+" ( "+ tuple.indexOf("?")+" , "+tuple.get(10)+" )");
					//System.out.println(tuple.toString());
					
					if(isWrite)
					{
						writer.println(tuple.toString());
							
					}
				}
			}
			writer.close();
			System.out.println("-------------> "+ar);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//System.out.println("Missing values "+hm.toString());
	}
	
	public void missingValueAverage(ArrayList<ArrayList<String>> data)
	{
		//HashMap<String,Integer> hm=new HashMap<>();
		int sum=0;
		int count=0;
		for(ArrayList<String> tuple: data)
		{
			if(!tuple.contains("?"))
			{
				System.out.println(tuple.get(6));
				sum=sum+new Integer(tuple.get(6));
				count++;
				if(frequencyDestribution.containsKey(tuple.get(6)))
				{
					int tc=frequencyDestribution.get(tuple.get(6));
					tc++;
					frequencyDestribution.put(tuple.get(6), tc);
				}
				else
				{
					frequencyDestribution.put(tuple.get(6), 1);
				}
			}
		}
		System.out.println("sum "+sum+" count "+count);
		System.out.println("average "+(double)sum/count);
		System.out.println("Frequency Destribution "+frequencyDestribution.toString());
		
		//System.out.println("Missing values "+hm.toString());
	}
	
	public double classConditionalProbabilities(ArrayList<String> attributeSet,String clas,ArrayList<ArrayList<String>> data,int posToPredict,ArrayList<String> attributes)
	{
		initializeCountMap(attributes);
		for(ArrayList<String> tuple: data)
		{
			if(tuple.get(posToPredict).equals(clas))
			{
				for(int i=1;i<tuple.size();i++)
				{
					if(i==posToPredict)
					{
						continue;
					}
					
					if(tuple.get(i).equals(attributeSet.get(i)))
					{
						int sum=count.get(attributeMapper(i));
						sum++;
						count.put(attributeMapper(i), sum);
					}
				}
			}
		}
		
		//System.out.println("counts "+count.toString());
		double classCount=new Double(frequencyDestribution.get(clas));
		double prob=1;
		for(String c:count.keySet())
		{
			if(count.get(c)==0)
			{
				continue;
			}
			prob = prob*(new Double(count.get(c))/classCount);
		}
		return prob;
	}
	
	public void initializeCountMap(ArrayList<String> attributes)
	{
		for(String attribute:attributes)
		{
			count.put(attribute, 0);
		}
	}
	
	public String attributeMapper(int position)
	{
		if(position==1)
		{
			return "CT";
		}else if(position==2)
		{
			return "UCSize";
		}else if(position==3)
		{
			return "UCShape";
		}else if(position==4)
		{
			return "MA";
		}else if(position==5)
		{
			return "SECS";
		}else if(position==6)
		{
			return "BN";
		}else if(position==7)
		{
			return "BC";
		}else if(position==8)
		{
			return "NN";
		}else if(position==9)
		{
			return "Mitoses";
		}else if(position==10)
		{
			return "Class";
		}
		
		return "";
	}
	
	public HashMap<String,HashMap<String,Double>> posteriorProbability(ArrayList<ArrayList<String>> missingValueDatas,ArrayList<ArrayList<String>> data,ArrayList<String> attributes)
	{
		double ccp=0.0;
		HashMap<String,ArrayList<Double>> pp=new HashMap<>();
		int size=data.size();
		HashMap<String,HashMap<String,Double>> postProb=new HashMap<>();
		for(ArrayList<String> tuple: missingValueDatas)
		{
			//System.out.println("****** Posterior Prob for "+ tuple.get(0) +"********* ");
			ArrayList<Double> ppDistribution=new ArrayList<>();
			HashMap<String,Double> postProbDist=new HashMap<>();
			for(int i=1;i<=10;i++)
			{
				ccp=classConditionalProbabilities(tuple, ""+i, data,6,attributes);
				//System.out.println("CCP "+ccp);
			//	ppDistribution.add(ccp*(frequencyDestribution.get(""+1)/683.0));
				postProbDist.put(""+i, ccp*((double)frequencyDestribution.get(""+i)/(double)size));
				//System.out.println("P("+tuple.get(0)+" | "+i+" ) = "+(ccp*(frequencyDestribution.get(""+1)/683.0)));
			}
			//pp.put(tuple.get(0), ppDistribution);
			postProb.put(tuple.get(0), postProbDist);
		}
		System.out.println("postProb "+postProb.toString());
		return postProb;
	}
	
	public void calMaximumPosteriorProbability(HashMap<String,HashMap<String,Double>>  postProb)
	{
		for(String key : postProb.keySet())
		{
			double max=0;
			Iterator it = postProb.get(key).entrySet().iterator();
			String clas=null;
			while (it.hasNext()) {
		        Map.Entry<String,Double> pair = (Map.Entry)it.next();
		        
		      if(max<pair.getValue())
		      {
		    	  max=pair.getValue();
		    	  clas=pair.getKey();
		      }
		    }
			
			System.out.println("SCN "+key+" Pridicted Value = "+clas+" , MaxValue"+max);
		}
	}
}
