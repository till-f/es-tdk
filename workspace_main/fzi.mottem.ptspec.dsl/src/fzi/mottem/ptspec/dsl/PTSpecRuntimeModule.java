/*
 * generated by Xtext
 */
package fzi.mottem.ptspec.dsl;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class PTSpecRuntimeModule extends fzi.mottem.ptspec.dsl.AbstractPTSpecRuntimeModule {

	// Standard Bindings moved from generated file to here (because fragments needed to
	// be removed from the MWE2 workflow and bindings needed to be customized)
	// ------------------------------

	// originally contributed by org.eclipse.xtext.generator.validation.ValidatorFragment
	// but moved to non-generated file
	@org.eclipse.xtext.service.SingletonBinding(eager=true)	public Class<? extends fzi.mottem.ptspec.dsl.validation.PTSpecValidator> bindPTSpecValidator() {
		return fzi.mottem.ptspec.dsl.validation.PTSpecValidator.class;
	}

	// originally contributed by org.eclipse.xtext.generator.generator.GeneratorFragment
	// but moved to non-generated file
	public Class<? extends org.eclipse.xtext.generator.IGenerator> bindIGenerator() {
		return fzi.mottem.ptspec.dsl.generator.PTSpecGenerator.class;
	}
	
	// originally contributed by org.eclipse.xtext.generator.types.TypesGeneratorFragment
	// but moved to non-generated file 
	public Class<? extends org.eclipse.xtext.scoping.IGlobalScopeProvider> bindIGlobalScopeProvider() {
		//return org.eclipse.xtext.common.types.xtext.TypesAwareDefaultGlobalScopeProvider.class;
		return org.eclipse.xtext.scoping.impl.ImportUriGlobalScopeProvider.class;
	}

	
	// Custom / Non-Standard Bindings
	// ------------------------------

	public Class<? extends fzi.mottem.ptspec.dsl.scoping.PTSpecSymbolReferenceScopeProvider> bindPTSSymbolReferenceScope()
	{
		return fzi.mottem.ptspec.dsl.scoping.PTSpecSymbolReferenceScopeProvider.class;
	}

	public Class<? extends fzi.mottem.ptspec.dsl.scoping.PTSpecJavaScopeProvider> bindJavaGlobalScopeProvider()
	{
		return fzi.mottem.ptspec.dsl.scoping.PTSpecJavaScopeProvider.class;
	}
	
}
