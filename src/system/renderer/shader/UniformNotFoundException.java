package system.renderer.shader;

@SuppressWarnings("serial")
public class UniformNotFoundException extends RuntimeException
{
	public UniformNotFoundException() {}
	public UniformNotFoundException(String message)
	{
		super(message);
	}
}
