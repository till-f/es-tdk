suite DSuite { DTest, PTest, RTest, MTest}

test PTest
{
	report info: "Lets start!";
	report info: "We're done!";
}

test DTest
{
	report info: "Lets start!";
	report warning: "We're done!";
}

test RTest
{
	report info: "Lets start!";
	report fatal: "We're done!";
}

test MTest
{
	report info: "Lets start!";
	report error: "We're done!";
}

/*
 * This is an exemplary test case that demonstrates how to program with PTSpec.
 */
test ExampleTest
{
	report info: "Lets start!";
	
	int32 i = 0;
	while(i < 10)
	{
		assert (i < 9) error: "Variable 'i' is too large!";
		i+=1;
	}
	
	report warning: "We're done!";
}