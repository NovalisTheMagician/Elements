package system.renderer.shader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class Shader 
{
	public static enum ShaderType
	{
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER),
		UNKNOWN(-1);
		
		private int glId;
		private ShaderType(int glId)
		{
			this.glId = glId;
		}
		
		public int getGLName()
		{
			return glId;
		}
	};
	
	static enum State 
	{
		COMPILED,
		ERROR,
		READY,
		NO_SOURCE_ADDED
	};
	
	private int shaderId;
	private ShaderType shaderType;
	
	private State state;
	
	private String errorMessage;
	
	public Shader()
	{
		shaderId = 0;
		shaderType = ShaderType.UNKNOWN;
		errorMessage = null;
		
		state = State.NO_SOURCE_ADDED;
	}
	
	public void setShaderSource(String source, ShaderType type)
	{
		if(shaderId == 0)
			shaderId = glCreateShader(type.getGLName());
		
		shaderType = type;
		glShaderSource(shaderId, source);
		
		state = State.READY;
	}
	
	public void compile() throws ShaderCompileException
	{
		if(shaderType == ShaderType.GEOMETRY)
			throw new ShaderCompileException("Geometryshader not yet supported");
		
		if(shaderId == 0 || (shaderType != ShaderType.VERTEX && shaderType != ShaderType.FRAGMENT))
		{
			state = State.ERROR;
			throw new ShaderCompileException("No source added or invalid shader type");
		}
		
		glCompileShader(shaderId);
		boolean status = glGetShaderi(shaderId, GL_COMPILE_STATUS) != 0;
		if(!status)
		{
			state = State.ERROR;
			errorMessage = glGetShaderInfoLog(shaderId, 255);
			String type = shaderType == ShaderType.VERTEX ? "Vertexshader" : "Fragmentsshader";
			throw new ShaderCompileException(type + " failed to compile");
		}
		
		state = State.COMPILED;
	}
	
	public int getId()
	{
		return shaderId;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public State getState()
	{
		return state;
	}
	
	public void destroy()
	{
		glDeleteShader(shaderId);
		shaderId = 0;
		state = State.NO_SOURCE_ADDED;
	}
}
