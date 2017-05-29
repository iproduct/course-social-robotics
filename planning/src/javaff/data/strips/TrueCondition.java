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

package javaff.data.strips;

import javaff.data.PDDLPrinter;
import javaff.data.GroundCondition;
import javaff.data.UngroundCondition;
import javaff.data.UngroundEffect;
import javaff.planning.State;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.PrintStream;

public class TrueCondition implements GroundCondition, UngroundCondition
{
	private static TrueCondition t;
	
	private TrueCondition()
    {
    }

	public GroundCondition staticifyCondition(Map fValues)
	{
		return this;
	}
	

	public static TrueCondition getInstance()
	{
		if (t == null) t = new TrueCondition();
		return t;
	}

	public UngroundCondition minus(UngroundEffect effect)
	{
		return this;
	}
	
	public boolean isTrue(State s)
    {
		return true;
    }
	
	public boolean isStatic()
	{
		return true;
	}

	public Set getStaticPredicates()
	{
		return new HashSet();
	}

	public Set getConditionalPropositions()
	{
		return new HashSet();
	}

  public Set getComparators()
  {
  	return new HashSet();
  }

	public GroundCondition groundCondition(Map varMap)
	{
		return this;
	}

	public String toString()
	{
		return "()";
	}

	public String toStringTyped()
	{
		return toString();
	}

	public void PDDLPrint(java.io.PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, false, false, indent);
	}

}
