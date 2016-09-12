package fzi.mottem.ptspec.dsl.ui.highlighting;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_EEVENT;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EINTEGRALDATATYPE;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ERUNTIMEPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESPECIALCONSTANT;

public class PTSpecAntlrTokenToAttributeIdMapper extends AbstractAntlrTokenToAttributeIdMapper
{

	private static final Pattern QUOTED = Pattern.compile("(?:^'([^']*)'$)|(?:^\"([^\"]*)\")$", Pattern.MULTILINE);
	private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}*");
	
	private static final HashSet<String> KEYWORDS_DATATYPE = enumLiteralListToHashSet(PTS_EINTEGRALDATATYPE.VALUES);
	private static final HashSet<String> KEYWORDS_SPECIALCONSTANT = enumLiteralListToHashSet(PTS_ESPECIALCONSTANT.VALUES);
	private static final HashSet<String> KEYWORDS_EVENT = enumLiteralListToHashSet(PTS_EEVENT.VALUES);
	private static final HashSet<String> KEYWORDS_PROPERTY = enumLiteralListToHashSet(PTS_EPROPERTY.VALUES);
	private static final HashSet<String> KEYWORDS_RUNTIMEPROPERTY = enumLiteralListToHashSet(PTS_ERUNTIMEPROPERTY.VALUES);
	
	@Override
	protected String calculateId(String tokenName, int tokenType)
	{
		if(PUNCTUATION.matcher(tokenName).matches())
		{
			return DefaultHighlightingConfiguration.PUNCTUATION_ID;
		}
		
		if(QUOTED.matcher(tokenName).matches())
		{
			String keyword = tokenName.substring(1, tokenName.length()-1);
			
			if (KEYWORDS_DATATYPE.contains(keyword))
				return PTSpecHighlightingConfiguration.DATATYPE_HL_ID;

			if ("physical".equals(keyword))
				return PTSpecHighlightingConfiguration.DATATYPE_HL_ID;
			
			if (KEYWORDS_SPECIALCONSTANT.contains(keyword))
				return PTSpecHighlightingConfiguration.SPECIALCONSTANT_HL_ID;
			
			if (KEYWORDS_EVENT.contains(keyword))
				return PTSpecHighlightingConfiguration.EVENT_HL_ID;
			
			if (KEYWORDS_PROPERTY.contains(keyword))
				return PTSpecHighlightingConfiguration.PROPERTY_HL_ID;
			
			if (KEYWORDS_RUNTIMEPROPERTY.contains(keyword))
				return PTSpecHighlightingConfiguration.PROPERTY_HL_ID;
			
			if ("Runtime".equals(keyword))
				return PTSpecHighlightingConfiguration.EXECUTOR_HL_ID;
				
			if ("Java".equals(keyword))
				return PTSpecHighlightingConfiguration.JAVAACCESSOR_HL_ID;
				
			if (PTS_ESEVERITY.FATAL.getLiteral().equals(keyword))
				return PTSpecHighlightingConfiguration.SEVERITY_FATAL_HL_ID;

			if (PTS_ESEVERITY.ERROR.getLiteral().equals(keyword))
				return PTSpecHighlightingConfiguration.SEVERITY_ERROR_HL_ID;

			if (PTS_ESEVERITY.WARNING.getLiteral().equals(keyword))
				return PTSpecHighlightingConfiguration.SEVERITY_WARNING_HL_ID;

			if (PTS_ESEVERITY.INFO.getLiteral().equals(keyword))
				return PTSpecHighlightingConfiguration.SEVERITY_INFO_HL_ID;

			return DefaultHighlightingConfiguration.KEYWORD_ID;
		}
		
		if("RULE_STRING".equals(tokenName))
		{
			return DefaultHighlightingConfiguration.STRING_ID;
		}
		
		if("RULE_INT".equals(tokenName) || "RULE_HEXINT".equals(tokenName))
		{
			return DefaultHighlightingConfiguration.NUMBER_ID;
		}
		
		if("RULE_ML_COMMENT".equals(tokenName) || "RULE_SL_COMMENT".equals(tokenName))
		{
			return DefaultHighlightingConfiguration.COMMENT_ID;
		}
		
		return DefaultHighlightingConfiguration.DEFAULT_ID;
	}
	
	@SuppressWarnings("unchecked")
	private static HashSet<String> enumLiteralListToHashSet(Object... enumItemsList)
	{
		HashSet<String> hs = new HashSet<String>();
		for (Object enumItems : enumItemsList)
		{
			for (Enumerator enumValue: (List<Enumerator>)(enumItems))
			{
				hs.add(enumValue.getLiteral());
			}
		}
		return hs;
	}

}
