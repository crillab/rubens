package fr.cril.rubens.arg.checking.checkers;

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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.utils.ACheckerFactoryCollection;

/**
 * A checker factory collection matching the regular tracks of ICCMA 2017.
 * 
 * These tracks consist of the Cartesian product of the queries (all extensions, one extension, credulous acceptance, skeptical acceptance)
 * and the semantics (complete, grounded, preferred, stable, semistable, stage and ideal) involved in the competition.
 * The competition also provides a dynamic track (successive AFs with one attack change between two AFs) for the CO, GR, PR and ST semantics.
 * 
 * Note that contrary to the competition, some equivalent tasks are launched more than once (e.g. SE-GR and EE-GR).
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="ICCMA2019", family="AF/Competition")
public class ICCMA2019Checker extends ACheckerFactoryCollection<ArgumentationFramework> {
	
	/**
	 * Builds a new instance of {@link ICCMA2019Checker}.
	 */
	public ICCMA2019Checker() {
		super(Stream.concat(
				Stream.of("CO", "GR", "PR", "ST", "SST", "STG", "ID").map(sem -> Stream.of("EE", "SE", "DS", "DC").map(q -> q+"-"+sem)).flatMap(s -> s),
				Stream.of("CO", "GR", "PR", "ST").map(sem -> Stream.of("EE", "SE", "DS", "DC").map(q -> q+"-"+sem+"-D")).flatMap(s -> s)
		).collect(Collectors.toList()));
	}

}
