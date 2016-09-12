
#ifdef __CORE0__
#define BAM_RCHW 0x05a0000
	.section .boot_data
	.global	_bam_data
	.type	_bam_data, @object
_bam_data:
	.long	BAM_RCHW		/* BAM data */
	.long	__start
	.size	_bam_data, .-_bam_data
#endif

.section .boot

/*
 * FUNCTION: __start
 * The entry point
 * ---------------------------------------------
 */
	.global	__start
	.type	__start, @function
__start:

    bl init_regs
    
    bl init_mmu

    bl init_sram_data

    /* setup trap table */
    lis r0,_trap_table@h
    ori r0,r0,_trap_table@l
    mtivpr r0
    
    /* setup interrupt table */
    lis r0,_interrupt_table@h
    ori r0,r0,_interrupt_table@l
    mtivpr r0
    
    /* required SP to always be 8-byte aligned */
    stwu   %r1, -8(%r1)
    
    /* no idea what this does */
    li     %r0, 0x0000
    stw    %r0, 0(%r1)
    li     %r0, -1
    stw    %r0, 4(%r1)

    /* jump to application code */
    bl	main

    wrteei 0
1:
    b	1b
    .size	__start, .-__start


/*
 * FUNCTION: __eabi
 * Dummy function for EABI conformance
 * powerpc-eabi-gcc generates call to __eabi at
 * beginning of main. This function is obsolete
 * with the compiler option "-mno-sdata", thus
 * we can use a simple dummy.
 * ---------------------------------------------
 */
    .global __eabi
    .type	__eabi, @function
__eabi:
    blr
    .size	__eabi, .-__eabi
    
    
/*
 * FUNCTION: init_regs
 * ---------------------------------------------
 */
	.type	init_regs, @function
init_regs:

    lis r3,0x200
    ori r3,r3,0x0
    mtmsr r3
    isync
    
    li   %r0,0
    li   %r1,0
    li   %r2,0
    li   %r3,0
    li   %r4,0
    li   %r5,0
    li   %r6,0
    li   %r7,0
    li   %r8,0
    li   %r9,0
    li   %r10,0
    li   %r11,0
    li   %r12,0
    li   %r13,0
    li   %r14,0
    li   %r15,0
    li   %r16,0
    li   %r17,0
    li   %r18,0
    li   %r19,0
    li   %r20,0
    li   %r21,0
    li   %r22,0
    li   %r23,0
    li   %r24,0
    li   %r25,0
    li   %r26,0
    li   %r27,0
    li   %r28,0
    li   %r29,0
    li   %r30,0
    li   %r31,0

    mtspr 400,%r30
    mtspr 401,%r30
    mtspr 402,%r30
    mtspr 403,%r30
    mtspr 404,%r30
    mtspr 405,%r30
    mtspr 406,%r30
    mtspr 407,%r30
    mtspr 408,%r30
    mtspr 409,%r30
    mtspr 410,%r30
    mtspr 411,%r30
    mtspr 412,%r30
    mtspr 413,%r30
    mtspr 414,%r30
    mtspr 415,%r30

    mtspr 528,%r30
    mtspr 529,%r30
    mtspr 530,%r30

    mtcrf 0xFF,%r31
    mtspr 284,%r31  /* TBL */
    mtspr 285,%r31  /* TBU */
    mtspr 272,%r31  /* SPRG0-7 */
    mtspr 273,%r31
    mtspr 274,%r31
    mtspr 275,%r31
    mtspr 276,%r31
    mtspr 277,%r31
    mtspr 278,%r31
    mtspr 279,%r31
    mtspr 604,%r31  /* SPRG8-9 */
    mtspr 605,%r31
    mtspr 26,%r31   /* SRR0-1 */
    mtspr 27,%r31
    mtspr 58,%r31   /* CSRR0-1 */
    mtspr 59,%r31
    mtspr 63,%r31   /* IVPR */
    mtspr 61,%r31   /* DEAR */
    mtspr 62,%r31   /* ESR */
    mtspr 570,%r31  /* MCSRR0 */
    mtspr 571,%r31  /* MCSRR1 */
    mtspr 572,%r31  /* MCSR */
    mtspr 573,%r31
    mtspr 574,%r31  /* DSRR0 */
    mtspr 575,%r31  /* DSRR1 */
    mtspr 340,%r31  /* TCR */
    mtspr 336,%r31  /* TSR */
    mtspr 512,%r31  /* SPEFSCR */
 
    mtspr 1,%r31    /* XER */
    mtspr 256,%r31  /* USPRG0 */
    mtspr 9,%r31    /* CTR */
    mtspr 308,%r31  /* DBCR0-6 */
    mtspr 309,%r31
    mtspr 310,%r31
    mtspr 561,%r31
    mtspr 563,%r31
    mtspr 564,%r31
    mtspr 603,%r31

   blr

.size	init_regs, .-init_regs

/*
 * FUNCTION: init_sram_data
 * ---------------------------------------------
 */
	.type	init_sram_data, @function
init_sram_data:

	addis	r8, 0, ee_stack0@ha		/* setup the stack pointer */
	addi	sp, r8, ee_stack0@l

	addis	r8, 0, ee_load_ram@ha	/* load _load_ram into r8 */
	addi	r8, r8, ee_load_ram@l
	addis	r9, 0, ee_sbss@ha		/* load _sbss into r9 */
	addi	r9, r9, ee_sbss@l
	addis	r10, 0, ee_sdata@ha		/* load _sdata into r10 */
	addi	r10, r10, ee_sdata@l

	cmplw	cr0, r8, r10			/* check if we have to move data */
	beq	2f

	/* NOTE: the loops below assume that .data and .bss are not empty */
1:
	lwz	r11, 0(r8)					/* copy .data where it belongs */
	addi	r8, r8, 4
	stw	r11, 0(r10)
	addi	r10, r10, 4
	cmplw	cr0, r10, r9
	blt	1b
	msync							/* Grant that all the copies are consistently in ram. This
											 ensure consistency if some code has been moved in RAM */

2:
	addis	r8, 0, ee_ebss@ha		/* load _ebss into r8 (r9 is _sbss) */
	addi	r8, r8, ee_ebss@l
	li	r11, 0						/* load 0 in r11 */

1:
	stw	r11, 0(r9)					/* zero out the BSS */
	addi	r9, r9, 4
	cmplw	cr0, r9, r8
	blt	1b

	addis	r8, 0, ee_estack@ha		/* load _estack into r8 */
	addi	r8, r8, ee_estack@l
	addis	r9, 0, ee_sstack@ha		/* load _sstack into r9 */
	addi	r9, r9, ee_sstack@l
	addis	r11, 0, 0xa5a5a5a5@ha	/* load the fill pattern in r11 */
	addi	r11, r11, 0xa5a5a5a5@l

1:
	stw	r11, 0(r9)					/* fill the stacks */
	addi	r9, r9, 4
	cmplw	cr0, r9, r8
	blt	1b

	addis	r13, 0, _SDA_BASE_@ha	/* load .sdata base into r13 */
	addi	r13, r13, _SDA_BASE_@l
	addis	r2, 0, _SDA2_BASE_@ha	/* load .sdata2 base into r2 */
	addi	r2, r2, _SDA2_BASE_@l

	blr

.size	init_sram_data, .-init_sram_data

