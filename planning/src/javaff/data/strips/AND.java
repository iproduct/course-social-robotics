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
import javaff.data.GroundCondition;
import javaff.data.GroundEffect;
import javaff.data.Condition;
import javaff.data.UngroundCondition;
import javaff.data.UngroundEffect;
import javaff.data.CompoundLiteral;
import javaff.data.PDDLPrinter;
import javaff.planning.State;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.io.PrintStream;

public class AND implements CompoundLiteral, GroundCondition, GroundEffect, UngroundCondition, UngroundEffect
{
	protected Set literals = new HashSet(); // set of Literals

	public void add(Object o)
	{
		if (o instanceof AND)
		{
			AND a = (AND) o;
			Iterator ait = a.literals.iterator();
			while (ait.hasNext())
			{
				add(ait.next());
			}
		}
		else literals.add(o);
	}

	public boolean isStatic()
	{
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			Condition c = (Condition) it.next();
			if (!c.isStatic()) return false;
		}
		return true;
	}

	public GroundCondition staticifyCondition(Map fValues)
    {
		Set newlit = new HashSet(literals.size());
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			GroundCondition c = (GroundCondition) it.next();
			if (! (c instanceof TrueCondition)) newlit.add(c.staticifyCondition(fValues));
		}
		literals = newlit;
		if (literals.isEmpty()) return TrueCondition.getInstance();
		else return this;
    }

	public GroundEffect staticifyEffect(Map fValues)
    {
		Set newlit = new HashSet(literals.size());
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			GroundEffect e = (GroundEffect) it.next();
			if (! (e instanceof NullEffect)) newlit.add(e.staticifyEffect(fValues));
		}
		literals = newlit;
		if (literals.isEmpty()) return NullEffect.getInstance();
		else return this;
    }

	public Set getStaticPredicates()
	{
		Set rSet = new HashSet();
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			UngroundCondition c = (UngroundCondition) it.next();
			rSet.addAll(c.getStaticPredicates());
		}
		return rSet;
	}

	public boolean effects(PredicateSymbol ps)
	{
		boolean rEff = false;
		Iterator lit = literals.iterator();
		while (lit.hasNext() && !(rEff))
		{
			UngroundEffect ue = (UngroundEffect) lit.next();
			rEff = ue.effects(ps);
		}
		return rEff;
	}

	public UngroundCondition minus(UngroundEffect effect)
    {
		AND a = new AND();
		Iterator lit = literals.iterator();
		while (lit.hasNext())
		{
			UngroundCondition p = (UngroundCondition) lit.next();
			a.add(p.minus(effect));
		}
		return a;
    }

	public UngroundCondition effectsAdd(UngroundCondition cond)
    {
		Iterator lit = literals.iterator();
		UngroundCondition c = null;
		while (lit.hasNext())
		{
			UngroundEffect p = (UngroundEffect) lit.next();
			UngroundCondition d = p.effectsAdd(cond);
			if (!d.equals(cond)) c=d;
		}
		if (c == null) return cond;
		else return c;
    }


	public GroundEffect groundEffect(Map varMap)
	{
		AND a = new AND();
		Iterator lit = literals.iterator();
		while (lit.hasNext())
		{
			UngroundEffect p = (UngroundEffect) lit.next();
			a.add(p.groundEffect(varMap));
		}
		return a;
	}

	public GroundCondition groundCondition(Map varMap)
	{
		AND a = new AND();
		Iterator lit = literals.iterator();
		while (lit.hasNext())
		{
			UngroundCondition p = (UngroundCondition) lit.next();
			a.add(p.groundCondition(varMap));
		}
		return a;
	}
	 
	public boolean isTrue(State s)
	{
		Iterator cit = literals.iterator();
		while (cit.hasNext())
		{
			GroundCondition c = (GroundCondition) cit.next();
			if (!c.isTrue(s)) return false;
		}
		return true;
	}

	public void apply(State s)
	{
		applyDels(s);
		applyAdds(s);
	}

	public void applyAdds(State s)
    {
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundEffect e = (GroundEffect) eit.next();
			e.applyAdds(s);
		}
	}

	public void applyDels(State s)
    {
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundEffect e = (GroundEffect) eit.next();
			e.applyDels(s);
		}
	}

	public Set getConditionalPropositions()
	{
		Set rSet = new HashSet();
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundCondition e = (GroundCondition) eit.next();
			rSet.addAll(e.getConditionalPropositions());
		}
		return rSet;
	}

	public Set getAddPropositions()
	{
		Set rSet = new HashSet();
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundEffect e = (GroundEffect) eit.next();
			rSet.addAll(e.getAddPropositions());
		}
		return rSet;
	}
	
	public Set getDeletePropositions()
	{
		Set rSet = new HashSet();
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundEffect e = (GroundEffect) eit.next();
			rSet.addAll(e.getDeletePropositions());
		}
		return rSet;
	}

  public Set getOperators()
  {
  	Set rSet = new HashSet();
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundEffect e = (GroundEffect) eit.next();
			rSet.addAll(e.getOperators());
		}
		return rSet;
	}

  public Set getComparators()
	{
		Set rSet = new HashSet();
		Iterator eit = literals.iterator();
		while (eit.hasNext())
		{
			GroundCondition e = (GroundCondition) eit.next();
			rSet.addAll(e.getComparators());
		}
		return rSet;
	}


	public boolean equals(Object obj)
    {
        if (obj instanceof AND)
		{
			AND a = (AND) obj;
			return (literals.equals(a.literals));
		}
		else return false;
    }

    public int hashCode()
    {
        return literals.hashCode();
    }

	
	public void PDDLPrint(PrintStream p, int indent)
	{
		PDDLPrinter.printToString(literals, "and", p, false, true, indent);
	}

	public String toString()
	{
		String str = "(and";
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			str += " "+it.next();
		}
		str += ")";
		return str;
	}

	public String toStringTyped()
	{
		String str = "(and";
		Iterator it = literals.iterator();
		while (it.hasNext())
		{
			Literal l = (Literal) it.next();
			str += " "+l.toStringTyped();
		}
		str += ")";
		return str;

	}
}
