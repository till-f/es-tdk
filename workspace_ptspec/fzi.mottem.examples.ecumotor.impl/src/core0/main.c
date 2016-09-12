#include <mpc5643l.h>

#include "../init_misc.h"
#include "finalize_boot.h"
#include "app.h"

#define UNUSED(x) (void)(x)

int main(int argc, char* argv[])
{
    UNUSED(argc);
    UNUSED(argv);
    
    clear_wdg();
    init_modes_and_clocks();
    init_interrupt_controller();

    init_app();

    start_core1();

    run_app();

    return 0;
}
