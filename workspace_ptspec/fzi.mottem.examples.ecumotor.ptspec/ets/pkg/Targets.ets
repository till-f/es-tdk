/*
 * Targets.pts
 * Created on 04.11.2014 11:44:17
 */
include "../../model/MotorDemo.etm-testrig";

target Demonstrator { MotorDemo.EvalBoard.MPC5643L.Core0, 
                      MotorDemo.EvalBoard.MPC5643L.Core1,
                      MotorDemo.EvalBoard.CANPort }

target Demonstrator_PPC { MotorDemo.EvalBoard.MPC5643L.Core0, 
                          MotorDemo.EvalBoard.MPC5643L.Core1 }

target Demonstrator_CAN { MotorDemo.EvalBoard.CANPort }

target PWMIntegration { MotorDemo.EvalBoard.MPC5643L.Core0,
						MotorDemo.EvalBoard.MPC5643L.Core1,
						MotorDemo.EvalBoard.PWM0 }
