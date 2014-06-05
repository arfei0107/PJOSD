package com.example.pjosd;



enum HEADER{OPR, REF, ANS, ACK }
enum UNITID{ID1, ID2}
enum CONTROL{CLA1, CLA2, SUB1, SUB2}

public class CediaCommand {
	final static byte PJREQ[] = {0x50, 0x4A, 0x52, 0x45, 0x51};
	/* Header + UnitID + Control Command */
	//-- Header type
//	final static byte[] HEADER = {0x21, 0x3F, 0x40, 0x06};
	final static byte OPR = 0x21;
	final static byte REF = 0x3F;
	final static byte ANS = 0x40;
	final static byte ACK = 0x06;
	/* Unit ID type */
	final static byte[] UID = {(byte)0x89, 0x01};
	/* Control command (Class1, Class2, SubClass1, SubClass2) */
	final static byte[] NULL = {0x00, 0x00};  // NULL
	final static byte[] PW = {0x50, 0x57};    // Power 
	// -- Picture Adjust -------------------
	final static byte[] PM = {0x50, 0x4D}; // Picture Mode 
	// -------------------------------STR---
	final static byte[] IE = {0x49, 0x45}; // isf Adjustment mode
	final static byte[] PR = {0x50, 0x52}; // Color Profile
	final static byte[] CL = {0x43, 0x4C}; // Color Temp. Table
	final static byte[] CN = {0x43, 0x4E}; // Contrast
	final static byte[] BR = {0x42, 0x52}; // Brightness
	final static byte[] CO = {0x43, 0x4F}; // Color
	final static byte[] TI = {0x54, 0x49}; // Tint
	final static byte[] SH = {0x53, 0x48}; // Sharpness
	final static byte[] DE = {0x44, 0x45}; // Detail Enhance
	final static byte[] RN = {0x52, 0x4E}; // RNR
	final static byte[] MN = {0x4D, 0x4E}; // MNR
	final static byte[] BN = {0x42, 0x4E}; // BNR
	final static byte[] GC = {0x47, 0x43}; // Gamma Correction
	final static byte[] GF = {0x47, 0x46}; // Gamma Correction2
	final static byte[] DR = {0x44, 0x52}; // Gamma Red Data
	final static byte[] DG = {0x44, 0x47}; // Gamma Green Data
	final static byte[] DB = {0x44, 0x42}; // Gamma Blue Data
	// -------------------------------END---
	
	// -- Information ----------------------
	final static byte[] IF = {0x49, 0x46}; // Information 
	// -------------------------------STR---
	final static byte[] IN = {0x49, 0x4E}; // Input
	final static byte[] IS = {0x49, 0x53}; // Source
	final static byte[] DC = {0x44, 0x43}; // Source
	final static byte[] LT = {0x4C, 0x54}; // Lamp Time
	final static byte[] SV = {0x53, 0x56}; // Soft Version
	// -------------------------------END---
	/* Terminal */
	final static byte TERM = 0x0A;
	//final byte[] a = {};
		
	
}
