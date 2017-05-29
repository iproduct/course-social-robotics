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

package javaff.data;

import javaff.data.strips.Proposition;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.SplitInstantAction;
import javaff.scheduling.TemporalConstraint;

import java.util.Map;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.PrintStream;
import java.io.PrintWriter;


public class PartialOrderPlan implements Plan
{
	public Map strictOrderings = new Hashtable();
	public Map equalOrderings = new Hashtable();
	public Set actions = new HashSet();

	public PartialOrderPlan()
	{
		
	}

	public void addStrictOrdering(Action first, Action second)
	{
		Set ord = null;
		Object o = strictOrderings.get(first);
		if (o == null)
		{
			ord = new HashSet();
			strictOrderings.put(first, ord);
		}
		else ord = (HashSet) o;
		ord.add(second);
		actions.add(first);
		actions.add(second);
	}

	public void addEqualOrdering(Action first, Action second)
	{
		Set ord = null;
		Object o = equalOrderings.get(first);
		if (o == null)
		{
			ord = new HashSet();
			equalOrderings.put(first, ord);
		}
		else ord = (HashSet) o;
		ord.add(second);
		actions.add(first);
		actions.add(second);
	}

	public void addOrder(Action first, Action second, Proposition p)
	{
		if (first instanceof SplitInstantAction)
		{
			SplitInstantAction sa = (SplitInstantAction) first;
			if (!sa.exclusivelyInvariant(p))
			{
				addEqualOrdering(first, second);
				return;
			}
			
		}

		if (second instanceof SplitInstantAction)
		{
			SplitInstantAction sa = (SplitInstantAction) second;
			if (!sa.exclusivelyInvariant(p))
			{
				addEqualOrdering(first, second);
				return;
			}
		}
		
		addStrictOrdering(first, second);
		
		
	}

	public void addAction(Action a)
	{
		actions.add(a);
		strictOrderings.put(a, new HashSet());
		equalOrderings.put(a, new HashSet());
	}

	public void addActions(Set s)
	{
		Iterator sit = s.iterator();
		while (sit.hasNext()) addAction((Action) sit.next());
	}

	public Set getActions()
	{
		return actions;
	}

	public Set getTemporalConstraints()
	{
		Set rSet = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			
			Set ss = (HashSet) strictOrderings.get(a);
			Iterator sit = ss.iterator();
			while (sit.hasNext())
			{
				Action b = (Action) sit.next();
				rSet.add(TemporalConstraint.getConstraint((InstantAction)a,(InstantAction)b));
			}

			Set es = (HashSet) equalOrderings.get(a);
			Iterator eit = es.iterator();
			while (eit.hasNext())
			{
				Action b = (Action) eit.next();
				rSet.add(TemporalConstraint.getConstraintEqual((InstantAction)a,(InstantAction)b));
			}
		}
		return rSet;
			
	}

	public void print(PrintStream p)
	{
		Iterator sit = actions.iterator();
		while (sit.hasNext())
		{
			Action a = (Action) sit.next();
			p.println(a);
			p.println("\tStrict Orderings: "+strictOrderings.get(a));
			p.println("\tLess than or equal Orderings: "+equalOrderings.get(a));
		}
	}

	public void print(PrintWriter p)
	{
		Iterator sit = actions.iterator();
		while (sit.hasNext())
		{
			Action a = (Action) sit.next();
			p.println(a);
			p.println("\tStrict Orderings: "+strictOrderings.get(a));
			p.println("\tLess than or equal Orderings: "+equalOrderings.get(a));
		}
	}
}
