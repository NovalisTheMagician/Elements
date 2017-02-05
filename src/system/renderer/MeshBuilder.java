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
	public static Mesh createTexturedNormalCube(float halfWidth, float halfHeight, float halfDepth)
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
		mesh.setVertices(vertices, 24, VertexFormat.Common.VertexNormalTexture);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createColoredCube(float halfWidth, float halfHeight, float halfDepth, Vector3 color)
	{	
		float hw = halfWidth;
		float hh = halfHeight;
		float hd = halfDepth;
		
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// front
			new Vector3(-hw, hh, hd), color,
			new Vector3(hw, hh, hd), color,
			new Vector3(hw, -hh, hd), color,
			new Vector3(-hw, -hh, hd), color,
			
			// back
			new Vector3(hw, hh, -hd), color,
			new Vector3(-hw, hh, -hd), color,
			new Vector3(-hw, -hh, -hd), color,
			new Vector3(hw, -hh, -hd), color,
			
			// left
			new Vector3(-hw, hh, -hd), color,
			new Vector3(-hw, hh, hd), color,
			new Vector3(-hw, -hh, hd), color,
			new Vector3(-hw, -hh, -hd), color,
			
			// right
			new Vector3(hw, hh, hd), color,
			new Vector3(hw, hh, -hd), color,
			new Vector3(hw, -hh, -hd), color,
			new Vector3(hw, -hh, hd), color,
			
			// top
			new Vector3(-hw, hh, -hd), color,
			new Vector3(hw, hh, -hd), color,
			new Vector3(hw, hh, hd), color,
			new Vector3(-hw, hh, hd), color,
			
			// bottom
			new Vector3(-hw, -hh, hd), color,
			new Vector3(hw, -hh, hd), color,
			new Vector3(hw, -hh, -hd), color,
			new Vector3(-hw, -hh, -hd), color
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
		
		FloatBuffer vertices = ByteBuffer.allocateDirect(24 * 6 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 24, VertexFormat.Common.VertexColor);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createTexturedNormalPyramid(float halfWidth, float halfDepth, float height)
	{
		float hw = halfWidth;
		float hd = halfDepth;
		
		float h = height;
		
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// ground plane
			new Vector3(-hw, 0, hd), new Vector3(0, -1, 0), new Vector2(0, 0),
			new Vector3(hw, 0, hd), new Vector3(0, -1, 0), new Vector2(1, 0),
			new Vector3(hw, 0, -hd), new Vector3(0, -1, 0), new Vector2(1, 1),
			new Vector3(-hw, 0, -hd), new Vector3(0, -1, 0), new Vector2(0, 1),
			
			// front triangle
			new Vector3(-hw, 0, hd), new Vector3(0, 0, 1), new Vector2(0, 0),
			new Vector3(hw, 0, hd), new Vector3(0, 0, 1), new Vector2(1, 0),
			new Vector3(0, h, 0), new Vector3(0, 0, 1), new Vector2(0.5f, 0.5f),
			
			// right triangle
			new Vector3(hw, 0, hd), new Vector3(1, 0, 0), new Vector2(0, 0),
			new Vector3(hw, 0, -hd), new Vector3(1, 0, 0), new Vector2(1, 0),
			new Vector3(0, h, 0), new Vector3(1, 0, 0), new Vector2(0.5f, 0.5f),
			
			// back triangle
			new Vector3(hw, 0, -hd), new Vector3(0, 0, -1), new Vector2(0, 0),
			new Vector3(-hw, 0, -hd), new Vector3(0, 0, -1), new Vector2(1, 0),
			new Vector3(0, h, 0), new Vector3(0, 0, -1), new Vector2(0.5f, 0.5f),
			
			// left triangle
			new Vector3(-hw, 0, -hd), new Vector3(-1, 0, 0), new Vector2(0, 0),
			new Vector3(-hw, 0, hd), new Vector3(-1, 0, 0), new Vector2(1, 0),
			new Vector3(0, h, 0), new Vector3(-1, 0, 0), new Vector2(0.5f, 0.5f)
		};
		
		short[] i = new short[]
		{
			// ground
			0, 2, 1,
			2, 0, 3,
			
			// front
			4, 5, 6,
			
			// right
			7, 8, 9,
			
			// back
			10, 11, 12,
			
			// left
			13, 14, 15,
		};
				
		FloatBuffer vertices = ByteBuffer.allocateDirect(16 * 8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 16, VertexFormat.Common.VertexNormalTexture);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createTexturedNormalPlane(float halfWidth, float halfDepth)
	{
		float hw = halfWidth;
		float hd = halfDepth;
		
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// ground plane
			new Vector3(-hw, 0, -hd), new Vector3(0, 1, 0), new Vector2(0, 0),
			new Vector3(hw, 0, -hd), new Vector3(0, 1, 0), new Vector2(1, 0),
			new Vector3(hw, 0, hd), new Vector3(0, 1, 0), new Vector2(1, 1),
			new Vector3(-hw, 0, hd), new Vector3(0, 1, 0), new Vector2(0, 1)
		};
		
		short[] i = new short[]
		{
			// ground
			0, 2, 1,
			2, 0, 3,
		};
				
		FloatBuffer vertices = ByteBuffer.allocateDirect(4 * 8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 4, VertexFormat.Common.VertexNormalTexture);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createTexturedPlane(float halfWidth, float halfDepth)
	{
		float hw = halfWidth;
		float hd = halfDepth;
		
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// ground plane
			new Vector3(-hw, 0, -hd), new Vector2(0, 0),
			new Vector3(hw, 0, -hd), new Vector2(1, 0),
			new Vector3(hw, 0, hd), new Vector2(1, 1),
			new Vector3(-hw, 0, hd), new Vector2(0, 1)
		};
		
		short[] i = new short[]
		{
			// ground
			0, 2, 1,
			2, 0, 3,
		};
				
		FloatBuffer vertices = ByteBuffer.allocateDirect(4 * 5 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 4, VertexFormat.Common.VertexTexture);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createScreenquad()
	{
		OGLInterOp[] v = new OGLInterOp[] 
		{
			// ground plane
			new Vector3(-1, -1, 0), new Vector2(0, 0),
			new Vector3(1, -1, 0), new Vector2(1, 0),
			new Vector3(1, 1, 0), new Vector2(1, 1),
			new Vector3(-1, 1, 0), new Vector2(0, 1)
		};
		
		short[] i = new short[]
		{
			// ground
			0, 2, 1,
			2, 0, 3,
		};
				
		FloatBuffer vertices = ByteBuffer.allocateDirect(4 * 5 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(2 * i.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		for(OGLInterOp vert : v)
			vert.putBuffer(vertices);
		vertices.flip();
		
		for(short ind : i)
			indices.put(ind);
		indices.flip();
		
		Mesh mesh = new Mesh();
		mesh.setVertices(vertices, 4, VertexFormat.Common.VertexTexture);
		mesh.setIndices(indices, i.length);
		
		return mesh;
	}
	
	public static Mesh createColoredSphere(float radius, Vector3 color)
	{
		//TODO finish the darn function
		
		int slices = 20;
		int triCount = slices*slices*2;
		
		OGLInterOp[] v = new OGLInterOp[slices*slices*2];
		
		for(int j = 0; j < slices; ++j)
		{
			float theta = (float)((Math.PI*j)/slices);
			
		}
		
		Mesh mesh = new Mesh();
		
		return mesh;
		
	}
}
