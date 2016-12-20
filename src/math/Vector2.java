package math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Vector2 implements OGLInterOp
{
	protected boolean isDirty;
	private FloatBuffer cache;
	
	public float x, y;
	public static final int BYTE_SIZE = 4 * 2;
	
	public Vector2()
	{
		this(0.0f);
	}
	
	public Vector2(float s)
	{
		this(s, s);
	}
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
		
		init();
	}
	
	public Vector2(Vector2 copyFrom)
	{
		x = copyFrom.x;
		y = copyFrom.y;
		
		init();
	}
	
	private void init()
	{
		cache = null;
		isDirty = true;
	}
	
	public static void add(Vector2 lhs, Vector2 rhs, Vector2 out)
	{
		out.x = lhs.x + rhs.x;
		out.y = lhs.y + rhs.y;
		out.isDirty = true;
	}
	
	public static void sub(Vector2 lhs, Vector2 rhs, Vector2 out)
	{
		out.x = lhs.x - rhs.x;
		out.y = lhs.y - rhs.y;
		out.isDirty = true;
	}
	
	public static void mul(Vector2 lhs, float rhs, Vector2 out)
	{
		out.x = lhs.x * rhs;
		out.y = lhs.y * rhs;
		out.isDirty = true;
	}
	
	public static float len(Vector2 vec)
	{
		return (float)Math.sqrt(vec.x*vec.x + vec.y*vec.y);
	}
	
	public static float len2(Vector2 vec)
	{
		return (vec.x*vec.x + vec.y*vec.y);
	}
	
	public static float dot(Vector2 lhs, Vector2 rhs)
	{
		return lhs.x*rhs.x + lhs.y*rhs.y;
	}
	
	public static void perpLeft(Vector2 lhs, Vector2 out)
	{
		out.x = -lhs.y;
		out.y = lhs.x;
		out.isDirty = true;
	}
	
	public static void perpRight(Vector2 lhs, Vector2 out)
	{
		out.x = lhs.y;
		out.y = -lhs.x;
		out.isDirty = true;
	}
	
	public static void normalize(Vector2 lhs, Vector2 out)
	{
		float len = Vector2.len(lhs);
		out.x = lhs.x / len;
		out.y = lhs.y / len;
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
	}
}
