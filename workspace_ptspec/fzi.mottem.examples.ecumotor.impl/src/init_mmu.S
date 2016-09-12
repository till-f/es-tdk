#include "init_mmu.h"

    .section .boot
    
	.global	init_mmu
	.type	init_mmu, @function
init_mmu:

    mflr     r29

    /*
     * Notice that is much more prudent initialize a MMU entry
     * for SRAM area as first operation otherwise it has to be PROPERLY
     * initialized by the debugger if the application has to be executed
     * directly from SRAM
     */
     
    /* SRAM area of core 0 */
    lis r3,TLB2_MAS0@h
    ori r3,r3,TLB2_MAS0@l
    lis r4,TLB2_MAS1@h
    ori r4,r4,TLB2_MAS1@l
    lis r5,TLB2_MAS2@h
    ori r5,r5,TLB2_MAS2@l
    lis r6,TLB2_MAS3@h
    ori r6,r6,TLB2_MAS3@l
    bl write_mmu_entry
    
    /* SRAM area of core 1 */
    lis r3,TLB3_MAS0@h
    ori r3,r3,TLB3_MAS0@l
    lis r4,TLB3_MAS1@h
    ori r4,r4,TLB3_MAS1@l
    lis r5,TLB3_MAS2@h
    ori r5,r5,TLB3_MAS2@l
    lis r6,TLB3_MAS3@h
    ori r6,r6,TLB3_MAS3@l
    bl write_mmu_entry

    lis r3,TLB0_MAS0@h
    ori r3,r3,TLB0_MAS0@l
    lis r4,TLB0_MAS1@h
    ori r4,r4,TLB0_MAS1@l
    lis r5,TLB0_MAS2@h
    ori r5,r5,TLB0_MAS2@l
    lis r6,TLB0_MAS3@h
    ori r6,r6,TLB0_MAS3@l
    bl write_mmu_entry

    lis r3,TLB1_MAS0@h
    ori r3,r3,TLB1_MAS0@l
    lis r4,TLB1_MAS1@h
    ori r4,r4,TLB1_MAS1@l
    lis r5,TLB1_MAS2@h
    ori r5,r5,TLB1_MAS2@l
    lis r6,TLB1_MAS3@h
    ori r6,r6,TLB1_MAS3@l
    bl write_mmu_entry

    lis r3,TLB4_MAS0@h
    ori r3,r3,TLB4_MAS0@l
    lis r4,TLB4_MAS1@h
    ori r4,r4,TLB4_MAS1@l
    lis r5,TLB4_MAS2@h
    ori r5,r5,TLB4_MAS2@l
    lis r6,TLB4_MAS3@h
    ori r6,r6,TLB4_MAS3@l
    bl write_mmu_entry

    lis r3,TLB5_MAS0@h
    ori r3,r3,TLB5_MAS0@l
    lis r4,TLB5_MAS1@h
    ori r4,r4,TLB5_MAS1@l
    lis r5,TLB5_MAS2@h
    ori r5,r5,TLB5_MAS2@l
    lis r6,TLB5_MAS3@h
    ori r6,r6,TLB5_MAS3@l
    bl write_mmu_entry

    lis r3,TLB6_MAS0@h
    ori r3,r3,TLB6_MAS0@l
    lis r4,TLB6_MAS1@h
    ori r4,r4,TLB6_MAS1@l
    lis r5,TLB6_MAS2@h
    ori r5,r5,TLB6_MAS2@l
    lis r6,TLB6_MAS3@h
    ori r6,r6,TLB6_MAS3@l
    bl write_mmu_entry

    lis r3,TLB7_MAS0@h
    ori r3,r3,TLB7_MAS0@l
    lis r4,TLB7_MAS1@h
    ori r4,r4,TLB7_MAS1@l
    lis r5,TLB7_MAS2@h
    ori r5,r5,TLB7_MAS2@l
    lis r6,TLB7_MAS3@h
    ori r6,r6,TLB7_MAS3@l
    bl write_mmu_entry

    lis r3,TLB8_MAS0@h
    ori r3,r3,TLB8_MAS0@l
    lis r4,TLB8_MAS1@h
    ori r4,r4,TLB8_MAS1@l
    lis r5,TLB8_MAS2@h
    ori r5,r5,TLB8_MAS2@l
    lis r6,TLB8_MAS3@h
    ori r6,r6,TLB8_MAS3@l
    bl write_mmu_entry

    lis r3,TLB9_MAS0@h
    ori r3,r3,TLB9_MAS0@l
    lis r4,TLB9_MAS1@h
    ori r4,r4,TLB9_MAS1@l
    lis r5,TLB9_MAS2@h
    ori r5,r5,TLB9_MAS2@l
    lis r6,TLB9_MAS3@h
    ori r6,r6,TLB9_MAS3@l
    bl write_mmu_entry

    mtlr r29

    blr
    .size	init_mmu, .-init_mmu


    .type	write_mmu_entry, @function
write_mmu_entry:
    /* Write MMU Assist Register 0 (MAS0); SPR 624 */
    mtspr   624, r3
    /* Write MMU Assist Register 1 (MAS1); SPR 625 */
    mtspr   625, r4
    /* Write MMU Assist Register 2 (MAS2); SPR 626 */
    mtspr   626, r5
    /* Write MMU Assist Register 3 (MAS3); SPR 627 */
    mtspr   627, r6
    /* Write the table entry */
    tlbwe

    blr
    .size	write_mmu_entry, .-write_mmu_entry
