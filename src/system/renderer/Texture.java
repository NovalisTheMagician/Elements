package system.renderer;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture 
{
	private int textureId;
	
	public Texture()
	{
		textureId = glGenTextures();
	}
	
	public void setData(ByteBuffer texels, int internalFormat, int pixelFormat, int pixelType, int width, int height)
	{	
		glBindTexture(GL_TEXTURE_2D, textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, pixelFormat, pixelType, texels);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public void bind(int activeTexture)
	{
		glActiveTexture(GL_TEXTURE0 + activeTexture);
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public int getId()
	{
		return textureId;
	}
	
	public void destroy()
	{
		glDeleteTextures(textureId);
	}
}
