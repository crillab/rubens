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

import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.CompleteSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.GroundedSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.IdealSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.PreferredSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.StableSemTestGeneratorFactory;
import fr.cril.rubens.arg.testgen.StageSemTestGeneratorFactory;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * A class defining many instances of {@link CheckerFactory} dedicated to elementary problems.
 * 
 * The elementary problems are which defined upon a task in {SE, EE, DC, DS} and a semantics in {CO, GR, PR, ST, SST, STG, ID}.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ElementaryCheckers {
	
	@ReflectorParam(name="CE-CO", family="AF/Base")
	public static class CECOChecker extends AbstractElementaryCheckerFactory {
		
		public CECOChecker() {
			super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-CO");
		}

	}
	
	@ReflectorParam(name="CE-GR", family="AF/Base")
	public static class CEGRChecker extends AbstractElementaryCheckerFactory {
		
		public CEGRChecker() {
			super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-GR");
		}

	}
	
	@ReflectorParam(name="CE-ID", family="AF/Base")
	public static class CEIDChecker extends AbstractElementaryCheckerFactory {
		
		public CEIDChecker() {
			super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-ID");
		}

	}
	
	@ReflectorParam(name="CE-PR", family="AF/Base")
	public static class CEPRChecker extends AbstractElementaryCheckerFactory {
		
		public CEPRChecker() {
			super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-PR");
		}

	}
	
	@ReflectorParam(name="CE-SST", family="AF/Base")
	public static class CESSTChecker extends AbstractElementaryCheckerFactory {
		
		public CESSTChecker() {
			super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-SST");
		}

	}
	
	@ReflectorParam(name="CE-ST", family="AF/Base")
	public static class CESTChecker extends AbstractElementaryCheckerFactory {
		
		public CESTChecker() {
			super(() -> new StableSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-ST");
		}

	}
	
	@ReflectorParam(name="CE-STG", family="AF/Base")
	public static class CESTGChecker extends AbstractElementaryCheckerFactory {
		
		public CESTGChecker() {
			super(() -> new StageSemTestGeneratorFactory(false), SoftwareOutputChecker.CE::check, "CE-STG");
		}

	}
	
	@ReflectorParam(name="DC-CO", family="AF/Base")
	public static class DCCOChecker extends AbstractElementaryCheckerFactory {
		
		public DCCOChecker() {
			super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-CO");
		}

	}
	
	@ReflectorParam(name="DC-GR", family="AF/Base")
	public static class DCGRChecker extends AbstractElementaryCheckerFactory {
		
		public DCGRChecker() {
			super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-GR");
		}

	}
	
	@ReflectorParam(name="DC-ID", family="AF/Base")
	public static class DCIDChecker extends AbstractElementaryCheckerFactory {
		
		public DCIDChecker() {
			super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-ID");
		}

	}
	
	@ReflectorParam(name="DC-PR", family="AF/Base")
	public static class DCPRChecker extends AbstractElementaryCheckerFactory {
		
		public DCPRChecker() {
			super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-PR");
		}

	}
	
	@ReflectorParam(name="DC-SST", family="AF/Base")
	public static class DCSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DCSSTChecker() {
			super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-SST");
		}

	}
	
	@ReflectorParam(name="DC-ST", family="AF/Base")
	public static class DCSTChecker extends AbstractElementaryCheckerFactory {
		
		public DCSTChecker() {
			super(() -> new StableSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-ST");
		}

	}
	
	@ReflectorParam(name="DC-STG", family="AF/Base")
	public static class DCSTGChecker extends AbstractElementaryCheckerFactory {
		
		public DCSTGChecker() {
			super(() -> new StageSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-STG");
		}

	}
	
	@ReflectorParam(name="DS-CO", family="AF/Base")
	public static class DSCOChecker extends AbstractElementaryCheckerFactory {
		
		public DSCOChecker() {
			super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-CO");
		}

	}
	
	@ReflectorParam(name="DS-GR", family="AF/Base")
	public static class DSGRChecker extends AbstractElementaryCheckerFactory {
		
		public DSGRChecker() {
			super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-GR");
		}

	}
	
	@ReflectorParam(name="DS-ID", family="AF/Base")
	public static class DSIDChecker extends AbstractElementaryCheckerFactory {
		
		public DSIDChecker() {
			super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-ID");
		}

	}
	
	@ReflectorParam(name="DS-PR", family="AF/Base")
	public static class DSPRChecker extends AbstractElementaryCheckerFactory {
		
		public DSPRChecker() {
			super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-PR");
		}

	}
	
	@ReflectorParam(name="DS-SST", family="AF/Base")
	public static class DSSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DSSSTChecker() {
			super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-SST");
		}

	}
	
	@ReflectorParam(name="DS-ST", family="AF/Base")
	public static class DSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DSSTChecker() {
			super(() -> new StableSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-ST");
		}

	}
	
	@ReflectorParam(name="DS-STG", family="AF/Base")
	public static class DSSTGChecker extends AbstractElementaryCheckerFactory {
		
		public DSSTGChecker() {
			super(() -> new StageSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-STG");
		}

	}
	
	@ReflectorParam(name="EE-CO", family="AF/Base")
	public static class EECOChecker extends AbstractElementaryCheckerFactory {

		public EECOChecker() {
			super(CompleteSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-CO");
		}

	}
	
	@ReflectorParam(name="EE-GR", family="AF/Base")
	public static class EEGRChecker extends AbstractElementaryCheckerFactory {

		public EEGRChecker() {
			super(GroundedSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-GR");
		}

	}
	
	@ReflectorParam(name="EE-ID", family="AF/Base")
	public static class EEIDChecker extends AbstractElementaryCheckerFactory {

		public EEIDChecker() {
			super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ID");
		}

	}
	
	@ReflectorParam(name="EE-PR", family="AF/Base")
	public static class EEPRChecker extends AbstractElementaryCheckerFactory {

		public EEPRChecker() {
			super(PreferredSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-PR");
		}

	}
	
	@ReflectorParam(name="EE-SST", family="AF/Base")
	public static class EESSTChecker extends AbstractElementaryCheckerFactory {

		public EESSTChecker() {
			super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-SST");
		}

	}
	
	@ReflectorParam(name="EE-ST", family="AF/Base")
	public static class EESTChecker extends AbstractElementaryCheckerFactory {

		public EESTChecker() {
			super(StableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ST");
		}

	}
	
	@ReflectorParam(name="EE-STG", family="AF/Base")
	public static class EESTGChecker extends AbstractElementaryCheckerFactory {

		public EESTGChecker() {
			super(StageSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-STG");
		}

	}
	
	@ReflectorParam(name="SE-CO", family="AF/Base")
	public static class SECOChecker extends AbstractElementaryCheckerFactory {
		
		public SECOChecker() {
			super(CompleteSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-CO");
		}

	}
	
	@ReflectorParam(name="SE-GR", family="AF/Base")
	public static class SEGRChecker extends AbstractElementaryCheckerFactory {
		
		public SEGRChecker() {
			super(GroundedSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-GR");
		}

	}
	
	@ReflectorParam(name="SE-ID", family="AF/Base")
	public static class SEIDChecker extends AbstractElementaryCheckerFactory {
		
		public SEIDChecker() {
			super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-ID");
		}

	}
	
	@ReflectorParam(name="SE-PR", family="AF/Base")
	public static class SEPRChecker extends AbstractElementaryCheckerFactory {
		
		public SEPRChecker() {
			super(PreferredSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-PR");
		}

	}
	
	@ReflectorParam(name="SE-SST", family="AF/Base")
	public static class SESSTChecker extends AbstractElementaryCheckerFactory {
		
		public SESSTChecker() {
			super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-SST");
		}

	}
	
	@ReflectorParam(name="SE-ST", family="AF/Base")
	public static class SESTChecker extends AbstractElementaryCheckerFactory {
		
		public SESTChecker() {
			super(StableSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-ST");
		}

	}
	
	@ReflectorParam(name="SE-STG", family="AF/Base")
	public static class SESTGChecker extends AbstractElementaryCheckerFactory {
		
		public SESTGChecker() {
			super(StageSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-STG");
		}

	}
	
	private ElementaryCheckers() {
		// nothing to do here
	}

}
