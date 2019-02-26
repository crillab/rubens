package fr.cril.rubens.arg.core;

import java.util.HashMap;
import java.util.Map;

/**
 * A class used to handle descriptions of argumentation framework translations.
 * 
 * Creating an empty argumentation framework is considered as a translation; an instance of {@link ArgumentationFrameworkTranslation} describing it
 * can be obtained by calling {@link ArgumentationFrameworkTranslation#emptyAF()}.
 * Adding an argument or an attack can be decribed by respectively {@link ArgumentationFrameworkTranslation#newArgument(Argument)} and {@link ArgumentationFrameworkTranslation.ArgumentFrameworkAttackTranslation#newAttack(Attack)}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ArgumentationFrameworkTranslation {
	
	private static final Map<String, ArgumentationFrameworkTranslation> INSTANCES = new HashMap<>();
	
	private final String description;

	private ArgumentationFrameworkTranslation(final String description) {
		this.description = description;
	}
	
	private static ArgumentationFrameworkTranslation getInstance(final String description) {
		return INSTANCES.computeIfAbsent(description, ArgumentationFrameworkTranslation::new);
	}
	
	/**
	 * The description for a new empty AF.
	 * 
	 * @return the corresponding description
	 */
	public static ArgumentationFrameworkTranslation emptyAF() {
		return getInstance("EMPTY_AF");
	}
	
	/**
	 * The description for a new argument.
	 * 
	 * @param arg the new argument
	 * @return the corresponding description
	 */
	public static ArgumentationFrameworkTranslation newArgument(final Argument arg) {
		return getInstance("+arg("+arg.getName()+").");
	}
	
	/**
	 * Inner class dedicated to translations based on attack changes.
	 * 
	 * @author Emmanuel Lonca - lonca@cril.fr
	 */
	public static class ArgumentFrameworkAttackTranslation extends ArgumentationFrameworkTranslation {
		
		private static final Map<String, ArgumentFrameworkAttackTranslation> INSTANCES = new HashMap<>();
		
		private final boolean isNewAttack;
		
		private static ArgumentFrameworkAttackTranslation getInstance(final String description, final boolean isNewAttack) {
			return INSTANCES.computeIfAbsent(description, k -> new ArgumentFrameworkAttackTranslation(k, isNewAttack));
		}
		
		private ArgumentFrameworkAttackTranslation(final String description, final boolean isNewAttack) {
			super(description);
			this.isNewAttack = isNewAttack;
		}
		
		/**
		 * The description for a new attack.
		 * 
		 * @param attack the new attack 
		 * @return the corresponding description
		 */
		public static ArgumentFrameworkAttackTranslation newAttack(final Attack attack) {
			return getInstance("+att("+attack.getAttacker().getName()+","+attack.getAttacked().getName()+").", true);
		}
		
		/**
		 * The description for an attack removal.
		 * 
		 * @param attack the attack that was removed
		 * @return the corresponding description
		 */
		public static ArgumentFrameworkAttackTranslation attackRemoval(final Attack attack) {
			return getInstance("-att("+attack.getAttacker().getName()+","+attack.getAttacked().getName()+").", false);
		}
		
		/**
		 * Returns <code>true</code> iff this translation is an attack addition.
		 * 
		 * @return <code>true</code> iff this translation is an attack addition
		 */
		public boolean isNewAttack() {
			return this.isNewAttack;
		}
		
	}
	
	/**
	 * Gets the textual description of this translation.
	 * 
	 * @return the textual description of this translation
	 */
	public String getDescription() {
		return this.description;
	}

}
