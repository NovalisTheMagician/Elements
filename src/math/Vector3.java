package math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Vector3 implements OGLInterOp
{
	public static final Vector3 X_AXIS = new Vector3(1, 0, 0);
	public static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
	public static final Vector3 Z_AXIS = new Vector3(0, 0, -1);
	
	protected boolean isDirty;
	private FloatBuffer cache;
	
	public float x, y, z;
	public static final int BYTE_SIZE = 4 * 3;
	
	public Vector3()
	{
		this(0.0f);
	}
	
	public Vector3(float s)
	{
		this(s, s, s);
	}
	
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		init();
	}
	
	public Vector3(Vector2 copyFrom)
	{
		x = copyFrom.x;
		y = copyFrom.y;
		z = 0;
		
		init();
	}
	
	public Vector3(Vector2 copyFrom, float z)
	{
		x = copyFrom.x;
		y = copyFrom.y;
		this.z = z;
		
		init();
	}
	
	public Vector3(Vector3 copyFrom)
	{
		x = copyFrom.x;
		y = copyFrom.y;
		z = copyFrom.z;
		
		init();
	}
	
	private void init()
	{
		cache = null;
		isDirty = true;
	}
	
	public void set(Vector3 setFrom)
	{
		x = setFrom.x;
		y = setFrom.y;
		z = setFrom.z;
	}
	
	public static void add(Vector3 lhs, Vector3 rhs, Vector3 out)
	{
		out.x = lhs.x + rhs.x;
		out.y = lhs.y + rhs.y;
		out.z = lhs.z + rhs.z;
		out.isDirty = true;
	}
	
	public static void sub(Vector3 lhs, Vector3 rhs, Vector3 out)
	{
		out.x = lhs.x - rhs.x;
		out.y = lhs.y - rhs.y;
		out.z = lhs.z - rhs.z;
		out.isDirty = true;
	}
	
	public static void mul(Vector3 lhs, float rhs, Vector3 out)
	{
		out.x = lhs.x * rhs;
		out.y = lhs.y * rhs;
		out.z = lhs.z * rhs;
		out.isDirty = true;
	}
	
	public static void mulAdd(Vector3 lhs, Vector3 rhs, float mul, Vector3 out)
	{
		out.x = lhs.x * mul + rhs.x;
		out.y = lhs.y * mul + rhs.y;
		out.z = lhs.z * mul + rhs.z;
		out.isDirty = true;
	}
	
	public static float len(Vector3 vec)
	{
		return (float)Math.sqrt(vec.x*vec.x + vec.y*vec.y + vec.z*vec.z);
	}
	
	public static float len2(Vector3 vec)
	{
		return (vec.x*vec.x + vec.y*vec.y + vec.z*vec.z);
	}
	
	public static float dot(Vector3 lhs, Vector3 rhs)
	{
		return lhs.x*rhs.x + lhs.y*rhs.y + lhs.z*rhs.z;
	}
	
	public static void cross(Vector3 lhs, Vector3 rhs, Vector3 out)
	{
		out.x = lhs.y*rhs.z - lhs.z*rhs.y;
		out.y = lhs.z*rhs.x - lhs.x*rhs.z;
		out.z = lhs.x*rhs.y - lhs.y*rhs.x;
		out.isDirty = true;
	}
	
	public static void normalize(Vector3 lhs, Vector3 out)
	{
		float len = Vector3.len(lhs);
		out.x = lhs.x / len;
		out.y = lhs.y / len;
		out.z = lhs.z / len;
		out.isDirty = true;
	}
	
	public FloatBuffer makeBuffer()
	{
		if(cache == null)
		{
			cache = ByteBuffer.allocateDirect(BYTE_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
		}
		
		if(isDirty)
		{
			isDirty = false;
			
			cache.position(0);
			
			putBuffer(cache);
		}
		
		cache.flip();
		
		return cache;
	}
	
	public void putBuffer(FloatBuffer buf)
	{
		buf.put(x);
		buf.put(y);
		buf.put(z);
	}
}
