package system.loader;

import java.io.InputStream;

import system.renderer.Mesh;

public class StaticMeshLoader implements Loader<Mesh>
{
	@Override
	public Mesh Load(InputStream in, String workingDir) 
	{
		return null;
	}
}
