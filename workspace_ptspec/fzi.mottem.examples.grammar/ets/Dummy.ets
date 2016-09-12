include "../model/Demo.etm-testrig";

target MyTarget { Demo.EvalBoard.CPU.Core0 }

test MyTest : MyTarget default Core0
{
	int32 result = doWork();
	assert (result == var + 1) error: "Not OK!";
}
