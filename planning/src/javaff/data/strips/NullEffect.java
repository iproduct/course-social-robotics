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
import javaff.data.UngroundEffect;
import javaff.data.GroundEffect;
import javaff.data.UngroundCondition;
import javaff.planning.State;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.PrintStream;

public class NullEffect implements UngroundEffect, GroundEffect
{
	private static NullEffect n;

	private NullEffect()
    {
    }

	public static NullEffect getInstance()
	{
		if (n == null) n = new NullEffect();
		return n;
	}

	public boolean effects(PredicateSymbol ps)
	{
		return false;
	}

	public UngroundCondition effectsAdd(UngroundCondition cond)
	{
		return cond;
	}

	public GroundEffect groundEffect(Map varMap)
	{
		return this;
	}
	
	public void apply(State s)
    {

    }

	public void applyAdds(State s)
    {
		
	}

	public void applyDels(State s)
    {
		
	}

	public GroundEffect staticifyEffect(Map fValues)
	{
		return this;
	}
	

	public Set getAddPropositions()
	{
		return new HashSet();
	}
	
	public Set getDeletePropositions()
	{
		return new HashSet();		
	}

  public Set getOperators()
  {
  	return new HashSet();
  }

	public String toString()
	{
		return "()";
	}

	public String toStringTyped()
	{
		return toString();
	}
	
	public void PDDLPrint(PrintStream p, int indent)
    {
		PDDLPrinter.printToString(this, p, false, false, indent);
	}
}
