package Projekt2_Audioformat;

import java.io.*;

public class Uebung_1 {

	// Dateioperationen:
	FileInputStream f;
	DataInputStream in;

	private void openFile(String filename) {
		try {
		f = new FileInputStream(filename);
		in = new DataInputStream(f);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} // catch
	} // openFile
	
	private void closeFile() {
		try {
		in.close();
		} catch(IOException e) {
			e.printStackTrace();
		} // catch
	}

	/**
	 * Liest 4 Zeichen aus dem Eingabestrom und wandelt sie in Zeichenkette um.
	 * @param count Anzahl der zu lesenden Zeichen.
	 * @return Zeichenkette mit count Zeichen.
	 */
	private String readChars(int count) {
		char[] c= new char[count];
		try {
		for (int i=0; i<count; i++) {
			c[i] = (char) in.readByte();
		} // for
		} catch(IOException e) {
			e.printStackTrace();
		} // catch
		return new String(c);
	} // read4Chars
	
	/**
	 * Lesen von 2-Byte Integer.
	 * @return
	 */
	private int readInt2() {
		int b[] = new int[2];
		try {
		for (int i=0; i<2; i++) {
			b[i] = in.readUnsignedByte();
		} // for
		} catch(IOException e) {
			e.printStackTrace();
		} // catch
		return b[0] + 256*b[1];
	} // readInt2
	
	/**
	 * Lesen von 4-Byte Integer.
	 * @return
	 */
	private int readInt4() {
		int b[] = new int[4];
		try {
		for (int i=0; i<4; i++) {
			b[i] = in.readUnsignedByte();
		} // for
		} catch(IOException e) {
			e.printStackTrace();
		} // catch
		return b[0] + 256*b[1] + 256*256*b[2] + 256*256*256*b[3];
	} // readInt4
	
	public void readFileHeader() {
		String s;
		int riffLen, formatLen=0, pcmForm=0, numChannels=0, sampRate=0, byteRate=0,
		blockAlign=0, bitsPerSample=0, dataSize=0;
		
		float nSec=0;

			// Test auf "RIFF":
			s = readChars(4);
			if (s.equals("RIFF")) {
				System.out.println("RIFF ok");
			} else {
				System.out.println("FEHLER (RIFF): \"" + s + "\"");
			}

			// RIFF-Len:
			riffLen = readInt4();
			System.out.println("RIFF-Len: " + riffLen);
			
			// Test auf "WAVE":
			s = readChars(4);
			if (s.equals("WAVE")) {
				System.out.println("WAVE ok");
			} else {
				System.out.println("FEHLER (WAVE): \"" + s + "\"");
			}
			// Test auf "fmt ":
			s = readChars(4);
			if (s.equals("fmt ")) {
				System.out.println("fmt ok");
			} else {
				System.out.println("FEHLER (fmt): \"" + s + "\"");
			}
			
			// Format-Size:

			formatLen = readInt4();
			System.out.println("Format-Size: " + formatLen);
			
			// PCM-Typ:

			pcmForm = readInt2();
			System.out.println("PCM-Typ: " + pcmForm);
			
			// Anzahl Kanaele:
			
			numChannels = readInt2();
			System.out.println("Anzahl Kanaele: " + numChannels);
			
			// Sampling Rate:
			
			sampRate = readInt4();
			System.out.println("Sampling Rate: " + sampRate);
			
			
			// Byterate:
			
			byteRate = readInt4();
			System.out.println("Byterate: " + byteRate);
			

			// Block Align (Bytes pro Abtastblock)
			
			blockAlign = readInt2();
			System.out.println("Block Align: " + blockAlign);
			
			// Bits pro Sample:
			
			bitsPerSample = readInt2();
			System.out.println("Bits pro Sample: " + bitsPerSample);

		
			// Test auf "data":
			// Test auf "fmt ":
			s = readChars(4);
			if (s.equals("data")) {
				System.out.println("data ok");
			} else {
				System.out.println("FEHLER (data): \"" + s + "\"");
			}
			
			// Data Size:
			
			dataSize = readInt4();
			System.out.println("Data Size: " + dataSize);
			
			// Laenge Tonsignal:
			
			nSec = (float)riffLen / (sampRate * numChannels * bitsPerSample / 8);
			
			System.out.println("Laenge Tonsignal: " + nSec + " Sekunden");
						
	} // raeadFileHeader
	
	public void readFile(String filename) {
		openFile(filename);
		readFileHeader();
		closeFile();
	} // readFile
	
	public static void main(String args[]) {
		Uebung_1 wav = new Uebung_1();
		String filename = "Doorslam.wav";
		wav.readFile(filename);
	} // main
	
} // class Uebung_1
