package math;

import java.nio.FloatBuffer;

public interface OGLInterOp 
{
	public FloatBuffer makeBuffer();
	public void putBuffer(FloatBuffer buf);
}
