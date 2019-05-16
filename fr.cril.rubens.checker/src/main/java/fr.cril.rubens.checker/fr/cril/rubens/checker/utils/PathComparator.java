package fr.cril.rubens.checker.utils;

import java.util.Comparator;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

/**
 * A comparator used to set an order on strings representing paths.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class PathComparator implements Comparator<String> {

	@Override
	public int compare(final String str1, final String str2) {
		final String[] s1 = str1.split("/");
		final String[] s2 = str2.split("/");
		int i=0;
		while(i < s1.length && i < s2.length) {
			final int cmp = s1[i].compareTo(s2[i]);
			if(cmp != 0) {
				return cmp;
			}
			i++;
		}
		return s1.length - s2.length;
	}

}
