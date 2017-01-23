package system.renderer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh 
{
	private int vao, vbo, ibo;
	private int numVertices, numIndices;
	
	public Mesh()
	{
		vao = glGenVertexArrays();
	}
	
	public void setVertices(FloatBuffer vertices, int count, VertexFormat[] format)
	{
		glBindVertexArray(vao);
		
		if(vbo != 0)
			glDeleteBuffers(vbo);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		for(VertexFormat vf : format)
		{
			glEnableVertexAttribArray(vf.index);
			glVertexAttribPointer(vf.index, vf.numComponents, vf.type.getGLName(), false, vf.stride, vf.offset);
		}

		numVertices = count;
	}
	
	public void setIndices(ShortBuffer indices, int count)
	{
		glBindVertexArray(vao);
		
		if(ibo != 0)
			glDeleteBuffers(ibo);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		numIndices = count;
	}
	
	public void draw()
	{
		glBindVertexArray(vao);
		if(ibo != 0)
			glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_SHORT, 0);
		else
			glDrawArrays(GL_TRIANGLE_FAN, numVertices, 0);
	}
	
	public void destroy()
	{
		glDeleteVertexArrays(vao); 	vao = 0;
		glDeleteBuffers(vbo); 		vbo = 0;
		glDeleteBuffers(ibo); 		ibo = 0;
	}
}
