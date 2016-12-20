package system.renderer.shader;

@SuppressWarnings("serial")
public class ShaderCompileException extends Exception 
{
	public ShaderCompileException() {}
	public ShaderCompileException(String message)
	{
		super(message);
	}
}
