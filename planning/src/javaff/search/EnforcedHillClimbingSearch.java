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

package javaff.search;

import javaff.planning.State;
import javaff.planning.Filter;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.math.BigDecimal;

import java.util.Hashtable;
import java.util.Iterator;

public class EnforcedHillClimbingSearch extends Search
{
	protected BigDecimal bestHValue;

	protected Hashtable closed;
	protected LinkedList open;
	protected Filter filter = null;
	
	public EnforcedHillClimbingSearch(State s)
	{
		this(s, new HValueComparator());
	}

	public EnforcedHillClimbingSearch(State s, Comparator c)
	{
		super(s);
		setComparator(c);
		
		closed = new Hashtable();
		open = new LinkedList();
	}

	public void setFilter(Filter f)
	{
		filter = f;
	}

	public State removeNext()
	{
			
		return (State) ((LinkedList) open).removeFirst();
	}
	
	public boolean needToVisit(State s) {
		Integer Shash = new Integer(s.hashCode()); // compute hash for state
		State D = (State) closed.get(Shash); // see if its on the closed list
		
		if (closed.containsKey(Shash) && D.equals(s)) return false;  // if it is return false
		
		closed.put(Shash, s); // otherwise put it on
		return true; // and return true
	}
	
	public State search() {
		
		if (start.goalReached()) { // wishful thinking
			return start;
		}

		needToVisit(start); // dummy call (adds start to the list of 'closed' states so we don't visit it again
		
		open.add(start); // add it to the open list
		bestHValue = start.getHValue(); // and take its heuristic value as the best so far

		javaff.JavaFF.infoOutput.println(bestHValue);
		
		while (!open.isEmpty()) // whilst still states to consider
		{	
			State s = removeNext(); // get the next one
			
			Set successors = s.getNextStates(filter.getActions(s)); // and find its neighbourhood
			
			Iterator succItr = successors.iterator();			
		
			while (succItr.hasNext()) {
				State succ = (State) succItr.next(); // next successor
			
				if (needToVisit(succ)) {
					if (succ.goalReached()) { // if we've found a goal state - return it as the solution
						return succ;
					} else if (succ.getHValue().compareTo(bestHValue) < 0) {
						// if we've found a state with a better heuristic value than the best seen so far
					
						bestHValue = succ.getHValue(); // note the new best avlue
						javaff.JavaFF.infoOutput.println(bestHValue);
						open = new LinkedList(); // clear the open list
						open.add(succ); // put this on it
						break; // and skip looking at the other successors
					} else {
						open.add(succ); // otherwise, add to the open list
					}
				}
			}
			
			
		}
		return null;
	}
}
