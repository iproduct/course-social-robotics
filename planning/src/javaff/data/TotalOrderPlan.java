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

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.PrintStream;
import java.io.PrintWriter;

public class TotalOrderPlan implements Plan, Cloneable
{
	private List plan = new ArrayList();

	public Object clone()
	{
		TotalOrderPlan rTOP = new TotalOrderPlan();
		rTOP.plan = (List) ((ArrayList) plan).clone();
		return rTOP;
	}

	public void addAction(Action a)
	{
		plan.add(a);
	}

	public int getPlanLength()
	{
		return plan.size();
	}

	public Iterator iterator()
	{
		return plan.iterator();
	}

	public ListIterator listIteratorEnd()
	{
		return plan.listIterator(plan.size());
	}

	public ListIterator listIterator(Action a)
	{
		return plan.listIterator(plan.indexOf(a));
	}

	public Set getActions()
	{
		return new HashSet(plan);
	}
	
	public boolean equals(Object obj)
    {
        if (obj instanceof TotalOrderPlan)
		{
			TotalOrderPlan p = (TotalOrderPlan) obj;
			return (plan.equals(p.plan));
		}
		else return false;
    }

    public int hashCode()
    {
        return plan.hashCode();
    }

	public void print(PrintStream ps)
	{
		Iterator pit = plan.iterator();
		while (pit.hasNext())
		{
			ps.println("("+pit.next()+")");
		}
	}

	public void print(PrintWriter pw)
	{
		Iterator pit = plan.iterator();
		while (pit.hasNext())
		{
			pw.println("("+pit.next()+")");
		}
	}
}
