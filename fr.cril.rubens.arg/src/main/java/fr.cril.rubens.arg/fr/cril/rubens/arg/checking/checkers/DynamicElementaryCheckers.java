package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * A class defining many instances of {@link CheckerFactory} dedicated to dynamic problems.
 * 
 * The elementary problems are which defined upon a task in {SE, EE, DC, DS} and a semantics in {CO-D, GR-D, PR-D, ST-D, SST-D, STG-D, ID-D}.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DynamicElementaryCheckers {
	
	@ReflectorParam(name="DC-CO-D")
	public static class DynDCCOChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCCOChecker() {
			super(new ElementaryCheckers.DCCOChecker(), EExtensionSetComputer.COMPLETE_SEM, "DC-CO-D");
		}

	}
	
	@ReflectorParam(name="DS-CO-D")
	public static class DynDSCOChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSCOChecker() {
			super(new ElementaryCheckers.DSCOChecker(), EExtensionSetComputer.COMPLETE_SEM, "DS-CO-D");
		}

	}
	
	@ReflectorParam(name="SE-CO-D")
	public static class DynSECOChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSECOChecker() {
			super(new ElementaryCheckers.SECOChecker(), EExtensionSetComputer.COMPLETE_SEM, "SE-CO-D");
		}

	}
	
	@ReflectorParam(name="EE-CO-D")
	public static class DynEECOChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEECOChecker() {
			super(new ElementaryCheckers.EECOChecker(), EExtensionSetComputer.COMPLETE_SEM, "EE-CO-D");
		}

	}
	
	@ReflectorParam(name="DC-GR-D")
	public static class DynDCGRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCGRChecker() {
			super(new ElementaryCheckers.DCGRChecker(), EExtensionSetComputer.GROUNDED_SEM, "DC-GR-D");
		}

	}
	
	@ReflectorParam(name="DS-GR-D")
	public static class DynDSGRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSGRChecker() {
			super(new ElementaryCheckers.DSGRChecker(), EExtensionSetComputer.GROUNDED_SEM, "DS-GR-D");
		}

	}
	
	@ReflectorParam(name="SE-GR-D")
	public static class DynSEGRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSEGRChecker() {
			super(new ElementaryCheckers.SEGRChecker(), EExtensionSetComputer.GROUNDED_SEM, "SE-GR-D");
		}

	}
	
	@ReflectorParam(name="EE-GR-D")
	public static class DynEEGRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEEGRChecker() {
			super(new ElementaryCheckers.EEGRChecker(), EExtensionSetComputer.GROUNDED_SEM, "EE-GR-D");
		}

	}
	
	@ReflectorParam(name="DC-PR-D")
	public static class DynDCPRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCPRChecker() {
			super(new ElementaryCheckers.DCPRChecker(), EExtensionSetComputer.PREFERRED_SEM, "DC-PR-D");
		}

	}
	
	@ReflectorParam(name="DS-PR-D")
	public static class DynDSPRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSPRChecker() {
			super(new ElementaryCheckers.DSPRChecker(), EExtensionSetComputer.PREFERRED_SEM, "DS-PR-D");
		}

	}
	
	@ReflectorParam(name="SE-PR-D")
	public static class DynSEPRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSEPRChecker() {
			super(new ElementaryCheckers.SEPRChecker(), EExtensionSetComputer.PREFERRED_SEM, "SE-PR-D");
		}

	}
	
	@ReflectorParam(name="EE-PR-D")
	public static class DynEEPRChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEEPRChecker() {
			super(new ElementaryCheckers.EEPRChecker(), EExtensionSetComputer.PREFERRED_SEM, "EE-PR-D");
		}

	}
	
	@ReflectorParam(name="DC-ST-D")
	public static class DynDCSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCSTChecker() {
			super(new ElementaryCheckers.DCSTChecker(), EExtensionSetComputer.STABLE_SEM, "DC-ST-D");
		}

	}
	
	@ReflectorParam(name="DS-ST-D")
	public static class DynDSSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSSTChecker() {
			super(new ElementaryCheckers.DSSTChecker(), EExtensionSetComputer.STABLE_SEM, "DS-ST-D");
		}

	}
	
	@ReflectorParam(name="SE-ST-D")
	public static class DynSESTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSESTChecker() {
			super(new ElementaryCheckers.SESTChecker(), EExtensionSetComputer.STABLE_SEM, "SE-ST-D");
		}

	}
	
	@ReflectorParam(name="EE-ST-D")
	public static class DynEESTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEESTChecker() {
			super(new ElementaryCheckers.EESTChecker(), EExtensionSetComputer.STABLE_SEM, "EE-ST-D");
		}

	}
	
	@ReflectorParam(name="DC-SST-D")
	public static class DynDCSSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCSSTChecker() {
			super(new ElementaryCheckers.DCSSTChecker(), EExtensionSetComputer.SEMISTABLE_SEM, "DC-SST-D");
		}

	}
	
	@ReflectorParam(name="DS-SST-D")
	public static class DynDSSSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSSSTChecker() {
			super(new ElementaryCheckers.DSSSTChecker(), EExtensionSetComputer.SEMISTABLE_SEM, "DS-SST-D");
		}

	}
	
	@ReflectorParam(name="SE-SST-D")
	public static class DynSESSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSESSTChecker() {
			super(new ElementaryCheckers.SESSTChecker(), EExtensionSetComputer.SEMISTABLE_SEM, "SE-SST-D");
		}

	}
	
	@ReflectorParam(name="EE-SST-D")
	public static class DynEESSTChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEESSTChecker() {
			super(new ElementaryCheckers.EESSTChecker(), EExtensionSetComputer.SEMISTABLE_SEM, "EE-SST-D");
		}

	}
	
	@ReflectorParam(name="DC-STG-D")
	public static class DynDCSTGChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCSTGChecker() {
			super(new ElementaryCheckers.DCSTGChecker(), EExtensionSetComputer.STAGE_SEM, "DC-STG-D");
		}

	}
	
	@ReflectorParam(name="DS-STG-D")
	public static class DynDSSTGChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSSTGChecker() {
			super(new ElementaryCheckers.DSSTGChecker(), EExtensionSetComputer.STAGE_SEM, "DS-STG-D");
		}

	}
	
	@ReflectorParam(name="SE-STG-D")
	public static class DynSESTGChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSESTGChecker() {
			super(new ElementaryCheckers.SESTGChecker(), EExtensionSetComputer.STAGE_SEM, "SE-STG-D");
		}

	}
	
	@ReflectorParam(name="EE-STG-D")
	public static class DynEESTGChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEESTGChecker() {
			super(new ElementaryCheckers.EESTGChecker(), EExtensionSetComputer.STAGE_SEM, "EE-STG-D");
		}

	}
	
	@ReflectorParam(name="DC-ID-D")
	public static class DynDCIDChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDCIDChecker() {
			super(new ElementaryCheckers.DCIDChecker(), EExtensionSetComputer.IDEAL_SEM, "DC-ID-D");
		}

	}
	
	@ReflectorParam(name="DS-ID-D")
	public static class DynDSIDChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynDSIDChecker() {
			super(new ElementaryCheckers.DSIDChecker(), EExtensionSetComputer.IDEAL_SEM, "DS-ID-D");
		}

	}
	
	@ReflectorParam(name="SE-ID-D")
	public static class DynSEIDChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynSEIDChecker() {
			super(new ElementaryCheckers.SEIDChecker(), EExtensionSetComputer.IDEAL_SEM, "SE-ID-D");
		}

	}
	
	@ReflectorParam(name="EE-ID-D")
	public static class DynEEIDChecker extends ADynamicElementaryCheckerFactoryDecorator {
		
		public DynEEIDChecker() {
			super(new ElementaryCheckers.EEIDChecker(), EExtensionSetComputer.IDEAL_SEM, "EE-ID-D");
		}

	}
	
	private DynamicElementaryCheckers() {
		// nothing to do here
	}

}
