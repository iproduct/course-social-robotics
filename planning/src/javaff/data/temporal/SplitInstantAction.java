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

import javaff.data.strips.Proposition;
import javaff.data.strips.InstantAction;
import javaff.planning.TemporalMetricState;

public abstract class SplitInstantAction extends InstantAction
{
    public DurativeAction parent;
	public abstract SplitInstantAction getSibling();

	public boolean equals(Object obj)
    {
		if (obj instanceof SplitInstantAction)
		{
			SplitInstantAction a = (SplitInstantAction) obj;
			return (name.equals(a.name) && params.equals(a.params) && this.getClass().equals(a.getClass()));
		}
		else return false;
    }

    public int hashCode()
    {
        return name.hashCode() ^ params.hashCode() ^ this.getClass().hashCode();
    }

	public abstract void applySplit(TemporalMetricState ts);
	public abstract boolean exclusivelyInvariant(Proposition p);
	
}
