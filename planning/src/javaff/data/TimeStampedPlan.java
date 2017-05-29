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

import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class TimeStampedPlan implements Plan
{
	public SortedSet actions = new TreeSet();

	public void addAction(Action a, BigDecimal t)
	{
		addAction(a, t, null);
	}

	public void addAction(Action a, BigDecimal t, BigDecimal d)
	{
		actions.add(new TimeStampedAction(a,t,d));
	}

		
	public void print(PrintStream p)
	{
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			p.println(a);
		}
	}

	public void print(PrintWriter p)
	{
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			p.println(a);
		}
	}
	
	public Set getActions()
	{
		Set s = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			s.add(a.action);
		}
		return s;
	}	
}
