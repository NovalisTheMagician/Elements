package system.loader;

@SuppressWarnings("serial")
public class AssetException extends RuntimeException 
{
	public AssetException() {}
	public AssetException(String message)
	{
		super(message);
	}
}
