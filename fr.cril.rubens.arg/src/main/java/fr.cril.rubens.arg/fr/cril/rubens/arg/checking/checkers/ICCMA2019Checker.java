package fr.cril.rubens.arg.checking.checkers;

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
@ReflectorParam(name="ICCMA2019")
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
