package fzi.mottem.runtime;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPROPERTY;

public enum EItemProperty
{
	NONE, InstructionPointer, Address, TriggerRising, TriggerFalling, TriggerAbove, TriggerBelow, SampleRate;
	
	public static PTS_EPROPERTY getPTSProperty(EItemProperty property)
	{
		switch(property)
		{
		case InstructionPointer:
			return PTS_EPROPERTY.INSTRUCTION_POINTER;
		case Address:
			return PTS_EPROPERTY.ADDRESS;
		case SampleRate:
			return PTS_EPROPERTY.SAMPLE_RATE;
		case TriggerAbove:
			return PTS_EPROPERTY.TRIGGER_ABOVE;
		case TriggerBelow:
			return PTS_EPROPERTY.TRIGGER_BELOW;
		case TriggerFalling:
			return PTS_EPROPERTY.TRIGGER_FALLING;
		case TriggerRising:
			return PTS_EPROPERTY.TRIGGER_RISING;
		case NONE:
			throw new RuntimeException();
		}
		
		throw new RuntimeException();
	}
}
