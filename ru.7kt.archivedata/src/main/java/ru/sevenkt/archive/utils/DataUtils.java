package ru.sevenkt.archive.utils;

import java.nio.ByteBuffer;

public class DataUtils {

	public static float getFloat24Value(byte[] bytes) {		
//		System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(value)));
//		//b[1]=(byte) 0x80;
//		int plus = (b[1]&0x80)>>7;
//		
//		int exponent = (b[1]<<1)+((b[2]&0x80)>>7);
//		float mantissa = ( (float)( ( (b[2]&0x7F) * 256)+b[3]) / 32768 )+1;
//		float retVal = (float) (Math.pow(2, exponent-127)*mantissa);
//		retVal = plus==1?retVal*-1:retVal;
//		System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(retVal)));
		byte[] b =new byte[4];	
		for(int i=0; i<bytes.length; i++){
			b[3-i]=bytes[i];
		}
		//b[4]=0;
		ByteBuffer buf = ByteBuffer.wrap(b);
		float value = buf.getFloat();
	//	System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(value)));
		
		int aa = (Float.floatToRawIntBits(value)<<8);
		
	//	System.out.println(Integer.toBinaryString(aa));
		 float retVal = Float.intBitsToFloat(aa);
		return retVal;
	}

	public static int getIntValue(byte[] bytes) {
		int result = 0;
		int l = bytes.length - 1;
		for (int i = 0; i < bytes.length; i++)
			if (i == l)
				result += (bytes[i] & 0xFF) << i * 8;
			else
				result += (bytes[i] & 0xFF) << i * 8;
		return result;
	}

	public static float getFloat32Value(byte[] bytes) {
		byte[] b =new byte[4];	
		for(int i=0; i<bytes.length; i++){
			b[3-i]=bytes[i];
		}
		ByteBuffer buf = ByteBuffer.wrap(b);
		float value = buf.getFloat();
		return value;
	}

	public static long getLongValue(byte[] bytes) {
		byte[] b =new byte[4];	
		for(int i=0; i<bytes.length; i++){
			b[3-i]=bytes[i];
		}
		ByteBuffer buf = ByteBuffer.wrap(b);
		return buf.getInt();
	}
}
