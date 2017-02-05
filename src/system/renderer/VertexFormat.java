package system.renderer;

import static org.lwjgl.opengl.GL11.*;

import math.Vector2;
import math.Vector3;

public class VertexFormat 
{
	public static class Common
	{
		public static final VertexFormat[] VertexNormalTexture = 
			{
					new VertexFormat() {{ index = 0; numComponents = 3; type = VertexType.FLOAT; stride = 32; offset = 0; }},
					new VertexFormat() {{ index = 1; numComponents = 3; type = VertexType.FLOAT; stride = 32; offset = Vector3.BYTE_SIZE; }},
					new VertexFormat() {{ index = 2; numComponents = 2; type = VertexType.FLOAT; stride = 32; offset = Vector3.BYTE_SIZE * 2; }}
			};
		
		public static final VertexFormat[] VertexNormalTextureTangent = 
			{
					new VertexFormat() {{ index = 0; numComponents = 3; type = VertexType.FLOAT; stride = 56; offset = 0; }},
					new VertexFormat() {{ index = 1; numComponents = 3; type = VertexType.FLOAT; stride = 56; offset = Vector3.BYTE_SIZE; }},
					new VertexFormat() {{ index = 2; numComponents = 2; type = VertexType.FLOAT; stride = 56; offset = Vector3.BYTE_SIZE * 2; }},
					new VertexFormat() {{ index = 3; numComponents = 3; type = VertexType.FLOAT; stride = 56; offset = Vector3.BYTE_SIZE * 2 + Vector2.BYTE_SIZE; }}
			};
		
		public static final VertexFormat[] VertexTexture = 
			{
					new VertexFormat() {{ index = 0; numComponents = 3; type = VertexType.FLOAT; stride = 20; offset = 0; }},
					new VertexFormat() {{ index = 1; numComponents = 2; type = VertexType.FLOAT; stride = 20; offset = Vector3.BYTE_SIZE; }}
			};
		
		public static final VertexFormat[] VertexColor =
			{
					new VertexFormat() {{ index = 0; numComponents = 3; type = VertexType.FLOAT; stride = 24; offset = 0; }},
					new VertexFormat() {{ index = 1; numComponents = 3; type = VertexType.FLOAT; stride = 24; offset = Vector3.BYTE_SIZE; }}
			};
	}
	
	public static enum VertexType
	{
		BYTE(GL_BYTE),
		UNSIGNED_BYTE(GL_UNSIGNED_BYTE),
		SHORT(GL_SHORT),
		UNSIGNED_SHORT(GL_UNSIGNED_SHORT),
		INT(GL_INT),
		UNSIGNED_INT(GL_UNSIGNED_INT),
		FLOAT(GL_FLOAT),
		DOUBLE(GL_DOUBLE);
		
		private int glId;
		VertexType(int glId)
		{
			this.glId = glId;
		}
		
		public int getGLName()
		{
			return glId;
		}
	};
	
	public int index;
	public int numComponents;
	public VertexType type;
	public int stride;
	public int offset;
}
