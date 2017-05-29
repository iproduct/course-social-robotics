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

import javaff.data.Literal;
import javaff.data.PDDLPrinter;
import javaff.data.CompoundLiteral;
import javaff.data.GroundEffect;
import javaff.data.UngroundEffect;
import javaff.data.UngroundCondition;
import javaff.planning.State;
import javaff.planning.STRIPSState;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.io.PrintStream;

public class NOT implements CompoundLiteral, GroundEffect, UngroundEffect
{
	Literal literal;

	public NOT(Literal l)
	{
		literal = l;
	}

	public void apply(State s)
	{
		STRIPSState ss = (STRIPSState) s;
		ss.removeProposition((Proposition) literal); 
	}

	public void applyAdds(State s)
    {
	}

	public void applyDels(State s)
    {
		apply(s);
	}

	public boolean effects(PredicateSymbol ps)
	{
		UngroundEffect ue = (UngroundEffect) literal;
		return ue.effects(ps);
	}

	public UngroundCondition effectsAdd(UngroundCondition cond)
	{
		return cond;
	}

	public GroundEffect groundEffect(Map varMap)
	{
		Predicate p = (Predicate) literal;
		return new NOT((Proposition) p.groundEffect(varMap));
	}

	public Set getAddPropositions()
	{
		return new HashSet();
	}
	
	public Set getDeletePropositions()
	{
		Set rSet = new HashSet();
		rSet.add(literal);
		return rSet;
	}

  public Set getOperators()
  {
  	return new HashSet();
  }


	public boolean equals(Object obj)
    {
        if (obj instanceof NOT)
		{
			NOT n = (NOT) obj;
			return (literal.equals(n.literal));
		}
		else return false;
    }

	public GroundEffect staticifyEffect(Map fValues)
	{
		return this;
	}

    public int hashCode()
    {
        return literal.hashCode()^2;
    }

	public String toString()
	{
		return "not ("+literal.toString()+")";
	}

	public String toStringTyped()
	{
		return "not ("+literal.toStringTyped()+")";
	}

	
	public void PDDLPrint(java.io.PrintStream p, int indent)
	{
		p.print("(not ");
		PDDLPrinter.printToString(literal, p, false, true, indent);
		p.print(")");
	}

}
