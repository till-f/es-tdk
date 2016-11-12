#include "stm32f4xx.h"
#include "stm32f4xx_gpio.h"

#include "SWC_Controller.h"

uint32 distance_control(float distance, float dist_change);
void search_control(float distance);
void writeSpeed();

#define SEARCH_CONTROL_ACTIVE 1

unsigned int distance_value = 0;
float distance_change = 0;
float current_distance = 0;
float prev_distance = 0;
uint32 firstTime = 0;
float returnDistance = 0;
uint32 returnDistance2 = 0;

uint32 searchMode = 0;
uint32 searchStep = 0;
uint32 searchTicks = 0;

int speed_override_active = 0;
int speed_left = 0;
int speed_right = 0;

void startSearchMode()
{
	searchMode = 1;
	searchStep = 0;
	searchTicks = 0;
}

void runnableSWC_Controller(void)
{
	distance_value=Rte_S2Read_Distance();

	if(firstTime == 0)
	{
		current_distance = distance_value;
		prev_distance = distance_value;
		distance_change = 0;
		firstTime = 1;
	}
	else
	{
		current_distance = distance_value;
		distance_change = current_distance-prev_distance;
	}

	if (!speed_override_active)
	{
#if SEARCH_CONTROL_ACTIVE
		if (searchMode)
		{
			search_control(distance_value);
		}
		else
#endif
		if(distance_value < 20)
		{
			speed_left = 0;
			speed_right = 0;

			startSearchMode();
		}
		else if(distance_value > 40)
		{
			speed_left = 100;
			speed_right = 100;
		}
		else
		{
			uint32_t speed = distance_control(current_distance,distance_change);

			speed_left = speed;
			speed_right = speed;

			if (speed < 6)
				startSearchMode();
		}
	}

	writeSpeed();

	prev_distance = distance_value;
}

uint32 distance_control(float distance, float dist_change)
{
	returnDistance = 0;

	if(distance_change<0)
	{
		//object nähert sich
		returnDistance = 100 + 5*dist_change;
	}
	else
	{
		//object entfernt sich
		returnDistance = 100 - 5*dist_change;
	}
	float temp_speed=(distance-20)*5/100;
	returnDistance2 = returnDistance * temp_speed;

	return returnDistance2;
}

#define SEARCH_SPEED_VALUE 40
void search_control(float distance)
{
	switch (searchStep)
	{
	case 0:
		speed_left = -SEARCH_SPEED_VALUE;
		speed_right = SEARCH_SPEED_VALUE;
		break;
	case 1:
		searchMode = 0;
		return;
	}

	searchTicks++;

	if (searchTicks > 110)
		searchStep = 1;
}

void writeSpeed()
{
	HW_SET_SPEED_LEFT(speed_left);
	HW_SET_SPEED_RIGHT(speed_right);
}
















