package system.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import math.OGLInterOp;
import math.Vector2;
import math.Vector3;

public class MeshBuilder 
{
	public static Mesh createCube(float halfWidth, float halfHeight, float halfDepth)
	{	
		float hw = halfWidth;
		float hh = halfHeight;
		float hd = halfDepth;
		
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// front
			new Vector3(-hw, hh, hd), new Vector3(0, 0, 1), new Vector2(0, 0),
			new Vector3(hw, hh, hd), new Vector3(0, 0, 1), new Vector2(1, 0),
			new Vector3(hw, -hh, hd), new Vector3(0, 0, 1), new Vector2(1, 1),
			new Vector3(-hw, -hh, hd), new Vector3(0, 0, 1), new Vector2(0, 1),
			
			// back
			new Vector3(hw, hh, -hd), new Vector3(0, 0, -1), new Vector2(0, 0),
			new Vector3(-hw, hh, -hd), new Vector3(0, 0, -1), new Vector2(1, 0),
			new Vector3(-hw, -hh, -hd), new Vector3(0, 0, -1), new Vector2(1, 1),
			new Vector3(hw, -hh, -hd), new Vector3(0, 0, -1), new Vector2(0, 1),
			
			// left
			new Vector3(-hw, hh, -hd), new Vector3(-1, 0, 0), new Vector2(0, 0),
			new Vector3(-hw, hh, hd), new Vector3(-1, 0, 0), new Vector2(1, 0),
			new Vector3(-hw, -hh, hd), new Vector3(-1, 0, 0), new Vector2(1, 1),
			new Vector3(-hw, -hh, -hd), new Vector3(-1, 0, 0), new Vector2(0, 1),
			
			// right
			new Vector3(hw, hh, hd), new Vector3(1, 0, 0), new Vector2(0, 0),
			new Vector3(hw, hh, -hd), new Vector3(1, 0, 0), new Vector2(1, 0),
			new Vector3(hw, -hh, -hd), new Vector3(1, 0, 0), new Vector2(1, 1),
			new Vector3(hw, -hh, hd), new Vector3(1, 0, 0), new Vector2(0, 1),
			
			// top
			new Vector3(-hw, hh, -hd), new Vector3(0, 1, 0), new Vector2(0, 0),
			new Vector3(hw, hh, -hd), new Vector3(0, 1, 0), new Vector2(1, 0),
			new Vector3(hw, hh, hd), new Vector3(0, 1, 0), new Vector2(1, 1),
			new Vector3(-hw, hh, hd), new Vector3(0, 1, 0), new Vector2(0, 1),
			
			// bottom
			new Vector3(-hw, -hh, hd), new Vector3(0, -1, 0), new Vector2(0, 0),
			new Vector3(hw, -hh, hd), new Vector3(0, -1, 0), new Vector2(1, 0),
			new Vector3(hw, -hh, -hd), new Vector3(0, -1, 0), new Vector2(1, 1),
			new Vector3(-hw, -hh, -hd), new Vector3(0, -1, 0), new Vector2(0, 1)
		};
		
		short[] i = new short[]
		{
			// front
			0, 2, 1,
			2, 0, 3,
			
			// back
			4, 6, 5,
			6, 4, 7,
			
			// left
			8, 10, 9,
			10, 8, 11,
			
			// right
			12, 14, 13,
			14, 12, 15,
			
			// top
			16, 18, 17,
			18, 16, 19,
			
			// bottom
			20, 22, 21,
			22, 20, 23
		};
		
		FloatBuffer vertices = ByteBuffer.allocateDirect(24 * 8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 24);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createCircle(float radiusX, float radiusY)
	{
		int numVertices = 17;
		float theta = 0;
		float step = ((float)Math.PI * 2) / (numVertices - 1);
		
		OGLInterOp vertices[] = new OGLInterOp[numVertices * 3];
		vertices[0] = new Vector3(0); vertices[1] = new Vector3(0, 0, 1); vertices[2] = new Vector2(0.5f, 0.5f);
		for(int i = 1; i < numVertices; ++i)
		{
			float x = (float)Math.cos(theta) * radiusX;
			float y = (float)Math.sin(theta) * radiusY;
			
			float u = (float)Math.cos(theta) * 0.5f + 0.5f;
			float v = (float)Math.sin(theta) * 0.5f + 0.5f;
			
			int index = i * 3;
			vertices[index + 0] = new Vector3(x, y, 0);
			vertices[index + 1] = new Vector3(0, 0, 1);
			vertices[index + 2] = new Vector2(u, v);
			
			theta += step;
		}
		
		short indices[] = new short[numVertices * 3];
		int j = 0;
		for(int i = 1; i < indices.length; i++)
		{
			indices[j++] = 0;
			indices[j++] = (short)i;
			indices[j++] = (short)(i+1);
		}
		
		FloatBuffer vertBuf = ByteBuffer.allocateDirect(32 * numVertices).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indBuf = ByteBuffer.allocateDirect(2 * indices.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : vertices)
			vert.putBuffer(vertBuf);
		vertBuf.flip();
		
		for(short ind : indices)
			indBuf.put(ind);
		indBuf.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertBuf, numVertices); // TODO dragons
		mesh.setIndices(indBuf, indices.length);
		
		return mesh;
	}
}
