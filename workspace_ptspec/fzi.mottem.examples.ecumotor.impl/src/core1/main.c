#include <mpc5643l.h>

#include "../init_misc.h"
#include "app.h"

#define UNUSED(x) (void)(x)

int main(int argc, char* argv[])
{
    UNUSED(argc);
    UNUSED(argv);

    clear_wdg();
    init_interrupt_controller();

    init_app();

    // delay (otherwise breakpoints are not hit fast enough...)
    int i=0;
    for (i=0; i<1500000; i++);

    run_app();
    
    return 0;
}
