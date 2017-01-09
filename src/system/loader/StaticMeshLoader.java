package system.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;

import system.renderer.Mesh;

public class StaticMeshLoader implements Loader<Mesh>, FileFilter
{
	@Override
	public Mesh load(InputStream in, String workingDir) 
	{
		return null;
	}
	
	public boolean accept(File file) 
	{ 
		return file.isFile() && file.getName().toLowerCase().endsWith(".mdl"); 
	}
}
