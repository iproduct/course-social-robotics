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

package javaff.planning;

import javaff.data.Action;
import javaff.data.GroundCondition;
import javaff.data.Plan;
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public abstract class State implements Cloneable
{
	public GroundCondition goal;

//	public Filter filter = null;

//	public void setFilter(Filter f)
//	{
//		filter = f;
//	}

//	public Filter getFilter()
//	{
//		return filter;
//	}

//	public abstract Set getNextStates();       // get all the next possible states reachable from this state

	public Set getNextStates(Set actions)      // get all the states after applying this set of actions
	{
		Set rSet = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			rSet.add(this.apply(a));
		}
		return rSet;
	}

	public State apply(Action a)    // return a cloned copy
	{
		State s = null;
		try {
			s = (State) this.clone();
		}
		catch (CloneNotSupportedException e){
			javaff.JavaFF.errorOutput.println(e);
		}
		a.apply(s);
		return s;
	}

	public abstract BigDecimal getHValue();
	public abstract BigDecimal getGValue();

	public boolean goalReached()
	{
		return goal.isTrue(this);
	}

	public abstract Plan getSolution();

	public abstract Set getActions();

	public boolean checkAvailability(Action a) //put in for invariant checking
	{
		return true;
	}
}
