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

package javaff.search;

import javaff.planning.State;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Iterator;
import java.math.BigDecimal;

public class LimitedEnforcedHillClimbingSearch extends EnforcedHillClimbingSearch
{
        private BigDecimal planSizeLimit;
        private BigDecimal currentHValue;

	public LimitedEnforcedHillClimbingSearch(State s, BigDecimal l)
	{
		this(s, new HValueComparator(), l);
	}

	public LimitedEnforcedHillClimbingSearch(State s, Comparator c, BigDecimal l)
	{
		super(s);
		setComparator(c);
                planSizeLimit = l;
	}

	public boolean needToVisit(State s) {
		if (s.getGValue().compareTo(planSizeLimit) > 0) return false; // if length bound has been exceeded...

		return super.needToVisit(s);
	}
}
