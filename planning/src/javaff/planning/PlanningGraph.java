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
import javaff.data.GroundProblem;
import javaff.data.GroundCondition;
import javaff.data.Plan;
import javaff.data.TotalOrderPlan;
import javaff.data.strips.Proposition;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Hashtable;

public class PlanningGraph
{
	//******************************************************
	// Data Structures
	//******************************************************
	Map propositionMap = new Hashtable();      // (Proposition => PGProposition)
	Map actionMap = new Hashtable();           // (Action => PGAction)

	Set propositions = new HashSet();
	Set actions = new HashSet();

	Set initial, goal;
	Set propMutexes, actionMutexes;
	List memorised;

	protected Set readyActions = null; // PGActions that have all their propositions met, but not their PGBinaryComparators or preconditions are mutex

	boolean level_off = false;
	static int NUMERIC_LIMIT = 4;
	int numeric_level_off = 0;
	int num_layers;

	//******************************************************
	// Main methods
	//******************************************************
	protected PlanningGraph()
	{

	}

	public PlanningGraph(GroundProblem gp)
	{
		setActionMap(gp.actions);
		setLinks();
		createNoOps();
		setGoal(gp.goal);
	}

	public Plan getPlan(State s)
	{
		setInitial(s);
		resetAll(s);

		//set up the intital set of facts
		Set scheduledFacts = new HashSet(initial);
		List scheduledActs = null;

		scheduledActs = createFactLayer(scheduledFacts, 0);
		List plan = null;

		//create the graph==========================================
		while (true)
		{
			scheduledFacts = createActionLayer(scheduledActs, num_layers);
			++ num_layers;
			scheduledActs = createFactLayer(scheduledFacts, num_layers);

			if (goalMet() && !goalMutex())
			{
				plan = extractPlan();
			}
			if (plan != null) break;
			if (!level_off) numeric_level_off = 0;
			if (level_off || numeric_level_off >= NUMERIC_LIMIT) {
				//printGraph();
				break;
			}
		}



		if (plan != null)
		{
			Iterator pit = plan.iterator();
			TotalOrderPlan p = new TotalOrderPlan();
			while (pit.hasNext())
			{
				PGAction a = (PGAction) pit.next();
				if (!(a instanceof PGNoOp)) p.addAction(a.action);
			}
			//p.print(javaff.JavaFF.infoOutput);
			return p;
		}
		else return null;

	}

	//******************************************************
	// Setting it all up
	//******************************************************
	protected void setActionMap(Set gactions)
	{
		Iterator ait = gactions.iterator();
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			PGAction pga = new PGAction(a);
			actionMap.put(a, pga);
			actions.add(pga);
		}
	}

    protected PGProposition getProposition(Proposition p)
    {
		Object o = propositionMap.get(p);
		PGProposition pgp;
		if (o == null)
		{
			pgp = new PGProposition(p);
			propositionMap.put(p, pgp);
			propositions.add(pgp);
		}
		else pgp = (PGProposition) o;
		return pgp;
    }

	protected void setLinks()
	{
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			PGAction pga = (PGAction) ait.next();

			Iterator csit = pga.action.getConditionalPropositions().iterator();
			while (csit.hasNext())
			{
				Proposition p = (Proposition) csit.next();
				PGProposition pgp = getProposition(p);
				pga.conditions.add(pgp);
				pgp.achieves.add(pga);
			}

			Iterator alit = pga.action.getAddPropositions().iterator();
			while (alit.hasNext())
			{
				Proposition p = (Proposition) alit.next();
				PGProposition pgp = getProposition(p);
				pga.achieves.add(pgp);
				pgp.achievedBy.add(pga);
			}

			Iterator dlit = pga.action.getDeletePropositions().iterator();
			while (dlit.hasNext())
			{
				Proposition p = (Proposition) dlit.next();
				PGProposition pgp = getProposition(p);
				pga.deletes.add(pgp);
				pgp.deletedBy.add(pga);
			}
		}
	}

	protected void resetAll(State s)
	{
		propMutexes = new HashSet();
		actionMutexes = new HashSet();

		memorised = new ArrayList();

		readyActions = new HashSet();

		num_layers=0;

		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			a.reset();
		}

		Iterator pit = propositions.iterator();
		while (pit.hasNext())
		{
			PGProposition p = (PGProposition) pit.next();
			p.reset();
		}
	}

	protected void setGoal(GroundCondition g)
	{
		goal = new HashSet();
		Iterator csit = g.getConditionalPropositions().iterator();
		while (csit.hasNext())
		{
			Proposition p = (Proposition) csit.next();
			PGProposition pgp = getProposition(p);
			goal.add(pgp);
		}
	}

	protected void setInitial(State S)
	{
		Set i = ((STRIPSState) S).facts;
		initial = new HashSet();
		Iterator csit = i.iterator();
		while (csit.hasNext())
		{
			Proposition p = (Proposition) csit.next();
			PGProposition pgp = getProposition(p);
			initial.add(pgp);
		}
	}

	protected void createNoOps()
	{
		Iterator pit = propositions.iterator();
		while (pit.hasNext())
		{
			PGProposition p = (PGProposition) pit.next();
			PGNoOp n = new PGNoOp(p);
			n.conditions.add(p);
			n.achieves.add(p);
			p.achieves.add(n);
			p.achievedBy.add(n);
			actions.add(n);
		}
	}

	//******************************************************
	// Graph Construction
	//******************************************************

	protected ArrayList createFactLayer(Set pFacts, int pLayer)
	{
		memorised.add(new HashSet());
		ArrayList scheduledActs = new ArrayList();
		HashSet newMutexes = new HashSet();
		Iterator fit = pFacts.iterator();
		while (fit.hasNext())
		{
			PGProposition f = (PGProposition) fit.next();
			if (f.layer < 0)
			{
				f.layer = pLayer;
				scheduledActs.addAll(f.achieves);
				level_off = false;

				//calculate mutexes
				if (pLayer != 0)
				{
					Iterator pit = propositions.iterator();
					while (pit.hasNext())
					{
						PGProposition p = (PGProposition) pit.next();
						if (p.layer >= 0 && checkPropMutex(f, p, pLayer))
						{
							makeMutex(f, p, pLayer, newMutexes);
						}
					}
				}

			}
		}

		//check old mutexes
		Iterator pmit = propMutexes.iterator();
		while (pmit.hasNext())
		{
			MutexPair m = (MutexPair) pmit.next();
			if (checkPropMutex(m, pLayer))
			{
				makeMutex(m.node1, m.node2, pLayer, newMutexes);
			}
			else
			{
				level_off = false;
			}
		}

		//add new mutexes to old mutexes and remove those which have disappeared
		propMutexes = newMutexes;


		return scheduledActs;
	}

	protected boolean checkPropMutex(MutexPair m, int l)
	{
		return checkPropMutex((PGProposition) m.node1, (PGProposition) m.node2, l);
	}

	protected boolean checkPropMutex(PGProposition p1, PGProposition p2, int l)
	{
		if (p1 == p2) return false;

		//Componsate for statics
		if (p1.achievedBy.isEmpty() || p2.achievedBy.isEmpty()) return false;

		Iterator a1it = p1.achievedBy.iterator();
		while(a1it.hasNext())
		{
			PGAction a1 = (PGAction) a1it.next();
			if (a1.layer >= 0)
			{
				Iterator a2it = p2.achievedBy.iterator();
				while (a2it.hasNext())
				{
					PGAction a2 = (PGAction) a2it.next();
					if (a2.layer >= 0 && !a1.mutexWith(a2, l-1)) return false;
				}
			}

		}
		return true;
	}

	protected void makeMutex(Node n1, Node n2, int l, Set mutexPairs)
	{
		n1.setMutex(n2, l);
		n2.setMutex(n1, l);
		mutexPairs.add(new MutexPair(n1, n2));
	}

	protected HashSet createActionLayer(List pActions, int pLayer)
	{
		level_off = true;
		HashSet actionSet = getAvailableActions(pActions, pLayer);
		actionSet.addAll(readyActions);
		readyActions = new HashSet();
		HashSet filteredSet = filterSet(actionSet, pLayer);
		HashSet scheduledFacts = calculateActionMutexesAndProps(filteredSet, pLayer);
		return scheduledFacts;
	}

	protected HashSet getAvailableActions(List pActions, int pLayer)
	{
		HashSet actionSet = new HashSet();
		Iterator ait = pActions.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			if (a.layer < 0)
			{
				a.counter++;
				a.difficulty += pLayer;
				if (a.counter >= a.conditions.size())
				{
					actionSet.add(a);
					level_off = false;
				}
			}
		}
		return actionSet;
	}

	protected HashSet filterSet(Set pActions, int pLayer)
	{                                                             
		HashSet filteredSet = new HashSet();
		Iterator ait = pActions.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			if (noMutexes(a.conditions, pLayer))
				filteredSet.add(a);
			else 
				readyActions.add(a);
		}
		return filteredSet;
	}

	protected HashSet calculateActionMutexesAndProps(Set filteredSet,int pLayer)
	{
		HashSet newMutexes = new HashSet();

		Set newPreActions = new HashSet();
		HashSet scheduledFacts = new HashSet();

		Iterator ait = filteredSet.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			scheduledFacts.addAll(a.achieves);
			a.layer = pLayer;
			level_off = false;

			//caculate new mutexes
			Iterator a2it = actions.iterator();
			while (a2it.hasNext())
			{
				PGAction a2 = (PGAction) a2it.next();
				if (a2.layer >= 0 && checkActionMutex(a, a2, pLayer))
				{
					makeMutex(a, a2, pLayer, newMutexes);
				}
			}
		}

		//check old mutexes
		Iterator amit = actionMutexes.iterator();
		while (amit.hasNext())
		{
			MutexPair m = (MutexPair) amit.next();
			if (checkActionMutex(m, pLayer))
			{
				makeMutex(m.node1, m.node2, pLayer, newMutexes);
			}
			else
			{
				level_off = false;
			}
		}

		//add new mutexes to old mutexes and remove those which have disappeared
		actionMutexes = newMutexes;
		return scheduledFacts;
	}

	protected boolean checkActionMutex(MutexPair m, int l)
	{
		return checkActionMutex((PGAction)m.node1,(PGAction)m.node2, l);
	}

	protected boolean checkActionMutex(PGAction a1, PGAction a2, int l)
	{
		if (a1 == a2) return false;

		Iterator p1it = a1.deletes.iterator();
		while (p1it.hasNext())
		{
			PGProposition p1 = (PGProposition) p1it.next();;
			if (a2.achieves.contains(p1)) return true;
			if (a2.conditions.contains(p1)) return true;
		}

		Iterator p2it = a2.deletes.iterator();
		while (p2it.hasNext())
		{
			PGProposition p2 = (PGProposition) p2it.next();
			if (a1.achieves.contains(p2)) return true;
			if (a1.conditions.contains(p2)) return true;
		}

		Iterator pc1it = a1.conditions.iterator();
		while (pc1it.hasNext())
		{
			PGProposition p1 = (PGProposition) pc1it.next();
			Iterator pc2it = a2.conditions.iterator();
			while (pc2it.hasNext())
			{
				PGProposition p2 = (PGProposition) pc2it.next();
				if (p1.mutexWith(p2, l)) return true;
			}
		}

		return false;
	}


	protected boolean goalMet()
	{
		Iterator git = goal.iterator();
		while (git.hasNext())
		{
			PGProposition p = (PGProposition) git.next();
			if (p.layer < 0) return false;
		}
		return true;
	}

	protected boolean goalMutex()
	{
		return !noMutexes(goal, num_layers);
	}

	protected boolean noMutexes(Set s, int l)
	{
		Iterator sit = s.iterator();
		if (sit.hasNext())
		{
			Node n = (Node) sit.next();
			HashSet s2 = new HashSet(s);
			s2.remove(n);
			Iterator s2it = s2.iterator();
			while (s2it.hasNext())
			{
				Node n2 = (Node) s2it.next();
				if (n.mutexWith(n2, l))	return false;
			}
			return noMutexes(s2, l);
		}
		else return true;
	}

	protected boolean noMutexesTest(Node n, Set s, int l) // Tests to see if there is a mutex between n and all nodes in s
	{
		Iterator sit = s.iterator();
		while (sit.hasNext())
		{
			Node n2 = (Node) sit.next();
			if (n.mutexWith(n2, l)) return false;
		}
		return true;
	}


	//******************************************************
	// Plan Extraction
	//******************************************************


	public List extractPlan()
	{
		return searchPlan(goal, num_layers);
	}

	public List searchPlan(Set goalSet, int l)
	{

		if (l == 0)
		{
			if (initial.containsAll(goalSet)) return new ArrayList();
			else return null;
		}
		// do memorisation stuff
		Set badGoalSet = (HashSet) memorised.get(l);
		if (badGoalSet.contains(goalSet)) return null;

		List ass = searchLevel(goalSet, (l-1)); // returns a set of sets of possible action combinations
		Iterator assit = ass.iterator();

		while (assit.hasNext())
		{
			Set as = (HashSet) assit.next();
			Set newgoal = new HashSet();

			Iterator ait = as.iterator();
			while (ait.hasNext())
			{
				PGAction a = (PGAction) ait.next();
				newgoal.addAll(a.conditions);
			}

			List al = searchPlan(newgoal, (l-1));
			if (al != null)
			{
				List plan = new ArrayList(al);
				plan.addAll(as);
				return plan;
			}

		}

		// do more memorisation stuff
		badGoalSet.add(goalSet);
		return null;


	}


	public List searchLevel(Set goalSet, int layer)
	{
		if (goalSet.isEmpty())
		{
			Set s = new HashSet();
			List li = new ArrayList();
			li.add(s);
			return li;
		}

		List actionSetList = new ArrayList();
		Set newGoalSet = new HashSet(goalSet);

		Iterator git = goalSet.iterator();
		PGProposition g = (PGProposition) git.next();
		newGoalSet.remove(g);

		Iterator ait = g.achievedBy.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			if ((a instanceof PGNoOp) && a.layer <= layer && a.layer >= 0)
			{
				Set newnewGoalSet = new HashSet(newGoalSet);
				newnewGoalSet.removeAll(a.achieves);
				List l = searchLevel(newnewGoalSet, layer);
				Iterator lit = l.iterator();
				while (lit.hasNext())
				{
					Set s = (HashSet) lit.next();
					if (noMutexesTest(a,s,layer))
					{
						s.add(a);
						actionSetList.add(s);
					}
				}
			}
		}

		ait = g.achievedBy.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			if (!(a instanceof PGNoOp) && a.layer <= layer && a.layer >= 0)
			{
				Set newnewGoalSet = new HashSet(newGoalSet);
				newnewGoalSet.removeAll(a.achieves);
				List l = searchLevel(newnewGoalSet, layer);
				Iterator lit = l.iterator();
				while (lit.hasNext())
				{
					Set s = (HashSet) lit.next();
					if (noMutexesTest(a,s,layer))
					{
						s.add(a);
						actionSetList.add(s);
					}
				}
			}
		}


		return actionSetList;
	}

	//******************************************************
	// Useful Methods
	//******************************************************

	public int getLayer(Action a)
	{
		PGAction pg = (PGAction) actionMap.get(a);
		return pg.layer;
	}

	//******************************************************
	// protected Classes
	//******************************************************
	protected class Node
	{
		public int layer;
		public Set mutexes;

		public Map mutexTable;

		public Node()
		{
		}

		public void reset()
		{
			layer = -1;
			mutexes = new HashSet();
			mutexTable = new Hashtable();
		}

		public void setMutex(Node n, int l)
		{
			n.mutexTable.put(this, new Integer(l));
			this.mutexTable.put(n, new Integer(l));
		}

		public boolean mutexWith(Node n, int l)
		{
			/*
			 if (this == n) return false;
			 Iterator mit = mutexes.iterator();
			 while (mit.hasNext())
			 {
				 Mutex m = (Mutex) mit.next();
				 if (m.contains(n))
				 {
					 return m.layer >= l;
				 }
			 }
			 return false;
			 */
			Object o = mutexTable.get(n);
			if (o == null) return false;
			Integer i = (Integer) o;
			return i.intValue() >= l;
		}
	}

	protected class PGAction extends Node
    {
		public Action action;
		public int counter, difficulty;

		public Set conditions = new HashSet();
		public Set achieves = new HashSet();
		public Set deletes = new HashSet();

		public PGAction()
		{

		}

		public PGAction(Action a)
		{
			action = a;
		}

		public Set getComparators()
		{
			return action.getComparators();
		}

		public Set getOperators()
		{
			return action.getOperators();
		}

		public void reset()
		{
			super.reset();
			counter = 0;
			difficulty = 0;
		}

		public String toString()
		{
			return action.toString();
		}
    }

	protected class PGNoOp extends PGAction
	{
		public PGProposition proposition;

		public PGNoOp(PGProposition p)
		{
			proposition = p;
		}

		public String toString()
		{
			return ("No-Op "+proposition);
		}

		public Set getComparators()
		{
			return new HashSet();
		}

		public Set getOperators()
		{
			return new HashSet();
		}
	}

	protected class PGProposition extends Node
    {
		public Proposition proposition;

		public Set achieves = new HashSet();
		public Set achievedBy = new HashSet();
		public Set deletedBy = new HashSet();

		public PGProposition(Proposition p)
		{
			proposition = p;
		}

		public String toString()
		{
			return proposition.toString();
		}
    }

	protected class MutexPair
	{
		public Node node1, node2;
		public MutexPair(Node n1, Node n2)
		{
			node1 = n1;
			node2 = n2;
		}
	}


	//******************************************************
	// Debugging Classes
	//******************************************************
	public void printGraph()
	{
		for (int i = 0; i <= num_layers; ++i)
		{
			System.out.println("-----Layer "+i+"----------------------------------------");
			printLayer(i);
		}
		System.out.println("-----End -----------------------------------------------");
	}

	public void printLayer(int i)
	{
		System.out.println("Facts:");
		Iterator pit = propositions.iterator();
		while (pit.hasNext())
		{
			PGProposition p = (PGProposition) pit.next();
			if (p.layer <= i && p.layer >= 0)
			{
				System.out.println("\t"+p);
				System.out.println("\t\tmutex with");
				Iterator mit = p.mutexTable.keySet().iterator();
				while (mit.hasNext())
				{
					PGProposition pm = (PGProposition) mit.next();
					Integer il = (Integer) p.mutexTable.get(pm);
					if (il.intValue() >= i)
					{
						System.out.println("\t\t\t"+pm);
					}
				}
			}
		}
		if (i == num_layers) return;
		System.out.println("Actions:");
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			PGAction a = (PGAction) ait.next();
			if (a.layer <= i && a.layer >= 0)
			{
				System.out.println("\t"+a);
				System.out.println("\t\tmutex with");
				Iterator mit = a.mutexTable.keySet().iterator();
				while (mit.hasNext())
				{
					PGAction am = (PGAction) mit.next();
					Integer il = (Integer) a.mutexTable.get(am);
					if (il.intValue() >= i)
					{
						System.out.println("\t\t\t"+am);
					}
				}
			}
		}
	}

}