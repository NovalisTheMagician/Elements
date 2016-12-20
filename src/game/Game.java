package game;

import system.GameBase;
import system.renderer.Mesh;
import system.renderer.MeshBuilder;
import system.renderer.Sampler;
import system.renderer.Sampler.FilterModes;
import system.renderer.Sampler.WrapModes;
import system.renderer.Texture;
import system.renderer.shader.Program;
import system.renderer.shader.ProgramLinkException;
import system.renderer.shader.Shader;
import system.renderer.shader.ShaderCompileException;
import system.renderer.shader.Uniform;
import system.renderer.shader.Shader.ShaderType;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import math.Matrix4;
import math.Vector3;

public class Game extends GameBase
{
	Uniform<Matrix4> worldUni, viewUni, projUni;
	Uniform<Integer> diffMapUni, specMapUni;
	Uniform<Float> roughnessUni;
	Uniform<Vector3> lightPosUni, lightDiffuseUni, viewPosUni, 
					 lightAmbientUni, lightSpecularUni;
	
	float angleY;
	float angleZ;
	
	Vector3 cubePosition;
	
	Vector3 lightPos, lightDiffuse, lightAmbient, lightSpecular, 
			cameraPos, cameraDir;
	Texture diffMap, specMap;
	Sampler sampler;
	float roughness;
	
	Program staticBlinnPhong;
	Mesh cube;
	
	@Override
	protected void init() 
	{
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glClearColor(0, 0, 0, 1);
		
		staticBlinnPhong = loadProgram("assets/shader/color.vs", "assets/shader/color.fs");
		
		worldUni = staticBlinnPhong.getUniformMatrix4("world");
		viewUni = staticBlinnPhong.getUniformMatrix4("view");
		projUni = staticBlinnPhong.getUniformMatrix4("proj");
		
		lightPosUni = staticBlinnPhong.getUniformVector3("light.position");
		lightDiffuseUni = staticBlinnPhong.getUniformVector3("light.diffuse");
		lightAmbientUni = staticBlinnPhong.getUniformVector3("light.ambient");
		lightSpecularUni = staticBlinnPhong.getUniformVector3("light.specular");
		
		viewPosUni = staticBlinnPhong.getUniformVector3("viewPos");
		
		diffMapUni = staticBlinnPhong.getUniformInteger("material.diffuseMap");
		specMapUni = staticBlinnPhong.getUniformInteger("material.specularMap");
		roughnessUni = staticBlinnPhong.getUniformFloat("material.roughness");
		
		diffMap = loadTexture("assets/textures/container2.png");
		specMap = loadTexture("assets/textures/container2_specular.png");
		
		sampler = new Sampler();
		sampler.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampler.setFilterMode(FilterModes.LINEAR_MIPMAP_LINEAR, FilterModes.LINEAR);
		sampler.setAnisotropyLevel(16.0f);
		
		angleY = 0;
		angleZ = 0;
		
		cube = MeshBuilder.createCube(0.5f, 0.5f, 0.5f);
		
		lightPos = new Vector3(1, 0.5f, 1);
		lightDiffuse = new Vector3(1);
		lightAmbient = new Vector3(0.4f);
		lightSpecular = new Vector3(0.8f);
		cameraPos = new Vector3(0, 0, 4);
		cameraDir = new Vector3(0, 0, -1);
		roughness = 32f;
		
		cubePosition = new Vector3(0);
	}

	@Override
	protected void update(float delta) 
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			stop();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			angleZ -= 1f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			angleZ += 1f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			angleY += 1f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			angleY -= 1f * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			cubePosition.z -= 5 * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			cubePosition.z += 5 * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			cubePosition.x -= 5 * delta;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			cubePosition.x += 5 * delta;
		
		if(angleY >= Math.PI*2)
			angleY -= Math.PI*2;
	}

	@Override
	protected void draw() 
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Matrix4 world = new Matrix4(1.0f);
		Matrix4 view = new Matrix4(1.0f);
		Matrix4 proj = new Matrix4(1.0f);
		
		Matrix4 trans = new Matrix4(1.0f);
		Matrix4 rot = new Matrix4(1.0f);
		Matrix4 rotY = new Matrix4(1.0f);
		Matrix4 rotZ = new Matrix4(1.0f);
		
		Matrix4.makeTranslation(cubePosition, trans);
		Matrix4.makeRotationY(angleY, rotY);
		Matrix4.makeRotationX(angleZ, rotZ);
		
		Matrix4.mul(rotY, rotZ, rot);
		Matrix4.mul(trans, rot, world);
		Matrix4.makeLookAt(cameraPos, cameraDir, new Vector3(0, 1, 0), view);
		Matrix4.makePerspective((float)Math.toRadians(45), getAspectRatio(), 0.1f, 100, proj);
		
		staticBlinnPhong.use();
		
		diffMap.bind(0);
		sampler.bind(0);
		diffMapUni.set(0);
		
		specMap.bind(1);
		sampler.bind(1);
		specMapUni.set(1);
		
		worldUni.set(world);
		viewUni.set(view);
		projUni.set(proj);
		
		roughnessUni.set(roughness);
		
		lightPosUni.set(lightPos);
		lightDiffuseUni.set(lightDiffuse);
		lightAmbientUni.set(lightAmbient);
		lightSpecularUni.set(lightSpecular);
		
		viewPosUni.set(cameraPos);
		
		cube.draw();
	}
	
	@Override
	protected void destroy()
	{
		cube.destroy();
		staticBlinnPhong.destroy();
		diffMap.destroy();
		specMap.destroy();
		sampler.destroy();
	}
	
	public String readTextFile(String filename)
	{
		StringBuilder source = new StringBuilder();
	     
	    try(BufferedReader reader = new BufferedReader(new FileReader(filename));)
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
	        System.err.println("Could not read file: " + filename);
	        e.printStackTrace();
	        return "";
	    }
	    
	    return source.toString();
	}
	
	public Texture loadTexture(String filename)
	{
		ByteBuffer texels = null;
		int width = 0;
		int height = 0;
		
		try 
		{
			BufferedImage img = ImageIO.read(new File(filename));
			width = img.getWidth();
			height = img.getHeight();
			int size = width * height;
			if(img.getType() != BufferedImage.TYPE_INT_ARGB)
			{
				BufferedImage newFormat = new BufferedImage(img.getWidth(), img.getHeight(), 
															BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = newFormat.createGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				img = newFormat;
			}
			
			int byteSize = size * 4;
			texels = ByteBuffer.allocateDirect(byteSize).order(ByteOrder.nativeOrder());
			for(int y = 0; y < img.getHeight(); ++y)
			{
				for(int x = 0; x < img.getWidth(); ++x)
				{
					int texel = img.getRGB(x, y);
					byte a = (byte)((texel >> 24) & 255);
					byte r = (byte)((texel >> 16) & 255);
					byte g = (byte)((texel >> 8) & 255);
					byte b = (byte)(texel & 255);
					
					texels.put(r);
					texels.put(g);
					texels.put(b);
					texels.put(a);
				}
			}
			
			texels.flip();
		} 
		catch (IOException e) 
		{
			System.err.println("Could not read texture file.");
			e.printStackTrace();
			return null;
		}
		
		Texture texture = new Texture();
		
		texture.setData(texels, GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE, width, height);
		
		return texture;
	}
	
	public Program loadProgram(String vertexSourcePath, String fragmentSourcePath)
	{
		String vertexSource = readTextFile(vertexSourcePath);
		String fragmentSource = readTextFile(fragmentSourcePath);
		
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
		}
		
		vertexShader.destroy();
		fragmentShader.destroy();
		
		return program;
	}
	
	public static void main(String... argv)
	{
		new Game().start("Elements", 800, 600, false);
	}	
}
