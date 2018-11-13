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
 * The "Dung's Triathlon" is added to these tracks.
 * 
 * Note that contrary to the competition, some equivalent tasks are launched more than once (e.g. SE-GR and EE-GR).
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="ICCMA2017")
public class ICCMA2017Checker extends ACheckerFactoryCollection<ArgumentationFramework> {
	
	/**
	 * Builds a new instance of {@link ICCMA2017Checker}.
	 */
	public ICCMA2017Checker() {
		super(Stream.concat(
				Stream.of("CO", "GR", "PR", "ST", "SST", "STG", "ID").map(sem -> Stream.of("EE", "SE", "DS", "DC").map(q -> q+"-"+sem)).flatMap(s -> s),
				Stream.of("D3")
		).collect(Collectors.toList()));
	}

}
