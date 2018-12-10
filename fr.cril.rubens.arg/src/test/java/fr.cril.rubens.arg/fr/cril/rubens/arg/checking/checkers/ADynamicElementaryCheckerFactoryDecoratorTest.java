package fr.cril.rubens.arg.checking.checkers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;

public class ADynamicElementaryCheckerFactoryDecoratorTest {
	
	private DynamicArgumentationFramework af;

	@Before
	public void setUp() {
		final Argument arg = Argument.getInstance("a");
		final ArgumentSet argSet = ArgumentSet.getInstance(Collections.singleton(arg));
		final ArgumentationFramework initAf = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.singleton(argSet)));
		initAf.setArgUnderDecision(arg);
		this.af = new DynamicArgumentationFramework(initAf);
		final Attack attack = Attack.getInstance(arg, arg);
		final ArgumentationFramework af2 = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.singleton(attack)),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))));
		this.af.addTranlationNewAttack(attack, af2);
		this.af.addTranslationAttackRemoval(attack, initAf);
	}
	
	@Test
	public void testEECODSuccess() {
		final EECODChecker checker = new EECODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		checker.newTestGenerator();
		assertEquals(CheckResult.SUCCESS, checker.checkSoftwareOutput(this.af, "[\n[\n[a]\n]\n[\n[]\n]\n[\n[a]\n]\n]\n"));
	}
	
	@Test
	public void testEECODSyntaxError() {
		final EECODChecker checker = new EECODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		assertFalse(checker.checkSoftwareOutput(this.af, "[\n[\n[a]\n]\n[\n[]\n]\n[\n[a]\n]\n").isSuccessful());
	}
	
	@Test
	public void testEECODWrongNumberOfResponses() {
		final EECODChecker checker = new EECODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		assertFalse(checker.checkSoftwareOutput(this.af, "[\n[\n[a]\n]\n[\n[]\n]\n]\n").isSuccessful());
	}
	
	@Test
	public void testEECODWrongInitResponse() {
		final EECODChecker checker = new EECODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		assertFalse(checker.checkSoftwareOutput(this.af, "[\n[\n[]\n]\n[\n[]\n]\n[\n[a]\n]\n]\n").isSuccessful());
	}
	
	@Test
	public void testEECODWrongDynResponse() {
		final EECODChecker checker = new EECODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		assertFalse(checker.checkSoftwareOutput(this.af, "[\n[\n[a]\n]\n[\n[]\n]\n[\n[]\n]\n]\n").isSuccessful());
	}
	
	@ReflectorParam(enabled=false)
	private class EECODChecker extends ADynamicElementaryCheckerFactoryDecorator {

		public EECODChecker() {
			super(new ElementaryCheckers.EECOChecker(), EExtensionSetComputer.COMPLETE_SEM, "EE-CO-D");
		}
		
	}
	
	@Test
	public void testSECODSuccess() {
		final SECODChecker checker = new SECODChecker();
		checker.setOptions("outputFormat=ICCMA19");
		assertEquals(CheckResult.SUCCESS, checker.checkSoftwareOutput(this.af, "[\n[a]\n[]\n[a]\n]\n"));
	}
	
	@ReflectorParam(enabled=false)
	private class SECODChecker extends ADynamicElementaryCheckerFactoryDecorator {

		public SECODChecker() {
			super(new ElementaryCheckers.SECOChecker(), EExtensionSetComputer.COMPLETE_SEM, "SE-CO-D");
		}
		
	}
	
	@Test
	public void testDSCODSuccess() {
		final DSCODChecker checker = new DSCODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		checker.newTestGenerator();
		assertEquals(CheckResult.SUCCESS, checker.checkSoftwareOutput(this.af, "[YES, NO, YES]"));
	}
	
	@ReflectorParam(enabled=false)
	private class DSCODChecker extends ADynamicElementaryCheckerFactoryDecorator {

		public DSCODChecker() {
			super(new ElementaryCheckers.DSCOChecker(), EExtensionSetComputer.COMPLETE_SEM, "DS-CO-D");
		}
		
	}
	
	@Test
	public void testDCCODSuccess() {
		final DCCODChecker checker = new DCCODChecker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA19.getDecoderInstance());
		checker.newTestGenerator();
		assertEquals(CheckResult.SUCCESS, checker.checkSoftwareOutput(this.af, "[YES, NO, YES]"));
	}
	
	@ReflectorParam(enabled=false)
	private class DCCODChecker extends ADynamicElementaryCheckerFactoryDecorator {

		public DCCODChecker() {
			super(new ElementaryCheckers.DCCOChecker(), EExtensionSetComputer.COMPLETE_SEM, "DC-CO-D");
		}
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnknownProblem() {
		final FooBarChecker checker = new FooBarChecker();
		checker.checkSoftwareOutput(this.af, "");
	}
	
	@ReflectorParam(enabled=false)
	private class FooBarChecker extends ADynamicElementaryCheckerFactoryDecorator {

		public FooBarChecker() {
			super(new ElementaryCheckers.EECOChecker(), EExtensionSetComputer.COMPLETE_SEM, "FOOBAR-D");
		}
		
	}

}
