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


import javaff.data.GroundCondition;
import javaff.data.GroundEffect;
import javaff.planning.State;
import javaff.planning.STRIPSState;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class Proposition extends javaff.data.Literal implements GroundCondition, GroundEffect
{
	public Proposition(PredicateSymbol p)
    {
		name = p;
    }
	
	public boolean isTrue(State s) // returns whether this conditions is true is State S
    {
		STRIPSState ss = (STRIPSState) s;
		return ss.isTrue(this);
	}

	public void apply(State s) // carry out the effects of this on State s
	{
		STRIPSState ss = (STRIPSState) s;
		ss.addProposition(this);
	}

	public void applyAdds(State s)
    {
		apply(s);
	}

	public void applyDels(State s)
    {
	}

	public boolean isStatic()
	{
		return name.isStatic();
	}

	public Set getDeletePropositions()
    {
		return new HashSet();
	}

	public Set getAddPropositions()
    {
		Set rSet = new HashSet();
		rSet.add(this);
		return rSet;
	}

	public GroundCondition staticifyCondition(Map fValues)
    {
		if (isStatic()) return TrueCondition.getInstance();
    else return this;
    }

	public GroundEffect staticifyEffect(Map fValues)
    {
		return this;
	}


	public Set getConditionalPropositions()
    {
		Set rSet = new HashSet();
		rSet.add(this);
		return rSet;
	}

  public Set getOperators()
  {
  	return new HashSet();
  }

  public Set getComparators()
  {
  	return new HashSet();
  }

	public int hashCode()
    {
		int hash = 6;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ parameters.hashCode();
		return hash;
	}
}
