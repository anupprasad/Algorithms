package com.KLMean;
import java.util.ArrayList;


public class SubsetPuzzle {
	

	public static int size=9;
	public static ArrayList<ArrayList<String>> allSubsets=new ArrayList<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<String> sets=new ArrayList<>();
		ArrayList<ArrayList<String>> set2=new ArrayList<>();
		/*sets.add("a");
		sets.add("b");
		sets.add("c");
		sets.add("d")*/;
		/*int size=100;*/
		/*initialize(size, sets);
		generateSubset(sets, 0);*/
		
		initialize2(size, set2,1);
		generateSubset2(set2, 0);
		System.out.println("All Subsets "+allSubsets.toString());
		
		
		
	}
	
	public static void initialize(int size, ArrayList<String> sets)
	{
		for(int i=0;i<size;i++)
		{
			sets.add(""+i);
		}
	}
	
	public static void initialize2(int size, ArrayList<ArrayList<String>> sets,int startFrom)
	{
		for(int i=1;i<=size;i++)
		{
			ArrayList<String>  s=new ArrayList<>();
			s.add(""+i);
			sets.add(s);
		}
	}
	public static void generateSubset(ArrayList<String> sets,int iteration)
	{
		ArrayList<String> tempSet=new ArrayList<>();
		
		if(sets.size()==1)
		{
			System.out.println("\n All Sets got generated \n");
			//System.out.println("\n Iteration no "+iteration +" ----> "+sets.toString());
			return;
		}
		if(0==iteration)
		{
			System.out.println("\n sbsets ----> "+sets.toString());
			for(int i=0;i<sets.size()-1;i++)
			{
				for(int j=i+1;j<sets.size();j++)
				{
					tempSet.add(sets.get(i)+sets.get(j));
				}
			}
			System.out.println("\n Iteration no "+iteration +" ----> "+tempSet.toString());
		}
		else
		{
			for(int i=0;i<sets.size()-1;i++)
			{
				for(int j=i+1;j<sets.size();j++)
				{
					String s=sets.get(j);
					if(s.startsWith(sets.get(i).substring(1,iteration+1)))
					{
						
						tempSet.add(sets.get(i)+s.substring(s.length()-1, s.length()));
					}
				}
			}
			
			System.out.println("\n Iteration no "+iteration +" ----> "+tempSet.toString());
		}
		iteration++;
		generateSubset(tempSet, iteration);
	}

	public static void generateSubset2(ArrayList<ArrayList<String>> sets,int iteration)
	{
		ArrayList<ArrayList<String>> tempSet=new ArrayList<>();
		ArrayList<String> temp=null;
		if(sets.size()==1)
		{
			System.out.println("\n All Sets got generated \n");
			//System.out.println("\n Iteration no "+iteration +" ----> "+sets.toString());
			return;
		}
		if(0==iteration)
		{
			System.out.println("\n sbsets ----> "+sets.toString());
			for(int i=0;i<sets.size()-1;i++)
			{
				for(int j=i+1;j<sets.size();j++)
				{
				   temp=new ArrayList<String>();
				   temp.add(sets.get(i).get(0));
				   temp.add(sets.get(j).get(0));
					tempSet.add(temp);
					allSubsets.add(temp);
				}
			}
			System.out.println("\n Iteration no "+iteration +" ----> "+tempSet.toString());
			
		}
		else
		{
			boolean isTerminate=false;
			for(int i=0;i<sets.size()-1;i++)
			{
				temp=sets.get(i);
				for(int j=i+1;j<sets.size();j++)
				{
					boolean isNoMatched=false;
					ArrayList<String> s=sets.get(j);
					for(int k=0;k<iteration;k++)
					{
						if(!temp.get(k+1).equals(s.get(k)))
						{
							isNoMatched=true;
							break;
						}
					}
					if(!isNoMatched)
					{
						ArrayList<String> arr=new ArrayList<>();
						arr.addAll(temp);
						arr.add(s.get(s.size()-1));
						tempSet.add(arr);
						allSubsets.add(arr);
						if(arr.size()==size)
						{
							isTerminate=true;
							break;
						}
					}
					
				}
				
				if(isTerminate)
				{
					break;
				}
			}
			
			System.out.println("\n Iteration no "+iteration +" ----> "+tempSet.toString());
		}
		iteration++;
		generateSubset2(tempSet, iteration);
	}
	
	public ArrayList<ArrayList<String>> getSubset(int size,int start)
	{
		ArrayList<ArrayList<String>> set2=new ArrayList<>();
		initialize2(size, set2,start);
		generateSubset2(set2, 0);
		return allSubsets;
	}
	
}
