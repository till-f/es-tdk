#ifndef SHARED_H_
#define SHARED_H_

struct core0infotype
{
	double rpm_act;
	double pwm_dc;
	double k_p;
	double k_i;
};

struct core1infotype
{
	int rpm_tar;
	double k_p;
	double k_i;
};

#endif
