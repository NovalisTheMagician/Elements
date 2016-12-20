package system;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class GameBase 
{
	private boolean stopRequested;
	
	private int width, height;
	
	public void start(String title, int width, int height, boolean fullscreen)
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			
			this.width = width;
			this.height = height;
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			return;
		}
		
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
		System.out.println("OpenGL Vendor: " + glGetString(GL_VENDOR));
		System.out.println("OpenGL Renderer: " + glGetString(GL_RENDERER));
		System.out.println("OpenGL GLSL: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
		
		init();
		
		stopRequested = false;
		while(!Display.isCloseRequested() && !stopRequested)
		{
			update(1f / 60f);
			draw();
			
			Display.update();
			Display.sync(60);
		}
		
		destroy();
		
		Display.destroy();
	}
	
	public final void stop()
	{
		stopRequested = true;
	}
	
	public final int getWidth()
	{
		return width;
	}
	
	public final int getHeight()
	{
		return height;
	}
	
	public final float getAspectRatio()
	{
		return (float)width / height;
	}
	
	protected abstract void init();
	protected abstract void update(float delta);
	protected abstract void draw();
	protected abstract void destroy();
}
