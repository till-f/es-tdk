include "Units.pts";

import fzi.mottem.examples.grammar.MeineKlasse;

package Common
{
    func int32 mul(int32 a, int32 b)
    {
        return a * b;
    }
}

test ExampleJavaUsage
{
	report info    : "An info";
    report warning : "A warning";
    report error   : "An error";
    report fatal   : "Another error";
    
    int32 i = 42;
    assert (i == 42) error: "This message should not be displayed";
    assert (i == 1)  error: "Value of i is not 1. It is " + i;
    
    Java.MeineKlasse klasse = new Java.MeineKlasse(Common.mul(i, 2));
    report info: "Klasse ist: " + klasse.getClass();
    int32 result = klasse.getParam();
    assert (result == 84) error: "This message should not be displayed";
}
