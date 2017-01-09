package system.loader;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import system.renderer.Texture;

public class TextureLoader implements Loader<Texture> 
{
	@Override
	public Texture load(InputStream in, String workingDir) 
	{
		ByteBuffer texels = null;
		int width = 0;
		int height = 0;
		
		try 
		{
			BufferedImage img = ImageIO.read(in);
			width = img.getWidth();
			height = img.getHeight();
			int size = width * height;
			if(img.getType() != BufferedImage.TYPE_INT_ARGB)
			{
				BufferedImage newFormat = new BufferedImage(img.getWidth(), img.getHeight(), 
															BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = newFormat.createGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				img = newFormat;
			}
			
			int byteSize = size * 4;
			texels = ByteBuffer.allocateDirect(byteSize).order(ByteOrder.nativeOrder());
			for(int y = 0; y < img.getHeight(); ++y)
			{
				for(int x = 0; x < img.getWidth(); ++x)
				{
					int texel = img.getRGB(x, y);
					byte a = (byte)((texel >> 24) & 255);
					byte r = (byte)((texel >> 16) & 255);
					byte g = (byte)((texel >> 8) & 255);
					byte b = (byte)(texel & 255);
					
					texels.put(r);
					texels.put(g);
					texels.put(b);
					texels.put(a);
				}
			}
			
			texels.flip();
		} 
		catch(IOException e) 
		{
			System.err.println("Could not read texture file.");
			e.printStackTrace();
			return null;
		}
		
		Texture texture = new Texture();
		texture.setData(texels, GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE, width, height);
		
		return texture;
	}
}
