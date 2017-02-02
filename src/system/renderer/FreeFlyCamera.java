package system.renderer;

import math.Matrix4;
import math.Vector3;

public class FreeFlyCamera extends Camera
{
	private Vector3 xAxis, yAxis, zAxis;
	private float forwardSpeed, upwardSpeed, sidewaysSpeed;
	private float yawRotation, pitchRotation, curYaw, curPitch;
	
	public FreeFlyCamera(float fov, float aspect)
	{
		super();
		
		xAxis = new Vector3(1, 0, 0);
		yAxis = new Vector3(0, 1, 0);
		zAxis = new Vector3(0, 0, -1);
		
		Matrix4.makePerspective(fov, aspect, 0.1f, 1000.0f, projection);
		Matrix4.setBasis(xAxis, yAxis, zAxis, view);
	}
	
	public void setPosition(Vector3 position)
	{
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	public void moveForward(float speed)
	{
		forwardSpeed = speed;
	}
	
	public void moveSideways(float speed)
	{
		sidewaysSpeed = speed;
	}
	
	public void moveUpward(float speed)
	{
		upwardSpeed = speed;
	}
	
	public void rotateYaw(float speed)
	{
		yawRotation = speed;
	}
	
	public void rotatePitch(float speed)
	{
		pitchRotation = speed;
	}
	
	public void update(float delta)
	{
		curYaw += yawRotation * delta;
		while(curYaw >= Math.PI*2)
			curYaw -= Math.PI*2;
		while(curYaw < 0)
			curYaw += Math.PI*2;
		
		curPitch += pitchRotation * delta;
		// avoid division by zero
		if(curPitch >= Math.toRadians(90))
			curPitch = (float)Math.toRadians(89.99);
		if(curPitch <= Math.toRadians(-90))
			curPitch = (float)Math.toRadians(-89.99);
		
		Matrix4 pitchRotMat = new Matrix4(1.0f);
		Matrix4.makeRotationX(curPitch, pitchRotMat);
		Matrix4.transformNormal(pitchRotMat, Vector3.Z_AXIS, zAxis);
		
		Vector3.normalize(zAxis, zAxis);
		
		Matrix4 yawRotMat = new Matrix4(1.0f);
		Matrix4.makeRotationY(curYaw, yawRotMat);
		Matrix4.transformNormal(yawRotMat, Vector3.X_AXIS, xAxis);
		Matrix4.transformNormal(yawRotMat, zAxis, zAxis);
		
		Vector3.normalize(xAxis, xAxis);
		Vector3.normalize(zAxis, zAxis);
		
		Vector3 moveXAxis = new Vector3(xAxis.x, 0, xAxis.z), moveZAxis = new Vector3(zAxis.x, 0, zAxis.z);
		Vector3.normalize(moveXAxis, moveXAxis);
		Vector3.normalize(moveZAxis, moveZAxis);
		
		Vector3.mulAdd(moveXAxis, position, sidewaysSpeed * delta, position);
		Vector3.mulAdd(moveZAxis, position, forwardSpeed * delta, position);
		Vector3.mulAdd(Vector3.Y_AXIS, position, upwardSpeed * delta, position);
		
		Vector3 target = new Vector3();
		Vector3.add(position, zAxis, target);
		Matrix4.makeLookAt(position, target, Vector3.Y_AXIS, view);
		
		forwardSpeed = 0;
		sidewaysSpeed = 0;
		upwardSpeed = 0;
		yawRotation = 0;
		pitchRotation = 0;
	}
}
