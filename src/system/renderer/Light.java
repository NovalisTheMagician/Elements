package system.renderer;

import math.Vector3;

public class Light 
{
	public Vector3 position, diffuse, ambient, specular;
	
	public Light(Vector3 position)
	{
		this(position, new Vector3(1), new Vector3(0.5f), new Vector3(0.75f));
	}
	
	public Light(Vector3 position, Vector3 diffuse, Vector3 ambient, Vector3 specular)
	{
		this.position = new Vector3(position);
		this.diffuse = new Vector3(diffuse);
		this.ambient = new Vector3(ambient);
		this.specular = new Vector3(specular);
	}
}
