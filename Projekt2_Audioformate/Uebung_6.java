package Projekt2_Audioformat;

import java.io.*;

public class Uebung_6 {

	// Parameter-Definitionen
	private static int NSECS = 8;			        /* Spieldauer der Datei [s] */
	private static int SAM_RATE = 8000;			    /* Abtastrate [Hz] */

	private static int FMT_SIZE = 16;			    /* Groesse des Format-'Chunks' */
	private static int NUM_CHAN = 1;			    /* Anzahl der Kanaele */
	private static int PCM_FORM = 1;			    /* "PCM"-Format */
	private static int BYTEPSAM = 1;			    /* Bytes je Abtastwert */

	private static int BIT_PSAM = BYTEPSAM * 8;		/* Bits je Abtastwert */
	private static int BLCKALGN = (BYTEPSAM * NUM_CHAN);
	private static int BYTERATE = (BYTEPSAM * NUM_CHAN * SAM_RATE); // 8000
	
	//private int FREQUENCY = 440; // Tonhoehe (in Hz)

	private static double  M_PI = Math.PI;

	// Hilfsmethoden fuer Umwandlung von 32Bit-Integer-Zahlen
	// (niederwertige Bytes zuerst):
	private int B0(int x) {
		 return (byte) (0xff & (x >> 0));
	} // B0

	private int B1(int x) {
		 return (byte) (0xff & (x >> 8));
	} // B1
	
	private int B2(int x) {
		 return (byte) (0xff & (x >> 16));
	} // B2
	
	private int B3(int x) {
		 return (byte) (0xff & (x >> 24));
	} // B3
		
	public void writeFile(String filename) {
		int dataSize = NSECS * BYTERATE; // 80000
		int frmtSize = FMT_SIZE;
		int riffLen = 4 + 8 + frmtSize + 8 + dataSize;

		try {
			FileOutputStream f = new FileOutputStream(filename);
			DataOutputStream out = new DataOutputStream(f);

			// Schreiben Dateikopf:
			out.writeByte('R');
			out.writeByte('I');
			out.writeByte('F');
			out.writeByte('F');

			out.writeByte(B0(riffLen));
			out.writeByte(B1(riffLen));
			out.writeByte(B2(riffLen));
			out.writeByte(B3(riffLen));

			out.writeByte('W');
			out.writeByte('A');
			out.writeByte('V');
			out.writeByte('E');

			out.writeByte('f');
			out.writeByte('m');
			out.writeByte('t');
			out.writeByte(' ');

			out.writeByte(B0(frmtSize));	//Das Kleinste zuerst
			out.writeByte(B1(frmtSize));
			out.writeByte(B2(frmtSize));
			out.writeByte(B3(frmtSize));

			out.writeByte(B0(PCM_FORM));
			out.writeByte(B1(PCM_FORM));

			out.writeByte(B0(NUM_CHAN));
			out.writeByte(B1(NUM_CHAN));

			out.writeByte(B0(SAM_RATE));
			out.writeByte(B1(SAM_RATE));
			out.writeByte(B2(SAM_RATE));
			out.writeByte(B3(SAM_RATE));

			out.writeByte(B0(BYTERATE));
			out.writeByte(B1(BYTERATE));
			out.writeByte(B2(BYTERATE));
			out.writeByte(B3(BYTERATE));

			out.writeByte(B0(BLCKALGN));
			out.writeByte(B1(BLCKALGN));

			out.writeByte(B0(BIT_PSAM));
			out.writeByte(B1(BIT_PSAM));

			out.writeByte('d');
			out.writeByte('a');
			out.writeByte('t');
			out.writeByte('a');

			out.writeByte(B0(dataSize));
			out.writeByte(B1(dataSize));
			out.writeByte(B2(dataSize));
			out.writeByte(B3(dataSize));
			
			

			// Schreiben Audio-Daten:
			System.out.println("+++ Schreibe " + dataSize
					+ " Daten in WAV-Datei...");
			
			/* Ganze Note: 8000
			 * Halbe Note: 4000
			 * Viertel Note: 2000
			 * Achtel Note: 1000
			 */
			
			for (int i = 0; i < dataSize; i++) {
				// PCM-Codierung:
				
				// 1.Takt
				if(i < 1990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 2000 && i < 3990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
					
				if(i > 4000 && i < 5990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 6000 && i < 7990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 2.Takt				
				if(i > 8000 && i < 9990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				} 
				
				if(i > 10000 && i < 11990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				} 
				
				if(i > 12000 && i < 13990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 14000 && i < 14990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 15000 && i < 17990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 352
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 3.Takt + 1 Viertel
				if(i > 18000 && i < 19990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 20000 && i < 21990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 352
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 22000 && i < 23990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 4.Takt
				if(i > 24000 && i < 25990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 352
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 26000 && i < 27990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 28000 && i < 29990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 352
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 30000 && i < 30990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 396
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 31000 && i < 33990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 440
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 5.Takt + 1 Viertel
				if(i > 34000 && i < 35990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 396
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 36000 && i < 37990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 440
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 38000 && i < 39990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 396
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 6.Takt
				if(i > 40000 && i < 41990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 440
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 42000 && i < 43990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 396
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 44000 && i < 45990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 440
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 46000 && i < 46990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 396
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 47000 && i < 49990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 7.Takt + 1 Viertel
				if(i > 50000 && i < 51990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 52000 && i < 53990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 54000 && i < 55990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 297
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				// 8.Takt
				if(i > 56000 && i < 59990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 330
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}
				
				if(i > 60000 && i < 63990) {
					short iss1 = (new Double(127.5 * Math.sin(2 * M_PI * 352
						/ SAM_RATE * i) + 128)).shortValue(); 
					out.writeByte(iss1);
				}	
			} // for
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		} // catch
	} // writeFile
	
	public static void main(String args[]) {
		Uebung_6 wav = new Uebung_6();
		String filename = "outUebung_6.wav";
		wav.writeFile(filename);
		System.out.println("Datei " + filename + " wurde geschrieben.");
	} // main
} // class Uebung_6
