include "Targets.ets";

include "Units.ets";
 
package Common : Demonstrator_PPC
{
    func void bootToMain()
    {
        report info : "Booting...";
        
        break at Core1.run_app;
        run until Core0.run_app;

        report info : "Boot completed.";
    }
}