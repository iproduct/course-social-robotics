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

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class HelpfulFilter implements Filter
{
	private static HelpfulFilter hf = null;

	private HelpfulFilter()
	{
	}

	public static HelpfulFilter getInstance()
	{
		if (hf == null) hf = new HelpfulFilter(); // Singleton, as in NullFilter
		return hf;
	}

	public Set getActions(State S)
	{
		STRIPSState SS = (STRIPSState) S;
		SS.calculateRP(); // get the relaxed plan to the goal, to make sure helpful actions exist for S
		Set ns = new HashSet();
		Iterator ait = SS.helpfulActions.iterator(); // iterate over helpful actions
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			if (a.isApplicable(S)) ns.add(a); // and add them to the set to return if they're applicable
		}
		return ns;
	}
} 