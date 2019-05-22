package com.test.web.document.html._long;

import com.neaterbits.util.buffers.BufferUtil;
import com.test.web.types.Debug;

public class LongHTMLBase extends BufferUtil {
	
	private static final boolean DEBUG = Debug.DEBUG_HTML_BASE;
	
	private static final int IDX_HEADER = 0;
	private static final long FLAG_TEXT = (1L << 63);

	public static int getUpper32(long [] buf, int offsetAndIdx) {
		return (int)(buf[offsetAndIdx] >>> 32);
	}

	public static void setUpper32(long [] buf, int offsetAndIdx, int next) {
		long encoded = buf[offsetAndIdx];

		encoded &= 0x00000000FFFFFFFFL;
		encoded |= unsignedIntToLong(next) << 32;
		set(buf, offsetAndIdx, encoded);
	}

	public static int getLower32(long [] buf, int offsetAndIdx) {
		return (int)(buf[offsetAndIdx] & 0x00000000FFFFFFFFL);
	}

	public static void setLower32(long [] buf, int offsetAndIdx, int value) {
		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFF00000000L;
		encoded |= unsignedIntToLong(value);
		set(buf, offsetAndIdx, encoded);
	}

	public static int get16To32(long [] buf, int offsetAndIdx) {
		return (int)((buf[offsetAndIdx] & 0x00000000FFFF0000L) >> 16);
	}

	public static void set16To32(long [] buf, int offsetAndIdx, int value) {
		
		if (value > Short.MAX_VALUE) {
			throw new IllegalArgumentException("value > Short.MAX_VALUE");
		}

		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFF0000FFFFL;
		encoded |= unsignedIntToLong(value) << 16;
		set(buf, offsetAndIdx, encoded);
	}

	public static int get0To16(long [] buf, int offset) {
		return (int)(buf[offset] & 0x000000000000FFFFL);
	}

	public static void set0To16(long [] buf, int offsetAndIdx, int value) {

		if (value > Short.MAX_VALUE) {
			throw new IllegalArgumentException("value > Short.MAX_VALUE");
		}

		long encoded = buf[offsetAndIdx];

		encoded &= 0xFFFFFFFFFFFF0000L;
		encoded |= unsignedIntToLong(value);
		set(buf, offsetAndIdx, encoded);
	}
	
	private static String getMethodName() {
		final StackTraceElement [] stackTrace = Thread.currentThread().getStackTrace();
		
		for (StackTraceElement element : stackTrace) {
			if (   !element.getClassName().equals(LongHTMLBase.class.getName())
			    && !element.getClassName().equals(Thread.class.getName())) {
				
				return element.getMethodName();
			}
		}
		
		return null;
	}
	
	static <E extends Enum<E>> void setHeaderEnumBits(long [] buf, int offset, E enumValue, int shift, int bits) {
		final long cur = getHeader(buf, offset);

		setHeader(buf, offset, setEnumBits(cur, enumValue, shift, bits));
	}

	static <E extends Enum<E>> E getHeaderEnum(long [] buf, int offset, Class<E> enumClass, int shift, int bits) {
		final long header = getHeader(buf, offset);
		
		return getEnum(header, enumClass, shift, bits);
	}

	
	static void setHeader(long [] buf, int offset, long updated) {
		set(buf, offset + IDX_HEADER, updated);
	}

	
	static void set(long [] buf, int offset, long updated) {

		if (DEBUG) {
		
			System.out.format("%s/%s: Update of %d from %016x to %016x\n",
				getMethodName(),
				Thread.currentThread().getStackTrace()[2].getMethodName(),
				offset, buf[offset + IDX_HEADER], updated);
		}
		
		buf[offset] = updated;
	}

	static long getHeader(long [] buf, int offset) {
		return buf[offset + IDX_HEADER];
	}

	static void setParent(long [] buf, int offset, int next) {
		
		if (next > 0x7FFFFFFF) {
			throw new IllegalArgumentException("next > 0x7FFFFFFF");
		}
		
		long encoded = buf[offset + IDX_HEADER];
		
		encoded &= 0xFFFFFFFF00000000L;
		encoded |= unsignedIntToLong(next);
		
		//setHeader(buf, offset, encoded);
	}

	static boolean isText(long [] buf, int offset) {
		final long header = getHeader(buf, offset);
		
		return (header & FLAG_TEXT) != 0;
	}

	static boolean isElement(long [] buf, int offset) {
		return !isText(buf, offset);
	}

	static void setAsText(long [] buf, int offset) {
		//System.out.println("## setAsText at " + offset + IDX_HEADER + " of " + System.identityHashCode(buf));
		
		set(buf, offset + IDX_HEADER, buf[offset + IDX_HEADER] | FLAG_TEXT);
	}
	
	static void setAsText(long [] buf, int offset, int next) {
		setHeader(buf, offset, FLAG_TEXT | unsignedIntToLong(next));
	}

	// Get buffer index of next element
	static int getParent(long [] buf, int offset) {
		return (int)(getHeader(buf, offset + IDX_HEADER) & 0x00000000FFFFFFFFL);
	}

	static void setFlag(long [] buf, int offset, long flag, boolean set) {
		final long cur = getHeader(buf, offset);
		
		final long updated;

		if (set) {
			updated = cur | flag;
		}
		else {
			updated = cur & (~flag);
		}

		setHeader(buf, offset, updated);
	}
}

