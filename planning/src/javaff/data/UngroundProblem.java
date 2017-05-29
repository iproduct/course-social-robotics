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

import javaff.data.strips.SimpleType;
import javaff.data.strips.PredicateSymbol;
import javaff.data.strips.Predicate;
import javaff.data.strips.PDDLObject;
import javaff.data.strips.Proposition;
import javaff.data.strips.Operator;
import javaff.data.strips.Variable;
import javaff.data.metric.FunctionSymbol;
import javaff.data.metric.NamedFunction;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

public class UngroundProblem
{
    public String DomainName;                               // Name of Domain
    public String ProblemName;                              // Name of Problem
	public String ProblemDomainName;                        // Name of Domain as specified by the Problem

	public Set requirements = new HashSet();                // Requirements of the domain     (String)

    public Set types = new HashSet();                       // For simple object types in this domain       (SimpleTypes)
	public Map typeMap = new Hashtable();                   // Set for mapping String -> types  (String => Type)
	public Map typeSets = new Hashtable();                 // Maps a type on to a set of PDDLObjects (Type => Set (PDDLObjects))

    public Set predSymbols = new HashSet();                 // Set of all (ungrounded) predicate     (PredicateSymbol)
	public Map predSymbolMap = new Hashtable();             // Maps Strings of the symbol to the Symbols (String => PredicateSymbol)

	public Set constants = new HashSet();                   // Set of all constant           (PDDLObjects)
	public Map constantMap = new Hashtable();               // Maps Strings of the constant to the PDDLObject

	public Set funcSymbols = new HashSet();                 // Set of all function symbols (FunctionSymbol)
	public Map funcSymbolMap = new Hashtable();             // Maps Strings onto the Symbols (String => FunctionSymbol)

	public Set actions = new HashSet();                     // List of all (ungrounded) actions      (Operators)

	public Set objects = new HashSet();                     // Objects in the problem        (PDDLObject)
    public Map objectMap =  new Hashtable();                // Maps Strings onto PDDLObjects (String => PDDLObject)

	public Set initial = new HashSet();                     // Set of initial facts          (Proposition)
	public Map funcValues = new Hashtable();                // Maps functions onto numbers (NamedFunction => BigDecimal)
	public GroundCondition goal;

	public Metric metric;

	public Map staticPropositionMap = new Hashtable();      // (PredicateName => Set (Proposition))

	public UngroundProblem()
	{
		typeMap.put(SimpleType.rootType.toString(), SimpleType.rootType);
	}	
	
	public GroundProblem ground()
    {
		calculateStatics();
		makeStaticPropositionMap();
		buildTypeSets();
		Set groundActions = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			Operator o = (Operator) ait.next();
			Set s = o.ground(this);
			groundActions.addAll(s);
		}

		//static-ify the functions
		Iterator gait = groundActions.iterator();
		while (gait.hasNext())
		{
			Action a = (Action) gait.next();
			a.staticify(funcValues);
		}

    //remove static functions from the intial state
    removeStaticsFromInitialState();

		//-could put in code here to
		// a) get rid of static functions in initial state - DONE
		// b) get rid of static predicates in initial state - DONE
		// c) get rid of static propositions in the actions (this may have already been done)
		// d) get rid of no use actions (i.e. whose preconditions can't be achieved) 

		GroundProblem rGP = new GroundProblem(groundActions, initial, goal, funcValues, metric);
		return rGP;
	}

	private void buildTypeSets() // builds typeSets for easy access of all the objects of a particular type
	{
		Iterator tit = types.iterator();
		while (tit.hasNext())
		{
			SimpleType st = (SimpleType) tit.next();
			Set s = new HashSet();
			typeSets.put(st, s);

			Iterator oit = objects.iterator();
			while (oit.hasNext())
			{
				PDDLObject o = (PDDLObject) oit.next();
				if (o.isOfType(st)) s.add(o);
			}

			Iterator cit = constants.iterator();
			while (cit.hasNext())
			{
				PDDLObject c = (PDDLObject) cit.next();
				if (c.isOfType(st)) s.add(c);
			}
		}

		Set s = new HashSet(objects);
		s.addAll(constants);
		typeSets.put(SimpleType.rootType, s);
	}

	private void calculateStatics() // Determines whether the predicateSymbols and funcSymbols are static or not
	{
		Iterator pit = predSymbols.iterator();
		while (pit.hasNext())
		{
			boolean isStatic = true;
			PredicateSymbol ps = (PredicateSymbol) pit.next();
			Iterator oit = actions.iterator();
			while (oit.hasNext() && isStatic)
			{
				Operator o = (Operator) oit.next();
				isStatic = !o.effects(ps);
			}
			ps.setStatic(isStatic);
		}

		Iterator fit = funcSymbols.iterator();
		while (fit.hasNext())
		{
			boolean isStatic = true;
			FunctionSymbol fs = (FunctionSymbol) fit.next();
			Iterator oit = actions.iterator();
			while (oit.hasNext() && isStatic)
			{
				Operator o = (Operator) oit.next();
				isStatic = !o.effects(fs);
			}
			fs.setStatic(isStatic);
		}
	}

	private void makeStaticPropositionMap()
	{
		Iterator pit = predSymbols.iterator();
		while (pit.hasNext())
		{
			PredicateSymbol ps = (PredicateSymbol) pit.next();
			if (ps.isStatic())
			{
				staticPropositionMap.put(ps, new HashSet());
			}
		}

		Iterator iit = initial.iterator();
		while (iit.hasNext())
		{
			Proposition p = (Proposition) iit.next();
			if (p.name.isStatic())
			{
				Set pset = (Set) staticPropositionMap.get(p.name);
				pset.add(p);
			}
		}
	}

  private void removeStaticsFromInitialState()
  {
  	//remove static functions
	  /*
  	Iterator fit = funcValues.keySet().iterator();
    Set staticFuncs = new HashSet();
    while (fit.hasNext())
    {
    	NamedFunction nf = (NamedFunction) fit.next();
      if (nf.isStatic()) staticFuncs.add(nf);
    }
    fit = staticFuncs.iterator();
    while (fit.hasNext())
    {
    	Object o = fit.next();
      funcValues.remove(o);
    }*/

    //remove static Propositions
    Iterator init = initial.iterator();
    Set staticProps = new HashSet();
    while (init.hasNext())
    {
    	Proposition p = (Proposition) init.next();
      if (p.isStatic()) staticProps.add(p);
    }
    initial.removeAll(staticProps);
  }

}