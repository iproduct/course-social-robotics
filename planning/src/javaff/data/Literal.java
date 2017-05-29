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

import javaff.data.strips.PredicateSymbol;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintStream;

public abstract class Literal implements Condition, Effect
{
	protected PredicateSymbol name;
    protected List parameters = new ArrayList(); // list of Parameters

	public void setPredicateSymbol(PredicateSymbol n)
	{
		name = n;
	}

	public PredicateSymbol getPredicateSymbol()
	{
		return name;
	}
	
	public List getParameters()
	{
		return parameters;
	}

	public void addParameter(Parameter p)
	{
		parameters.add(p);
	}

	public void addParameters(List l)
	{
		parameters.addAll(l);
	}

	public String toString()
    {
		String stringrep = name.toString();
		Iterator i = parameters.iterator();
		while(i.hasNext())
		{
			stringrep = stringrep + " " + i.next();
		}
		return stringrep;
    }

	public String toStringTyped()
    {
		String stringrep = name.toString();
		Iterator i = parameters.iterator();
		while(i.hasNext())
		{
			Parameter o = (Parameter) i.next();
			stringrep += " " + o + " - " + o.type.toString();
		}
		return stringrep;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Literal)
		{
			Literal p = (Literal) obj;
			return (name.equals(p.name) && parameters.equals(p.parameters) && this.getClass() == p.getClass());
		}
		else return false;
    }
  

	public boolean isStatic()
	{
		return name.isStatic();
	}

	public void PDDLPrint(PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, false, true, indent);
	}
}
