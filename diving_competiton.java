/*--------------------------------------------------------------------------------------*/
/*  diving_competiton.java  -  This program reads diver info (name/school/level) from 
			       "Competitors.txt". 3 Judges can input dive difficulty and 
			       individual scores over 3 competition rounds. 
			       Program displays ranked final score of each diver. 
			       Competition results written to "Competition Scores.txt".
/*                                                                                      */
/*--------------------------------------------------------------------------------------*/
/*  Author: Michael Poon                                                                */
/*  Date: May 2016                                                                      */
/*--------------------------------------------------------------------------------------*/
/*  Input: "Competitors.txt"                                                            */
/*  Output: Diver ranked final scores                                                   */
/*--------------------------------------------------------------------------------------*/

import java.io.*;
import java.util.*;
import java.text.*;

public class diving_competiton
{
    static void tab (String[] name, int i)  //tab method to align display of diver info
    {
	if (name [i].length () >= 16) //adjust tabbing for aesthetic appeal
	    System.out.print ("\t");
	else if (name [i].length () >= 8)
	    System.out.print ("\t\t");
	else
	    System.out.print ("\t\t");
    }


    static void read (String[] name, String[] school, String[] level, int[] count) throws IOException
    {
	BufferedReader reader = new BufferedReader (new FileReader ("Competitors.txt"));
	String line = null;
	while ((line = reader.readLine ()) != null) //read diver info into file
	{
	    name [count [0]] = line;
	    school [count [0]] = reader.readLine ();
	    level [count [0]] = reader.readLine ();
	    count [0]++;
	}
    }


    static void display (String[] name, String[] school, String[] level, int[] count)
    {
	System.out.println ("Diver\t\t\tSchool\t\tLevel\t(Original Order)");
	System.out.println ();
	for (int i = 0 ; i < count [0] ; i++)
	{
	    System.out.print (name [i]);
	    tab (name, i); //tab method allows for proper alignment in display
	    System.out.print (school [i]);
	    tab (school, i);
	    System.out.println (level [i]);
	}
	System.out.println ();
	System.out.println ("------------------------------------------------------------------");
    }


    static void scoreInput (double[] score, int JUDGES, int[] count, int ROUNDS, double[] [] totalScores, double[] total, String[] name) throws IOException
    {
	BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in));
	DecimalFormat df = new DecimalFormat ("#.#");
	for (int i = 0 ; i < count [0] ; i++) //fill 2D array with Divers
	{
	    totalScores [i] [0] = i + 1;
	}
	for (int i = 0 ; i < ROUNDS ; i++) //for loop for changing rounds
	{
	    System.out.println ("Round " + (i + 1) + ":");
	    System.out.println ("Let's cheer on the divers!!!");
	    System.out.println ();
	    for (int j = 0 ; j < count [0] ; j++) //for loop for changing divers
	    {
		System.out.println (name [j] + " is up on the diving blocks.");
		System.out.println ();
		for (int k = 0 ; k < JUDGES ; k++) //for loop for changing judges
		{
		    do
		    {
			System.out.print ("Please enter the score for " + name [j] + " from Judge " + (k + 1) + ": ");
			score [k] = Double.parseDouble (stdin.readLine ());
			if (score [k] > 10 || score [k] < 1)
			{
			    System.out.println ("Please enter a score from 1 - 10.");
			    System.out.println ();
			}
		    }
		    while (score [k] > 10 || score [k] < 1); //validation for diver score

		}
		System.out.println ();
		calculate (score, total); //calculate difficulty of dive
		System.out.println (name [j] + "'s score is " + df.format (total [0]) + ". (Round " + (i + 1) + ")");
		System.out.println ();
		System.out.println ("------------------------------------------------------------------");
		totalScores [j + 1] [i + 1] = total [0];
	    }
	    System.out.println ("------------------------------------------------------------------");
	    System.out.println ("End of round " + (i + 1) + ", Results:"); //results per round per diver
	    System.out.println ("------------------------------------------------------------------");
	    for (int m = 0 ; m < count [0] ; m++)
	    {
		System.out.println (name [m] + "'s scores:");
		double accumulated = 0;
		for (int n = 0 ; n < i + 1 ; n++)
		{

		    System.out.println ("Round " + (n + 1) + ": " + totalScores [m + 1] [n + 1]);
		    accumulated = accumulated + totalScores [m + 1] [n + 1]; //calculate accumulated scores
		}
		System.out.println ("Total Accumulated Score: " + df.format (accumulated));
		System.out.println ("------------------------------------------------------------------");

	    }
	    System.out.println ("------------------------------------------------------------------");
	}
    }


    static void calculate (double[] score, double[] total) throws IOException //calculate diver scores
    {
	BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in));
	double max = score [0];
	double min = score [0];
	double sum = 0.0;
	double difficulty;
	do
	{
	    System.out.print ("Please enter the difficulty of this dive: ");
	    difficulty = Double.parseDouble (stdin.readLine ());
	    if (difficulty > 3.5 || difficulty < 1)
	    {
		System.out.println ("Please enter a score from 1 - 3.5.");
		System.out.println ();
	    }
	}
	while (difficulty > 3.5 || difficulty < 1); //validation for diver difficulty

	System.out.println ();
	for (int i = 0 ; i < score.length ; i++) //calculate max score
	{
	    if (score [i] > max)
		max = score [i];
	}
	for (int i = 0 ; i < score.length ; i++) //calculate min score
	{
	    if (score [i] < min)
		min = score [i];
	}
	for (int i = 0 ; i < score.length ; i++) //add judges' scores
	    sum = sum + score [i];
	total [0] = difficulty * (sum - max - min); //multiply score by difficulty to get final score
    }


    static void write (double[] score, int JUDGES, int[] count, int ROUNDS, double[] [] totalScores, double[] total, String[] name) throws IOException
    {
	BufferedWriter writer = new BufferedWriter (new FileWriter ("Competition Scores.txt"));
	DecimalFormat df = new DecimalFormat ("#.#");
	double accumulated[] = new double [count [0]];
	for (int j = 0 ; j < count [0] ; j++)
	{
	    for (int i = 0 ; i < ROUNDS ; i++)
	    {
		accumulated [j] = accumulated [j] + totalScores [j + 1] [i + 1]; //calculate final accumulated scores per diver
	    }
	}
	System.out.println ("------------------------------------------------------------------");
	String tempString;
	double tempDouble;
	boolean swapped = false;
	for (int i = 0 ; i < count [0] - 1 ; i++)
	{
	    swapped = false;
	    for (int j = 0 ; j < (count [0] - 1) - i ; j++) //modified bubble sort to rank divers
		if (accumulated [j] < accumulated [j + 1])
		{
		    swapped = true;
		    tempString = name [j];
		    name [j] = name [j + 1];
		    name [j + 1] = tempString;
		    tempDouble = accumulated [j];
		    accumulated [j] = accumulated [j + 1];
		    accumulated [j + 1] = tempDouble;
		}
	    if (!swapped)
		i = count[0] - 1;
	}
	System.out.println ("Welcome to the Medal Ceremony! (cue music)"); //display ranked divers
	for (int j = 0 ; j < count [0] ; j++)
	{
	    System.out.println ("------------------------------------------------------------------");
	    System.out.println ("Number " + (j + 1) + ":");
	    System.out.println ("------------------------------------------------------------------");
	    System.out.println (name [j] + "'s Final Score:");
	    System.out.println (df.format (accumulated [j]) + "");
	    writer.write (name [j] + "'s Final Score:"); //write ranked divers to file
	    writer.newLine ();
	    writer.write (accumulated [j] + "");
	    writer.newLine ();
	}
	writer.close ();
	System.out.println ("------------------------------------------------------------------");
	System.out.println ("File Saved to -Competition Scores-");
	System.out.println ("------------------------------------------------------------------");
    }


    public static void main (String str[]) throws IOException
    {
	BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in));
	DecimalFormat df = new DecimalFormat ("#.#");
	final int DIVER = 50, ROUNDS = 3, JUDGES = 3; //initialize variables
	String[] name = new String [DIVER];
	String[] school = new String [DIVER];
	String[] level = new String [DIVER];
	double[] total = new double [1];
	double[] score = new double [JUDGES];
	double[] [] totalScores = new double [DIVER] [ROUNDS + 1];
	int[] count = new int [1];
	System.out.println ("------------------------------------------------------------------");
	System.out.println ("Welcome to the Diver Score Calculator!");
	System.out.println ();
	read (name, school, level, count); //read diver names/school/level from file to 3 arrays
	display (name, school, level, count); //display diver names/school/level
	scoreInput (score, JUDGES, count, ROUNDS, totalScores, total, name); //input diver scores
	write (score, JUDGES, count, ROUNDS, totalScores, total, name);
	System.out.println ("Diving complete.");
	System.out.println ("------------------------------------------------------------------");

    }
}



