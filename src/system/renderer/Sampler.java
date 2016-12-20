package system.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Sampler 
{
	public static enum WrapModes
	{
		REPEAT(GL_REPEAT),
		CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
		CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
		MIRROR(GL_MIRRORED_REPEAT);
		
		private int glId;
		private WrapModes(int glId)
		{
			this.glId = glId;
		}
		
		public int getGLName()
		{
			return glId;
		}
	};
	
	public static enum FilterModes
	{
		NEAREST(GL_NEAREST),
		LINEAR(GL_LINEAR),
		NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST),
		NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
		LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
		LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);
		
		private int glId;
		private FilterModes(int glId)
		{
			this.glId = glId;
		}
		
		public int getGLName()
		{
			return glId;
		}
	};
	
	private int samplerId;
	
	public Sampler()
	{
		samplerId = glGenSamplers();
	}
	
	public void setParameter(int parameter, int value)
	{
		glSamplerParameteri(samplerId, parameter, value);
	}
	
	public void setParameter(int parameter, float value)
	{
		glSamplerParameterf(samplerId, parameter, value);
	}
	
	public void setParameter(int parameter, int[] values)
	{
		IntBuffer buf = ByteBuffer.allocateDirect(values.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		buf.put(values); buf.flip();
		glSamplerParameter(samplerId, parameter, buf);
	}
	
	public void setParameter(int parameter, float[] values)
	{
		FloatBuffer buf = ByteBuffer.allocateDirect(values.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buf.put(values); buf.flip();
		glSamplerParameter(samplerId, parameter, buf);
	}
	
	public void setWrapMode(WrapModes u, WrapModes v)
	{
		setParameter(GL_TEXTURE_WRAP_S, u.getGLName());
		setParameter(GL_TEXTURE_WRAP_T, v.getGLName());
	}
	
	public void setWrapMode(WrapModes u, WrapModes v, WrapModes w)
	{
		setParameter(GL_TEXTURE_WRAP_S, u.getGLName());
		setParameter(GL_TEXTURE_WRAP_T, v.getGLName());
		setParameter(GL_TEXTURE_WRAP_R, w.getGLName());
	}
	
	public void setFilterMode(FilterModes min, FilterModes mag)
	{
		setParameter(GL_TEXTURE_MIN_FILTER, min.getGLName());
		setParameter(GL_TEXTURE_MAG_FILTER, mag.getGLName());
	}
	
	public void setAnisotropyLevel(float level)
	{
		setParameter(GL_TEXTURE_MAX_ANISOTROPY_EXT, level);
	}
	
	public void bind(int textureUnit)
	{
		glBindSampler(textureUnit, samplerId);
	}
	
	public int getId()
	{
		return samplerId;
	}
	
	public void destroy()
	{
		glDeleteSamplers(samplerId);
	}
}
