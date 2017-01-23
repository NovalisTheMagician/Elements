package system.renderer;

public abstract class EnumToGLName 
{
	private int glId;
	
	protected EnumToGLName(int glId)
	{
		this.glId = glId;
	}
	
	public int getGLName()
	{
		return glId;
	}
}
