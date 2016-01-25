package com.dataprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import com.NieveBase.Util;



public class KMeanUtil {
  
	 private static String path="C:\\Users\\Anup\\Desktop\\DM\\BrestCancerData.txt";
	 private static String testPath="C:\\Users\\Anup\\Desktop\\DM\\test.txt";
	 static int iteration=0;
	 static String attributesToConsider="1,2,3,4,5,6,7,8,9,10";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		KMeanUtil kmeanObj=new  KMeanUtil();
		//kmeanObj.Initialize(util.readDataFile(path, true, "?", false), 4);
		//System.out.println("initial centroids"+KMean.initialCentroids);
		
		//kmeanObj.calEntropyAndVariance(kmeanObj.readDataFile(path, true, "?", false,"1,2,3,4,5,6,7,8,9,10"), "123456789",false);
		kmeanObj.calEntropyAndVariance(kmeanObj.readDataFile(path, true, "?", false,"1,2,3,4,5,6,7,8,9,10"), "0123456789",false);
		HashMap<String, ArrayList<Integer>> input=kmeanObj.conFreqDistrToHasArray(kmeanObj.calEntropyAndVariance(kmeanObj.readDataFile(path, true, "?", false,attributesToConsider), "0123456789",true),attributesToConsider);
		System.out.println("************* Correlation(X,Y) *****************");
		//System.out.println("************* KL-Divergance(X,Y) *****************");
		for(String attr: input.keySet())
		{
			for(String attr2:input.keySet())
			{
				/*if(attr.equals(attr2))
				{
					continue;
				}*/
				
				System.out.println("CoRRelation( "+attr+" , "+attr2+" ) = "+kmeanObj.calCorrelation(input.get(attr),input.get(attr2)));
				
				//System.out.println("KL-Divergance( "+attr+" , "+attr2+" ) = "+kmeanObj.calKLDivergence(input.get(attr),input.get(attr2)));
				
			}
		}
	}

	
	public void  Initialize(ArrayList<ArrayList<String>> data,int numberOfClusters,boolean isRandom,String attToConsider,String skipSequence)
	{
		Random rand=new Random();
		 
		HashMap<String,ArrayList<String>> initialCentroids=KMean.initialCentroids;
		int size=data.size();
		ArrayList<Integer> duplicateRand=new ArrayList<>();
		if(size<numberOfClusters)
		{
			System.err.println("invalid cluster size ! Please Reduce cluster size");
			return ;
		}
		if(isRandom)
		{
			for(int i=0;i<numberOfClusters;i++)
			{
				
				int randNum=rand.nextInt(size);

				while(duplicateRand.contains(randNum))
				{
					randNum=rand.nextInt(size);
				}
				duplicateRand.add(randNum);

				initialCentroids.put("Centroid"+(i+1),data.get(randNum));
				System.out.println("Tuple Picked as centroid "+randNum);
				KMean.recorNumber.append(randNum+",");
			}
		}
		else
		{
			ArrayList<Integer> positionOfTuples=new ArrayList<>();
			String arr2[]=KMean.SCN.split(",");
			 ArrayList<Integer> attributes=convertStringToArrayList(attToConsider);
			if(KMean.SCN.isEmpty()||arr2.length==0)
			{
				System.err.println("Cannot initialize centroids please inter some value ");
				return;
			}
			
			for(String s:arr2)
			{
				positionOfTuples.add(new Integer(s));
			}
			
			FileReader dataPointReader;
			try {
				dataPointReader = new FileReader(path);
				BufferedReader bufferedReader = new BufferedReader(dataPointReader);
				String line=null;
				int i=0;
				int j=0;
				 while(( line = bufferedReader.readLine()) != null)
				 {
					 if(line.contains(skipSequence))
					 {
						 continue;
					 }
					if(i==positionOfTuples.get(j))
					{
						j++;
						 ArrayList<String> tuple=new ArrayList<>();
						   String arr[]=line.split(",");
						   for(int k=0;k<arr.length;k++)
						   {
							   if(attributes.contains(k))
							   {
								   tuple.add(arr[k]);
							   }
							   
						   }
						   initialCentroids.put("Centroid"+(j),tuple);
					}
					if(j==positionOfTuples.size())
					{
						break;
					}
					
					i++;
				 }
				 bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Inicialization of centroid is over");
		//return initialCentroids;
	}
	
	public double euclideanDistance(ArrayList<Integer> a, ArrayList<Integer> b )
	{
		 if(a.size()!=b.size())
		 {
			 System.err.println("cannot calculate eculidian distance !");
			 System.out.println("A "+a);
			 System.out.println("B "+b);
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
	/*data : filtered data
	 *centroids : 
	 *            string : centroid name
	 *            ArrayList<String> : [1,3,4,2,1,4,3,1]
	 * return cluster
	 *              centroid1
	 *                       [1,2,1,4,5,3]
	 *                       [2,3,1,1,3,4]
	 *                       [1,1,1,2,1,2]
	 *                       
	 *              centroid2
	 *                       [2,2,2,3,4]
	 *                       [1,2,3,4,5]
	 *                       
	 */
	public HashMap<String,ArrayList<ArrayList<String>>> assignPointsToNearestCentroid(ArrayList<ArrayList<String>> data,HashMap<String,ArrayList<String>> centroids)
	{
		String centroid=null;
		double distance=0.0;
		double minDistance=9999999;
		HashMap<String,ArrayList<ArrayList<String>>> cluster=new HashMap<>();
		for(ArrayList<String> tuple: data)
		{
			minDistance=9999999;
			ArrayList<Integer> tempTup=convertStringToIntegerArray(tuple);
			for(String cent:centroids.keySet())
			{
				distance=euclideanDistance(tempTup, convertStringToIntegerArray(centroids.get(cent)));
				if(distance<minDistance)
				{
					centroid=cent;
					minDistance=distance;
				}
			}
			if(cluster.containsKey(centroid))
			{
				cluster.get(centroid).add(tuple);
			}
			else
			{
				ArrayList<ArrayList<String>> member=new ArrayList<>();
				member.add(tuple);
				cluster.put(centroid, member);
			}
			
		}
		//System.out.println("clusters "+cluster.toString());
		publishResult(cluster);
		return cluster;
	}
	
	public ArrayList<Integer> convertStringToIntegerArray(ArrayList<String> data)
	{
		ArrayList<Integer> temp=new ArrayList<>();
		int i=0;
		for(String s: data)
		{
			/*if(i==0)
			{
				Skip record number . do not consider it in calculating distance
				i++;
				continue;
			}*/
			
			temp.add(new Integer(s.trim()));
		}
		
		return temp;
	}
	
	public void reCalCentroids(HashMap<String,ArrayList<ArrayList<String>>> cluster,int iteration)
	{
		
		if(iteration==0)
		{
			copyCurrCentroidToPrevCentroid(KMean.previousCentroid,KMean.initialCentroids);
		}
		else
		{
			copyCurrCentroidToPrevCentroid(KMean.previousCentroid,KMean.currentCentroids);
		}
		for(String  centroid:cluster.keySet())
		{
			/*
			 * Adding a fake record number at the beginning .
			 */
			ArrayList<String> newMean=calMean(cluster.get(centroid));
			//newMean.add(0,"12345");
			KMean.currentCentroids.put(centroid, newMean);
		}
		System.out.println("Centroid recalculation over !");
	}
	
	public ArrayList<String> calMean(ArrayList<ArrayList<String>> tuples)
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
	
	public void copyCurrCentroidToPrevCentroid(HashMap<String,ArrayList<String>> prev,HashMap<String,ArrayList<String>> cur)
	{
		for(String s: cur.keySet())
		{
			prev.put(s, cur.get(s));
		}
	}
	
	public double calDiffBTWCentroids(HashMap<String,ArrayList<String>> prev,HashMap<String,ArrayList<String>> cur)
	{
		double distance=0.0;
		for(String key:prev.keySet())
		{
			ArrayList<Integer> previousCentroid=convertStringToIntegerArray(prev.get(key));
			for(String key2:cur.keySet())
			{
				distance=distance+euclideanDistance(previousCentroid, convertStringToIntegerArray(cur.get(key2)));
			}
		}
		
		return distance;
	}
	
	public void bootStrap(int k,int attNumber)
	{
		Random rand=new Random();
		//KMean bootStrapedCenroid
		ArrayList<String> centroids=null;
		for(int i=0;i<k;i++)
		{
			centroids=new ArrayList<>();
			for(int j=0;j<attNumber;j++)
			{
				int num=rand.nextInt(10);
			
					
			
					centroids.add(""+num);
				
				
			}
			KMean.bootStrapedCenroid.put("Centroid"+(i+1), centroids);
		}
	}
	
	public void publishResult(HashMap<String,ArrayList<ArrayList<String>>> cluster)
	{
		System.out.println("*********************** Iteration NO "+iteration+" ***********************");
		int benign=0;
		int malignant=0;
		int missing=0;
		GenerateTransaction tn=new GenerateTransaction();
		
		if(cluster.size()!=KMean.K)
		{
			System.err.println("Desired Number of Cluster cannot be created !");
			return;
		}
		int TP=0;
		int FP=0;
		for(String s:cluster.keySet())
		{
			HashMap<String,Integer> generCountMap=new HashMap<>();
			tn.initializeMovieMap(generCountMap);
			benign=0;
			malignant=0;
			missing=0;
			ArrayList<ArrayList<String>> arr=cluster.get(s);
			for(ArrayList<String> tuple:arr)
			{
				/*if(tuple.get(tuple.size()-1).equals("2"))
				{
					benign++;
				}
				else if(tuple.get(tuple.size()-1).equals("4"))
				{
					malignant++;
				}
				else
				{
					missing++;
				}*/
				for(int i=0;i<tuple.size();i++)
				{
					int count=0;
					if(!tuple.get(i).equals("0"))
					{
						count=generCountMap.get(tn.generMapper(i));
						count++;
						generCountMap.put(tn.generMapper(i), count);
					}
					
				}
				
				
			}
			
			System.out.println(s +" Size "+arr.size()+" gener Distribution "+ generCountMap);
		}
	
		iteration++;
	}
	
	public int randGenerate()
	{
		int arr[]={2,4,2,4,2,4,2,4,2,4};
		Random ran=new Random();
		return arr[ran.nextInt(9)];
	}
	
	public HashMap<String, HashMap<String,Integer>> calEntropyAndVariance(ArrayList<ArrayList<String>> tuples,String posToConsider,boolean isEntropy )
	{
		int sizeOuter=tuples.size();
		int sizeInner=tuples.get(0).size();
		//ArrayList<String> meanArr=new ArrayList<>();
		ArrayList<Integer> positions=convertStringToArrayList(posToConsider);
		HashMap<String, HashMap<String,Integer>> frequencyDistribution=new HashMap<>();
		ArrayList<String> att=getAttributeName(attributesToConsider);
		//int mean=0;
		for(int i=0;i<sizeInner;i++)
		{
			/*i = 1 to skip record number . Do not consider it in calculating mean
			 * will consider only integer part of mean . we will not consider decimal
			 *  part of mean. so sum and mean is int instead of decimal .
			 */
			if(!positions.contains(i))
			{
				continue;
			}
			HashMap<String, Integer> attValDistribution=new HashMap<>();
			//int sum=0;
			for(int j=0;j<sizeOuter;j++)
			{
				String key=""+tuples.get(j).get(i);
				if(attValDistribution.containsKey(key))
				{
					int sum=0;
					sum=attValDistribution.get(key);
					sum++;
					attValDistribution.put(key, sum);
				}
				else
				{
					attValDistribution.put(key,1);
				}
						
			}
			frequencyDistribution.put(att.get(i), attValDistribution);
		
		}
		
		if(isEntropy)
		{
		HashMap<String,Double> entropyDestribution=new HashMap<>();
		for(String attribute:frequencyDistribution.keySet())
		{
			Iterator it = frequencyDistribution.get(attribute).entrySet().iterator();
			String clas=null;
			double entropy=0.0;
			while (it.hasNext()) 
			{
				double probablity=0.0;
		        Map.Entry<String,Integer> pair = (Map.Entry)it.next();
		        probablity=(double)pair.getValue()/(double)sizeOuter;
		        entropy=entropy+probablity*(Math.log10(probablity)/Math.log10(2));
			}
			entropyDestribution.put(attribute, -entropy);
		}
		/*for(String index:entropyDestribution.keySet())
		{
			System.out.println(att.get(new Integer(index))+" = "+entropyDestribution.get(index));
			
		}*/
		
		System.out.println("Entropy "+entropyDestribution.toString());
		}
		else
		{
			HashMap<String,Double> variance=new HashMap<>();
			for(String attribute:frequencyDistribution.keySet())
			{
				Iterator it = frequencyDistribution.get(attribute).entrySet().iterator();
				int i=0;
				
				double sum=0.0;
				while (it.hasNext()) 
				{
					i++;
					
			        Map.Entry<String,Integer> pair = (Map.Entry)it.next();
			        sum=sum+pair.getValue();
			        
			        
				}
				Iterator itVariance = frequencyDistribution.get(attribute).entrySet().iterator();
				double mean=(double)sum/(double)i;
				double sumOfDiffSqr=0.0;
				while (itVariance.hasNext()) 
				{
					
			        Map.Entry<String,Integer> pair = (Map.Entry)itVariance.next();
			        double difference=0.0;
			        difference=pair.getValue()-mean;
			        sumOfDiffSqr=sumOfDiffSqr+(difference*difference);
			        
				}
				
				variance.put(attribute, (sumOfDiffSqr/(double)(i-1)));
			}
			 
			/*for(String index:variance.keySet())
			{
				System.out.println(att.get(new Integer(index))+" = "+variance.get(index));
				
			}*/
			System.out.println("variance "+variance.toString());
		}
		System.out.println("frequency "+frequencyDistribution.toString());
		System.out.println(conFreqDistrToHasArray(frequencyDistribution,attributesToConsider));
		return frequencyDistribution;
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
	
	public ArrayList<ArrayList<String>> readDataFile(String path,boolean isFilter,String valueToSkip,boolean isMissingRequired,String attToConsider)
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
			 ArrayList<Integer> attributes=convertStringToArrayList(attToConsider);
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
				   if(attributes.contains(i))
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
	
	public HashMap<String, ArrayList<Integer>> conFreqDistrToHasArray(HashMap<String, HashMap<String,Integer>> frequencyDistribution,String valToConsider)
	{
		/*valToConsider value of attribute which is from 1 to 10*/
		String arr[]=valToConsider.split(",");
		
		HashMap<String, ArrayList<Integer>> attributeDistribution=new HashMap<>();
		for(String attribute:frequencyDistribution.keySet())
		{
			int i=0;
			HashMap<String,Integer> missedValues=new HashMap<>();
			Iterator it = frequencyDistribution.get(attribute).entrySet().iterator();
			String clas=null;
			ArrayList<Integer> values=new ArrayList<>();
			while (it.hasNext()) 
			{
		        Map.Entry<String,Integer> pair = (Map.Entry)it.next();
		        if(arr[i].equals(pair.getKey()))
		        {
		        	 values.add(pair.getValue());
		        }
		        else
		        {
		        	values.add(0);
		        	missedValues.put(""+(i+1), pair.getValue());
		        }
		       i++;
			}
			for(String pos:missedValues.keySet())
			{
				values.add(new Integer(pos), missedValues.get(pos));
			}
			attributeDistribution.put(attribute, values);
		}
		return attributeDistribution;
		
	}
	
	public double calCorrelation(ArrayList<Integer> a,ArrayList<Integer> b)
	{
		double coRRelation=0.00;
		coRRelation=calCovariance(a, b)/(calStandard_Deviation(a)*calStandard_Deviation(b));
		return coRRelation;
	}
	
	public double calCovariance(ArrayList<Integer> a,ArrayList<Integer> b)
	{
		double meanA=calMeanInt(a);
		double meanB=calMeanInt(b);
		double coVariance=0.0;
		if(a.size()!=b.size())
		{
			System.err.println("Cannot cal Mean!");
			return 0.0;
		}
		
		double sum=0.0;
		for(int i=0;i<a.size();i++)
		{
			sum=sum+(((double)a.get(i)-meanA)*((double)b.get(i)-meanB));
		}
		
		coVariance=sum/(double)(a.size()-1);
		return coVariance;
	}
	
	public double calMeanInt(ArrayList<Integer> a)
	{
		int sum=0;
		for(Integer val:a)
		{
			sum=sum+val;
		}
		return (double)sum/(double)a.size();
	}
	
	public double calStandard_Deviation(ArrayList<Integer> a)
	{
		double meanA=calMeanInt(a);
		double sum=0.0;
		for(int i=0;i<a.size();i++)
		{
			sum=sum+(((double)a.get(i)-meanA)*((double)a.get(i)-meanA));
		}
		double average=sum/(double)a.size();
		return Math.sqrt(average);
	}
	
	public ArrayList<String> getAttributeName(String s)
	{
		ArrayList<Integer> num=convertStringToArrayList(s);
		ArrayList<String> att=new ArrayList<>();
		
		for(Integer i:num)
		{
			att.add(attributeMapper(i));
		}
		return att;
		
	}
	public String attributeMapper(int position)
	{
		if(position==0)
		{
			return "SCN";
		}
		else if(position==1)
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
	
	public double calKLDivergence(ArrayList<Integer> x,ArrayList<Integer> y)
	{
		int sumX=0;
		int sumY=0;
		
		if(x.size()!=y.size())
		{
			System.err.println("Cannot calculate K-L Divergance !");
			return -1;
		}
		
		for(int i=0;i<x.size();i++)
		{
			sumX=sumX+x.get(i);
			sumY=sumY+y.get(i);
		}
		ArrayList<Double> probabilityX=new ArrayList<>();
		ArrayList<Double> probabilityY=new ArrayList<>();
		
		for(int i=0;i<x.size();i++)
		{
			if(x.get(i)==0)
			{
				probabilityX.add((double)0);
			}
			else
			{
				probabilityX.add((double)x.get(i)/(double)sumX);
			}
			if(y.get(i)==0)
			{
				probabilityY.add((double)0);
			}
			else
			{
				probabilityY.add((double)y.get(i)/(double)sumY);
			}
			
		}
		double kLD=0.0;
		for(int i=0;i<probabilityX.size();i++)
		{
			double xi=probabilityX.get(i);
			if(xi==0 || probabilityY.get(i)==0)
			{
				continue;
			}
			kLD=kLD+(xi*(logBase2(xi)-logBase2(probabilityY.get(i))));
		}
		
		return kLD;
	}
	
	public double logBase2(double d)
	{
		return Math.log10(d)/Math.log10(2);
	}
}
