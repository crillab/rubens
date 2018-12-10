package fr.cril.rubens.arg.checking.checkers;

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
	
	@ReflectorParam(name="DC-CO")
	public static class DCCOChecker extends AbstractElementaryCheckerFactory {
		
		public DCCOChecker() {
			super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-CO");
		}

	}
	
	@ReflectorParam(name="DC-GR")
	public static class DCGRChecker extends AbstractElementaryCheckerFactory {
		
		public DCGRChecker() {
			super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-GR");
		}

	}
	
	@ReflectorParam(name="DC-ID")
	public static class DCIDChecker extends AbstractElementaryCheckerFactory {
		
		public DCIDChecker() {
			super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-ID");
		}

	}
	
	@ReflectorParam(name="DC-PR")
	public static class DCPRChecker extends AbstractElementaryCheckerFactory {
		
		public DCPRChecker() {
			super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-PR");
		}

	}
	
	@ReflectorParam(name="DC-SST")
	public static class DCSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DCSSTChecker() {
			super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-SST");
		}

	}
	
	@ReflectorParam(name="DC-ST")
	public static class DCSTChecker extends AbstractElementaryCheckerFactory {
		
		public DCSTChecker() {
			super(() -> new StableSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-ST");
		}

	}
	
	@ReflectorParam(name="DC-STG")
	public static class DCSTGChecker extends AbstractElementaryCheckerFactory {
		
		public DCSTGChecker() {
			super(() -> new StageSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-STG");
		}

	}
	
	@ReflectorParam(name="DS-CO")
	public static class DSCOChecker extends AbstractElementaryCheckerFactory {
		
		public DSCOChecker() {
			super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-CO");
		}

	}
	
	@ReflectorParam(name="DS-GR")
	public static class DSGRChecker extends AbstractElementaryCheckerFactory {
		
		public DSGRChecker() {
			super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-GR");
		}

	}
	
	@ReflectorParam(name="DS-ID")
	public static class DSIDChecker extends AbstractElementaryCheckerFactory {
		
		public DSIDChecker() {
			super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-ID");
		}

	}
	
	@ReflectorParam(name="DS-PR")
	public static class DSPRChecker extends AbstractElementaryCheckerFactory {
		
		public DSPRChecker() {
			super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-PR");
		}

	}
	
	@ReflectorParam(name="DS-SST")
	public static class DSSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DSSSTChecker() {
			super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-SST");
		}

	}
	
	@ReflectorParam(name="DS-ST")
	public static class DSSTChecker extends AbstractElementaryCheckerFactory {
		
		public DSSTChecker() {
			super(() -> new StableSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-ST");
		}

	}
	
	@ReflectorParam(name="DS-STG")
	public static class DSSTGChecker extends AbstractElementaryCheckerFactory {
		
		public DSSTGChecker() {
			super(() -> new StageSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-STG");
		}

	}
	
	@ReflectorParam(name="EE-CO")
	public static class EECOChecker extends AbstractElementaryCheckerFactory {

		public EECOChecker() {
			super(CompleteSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-CO");
		}

	}
	
	@ReflectorParam(name="EE-GR")
	public static class EEGRChecker extends AbstractElementaryCheckerFactory {

		public EEGRChecker() {
			super(GroundedSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-GR");
		}

	}
	
	@ReflectorParam(name="EE-ID")
	public static class EEIDChecker extends AbstractElementaryCheckerFactory {

		public EEIDChecker() {
			super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ID");
		}

	}
	
	@ReflectorParam(name="EE-PR")
	public static class EEPRChecker extends AbstractElementaryCheckerFactory {

		public EEPRChecker() {
			super(PreferredSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-PR");
		}

	}
	
	@ReflectorParam(name="EE-SST")
	public static class EESSTChecker extends AbstractElementaryCheckerFactory {

		public EESSTChecker() {
			super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-SST");
		}

	}
	
	@ReflectorParam(name="EE-ST")
	public static class EESTChecker extends AbstractElementaryCheckerFactory {

		public EESTChecker() {
			super(StableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ST");
		}

	}
	
	@ReflectorParam(name="EE-STG")
	public static class EESTGChecker extends AbstractElementaryCheckerFactory {

		public EESTGChecker() {
			super(StageSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-STG");
		}

	}
	
	@ReflectorParam(name="SE-CO")
	public static class SECOChecker extends AbstractElementaryCheckerFactory {
		
		public SECOChecker() {
			super(CompleteSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-CO");
		}

	}
	
	@ReflectorParam(name="SE-GR")
	public static class SEGRChecker extends AbstractElementaryCheckerFactory {
		
		public SEGRChecker() {
			super(GroundedSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-GR");
		}

	}
	
	@ReflectorParam(name="SE-ID")
	public static class SEIDChecker extends AbstractElementaryCheckerFactory {
		
		public SEIDChecker() {
			super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-ID");
		}

	}
	
	@ReflectorParam(name="SE-PR")
	public static class SEPRChecker extends AbstractElementaryCheckerFactory {
		
		public SEPRChecker() {
			super(PreferredSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-PR");
		}

	}
	
	@ReflectorParam(name="SE-SST")
	public static class SESSTChecker extends AbstractElementaryCheckerFactory {
		
		public SESSTChecker() {
			super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-SST");
		}

	}
	
	@ReflectorParam(name="SE-ST")
	public static class SESTChecker extends AbstractElementaryCheckerFactory {
		
		public SESTChecker() {
			super(StableSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-ST");
		}

	}
	
	@ReflectorParam(name="SE-STG")
	public static class SESTGChecker extends AbstractElementaryCheckerFactory {
		
		public SESTGChecker() {
			super(StageSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-STG");
		}

	}
	
	private ElementaryCheckers() {
		// nothing to do here
	}

}