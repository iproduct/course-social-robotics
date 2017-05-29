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


package javaff.data.temporal;

import javaff.data.PDDLPrinter;
import javaff.data.strips.Operator;
import javaff.data.strips.UngroundInstantAction;
import javaff.data.strips.PredicateSymbol;
import javaff.data.strips.AND;
import javaff.data.strips.NOT;
import javaff.data.strips.PDDLObject;
import javaff.data.strips.Variable;
import javaff.data.strips.Predicate;
import javaff.data.strips.OperatorName;
import javaff.data.Action;
import javaff.data.UngroundCondition;
import javaff.data.UngroundEffect;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class UngroundDurativeAction extends Operator
{
    public DurationFunction duration;

	public DurationConstraint durationConstraint;

    public UngroundCondition startCondition = new AND();
    public UngroundCondition endCondition = new AND();
    public UngroundCondition invariant = new AND();

    public UngroundEffect startEffect = new AND();
    public UngroundEffect endEffect = new AND();

	public UngroundInstantAction startAction;
	public UngroundInstantAction endAction;

	public Predicate dummyJoin;
	public Predicate dummyGoal;

	public UngroundDurativeAction()
	{
		duration = new DurationFunction(this);
	}

	public boolean effects(PredicateSymbol ps)
	{
		return (startEffect.effects(ps) || endEffect.effects(ps));
	}

	public Action ground(Map varMap)
	{
		DurativeAction a = new DurativeAction();
		a.name = this.name;

		Iterator pit = params.iterator();
		while (pit.hasNext())
		{
			Variable v = (Variable) pit.next();
			PDDLObject o = (PDDLObject) varMap.get(v);
			a.params.add(o);
		}
		
		varMap.put(duration, a.duration);
		
		a.duration = (DurationFunction) duration.ground(varMap);
		a.startCondition = startCondition.groundCondition(varMap);
		a.endCondition = endCondition.groundCondition(varMap);
		a.invariant = invariant.groundCondition(varMap);
		a.startEffect = startEffect.groundEffect(varMap);
		a.endEffect = endEffect.groundEffect(varMap);

		a.durationConstraint = durationConstraint.ground(varMap);

		a.startAction = new StartInstantAction();
		startAction.ground(varMap, a.startAction);

		a.endAction =  new EndInstantAction();
		endAction.ground(varMap, a.endAction);

		a.dummyJoin = dummyJoin.ground(varMap);
		a.dummyGoal = dummyGoal.ground(varMap);

		a.startAction.parent = a;
		a.endAction.parent = a;
		
		return a;
	}

	
	 public void makeInstants()
	{
		 PredicateSymbol ps = new PredicateSymbol("i"+name);
		 Predicate j = new Predicate(ps);
		 j.addParameters(params);
		 dummyJoin = j;

		 PredicateSymbol ps2 = new PredicateSymbol("g"+name);
		 Predicate g = new Predicate(ps2);
		 g.addParameters(params);
		 dummyGoal = g;
		 
		 startAction = new UngroundInstantAction();
		 startAction.name = new OperatorName(name.toString()+"_START");
		 startAction.params = params;
		 AND s = new AND();
		 s.add(startCondition);
		 s.add(invariant.minus(startEffect));
		 startAction.condition = s;
		 AND se = new AND();
		 startAction.effect = se;
		 se.add(startEffect);
		 se.add(j); 
		 se.add(new NOT(g));

		 endAction = new UngroundInstantAction();
		 endAction.name = new OperatorName(name.toString()+"_END");
		 endAction.params = params;
		 AND e = new AND();
		 e.add(endCondition);
		 e.add(invariant);
		 e.add(j);
		 endAction.condition = e;
		 AND ee = new AND();
		 endAction.effect = ee;
		 ee.add(endEffect);
		 ee.add(g); 
		 ee.add(new NOT(j));

	 }
	 

	public Set getStaticConditionPredicates()
	{
		Set rSet = startCondition.getStaticPredicates();
		rSet.addAll(endCondition.getStaticPredicates());
		rSet.addAll(invariant.getStaticPredicates());
		return rSet;
	}

	//WARNING - This is right either (at start condition) (at end effect) etc....
	public void PDDLPrint(java.io.PrintStream p, int indent)
	{
		p.println();
		PDDLPrinter.printIndent(p, indent);
		p.print("(:durative-action ");
		p.print(name);
		p.println();
		PDDLPrinter.printIndent(p, indent+1);
		p.print(":parameters(\n");
		PDDLPrinter.printToString(params, p, true, false, indent+2);
		p.println(")");
		PDDLPrinter.printIndent(p, indent+1);
		p.print(":duration(\n");
		PDDLPrinter.printToString(durationConstraint, p, true, false, indent+2);
		p.println(")");
		PDDLPrinter.printIndent(p, indent+1);
		p.print(":condition");
		//condition.PDDLPrint(p, indent+2);
		p.println();
		PDDLPrinter.printIndent(p, indent+1);
		p.print(":effect");
		//effect.PDDLPrint(p, indent+2);
		p.print(")");
	}

	public int hashCode()
	{
		int hash = 3;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ params.hashCode();
		return hash;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof UngroundDurativeAction)
		{
			UngroundDurativeAction a = (UngroundDurativeAction) obj;
			return (name.equals(a.name) && params.equals(a.params));
		}
		else return false;
	}
}
