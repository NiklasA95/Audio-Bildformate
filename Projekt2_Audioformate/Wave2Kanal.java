package Projekt2_Audioformat;

import java.io.*;

public class Wave2Kanal {

	// Parameter-Definitionen
	private static int NSECS = 8;			        /* Spieldauer der Datei [s] */
	private static int SAM_RATE = 8000;			    /* Abtastrate [Hz] */

	private static int FMT_SIZE = 16;			    /* Groesse des Format-'Chunks' */
	private static int NUM_CHAN = 2;			    /* Anzahl der Kanaele */
	private static int PCM_FORM = 1;			    /* "PCM"-Format */
	private static int BYTEPSAM = 1;			    /* Bytes je Abtastwert */

	private static int BIT_PSAM = BYTEPSAM * 8;		/* Bits je Abtastwert */
	private static int BLCKALGN = (BYTEPSAM * NUM_CHAN);
	private static int BYTERATE = (BYTEPSAM * NUM_CHAN * SAM_RATE);
	
	private int FREQUENCY = 440; // Tonhoehe (in Hz)

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
		int dataSize = NSECS * BYTERATE;
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
			
			boolean toggle = true;
			
			for (int i = 0; i < dataSize; i++) {
				// PCM-Codierung:
				
				if(i % 16000 != 0) {
					toggle = !toggle;
				}
				
				if(toggle == true) {
					
					short channel1 = (new Double(127.5 * Math.sin(2 * M_PI * FREQUENCY
							/ SAM_RATE * i) + 128)).shortValue(); 
						out.writeByte(channel1);
				
				} else {
						
					short channel2 = (new Double(127.5 * Math.sin(2 * M_PI * 0
							/ SAM_RATE * i) + 128)).shortValue(); 
						out.writeByte(channel2);
				}
	
			} // for
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		} // catch
	} // writeFile
	
	public static void main(String args[]) {
		Wave2Kanal wav = new Wave2Kanal();
		String filename = "out2Kanal.wav";
		wav.writeFile(filename);
		System.out.println("Datei " + filename + " wurde geschrieben.");
	} // main
} // class Wave2Kanal
