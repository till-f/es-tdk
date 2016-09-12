package fzi.mottem.ptspec.dsl.ui.highlighting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class PTSpecHighlightingConfiguration extends DefaultHighlightingConfiguration
{
	// id strings used during lexical highlighting (PTSpecAntlrTokenToAttributeIdMapper)
	public static final String DATATYPE_HL_ID        = "DATATYPE_HL_ID";
	public static final String SPECIALCONSTANT_HL_ID = "SPECIALCONSTANT_HL_ID";
	public static final String EVENT_HL_ID           = "EVENT_HL_ID";
	public static final String PHYSICALUNIT_HL_ID    = "PHYSICALUNIT_HL_ID";
	public static final String PROPERTY_HL_ID        = "PROPERTY_HL_ID";
	public static final String JAVAACCESSOR_HL_ID    = "JAVAACCESSOR_HL_ID";
	public static final String SEVERITY_FATAL_HL_ID  = "SEVERITY_FATAL_HL_ID";
	public static final String SEVERITY_ERROR_HL_ID  = "SEVERITY_ERROR_HL_ID";
	public static final String SEVERITY_WARNING_HL_ID= "SEVERITY_WARNING_HL_ID";
	public static final String SEVERITY_INFO_HL_ID   = "SEVERITY_INFO_HL_ID";

	// id strings used during semantic highlighting (PTSpecHighlightingCalculator)
	public static final String EXECUTOR_HL_ID            = "EXECUTOR_HL_ID";
	public static final String EXTERNALPACKAGE_HL_ID     = "EXTERNALPACKAGE_HL_ID";
	public static final String EXTERNALSYMBOL_HL_ID      = "EXTERNALSYMBOL_HL_ID";
	public static final String INSPECTORSYMBOL_HL_ID     = "INSPECTORSYMBOL_HL_ID";
	public static final String TESTSYMBOL_HL_ID          = "TESTSYMBOL_HL_ID";
	public static final String PACKAGE_HL_ID             = "TESTPACKAGE_HL_ID";
	public static final String PACKAGEVARIABLE_HL_ID     = "TESTPACKAGEVARIABLE_HL_ID";
	public static final String PACKAGEFUNCTION_HL_ID     = "TESTPACKAGEFUNCTION_HL_ID";
	public static final String JAVADECLTYPE_HL_ID        = "JAVAELEMENT_HL_ID";
	public static final String JAVAOTHERELEMENT_HL_ID    = "JAVAFINALELEMENT_HL_ID";
	
	// configure the acceptor providing the id, the description string
	// that will appear in the preference page, and the initial text style
	public void configure(IHighlightingConfigurationAcceptor acceptor)
	{
		super.configure(acceptor);
		
		acceptor.acceptDefaultHighlighting(DATATYPE_HL_ID,            "Data types", getDefaultDataTypeTextStyle());
		acceptor.acceptDefaultHighlighting(SPECIALCONSTANT_HL_ID,     "Special constants", getDefaultSpecialConstantTextStyle());
		acceptor.acceptDefaultHighlighting(EVENT_HL_ID,               "Events", getDefaultEventTextStyle());
		acceptor.acceptDefaultHighlighting(PHYSICALUNIT_HL_ID,        "Physical units", getDefaultPhysicalUnitTextStyle());
		acceptor.acceptDefaultHighlighting(PROPERTY_HL_ID,            "Properties", getDefaultPropertyTextStyle());
		acceptor.acceptDefaultHighlighting(JAVAACCESSOR_HL_ID,        "Java accessor", getDefaultJavaAccessorTextStyle());
		acceptor.acceptDefaultHighlighting(SEVERITY_FATAL_HL_ID,      "Severity fatal", getDefaultSeverityFatalTextStyle());
		acceptor.acceptDefaultHighlighting(SEVERITY_ERROR_HL_ID,      "Severity error", getDefaultSeverityErrorTextStyle());
		acceptor.acceptDefaultHighlighting(SEVERITY_WARNING_HL_ID,    "Severity warning", getDefaultSeverityWarningTextStyle());
		acceptor.acceptDefaultHighlighting(SEVERITY_INFO_HL_ID,       "Severity info", getDefaultSeverityInfoTextStyle());
		
		acceptor.acceptDefaultHighlighting(EXECUTOR_HL_ID,            "Executors", getDefaultExecutorTextStyle());
		acceptor.acceptDefaultHighlighting(EXTERNALPACKAGE_HL_ID,     "External packages", getDefaultExternalPackageTextStyle());
		acceptor.acceptDefaultHighlighting(EXTERNALSYMBOL_HL_ID,      "External symbols", getDefaultExternalSymbolTextStyle());
		acceptor.acceptDefaultHighlighting(INSPECTORSYMBOL_HL_ID,     "Inspector symbols", getDefaultInspectorSymbolTextStyle());
		acceptor.acceptDefaultHighlighting(TESTSYMBOL_HL_ID,          "Test symbols", getDefaultTestSymbolTextStyle());
		acceptor.acceptDefaultHighlighting(PACKAGE_HL_ID,             "Packages", getDefaultPackageTextStyle());
		acceptor.acceptDefaultHighlighting(PACKAGEVARIABLE_HL_ID,     "Package variables", getDefaultPackageConstantTextStyle());
		acceptor.acceptDefaultHighlighting(PACKAGEFUNCTION_HL_ID,     "Package functions", getDefaultPackageFunctionTextStyle());
		acceptor.acceptDefaultHighlighting(JAVADECLTYPE_HL_ID,        "Java classes", getDefaultJavaElementTextStyle());
		acceptor.acceptDefaultHighlighting(JAVAOTHERELEMENT_HL_ID,    "Java fields and functions", getDefaultTestSymbolTextStyle());
	}
	
	public TextStyle getDefaultDataTypeTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 162, 232));
		return textStyle; 
	}

	public TextStyle getDefaultSpecialConstantTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 0, 255));
		textStyle.setStyle(SWT.BOLD);
		return textStyle; 
	}

	public TextStyle getDefaultEventTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(255, 90, 0));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle getDefaultPhysicalUnitTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(128, 128, 128));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle getDefaultPropertyTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(128, 128, 128));
		return textStyle;
	}

	public TextStyle getDefaultExecutorTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle getDefaultExternalPackageTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 100, 120));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle getDefaultExternalSymbolTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle getDefaultInspectorSymbolTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setStyle(SWT.BOLD | SWT.ITALIC);
		return textStyle;
	}

	public TextStyle getDefaultTestSymbolTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		return textStyle;
	}

	public TextStyle getDefaultPackageTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(136, 0, 21));
		return textStyle; 
	}

	public TextStyle getDefaultPackageConstantTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setStyle(SWT.ITALIC);
		return textStyle; 
	}

	public TextStyle getDefaultPackageFunctionTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		return textStyle; 
	}
	
	
	public TextStyle getDefaultSeverityFatalTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(163, 21, 21));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle getDefaultSeverityErrorTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(163, 21, 21));
		return textStyle;
	}
	
	public TextStyle getDefaultSeverityWarningTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(230, 100, 40));
		return textStyle;
	}
	
	public TextStyle getDefaultSeverityInfoTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(60, 60, 60));
		return textStyle;
	}
	
	

	public TextStyle getDefaultJavaAccessorTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(180, 0, 110));
		textStyle.setStyle(SWT.BOLD);
		return textStyle; 
	}

	public TextStyle getDefaultJavaElementTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(180, 0, 110));
		return textStyle; 
	}

	// override some standard styles
	
	@Override
	public TextStyle numberTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		return textStyle;
	}

	@Override
	public TextStyle stringTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(163, 21, 21));
		return textStyle;
	}

	@Override
	public TextStyle commentTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 128, 0));
		return textStyle;
	}

	@Override
	public TextStyle keywordTextStyle()
	{
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 0, 255));
		return textStyle;
	}
}

