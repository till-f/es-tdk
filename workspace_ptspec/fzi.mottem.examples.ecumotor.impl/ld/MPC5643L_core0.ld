
__STACK_SIZE = DEFINED(__STACK_SIZE) ? __STACK_SIZE : 4K ;

MEMORY
{
	iboot(rx):	org = 0x00000000, len = 0x00004000
	iflash(rx):	org = 0x00010000, len = 0x00030000
	isram(rxw):	org = 0x40000000, len = 0x00040000
}

isram_base = ORIGIN(isram);
isram_len = LENGTH(isram);


ENTRY(__start)

SECTIONS
{

	.boot :
	{
		KEEP(*(.boot_data))
		*(.boot)
	} >iboot

    .tables : ALIGN(4096)
    {
        *(.interrupt_table)
        . = ALIGN(2048);
        *(.trap_table)
    } >iflash

	.text :
	{
		*(.text)
		*(.text.*)
		*(.init)
		*(.fini)
		*(rdata)
		*(.eini)
	} >iflash

	.rodata :
	{
		*(.rodata)
		*(.rodata.*)
	} >iflash

	.sdata2 :
	{
		_SDA2_BASE_ = 0x8000 ;
		*(.sdata2)
		*(.sdata2.*)
	} >iflash

	.sbss2	(NOLOAD) :
	{
		ee_ssbss2 = . ;
		*(.sbss2)
		*(.sbss2.*)
		ee_esbss2 = . ;
	} >iflash

	.rom_copy_info :
	{
		PROVIDE(ee_load_ram = . ) ;
	} >iflash

	.data  : AT(ee_load_ram)
	{
		ee_sdata = . ;
		_SDA_BASE_ = 0x8000 ;
		*(.data)
		*(.data.*)
	} >isram

	.sdata  : AT(ee_load_ram + SIZEOF(.data))
	{
		*(.sdata)
		*(.sdata.*)
	} >isram

	.sbss	(NOLOAD) :
	{
		ee_sbss = . ;
		*(.sbss)
		*(.sbss.*)
	} >isram

	.bss	(NOLOAD) :
	{
		*(.bss)
		*(.bss.*)
		ee_ebss = . ;
	} >isram

	.stack	(NOLOAD) :
	{
		ee_sstack = . ;
        . +=  __STACK_SIZE - 16 ;
        ee_stack0 = . ;
        . += 16 ;
		ee_estack = . ;
	} >isram

}