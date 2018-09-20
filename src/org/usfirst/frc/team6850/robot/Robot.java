/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6850.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends IterativeRobot {
	/**/
	private static final String kAutoL = "LEFT";
	private static final String kAutoM = "MIDDLE";
	private static final String kAutoR = "RIGHT";
	
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	/* Defining Port #*/
	private static final int kMotorPortL = 0;
	private static final int kMotorPortR = 1;
	private static final int kMotorBeltPort = 2;
	private static final int kJoystickPortL = 0; /*CHECK FOR CHANGE!*/
	private static final int kJoystickPortR = 1; /*CHECK FOR CHANGE!*/

	/* Defining SpeedController and JoyStick Names*/
	private SpeedController m_motorL;
	private SpeedController m_motorR;
	private Joystick m_joystickL;
	private Joystick m_joystickR;
	private Victor m_belt;
	
	/* Timer */
	private Timer timeAuto;
	private Timer timeFirst;
	private Timer timeSecond;
	private Timer timeThird;
	private Timer timeFourth;
	private Timer timeFifth;
	public static Boolean first = false, second = false, third = false, fourth = false, fifth = false, firsttime = true;
	
	
	@Override
	public void robotInit() {
		/* Camera */
		CameraServer.getInstance().startAutomaticCapture();
		/**/
		m_chooser.addDefault("LEFT", kAutoL);
		m_chooser.addObject("MIDDLE", kAutoM);
		m_chooser.addObject("RIGHT", kAutoR);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		/*Setting up Spark and Joystick with Ports and Group with Sparks*/
		m_motorL = new Spark(kMotorPortL);
		m_motorR = new Spark(kMotorPortR);
		
		m_belt = new Victor(kMotorBeltPort);
		
		m_joystickL = new Joystick(kJoystickPortL);
		m_joystickR = new Joystick(kJoystickPortR);
		
	}

	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		System.out.println("Auto selected: " + m_autoSelected);
		timeAuto = new Timer();  timeFirst = new Timer(); timeSecond = new Timer(); timeThird = new Timer(); timeFourth = new Timer(); timeFifth = new Timer();
		timeAuto.reset(); timeFirst.reset(); timeSecond.reset(); timeThird.reset(); timeFourth.reset(); timeFifth.reset(); timeAuto.start();
		first = true; second = false; third = false; fourth = false; fifth = false; firsttime = true;
	}

	@Override
	public void autonomousPeriodic() {
		String position = DriverStation.getInstance().getGameSpecificMessage();
		String[] positionarray = position.split("");
		String firstposition = positionarray[0];
		switch (m_autoSelected.toUpperCase()) {
		case "LEFT":
			if (firstposition.contains("L")) {
				System.out.println("Route: LL");
				if (first == true) {
					if (firsttime == true) {
						System.out.println("time start");
						timeFirst.start();
						firsttime = false;
					}
					Route_LL_FirstSeg();
				}
				if (first == false & second == true) {
					Route_TurnLeft();
				}
				if (first == false & second == false & third == true) {
					Route_LL_ThirdSeg();
				}
				if (first == false & second == false & third == false & fourth == true) {
					Route_Belt();
				}
			}
			if (firstposition.contains("R")) {
				System.out.println("Route: LR");
				if (first == true) {
					if (firsttime == true) {
						System.out.println("time start");
						timeFirst.start();
						firsttime = false;
					}
					Route_five_feet();
				}
			}
		break;
			case "RIGHT":
				if (firstposition.contains("L")) {
					System.out.println("Route: RL");
					if (first == true) {
						if (firsttime == true) {
							System.out.println("time start");
							timeFirst.start();
							firsttime = false;
						}
						Route_five_feet();
					}
				}
				if (firstposition.contains("R")) {
					System.out.println("Route: RR");
					if (first == true) {
						if (firsttime == true) {
							timeFirst.start();
							firsttime = false;
						}
						Route_RR_FirstSeg();
					}
					if (first == false & second == true) {
						Route_TurnRight();
					}
					if (first == false & second == false & third == true) {
						Route_RR_ThirdSeg();
					}
					if (first == false & second == false & third == false & fourth == true) {
						Route_Belt();
					}
				}
		break;
			case "MIDDLE":
				if (firstposition.contains("L")) {
					System.out.println("Route: ML");
					if (first == true) {
						if (firsttime == true) {
							System.out.println("time start");
							timeFirst.start();
							firsttime = false;
						}
						Route_ML_Seg();
					}
				}
				if (firstposition.contains("R")) {
					System.out.println("Route: MR");
					if (first == true) {
						if (firsttime == true) {
							timeFirst.start();
							firsttime = false;
						}
					Route_MR_FirstSeg();
				}
					if(first == false & second == true) {
							Route_MR_Belt();
					}
				}
		break;
	}
	}

	@Override
	public void teleopPeriodic() {
		/*Defining variable for joystick input value*/
		Double JoystickLY, JoystickRY;
		Boolean JoystickbtnL, JoystickbtnR;
		
		/*Get input value from the joystick*/
		JoystickLY = m_joystickL.getY();
		JoystickRY = m_joystickR.getY();
		JoystickbtnR = m_joystickR.getTrigger();
		JoystickbtnL = m_joystickL.getTrigger();
		
		System.out.println("R:" + JoystickbtnR);
		System.out.println("L:" + JoystickbtnL);
		
		/* belt motor*/
		if(JoystickbtnL == true & JoystickbtnR == true) {
			m_belt.set(0);
		}
		if(JoystickbtnL == false & JoystickbtnR == false) {
			m_belt.set(0);
		}
		if(JoystickbtnR == true & JoystickbtnL == false){
			m_belt.set(0.4);
		}
		if (JoystickbtnL == true & JoystickbtnR == false) {
			m_belt.set(-0.4);
		}
		

		
		/*set SpeedControllerGroup(L(left), R(right)) Speed to the value obtained from joystick*/
		m_motorL.set(-JoystickLY * 1.067);
		m_motorR.set(JoystickRY);
		
	}
	
	public void Route_RR_FirstSeg() {
		System.out.println("FirstTimer:" + timeFirst.get());
		if (first == true & timeFirst.get() < 1.5) {
			System.out.println("firstSeg");
			m_motorL.set(-0.7);
			m_motorR.set(0.88);
		}
		if (first == true & timeFirst.get() > 1.5) {
				System.out.println("firstSeg Stop");
				m_motorL.set(0);
				m_motorR.set(0);
				timeFirst.stop();
				timeFirst.reset();
				timeSecond.start();
				second = true;
				first = false;
			}
	}
	
	public void Route_TurnRight() {
		if (second == true & timeSecond.get() < 0.4) {
			System.out.println("Turning");
			m_motorL.set(0.6);
			m_motorR.set(0.6);
		}
		if (second == true & timeSecond.get() > 0.4) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeSecond.stop();
			timeSecond.reset();		
			timeThird.reset();
			timeThird.start();
			second = false;
			third = true;
			}
		}
	
	public void Route_RR_ThirdSeg() {
		System.out.println("TimeThird:" + timeThird.get());
		if (third == true & timeThird.get() < 0.41) {
			System.out.println("Third Last Seg");
			m_motorL.set(0.7);
			m_motorR.set(-0.88);
		}
		if (third == true & timeThird.get() > 0.41) {
			System.out.println("Third Last Seg Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeThird.stop();
			timeThird.reset();
			timeFourth.start();
			fourth = true;
			third = false;
			}
	}
	
	public void Route_LL_FirstSeg() {
		System.out.println("FirstTimer:" + timeFirst.get());
		if (first == true & timeFirst.get() < 1.8) {
			System.out.println("firstSeg");
			m_motorL.set(-0.7);
			m_motorR.set(0.88);
		}
		if (first == true & timeFirst.get() > 1.8) {
				System.out.println("firstSeg Stop");
				m_motorL.set(0);
				m_motorR.set(0);
				timeFirst.stop();
				timeFirst.reset();
				timeSecond.start();
				second = true;
				first = false;
			}
	}

	public void Route_TurnLeft() {
		if (second == true & timeSecond.get() < 0.4) {
			System.out.println("Turning");
			m_motorL.set(-0.6);
			m_motorR.set(-0.6);
		}
		if (second == true & timeSecond.get() > 0.4) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeSecond.stop();
			timeSecond.reset();		
			timeThird.reset();
			timeThird.start();
			second = false;
			third = true;
			}
		}
	
	public void Route_MR_Belt(){
		System.out.println("TimeThird:" + timeThird.get());
		if (second == true & timeSecond.get() < 2) {
			System.out.println("Belt Seg");
			m_belt.set(0.4);
		}
		if (second == true & timeSecond.get() > 2) {
			System.out.println("Third Last Seg Stop");
			m_belt.set(0);
			timeSecond.stop();
			timeSecond.reset();
			third = false;
			}
	}

	public void Route_LL_ThirdSeg() {
		System.out.println("TimeThird:" + timeThird.get());
		if (third == true & timeThird.get() < 0.41) {
			System.out.println("Third Last Seg");
			m_motorL.set(0.7);
			m_motorR.set(-0.88);
		}
		if (third == true & timeThird.get() > 0.41) {
			System.out.println("Third Last Seg Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeThird.stop();
			timeThird.reset();
			timeFourth.start();
			fourth = true;
			third = false;
			}
	}
	
	public void Route_Belt() {
		System.out.println("TimeThird:" + timeThird.get());
		if (fourth == true & timeFourth.get() < 2) {
			System.out.println("Belt Seg");
			m_belt.set(0.4);
		}
		if (fourth == true & timeFourth.get() > 2) {
			System.out.println("Third Last Seg Stop");
			m_belt.set(0);
			timeThird.stop();
			timeThird.reset();
			third = false;
			}
	}

	public void Route_MR_FirstSeg() {
		System.out.println("FirstTimer:" + timeFirst.get());
		if (first == true & timeFirst.get() < 1.55) {
			System.out.println("firstSeg");
			m_motorL.set(0.7);
			m_motorR.set(-0.8);
		}
		if (first == true & timeFirst.get() > 1.55) {
				System.out.println("firstSeg Stop");
				m_motorL.set(0);
				m_motorR.set(0);
				timeFirst.stop();
				timeFirst.reset();
				timeThird.start();
				second = true;
				first = false;
			}
	}
	
	public void Route_five_feet() {
		/* TIME NEEDS TO CHANGE! DISTANCE: 70*/
		System.out.println("FirstTimer:" + timeFirst.get());
		if (first == true & timeFirst.get() < 1.8) {
			System.out.println("firstSeg");
			m_motorL.set(-0.7);
			m_motorR.set(0.88);
		}
		if (first == true & timeFirst.get() > 1.8) {
				System.out.println("firstSeg Stop");
				m_motorL.set(0);
				m_motorR.set(0);
				timeFirst.stop();
				timeFirst.reset();
				timeSecond.start();
				second = true;
				first = false;
			}
	}
	
	public void Route_LR_SecondSeg() {
		System.out.println("SecondTimer:" + timeSecond.get());
		if (second == true & timeSecond.get() < 0.3) {
			System.out.println("Turning");
			m_motorL.set(1);
			m_motorR.set(1);
		}
		if (second == true & timeSecond.get() > 0.3) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeSecond.stop();
			timeSecond.reset();		
			timeThird.start();
			second = false;
			third = true;
			}
		}
	
	public void Route_LR_ThirdSeg() {
		/* CHECK FOR TIME CHANGE! DISTANCE: 181.56*/
		System.out.println("ThirdTimer:" + timeSecond.get());
		if (third == true & timeThird.get() < 0.3) {
			System.out.println("Turning");
			m_motorL.set(1);
			m_motorR.set(1);
		}
		if (second == true & timeThird.get() > 0.3) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeThird.stop();
			timeThird.reset();		
			timeFourth.start();
			third = false;
			fourth = true;
			}
		}
	
	public void Route_LR_FourthSeg() {
		System.out.println("SecondTimer:" + timeSecond.get());
		if (fourth == true & timeFourth.get() < 0.3) {
			System.out.println("Turning");
			m_motorL.set(1);
			m_motorR.set(1);
		}
		if (fourth == true & timeFourth.get() > 0.3) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeFourth.stop();
			timeFourth.reset();		
			timeFifth.start();
			fourth = false;
			fifth = true;
			}
		}
	
	public void Route_LR_FifthSeg() {
		/* CHECK FOR TIME CHANGE! DISTANCE: 70*/
		System.out.println("FifthTimer:" + timeFifth.get());
		if (fourth == true & timeFifth.get() < 0.3) {
			System.out.println("Turning");
			m_motorL.set(1);
			m_motorR.set(1);
		}
		if (fourth == true & timeFifth.get() > 0.3) {
			System.out.println("Turning Stop");
			m_motorL.set(0);
			m_motorR.set(0);
			timeFifth.stop();
			timeFifth.reset();
			fifth = false;
			}
		}
	
	public void Route_ML_Seg() {
			/* TIME NEEDS TO CHANGE! DISTANCE: 70*/
			System.out.println("FirstTimer:" + timeFirst.get());
			if (first == true & timeFirst.get() < 1.8) {
				System.out.println("firstSeg");
				m_motorL.set(0.7);
				m_motorR.set(-0.88);
			}
			if (first == true & timeFirst.get() > 1.8) {
					System.out.println("firstSeg Stop");
					m_motorL.set(0);
					m_motorR.set(0);
					timeFirst.stop();
					timeFirst.reset();
					timeSecond.start();
					second = true;
					first = false;
				}
		}
}