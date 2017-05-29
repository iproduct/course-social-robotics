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

import javaff.planning.STRIPSState;
import javaff.planning.MetricState;
import javaff.planning.TemporalMetricState;
import javaff.planning.RelaxedPlanningGraph;
import javaff.planning.RelaxedMetricPlanningGraph;
import javaff.planning.RelaxedTemporalMetricPlanningGraph;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.DurativeAction;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

public class GroundProblem
{
    //public Set facts = new HashSet();                  // (Proposition)
    public Set actions = new HashSet();                // (GroundAction)
    public Map functionValues = new Hashtable();     // (NamedFunction => BigDecimal)
	public Metric metric;

    public GroundCondition goal;
    public Set initial;                                // (Proposition)

	public TemporalMetricState state = null;

	public GroundProblem(Set a, Set i, GroundCondition g, Map f, Metric m)
	{
		actions = a;
		initial = i;
		goal = g;
		functionValues = f;
		metric = m;
	}
	
    public STRIPSState getSTRIPSInitialState()
    {
    	STRIPSState s = new STRIPSState(actions, initial, goal);
		s.setRPG(new RelaxedPlanningGraph(this));
		return s;
	}

	public MetricState getMetricInitialState()
    {
		MetricState ms = new MetricState(actions, initial, goal, functionValues, metric);
		ms.setRPG(new RelaxedMetricPlanningGraph(this));
		return ms;
	}

	public TemporalMetricState getTemporalMetricInitialState()
    {
		if (state == null)
		{
			Set na = new HashSet();
			Set ni = new HashSet();
			Iterator ait = actions.iterator();
			while (ait.hasNext())
			{
				Action act = (Action) ait.next();
				if (act instanceof InstantAction)
				{
					na.add(act);
					ni.add(act);
				}
				else if (act instanceof DurativeAction)
				{
					DurativeAction dact = (DurativeAction) act;
					na.add(dact.startAction);
					na.add(dact.endAction);
					ni.add(dact.startAction);
				}
			}
			TemporalMetricState ts = new TemporalMetricState(ni, initial, goal, functionValues, metric);
			GroundProblem gp = new GroundProblem(na, initial, goal, functionValues, metric);
			ts.setRPG(new RelaxedTemporalMetricPlanningGraph(gp));
			state = ts;
		}
		return state;
	}

	
}
