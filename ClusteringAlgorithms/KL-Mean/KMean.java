package com.KLMean;

import java.util.ArrayList;
import java.util.HashMap;

import com.NieveBase.Util;
/*
 * Functions : 
 *   1 . obj.createCluster(K,tao) : to run K-Menas algo
 *   2.  obj.getMaxPPV() : to get maximum PPV by running algorithm n number of times . where n=numberOfIteration 
 *   3. obj.kFoldValidation() : to perform 10 fold validation 
 *   4. obj.attSubsetExperiment() : to run K-Mean with different subset of attributes .
 *   
 *   Variables 
 *   SCN : Tuple number used as initial centroids
 *   skipSequence : will skip a record having this value.
 *   K : Number of cluster
 *   isRandomInitialize :  True if initialization is done randomly 
 *                         False if initial fixed centroid is required . If this is set false then SCN should be populated
 *   tao : threshold between previous centroid difference and present centroid difference
 *   attToConsider : starting from 1 to 10 this string holds all the attributes which has to be considered for clustering .
 *   numberOfIteration : number of times kmeans has to be run to get maximum PPV .
 *   path : path of the file containing original data .
 *    
 */  
public class KMean {

	public static HashMap<String,ArrayList<String>> initialCentroids=new HashMap<>();
	public static HashMap<String,ArrayList<String>> currentCentroids=new HashMap<>();
	public static HashMap<String,ArrayList<String>> previousCentroid=new HashMap<>();
	public static HashMap<String,ArrayList<String>> bootStrapedCenroid=new HashMap<>();
	public static String SCN="26,129,252,265,467,473,608,613";
	public static String skipSequence="?";
	public static int K=8;
	public static int l=1;
	public static boolean isRandomInitialize=false;
	public static double PPV=0.0;
	public static double tao=0.1;
	public static String attToConsider="1,2,3,4,5,6,7,8,9,10";
	public static int numberOfIteration=5000;
	public static StringBuilder recorNumber=new StringBuilder();
	
	 private static String path="C:\\Users\\Anup\\Desktop\\DM\\BrestCancerData.txt";
	// public static String path="C:\\Users\\Anup\\Desktop\\DM\\TakeHome\\dataPointRefined.txt";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
         KMean obj=new KMean();
         obj.createCluster(K,tao);
         
         //obj.attSubsetExperiment();
       //obj.getMaxPPV();
         //obj.kFoldValidation();
	}
	
	public void createCluster(int k,double tao)
	{
		KMeanUtil kMeanUtil=new KMeanUtil();
		double fi=0.0;
		double fi_1=0.0;
		ArrayList<ArrayList<String>> data=null;
		
		data=kMeanUtil.readDataFile(path, true, skipSequence, false,attToConsider);
		
		
		
		kMeanUtil.bootStrap(k, kMeanUtil.convertStringToArrayList(attToConsider).size());
		kMeanUtil.Initialize(data, k,isRandomInitialize,attToConsider,skipSequence);/*only one time*/
		fi_1=kMeanUtil.calDiffBTWCentroids(bootStrapedCenroid, initialCentroids);
		kMeanUtil.reCalCentroids(kMeanUtil.assignPointsToNearestCentroid(data, initialCentroids,l),0);
		for(int i=1;i<999999999;i++)
		{
			kMeanUtil.reCalCentroids(kMeanUtil.assignPointsToNearestCentroid(data, currentCentroids,l),i);
			fi=kMeanUtil.calDiffBTWCentroids(previousCentroid, currentCentroids);
			double diff=fi-fi_1;
			if(diff<0)
			{
				diff =-1*diff;
			}
			
			if(diff<=tao*fi_1)
			{
				break;
			}
			fi_1=fi;
			
		}
		
	}
	
	public void getMaxPPV()
	{
		double greatestPPV=0.00;
		String tuples=null;
		for(int i=0;i<numberOfIteration;i++)
		{
			if(PPV >greatestPPV)
			{
				greatestPPV=PPV;
				tuples=recorNumber.toString();
			}
			recorNumber.delete(0, recorNumber.length());
			createCluster(K,tao);
			KMeanUtil.iteration=0;
		}
		
		System.out.println("--------------- Winner ------------- ");
		System.out.println("PPV = "+greatestPPV+ " Record Num = "+tuples);
	}
	
	public void attSubsetExperiment()
	{
		KMean obj=new KMean();
		SubsetPuzzle sub=new SubsetPuzzle();
		ArrayList<ArrayList<String>> subSets=sub.getSubset(9,1);
		StringBuilder sb=new StringBuilder();
		double greatestPPV=0.00;
		double smallestPPV=9999999;
		String smallest=null;
		String largest=null;
		for(ArrayList<String> s:subSets)
		{
			for(String attribute:s)
			{
				sb.append(attribute+",");
			}
			if(PPV >greatestPPV)
			{
				largest=s.toString();
				greatestPPV=PPV;
			}
			
			if(PPV<smallestPPV &&PPV!=0.0)
			{
				smallestPPV=PPV;
				smallest=s.toString();
			}
				
			sb.append("10");
			attToConsider=sb.toString();
			sb.delete(0, sb.length());
	         obj.createCluster(K,tao);
	         KMeanUtil.iteration=0;
		}
		System.out.println("Largest PPV "+greatestPPV+" Attributes "+largest);
		System.out.println("Smallest PPV "+smallestPPV+" Attributes "+smallest);
	}
	
	
	public void kFoldValidation()
	{
		KMeanUtil kMeanUtil=new KMeanUtil();
		ArrayList<ArrayList<String>> data=kMeanUtil.readDataFile(path, true, skipSequence, false,attToConsider);
		HashMap<String,ArrayList<ArrayList<String>>> folds=new HashMap<>();
		int size=data.size();
		int foldSize=size/10;
		int k=0;
		for(int i=0;i<10;i++)
		{
			ArrayList<ArrayList<String>> arr=new ArrayList<>();
			/*if(i==9)
			{
				for(;k<size;k++)
				{
					arr.add(data.get(k));
				}
			}
			else
			{*/
			for(;k<foldSize*(i+1);k++)
			{
				arr.add(data.get(k));
				
			}
			//}
			folds.put("fold"+(i+1),arr );
		}
		
		for(String fold:folds.keySet())
		{
			vFoldClustering(folds.get(fold));
			KMeanUtil.iteration=0;
		}
	}
	
	public void vFoldClustering(ArrayList<ArrayList<String>> data)
	{
		KMeanUtil kMeanUtil=new KMeanUtil();
		double fi=0.0;
		double fi_1=0.0;
	
		kMeanUtil.bootStrap(K, kMeanUtil.convertStringToArrayList(attToConsider).size());
		kMeanUtil.Initialize(data, K,isRandomInitialize,attToConsider,skipSequence);/*only one time*/
		fi_1=kMeanUtil.calDiffBTWCentroids(bootStrapedCenroid, initialCentroids);
		kMeanUtil.reCalCentroids(kMeanUtil.assignPointsToNearestCentroid(data, initialCentroids,l),0);
		for(int i=1;i<999999999;i++)
		{
			kMeanUtil.reCalCentroids(kMeanUtil.assignPointsToNearestCentroid(data, currentCentroids,l),i);
			fi=kMeanUtil.calDiffBTWCentroids(previousCentroid, currentCentroids);
			double diff=fi-fi_1;
			if(diff<0)
			{
				diff =-1*diff;
			}
			
			if(diff<=tao*fi_1)
			{
				break;
			}
			fi_1=fi;
			
		}
		
	}

}
