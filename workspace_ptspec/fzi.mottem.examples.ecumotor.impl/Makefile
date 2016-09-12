# ----------------------------------------------------------
# Configuration
# ----------------------------------------------------------

# Select source code files to compile here. This must be modified if new
# files are added to the project.
ASMSRC_CORE0=crt0.S init_mmu.S core0/tables.S core0/interrupt_handlers.S
CSRC_CORE0=init_misc.c core0/main.c core0/finalize_boot.c core0/app.c

ASMSRC_CORE1=crt0.S init_mmu.S core1/tables.S core1/interrupt_handlers.S
CSRC_CORE1=init_misc.c core1/main.c core1/app.c

# ----------------------------------------------------------
# Environment initialization
# ----------------------------------------------------------

SRCDIR=src
LNKDIR=ld
OBJDIR_CORE0=obj/core0
OBJDIR_CORE1=obj/core1
OUTDIR=bin

ASMOBJ_CORE0:=$(addprefix $(OBJDIR_CORE0)/, $(ASMSRC_CORE0:%.S=%.o))
ASMOBJ_CORE1:=$(addprefix $(OBJDIR_CORE1)/, $(ASMSRC_CORE1:%.S=%.o))
COBJ_CORE0:=$(addprefix $(OBJDIR_CORE0)/, $(CSRC_CORE0:%.c=%.o))
COBJ_CORE1:=$(addprefix $(OBJDIR_CORE1)/, $(CSRC_CORE1:%.c=%.o))

OBJ_CORE0:=$(ASMOBJ_CORE0) $(COBJ_CORE0)
OBJ_CORE1:=$(ASMOBJ_CORE1) $(COBJ_CORE1)

INCLUDE=-I./include -I.

OUT_CORE0=MPC5643L_core0
OUT_CORE1=MPC5643L_core1

LINK_CORE0=MPC5643L_core0.ld
LINK_CORE1=MPC5643L_core1.ld

DEFINES_CORE0+=-D__CORE0__
DEFINES_CORE1+=-D__CORE1__

ASM=$(MOTTEM_PPC_GCC)/powerpc-eabi-gcc
CC=$(MOTTEM_PPC_GCC)/powerpc-eabi-gcc
LD=$(MOTTEM_PPC_GCC)/powerpc-eabi-gcc
OBJCPY=$(MOTTEM_PPC_GCC)/powerpc-eabi-objcopy
OBJDUMP=$(MOTTEM_PPC_GCC)/powerpc-eabi-objdump

CFLAGS=-mcpu=8540 -mno-eabi -mregnames -Wa,-mregnames -Wa,-mbooke32 -Wa,-me500 -misel=yes -mspe=yes -mfloat-gprs=no -msoft-float -mno-string -msdata=none -fno-common -fzero-initialized-in-bss -std=gnu99 -fno-tree-loop-optimize -ffunction-sections -fdata-sections -O0 -g3 -Wa,-g3 -Wall $(INCLUDE)
AFLAGS=-mcpu=8540 -mno-eabi -mregnames -Wa,-mregnames -Wa,-mbooke32 -Wa,-me500 -misel=yes -mspe=yes -mfloat-gprs=no -msoft-float -mno-string -msdata=none -fshort-double -g3 -Wa,-g3 -gdwarf-2 -Wa,-gdwarf-2 -O0 -Wall -W $(INCLUDE)
LDFLAGS=-mcpu=8540 -nostartfiles -Wl,--gc-sections -g -Wl,-g


# ----------------------------------------------------------
# Target Rules
# ----------------------------------------------------------

all: $(OUTDIR)/$(OUT_CORE0).elf $(OUTDIR)/$(OUT_CORE1).elf

list:
	@echo CORE0
	@echo ASMSRC=$(ASMSRC_CORE0)
	@echo CSRC=$(CSRC_CORE0)
	@echo ASMOBJ=$(ASMOBJ_CORE0)
	@echo COBJ=$(COBJ_CORE0)
	@echo OBJ=$(OBJ_CORE0)
	@echo CORE1
	@echo ASMSRC=$(ASMSRC_CORE1)
	@echo CSRC=$(CSRC_CORE1)
	@echo ASMOBJ=$(ASMOBJ_CORE1)
	@echo COBJ=$(COBJ_CORE1)
	@echo OBJ=$(OBJ_CORE1)

$(OUTDIR):
	mkdir $(OUTDIR)

$(OBJDIR_CORE0):
	mkdir -p $(OBJDIR_CORE0)/core0

$(OBJDIR_CORE1):
	mkdir -p $(OBJDIR_CORE1)/core1

$(OBJDIR_CORE0)/%.o: $(SRCDIR)/%.c
	$(CC) $(CFLAGS) $(DEFINES_CORE0) -c -o $@ $<

#$(OBJDIR_CORE0)/%.o: $(SRCDIR)/%.Sx
#	$(CC) -E -o $(@:%.o=%.Sxx) $<
#	sed s/\\#NL\\#/\\n/ $(@:%.o=%.Sxx) > $(@:%.o=%.S)
#	$(ASM) $(AFLAGS) $(DEFINES_CORE0) -c -o $@ $(@:%.o=%.S)
#	$(CC) -E $< | sed s/\#NL\#/\\n/ > test.sss

$(OBJDIR_CORE0)/%.o: $(SRCDIR)/%.S
	$(ASM) $(AFLAGS) $(DEFINES_CORE0) -c -o $@ $<

$(OBJDIR_CORE1)/%.o: $(SRCDIR)/%.c
	$(CC) $(CFLAGS) $(DEFINES_CORE1) -c -o $@ $<

$(OBJDIR_CORE1)/%.o: $(SRCDIR)/%.S
	$(ASM) $(AFLAGS) $(DEFINES_CORE1) -c -o $@ $<

$(OUTDIR)/$(OUT_CORE0).elf: $(OUTDIR) $(OBJDIR_CORE0) $(LNKDIR)/$(LINK_CORE0) $(OBJ_CORE0)
	$(LD) $(LDFLAGS) -Wl,-Map,$(@:%.elf=%.map) -T $(LNKDIR)/$(LINK_CORE0) -o $@ $(OBJ_CORE0)
	$(OBJDUMP) -D $@ > $(@:%.elf=%.disassembly)

$(OUTDIR)/$(OUT_CORE1).elf: $(OUTDIR) $(OBJDIR_CORE1) $(LNKDIR)/$(LINK_CORE1) $(OBJ_CORE1)
	$(LD) $(LDFLAGS) -Wl,-Map,$(@:%.elf=%.map) -T $(LNKDIR)/$(LINK_CORE1) -o $@ $(OBJ_CORE1)
	$(OBJDUMP) -D $@ > $(@:%.elf=%.disassembly)

clean:
	rm -f $(OUTDIR)/$(OUT_CORE0).elf
	rm -f $(OUTDIR)/$(OUT_CORE0).map
	rm -f $(OUTDIR)/$(OUT_CORE0).srec
	rm -f $(OUTDIR)/$(OUT_CORE0).ihex
	rm -f $(OUTDIR)/$(OUT_CORE0).ieee
	rm -f $(OUTDIR)/$(OUT_CORE0).disassembly
	rm -f $(OUTDIR)/$(OUT_CORE1).elf
	rm -f $(OUTDIR)/$(OUT_CORE1).map
	rm -f $(OUTDIR)/$(OUT_CORE1).srec
	rm -f $(OUTDIR)/$(OUT_CORE1).ihex
	rm -f $(OUTDIR)/$(OUT_CORE1).ieee
	rm -f $(OUTDIR)/$(OUT_CORE1).disassembly
	rm -f $(patsubst %.o, %.i, $(OBJ_CORE0))
	rm -f $(patsubst %.o, %.s, $(OBJ_CORE0))
	rm -f $(OBJ_CORE0)
	rm -f $(patsubst %.o, %.i, $(OBJ_CORE1))
	rm -f $(patsubst %.o, %.s, $(OBJ_CORE1))
	rm -f $(OBJ_CORE1)

