package com.net.test;

public class CrcTest {
	public static void main(String[] args) {

		// AA F5 32 00 10 29 08 00 08 00 54 42 30 31 30 35 35 31 30 30 30 37 30
		// 32 30 30 31 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 CC 2D
		// 00 00 00 B5

		String str = "08 00 08 00 54 42 30 31 30 35 35 31 30 30 30 37 30 32 30 30 31 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 CC 2D 00 00 00 ";

		String[] bytes = str.split(" ");
		byte[] bs = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			bs[i] = (byte) Integer.parseInt(bytes[i], 16);
		}

		byte i = crc16(bs);

		int[] test = { 1, 0 };

		test[1] = crc16(bs);

		byte[] b = tobyte(test);

		// System.out.println(b);

		byte[] bb = { (byte) 0xcc };

		System.out.println(byteTo(bb));
	}

	public static byte crc16(byte[] hds) {
		int crc = 0;
		for (int i = 0; i < hds.length; i++) {
			crc += hds[i];
		}
		return (byte) (crc >>> 0);
	}

	public static byte[] tobyte(int[] obj) {
		int len = obj[0];
		int data = obj[1];

		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			int shift = i * 8;
			buf[i] = (byte) (data >>> shift);
		}

		return buf;
	}

	public static int byteTo(byte[] bytes) {
		int value = 0;
		value += (bytes[0] & 0x000000FF) << 0;
		return value;
	}

	public static String byteToAscii(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		byte b = bytes[0];
		if (b != 0) {
			char c = (char) b;
			buff.append(c);
		}
		return buff.toString();
	}

}
