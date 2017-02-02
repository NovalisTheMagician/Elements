package system.renderer;

import math.Matrix4;
import math.Vector3;

public abstract class Camera 
{
	protected Matrix4 view, projection;
	protected Vector3 position;
	
	public Camera()
	{
		view = new Matrix4(1.0f);
		projection = new Matrix4(1.0f);
		position = new Vector3();
	}
	
	public final Vector3 getPosition()
	{
		return position;
	}
	
	public final Matrix4 getView()
	{
		return view;
	}
	
	public final Matrix4 getProjection()
	{
		return projection;
	}
	
	public final Matrix4 getViewProj()
	{
		Matrix4 res = new Matrix4();
		Matrix4.mul(projection, view, res);
		return res;
	}
}
