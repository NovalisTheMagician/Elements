package system.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import system.renderer.shader.Program;
import system.renderer.shader.ProgramLinkException;
import system.renderer.shader.Shader;
import system.renderer.shader.ShaderCompileException;
import system.renderer.shader.Shader.ShaderType;

public class ShaderLoader implements Loader<Program>, FileFilter
{
	private class ShaderFile
	{
		public String vertex;
		public String fragment;
	}
	
	public Program loadManually(InputStream vertexShader, InputStream fragmentShader)
	{
		String vertexSource = readTextFile(vertexShader);
		String fragmentSource = readTextFile(fragmentShader);
		Program p = loadProgram(vertexSource, fragmentSource);
		return p;
	}
	
	@Override
	public Program load(InputStream in, String workingDir) 
	{	
		Gson gson = new Gson();
		ShaderFile shdFile = gson.fromJson(readTextFile(in), ShaderFile.class);
		
		String vertexSource = readTextFile(workingDir + shdFile.vertex);
		String fragmentSource = readTextFile(workingDir + shdFile.fragment);
		
		Program p = loadProgram(vertexSource, fragmentSource);
		return p;
	}
	
	private String readTextFile(String filename)
	{
		try
		{
			return readTextFile(new FileInputStream(new File(filename)));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Could not read file: " + filename);
	        e.printStackTrace();
			return "";
		}
	}
	
	private String readTextFile(InputStream in)
	{
		StringBuilder source = new StringBuilder();
	     
	    try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))
	    {
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            source.append(line).append("\n");
	        }
	        reader.close();
	    } 
	    catch (IOException e) 
	    {
	        System.err.println("Could not read file");
	        e.printStackTrace();
	        return "";
	    }
	    
	    return source.toString();
	}
	
	private Program loadProgram(String vertexSource, String fragmentSource)
	{	
		Shader vertexShader = new Shader();
		Shader fragmentShader = new Shader();
		
		vertexShader.setShaderSource(vertexSource, ShaderType.VERTEX);
		fragmentShader.setShaderSource(fragmentSource, ShaderType.FRAGMENT);
		
		try
		{
			vertexShader.compile();
			fragmentShader.compile();
		}
		catch(ShaderCompileException e)
		{
			e.printStackTrace();
			System.err.println(vertexShader.getErrorMessage());
			System.err.println(fragmentShader.getErrorMessage());
			
			return null;
		}
		
		Program program = new Program();
		try
		{
			program.attachShader(vertexShader);
			program.attachShader(fragmentShader);
			program.link();
		}
		catch(ProgramLinkException e)
		{
			e.printStackTrace();
			System.err.println(program.getErrorMessage());
			return null;
		}
		
		vertexShader.destroy();
		fragmentShader.destroy();
		
		return program;
	}

	public boolean accept(File file) 
	{ 
		return file.isFile() && file.getName().toLowerCase().endsWith(".shd"); 
	}
}
