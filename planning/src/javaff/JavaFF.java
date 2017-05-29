/************************************************************************
 * Strathclyde Planning Group,
 * Department of Computer and Information Sciences,
 * University of Strathclyde, Glasgow, UK
 * http://planning.cis.strath.ac.uk/
 * 
 * Copyright 2007, Keith Halsey
 * Copyright 2008, Andrew Coles and Amanda Smith
 *
 * (Questions/bug reports now to be sent to Andrew Coles)
 *
 * This file is part of JavaFF.
 * 
 * JavaFF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * JavaFF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JavaFF.  If not, see <http://www.gnu.org/licenses/>.
 * 
 ************************************************************************/

package javaff;

import javaff.data.PDDLPrinter;
import javaff.data.UngroundProblem;
import javaff.data.GroundProblem;
import javaff.data.Plan;
import javaff.data.TotalOrderPlan;
import javaff.data.TimeStampedPlan;
import javaff.parser.PDDL21parser;
import javaff.planning.State;
import javaff.planning.TemporalMetricState;
import javaff.planning.RelaxedTemporalMetricPlanningGraph;
import javaff.planning.HelpfulFilter;
import javaff.planning.NullFilter;
import javaff.scheduling.Scheduler;
import javaff.scheduling.JavaFFScheduler;
import javaff.search.Search;
import javaff.search.BestFirstSearch;
import javaff.search.EnforcedHillClimbingSearch;


import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

public class JavaFF
{
    public static BigDecimal EPSILON = new BigDecimal(0.01);
	public static BigDecimal MAX_DURATION = new BigDecimal("100000"); //maximum duration in a duration constraint
	public static boolean VALIDATE = false;

 
	public static Random generator = null;
   
   

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main (String args[]) {
		EPSILON = EPSILON.setScale(2,BigDecimal.ROUND_HALF_EVEN);
		MAX_DURATION = MAX_DURATION.setScale(2,BigDecimal.ROUND_HALF_EVEN);
		
		generator = new Random();
		
		if (args.length < 2) {
			System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol");

		} else {
			File domainFile = new File(args[0]);
			File problemFile = new File(args[1]);
			File solutionFile = null;
			if (args.length > 2)
			{
				generator = new Random(Integer.parseInt(args[2]));
			}
	
			if (args.length > 3)
			{
				solutionFile = new File(args[3]);
			}
	
			Plan plan = plan(domainFile,problemFile);
	
			if (solutionFile != null && plan != null) writePlanToFile(plan, solutionFile);
			
		}
	}


    public static Plan plan(File dFile, File pFile)
    {
		// ********************************
		// Parse and Ground the Problem
		// ********************************
		long startTime = System.currentTimeMillis();
		
		UngroundProblem unground = PDDL21parser.parseFiles(dFile, pFile);

		if (unground == null)
		{
			System.out.println("Parsing error - see console for details");
			return null;
		}


		//PDDLPrinter.printDomainFile(unground, System.out);
		//PDDLPrinter.printProblemFile(unground, System.out);

		GroundProblem ground = unground.ground();

		long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		TemporalMetricState initialState = ground.getTemporalMetricInitialState();
		
                State goalState = goalState = performFFSearch(initialState);
                
		long afterPlanning = System.currentTimeMillis();

                TotalOrderPlan top = null;
		if (goalState != null) top = (TotalOrderPlan) goalState.getSolution();
		if (top != null) top.print(planOutput);


		/*javaff.planning.PlanningGraph pg = initialState.getRPG();
		Plan plan  = pg.getPlan(initialState);
		plan.print(planOutput);
		return null;*/

		// ********************************
		// Schedule a plan
		// ********************************

                //TimeStampedPlan tsp = null;
                
                //if (goalState != null)
                //{
                   
                   //infoOutput.println("Scheduling");
		
                   //Scheduler scheduler = new JavaFFScheduler(ground);
                   //tsp = scheduler.schedule(top);
                //}
                

		//long afterScheduling = System.currentTimeMillis();
		
		//if (tsp != null) tsp.print(planOutput);

		double groundingTime = (afterGrounding - startTime)/1000.00;
		double planningTime = (afterPlanning - afterGrounding)/1000.00;
		//double schedulingTime = (afterScheduling - afterPlanning)/1000.00;

		infoOutput.println("Instantiation Time =\t\t"+groundingTime+"sec");
		infoOutput.println("Planning Time =\t"+planningTime+"sec");
		//infoOutput.println("Scheduling Time =\t"+schedulingTime+"sec");

		
		return top;
	}

	private static void writePlanToFile(Plan plan, File fileOut)
    {
		try
	    {
			FileOutputStream outputStream = new FileOutputStream(fileOut);
			PrintWriter printWriter = new PrintWriter(outputStream);
			plan.print(printWriter);
			printWriter.close();
		}
		catch (FileNotFoundException e)
	    {
			errorOutput.println(e);
			e.printStackTrace();
		}
		catch (IOException e)
	    {
			errorOutput.println(e);
			e.printStackTrace();
		}

    }
    
    public static State performFFSearch(TemporalMetricState initialState) {
	

	
	// Implementation of standard FF-style search

	infoOutput.println("Performing search as in FF - first considering EHC with only helpful actions");

	// Now, initialise an EHC searcher
	EnforcedHillClimbingSearch EHCS = new EnforcedHillClimbingSearch(initialState);
	
	EHCS.setFilter(HelpfulFilter.getInstance()); // and use the helpful actions neighbourhood
	
	// Try and find a plan using EHC
	State goalState = EHCS.search();

	if (goalState == null) // if we can't find one
	{
		infoOutput.println("EHC failed, using best-first search, with all actions");
		
		// create a Best-First Searcher
		BestFirstSearch BFS = new BestFirstSearch(initialState);
		
		// ... change to using the 'all actions' neighbourhood (a null filter, as it removes nothing)
		
		BFS.setFilter(NullFilter.getInstance());
		
		// and use that
		goalState = BFS.search();
	}
	
	return goalState; // return the plan 
	    
    }
}
