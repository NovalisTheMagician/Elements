package math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Matrix4 implements OGLInterOp
{
	protected boolean isDirty;
	private FloatBuffer cache;
	
	public float 	m11, m12, m13, m14,
					m21, m22, m23, m24,
					m31, m32, m33, m34,
					m41, m42, m43, m44;
	
	public static final int BYTE_SIZE = 4 * 16;
	
	public Matrix4()
	{
		this(	0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0);
	}
	
	public Matrix4(float v)
	{
		this(	v, 0, 0, 0,
				0, v, 0, 0,
				0, 0, v, 0,
				0, 0, 0, v);
	}
	
	public Matrix4(	float m11, float m12, float m13, float m14,
					float m21, float m22, float m23, float m24,
					float m31, float m32, float m33, float m34,
					float m41, float m42, float m43, float m44)
	{
		this.m11 = m11; this.m12 = m12; this.m13 = m13; this.m14 = m14;
		this.m21 = m21; this.m22 = m22; this.m23 = m23; this.m24 = m24;
		this.m31 = m31; this.m32 = m32; this.m33 = m33; this.m34 = m34;
		this.m41 = m41; this.m42 = m42; this.m43 = m43; this.m44 = m44;
		
		cache = null;
		isDirty = true;
	}
	
	public void set(float m11, float m12, float m13, float m14,
					float m21, float m22, float m23, float m24,
					float m31, float m32, float m33, float m34,
					float m41, float m42, float m43, float m44)
	{
		this.m11 = m11; this.m12 = m12; this.m13 = m13; this.m14 = m14;
		this.m21 = m21; this.m22 = m22; this.m23 = m23; this.m24 = m24;
		this.m31 = m31; this.m32 = m32; this.m33 = m33; this.m34 = m34;
		this.m41 = m41; this.m42 = m42; this.m43 = m43; this.m44 = m44;
		
		isDirty = true;
	}
	
	public static void setBasis(Vector3 x, Vector3 y, Vector3 z, Matrix4 out)
	{
		out.m11 = x.x; out.m12 = y.x; out.m13 = z.x;
		out.m21 = x.y; out.m22 = y.y; out.m23 = z.y;
		out.m31 = x.z; out.m32 = y.z; out.m33 = z.z;
		
		out.isDirty = true;
	}
	
	public static void makeIdentity(Matrix4 out)
	{
		out.m11 = out.m22 = out.m33 = out.m44 = 1;
		out.m12 = out.m13 = out.m14 = 0;
		out.m21 = out.m23 = out.m24 = 0;
		out.m31 = out.m32 = out.m34 = 0;
		out.m41 = out.m42 = out.m43 = 0;
		
		out.isDirty = true;
	}
	
	public static void mul(Matrix4 lhs, Matrix4 rhs, Matrix4 out)
	{
		float m11 = lhs.m11 * rhs.m11 + lhs.m12 * rhs.m21 + lhs.m13 * rhs.m31 + lhs.m14 * rhs.m41;
		float m12 = lhs.m11 * rhs.m12 + lhs.m12 * rhs.m22 + lhs.m13 * rhs.m32 + lhs.m14 * rhs.m42;
		float m13 = lhs.m11 * rhs.m13 + lhs.m12 * rhs.m23 + lhs.m13 * rhs.m33 + lhs.m14 * rhs.m43;
		float m14 = lhs.m11 * rhs.m14 + lhs.m12 * rhs.m24 + lhs.m13 * rhs.m34 + lhs.m14 * rhs.m44;
		
		float m21 = lhs.m21 * rhs.m11 + lhs.m22 * rhs.m21 + lhs.m23 * rhs.m31 + lhs.m24 * rhs.m41;
		float m22 = lhs.m21 * rhs.m12 + lhs.m22 * rhs.m22 + lhs.m23 * rhs.m32 + lhs.m24 * rhs.m42;
		float m23 = lhs.m21 * rhs.m13 + lhs.m22 * rhs.m23 + lhs.m23 * rhs.m33 + lhs.m24 * rhs.m43;
		float m24 = lhs.m21 * rhs.m14 + lhs.m22 * rhs.m24 + lhs.m23 * rhs.m34 + lhs.m24 * rhs.m44;
		
		float m31 = lhs.m31 * rhs.m11 + lhs.m32 * rhs.m21 + lhs.m33 * rhs.m31 + lhs.m34 * rhs.m41;
		float m32 = lhs.m31 * rhs.m12 + lhs.m32 * rhs.m22 + lhs.m33 * rhs.m32 + lhs.m34 * rhs.m42;
		float m33 = lhs.m31 * rhs.m13 + lhs.m32 * rhs.m23 + lhs.m33 * rhs.m33 + lhs.m34 * rhs.m43;
		float m34 = lhs.m31 * rhs.m14 + lhs.m32 * rhs.m24 + lhs.m33 * rhs.m34 + lhs.m34 * rhs.m44;
		
		float m41 = lhs.m41 * rhs.m11 + lhs.m42 * rhs.m21 + lhs.m43 * rhs.m31 + lhs.m44 * rhs.m41;
		float m42 = lhs.m41 * rhs.m12 + lhs.m42 * rhs.m22 + lhs.m43 * rhs.m32 + lhs.m44 * rhs.m42;
		float m43 = lhs.m41 * rhs.m13 + lhs.m42 * rhs.m23 + lhs.m43 * rhs.m33 + lhs.m44 * rhs.m43;
		float m44 = lhs.m41 * rhs.m14 + lhs.m42 * rhs.m24 + lhs.m43 * rhs.m34 + lhs.m44 * rhs.m44;
		
		out.set(m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44);
	}
	
	// assuming vector = (x, y, z, 1)
	public static void transformCoord(Matrix4 mat, Vector3 vec, Vector3 out)
	{
		float x = mat.m11 * vec.x + mat.m12 * vec.y + mat.m13 * vec.z + mat.m14;
		float y = mat.m21 * vec.x + mat.m22 * vec.y + mat.m23 * vec.z + mat.m24;
		float z = mat.m31 * vec.x + mat.m32 * vec.y + mat.m33 * vec.z + mat.m34;
		
		out.x = x; out.y = y; out.z = z;
		out.isDirty = true;
	}
	
	// assuming vector = (x, y, z, 0)
	public static void transformNormal(Matrix4 mat, Vector3 vec, Vector3 out)
	{
		float x = mat.m11 * vec.x + mat.m12 * vec.y + mat.m13 * vec.z;
		float y = mat.m21 * vec.x + mat.m22 * vec.y + mat.m23 * vec.z;
		float z = mat.m31 * vec.x + mat.m32 * vec.y + mat.m33 * vec.z;
		
		out.x = x; out.y = y; out.z = z;
		out.isDirty = true;
	}
	
	public static float det(Matrix4 mat)
	{
		// TODO implement determinant of matrix
		throw new UnsupportedOperationException("Dragons");
	}
	
	public static void inverse(Matrix4 mat, Matrix4 out)
	{
		// TODO implement inverse of matrix
		throw new UnsupportedOperationException("Dragons");
	}
	
	public static void makeTranslation(Vector3 offset, Matrix4 out)
	{
		out.m14 = offset.x;
		out.m24 = offset.y;
		out.m34 = offset.z;
		
		out.isDirty = true;
	}
	
	public static void makeScale(Vector3 scale, Matrix4 out)
	{
		out.m11 = scale.x;
		out.m22 = scale.y;
		out.m33 = scale.z;
		
		out.isDirty = true;
	}
	
	public static void makeRotationX(float rad, Matrix4 out)
	{
		float s = (float)Math.sin(rad);
		float c = (float)Math.cos(rad);
		
		out.m22 = c; out.m23 = -s;
		out.m32 = s; out.m33 = c;
		
		out.isDirty = true;
	}
	
	public static void makeRotationY(float rad, Matrix4 out)
	{
		float s = (float)Math.sin(rad);
		float c = (float)Math.cos(rad);
		
		out.m11 = c; out.m13 = -s;
		out.m31 = s; out.m33 = c;
		
		out.isDirty = true;
	}
	
	public static void makeRotationZ(float rad, Matrix4 out)
	{
		float s = (float)Math.sin(rad);
		float c = (float)Math.cos(rad);
		
		out.m11 = c; out.m12 = -s;
		out.m21 = s; out.m22 = c;
		
		out.isDirty = true;
	}
	
	public static void makeRotationAxis(float rad, Vector3 axis, Matrix4 out)
	{
		float x = axis.x;
		float y = axis.y;
		float z = axis.z;
		float xx = x*x;
		float yy = y*y;
		float zz = z*z;
		float xy = x*y;
		float xz = x*z;
		float yz = y*z;
		float s = (float)Math.sin(rad);
		float c = (float)Math.cos(rad);
		
		out.m11 = c + xx*(1 - c);
		out.m12 = xy*(1 - c) - z*s;
		out.m13 = xz*(1 - c) + y*s;
		
		out.m21 = xy*(1 - c) + z*s;
		out.m22 = c + yy*(1 - c);
		out.m23 = yz*(1 - c) - x*s;
		
		out.m31 = xz*(1 - c) - y*s;
		out.m32 = yz*(1 - c) + x*s;
		out.m33 = c + zz*(1-c);
		
		out.isDirty = true;
	}
	
	public static void makePerspective(float fov, float aspect, float near, float far, Matrix4 out)
	{
		float top, bottom, left, right;
		
		top = (float)Math.tan(fov / 2) * near;
		bottom = -top;
		right = top * aspect;
		left = -top * aspect;
		
		makePerspective(left, right, bottom, top, near, far, out);
	}
	
	public static void makePerspective(float left, float right, float bottom, float top, float near, float far, Matrix4 out)
	{
		float w = right - left;
		float h = top - bottom;
		float d = far - near;
		
		out.m11 = (2 * near) / w;
		out.m22 = (2 * near) / h;
		out.m33 = (-far - near) / d;
		out.m34 = (-2 * far * near) / d;
		out.m43 = -1;
		out.m44 = 0;
		
		out.isDirty = true;
	}
	
	public static void makeOrtho(Matrix4 out)
	{
		out.m33 = 0;
		
		out.isDirty = true;
	}
	
	public static void makeOrtho(float left, float right, float bottom, float top, float near, float far, Matrix4 out)
	{
		// TODO implement orthographic projection in matrix form
		throw new UnsupportedOperationException("Dragons");
	}
	
	public static void makeOrtho(float width, float height, float depth, Matrix4 out)
	{
		out.m11 = 2 / width;
		out.m22 = 2 / height;
		out.m33 = 2 / depth;
		out.m41 = -1; out.m42 = 1; out.m44 = 1;
		
		out.isDirty = true;
	}
	
	public static void makeLookAt(Vector3 eye, Vector3 target, Vector3 up, Matrix4 out)
	{
		Vector3 x = new Vector3(), y = new Vector3(), z = new Vector3();
		
		Vector3.sub(eye, target, z); // z = eye - target   ---look direction
		Vector3.normalize(z, z);
		
		Vector3.cross(up, z, x); // x = up * z   ---right direction
		Vector3.normalize(x, x);
		
		Vector3.cross(z, x, y); // y = z * x   ---up direction
		Vector3.normalize(y, y);
		
		float tx = -Vector3.dot(x, eye);
		float ty = -Vector3.dot(y, eye);
		float tz = -Vector3.dot(z, eye);
		
		out.m11 = x.x; out.m21 = y.x; out.m31 = z.x; out.m14 = tx;
		out.m12 = x.y; out.m22 = y.y; out.m32 = z.y; out.m24 = ty;
		out.m13 = x.z; out.m23 = y.z; out.m33 = z.z; out.m34 = tz;
		
		out.m41 = 0; out.m42 = 0; out.m43 = 0; out.m44 = 1;
		
		out.isDirty = true;
	}
	
	public FloatBuffer makeBuffer()
	{
		if(cache == null)
		{
			cache = ByteBuffer.allocateDirect(BYTE_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
		}
		
		//if(isDirty)
		//{
			isDirty = false;
			
			cache.position(0);
			
			putBuffer(cache);
		//}
		
		cache.flip();
		
		return cache;
	}
	
	public void putBuffer(FloatBuffer buf)
	{	
		buf.put(m11); buf.put(m21); buf.put(m31); buf.put(m41);
		buf.put(m12); buf.put(m22); buf.put(m32); buf.put(m42);
		buf.put(m13); buf.put(m23); buf.put(m33); buf.put(m43);
		buf.put(m14); buf.put(m24); buf.put(m34); buf.put(m44);
	}
	
	@Override
	public String toString()
	{
		String res = "[";
		
		res += m11 + ", " + m12 + ", " + m13 + ", " + m14 + "\n";
		res += m21 + ", " + m22 + ", " + m23 + ", " + m24 + "\n";
		res += m31 + ", " + m32 + ", " + m33 + ", " + m34 + "\n";
		res += m41 + ", " + m42 + ", " + m43 + ", " + m44 + "\n";
		
		res += "]";
		return res;
	}
}
