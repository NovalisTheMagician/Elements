package system.renderer.shader;

import static org.lwjgl.opengl.GL20.*;

import math.Matrix4;
import math.Vector2;
import math.Vector3;

public class Program 
{
	private static final int INVALID_LOCATION = -1;
	
	static enum State
	{
		LINKED,
		READY,
		ERROR,
		NOT_READY
	};
	
	private int programId;
	private State state;
	private String errorMessage;
	
	public Program()
	{
		programId = glCreateProgram();
		
		state = State.NOT_READY;
	}
	
	public void attachShader(Shader shader) throws ProgramLinkException
	{
		if(shader.getState() != Shader.State.COMPILED)
			throw new ProgramLinkException("Could not attach shader: Shader not compiled");
		
		glAttachShader(programId, shader.getId());
	}
	
	public void link() throws ProgramLinkException
	{
		if(programId == 0)
		{
			state = State.ERROR;
			throw new ProgramLinkException("No shader attached to this program");
		}
		
		glLinkProgram(programId);
		glValidateProgram(programId);
		
		boolean programState = glGetProgrami(programId, GL_LINK_STATUS) != 0;
		if(!programState)
		{
			errorMessage = glGetProgramInfoLog(programId, 255);
			state = State.ERROR;
			throw new ProgramLinkException("Program failed to link");
		}
		
		state = State.LINKED;
	}
	
	public Uniform<Matrix4> getUniformMatrix4(String location)
	{
		int loc = glGetUniformLocation(programId, location);
		if(loc == INVALID_LOCATION)
			throw new UniformNotFoundException(location);
		
		return new Uniform<Matrix4>() 
			{
				int location = loc; 
				@Override
				public void set(Matrix4 data) 
				{
					glUniformMatrix4(location, false, data.makeBuffer());
				} 
			};
	}
	
	public Uniform<Vector3> getUniformVector3(String location)
	{
		int loc = glGetUniformLocation(programId, location);
		if(loc == INVALID_LOCATION)
			throw new UniformNotFoundException(location);
		
		return new Uniform<Vector3>() 
			{
				int location = loc;
				@Override
				public void set(Vector3 data) 
				{
					glUniform3(location, data.makeBuffer());
				} 
			};
	}
	
	public Uniform<Vector2> getUniformVector2(String location)
	{
		int loc = glGetUniformLocation(programId, location);
		if(loc == INVALID_LOCATION)
			throw new UniformNotFoundException(location);
		
		return new Uniform<Vector2>() 
			{
				int location = loc; 
				@Override
				public void set(Vector2 data) 
				{
					glUniform2(location, data.makeBuffer());
				} 
			};
	}
	
	public Uniform<Float> getUniformFloat(String location)
	{
		int loc = glGetUniformLocation(programId, location);
		if(loc == INVALID_LOCATION)
			throw new UniformNotFoundException(location);
		
		return new Uniform<Float>() 
			{
				int location = loc; 
				@Override
				public void set(Float data) 
				{
					glUniform1f(location, data);
				} 
			};
	}
	
	public Uniform<Integer> getUniformInteger(String location)
	{
		int loc = glGetUniformLocation(programId, location);
		if(loc == INVALID_LOCATION)
			throw new UniformNotFoundException(location);
		
		return new Uniform<Integer>() 
			{
				int location = loc; 
				@Override
				public void set(Integer data) 
				{
					glUniform1i(location, data);
				} 
			};
	}
	
	public void use()
	{
		glUseProgram(programId);
	}
	
	public int getProgramId()
	{
		return programId;
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
		glDeleteProgram(programId);
		programId = 0;
		state = State.NOT_READY;
	}
}
