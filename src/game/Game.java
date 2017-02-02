package game;

import system.Config;
import system.GameBase;
import system.input.InputProcessor;
import static system.input.InputProcessor.*;
import system.loader.AssetManager;
import static system.loader.AssetManager.AssetType.*;
import system.loader.ShaderLoader;
import system.loader.TextureLoader;
import system.renderer.Mesh;
import system.renderer.MeshBuilder;
import system.renderer.Sampler;
import system.renderer.Sampler.FilterModes;
import system.renderer.Sampler.WrapModes;
import system.renderer.Texture;
import system.renderer.shader.Program;
import system.renderer.shader.Uniform;

import static org.lwjgl.opengl.GL11.*;

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
	
	AssetManager assetManager;
	Config config;
	
	InputProcessor inputProcessor;
	Input quitInput;
	Input rotateLeftInput;
	Input rotateRightInput;
	Input rotateUpInput;
	Input rotateDownInput;
	
	Input rotateOnceRight;
	
	@Override
	protected void init() 
	{
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		
		glClearColor(0, 0, 0, 1);
		
		config = new Config();
		
		assetManager = new AssetManager(config.getRootPath());
		TextureLoader texLoader = new TextureLoader();
		assetManager.registerLoader(TEXTURE, texLoader, config.getTextureFolder(), texLoader);
		ShaderLoader shdLoader = new ShaderLoader();
		assetManager.registerLoader(SHADER, shdLoader, config.getShaderFolder(), shdLoader);
		
		assetManager.loadAssets();
		
		diffMap = assetManager.get(TEXTURE, "container2");
		specMap = assetManager.get(TEXTURE, "container2_specular");
		
		staticBlinnPhong = assetManager.get(SHADER, "color.static");
		
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
		
		sampler = new Sampler();
		sampler.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampler.setFilterMode(FilterModes.LINEAR_MIPMAP_LINEAR, FilterModes.LINEAR);
		sampler.setAnisotropyLevel(16.0f);
		
		angleY = 0;
		angleZ = 0;
		
		cube = MeshBuilder.createTexturedCube(0.5f, 0.5f, 0.5f);
		
		lightPos = new Vector3(1, 0.5f, 1);
		lightDiffuse = new Vector3(1);
		lightAmbient = new Vector3(0.4f);
		lightSpecular = new Vector3(0.8f);
		cameraPos = new Vector3(0, 0, 4);
		cameraDir = new Vector3(0, 0, -1);
		roughness = 32f;
		
		cubePosition = new Vector3(0);
		
		inputProcessor = new InputProcessor();
		
		quitInput = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_ESCAPE; 
									state = InputState.PRESSED; action = InputAction.CONTINUOUS; }};
		
		rotateLeftInput = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_LEFT; 
									state = InputState.PRESSED; action = InputAction.CONTINUOUS; }};
									
		rotateRightInput = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_RIGHT; 
									state = InputState.PRESSED; action = InputAction.CONTINUOUS; }};
									
		rotateUpInput = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_UP; 
									state = InputState.PRESSED; action = InputAction.CONTINUOUS; }};
									
		rotateDownInput = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_DOWN; 
									state = InputState.PRESSED; action = InputAction.CONTINUOUS; }};
									
		rotateOnceRight = new Input() {{ 	type = InputType.KEYBOARD; key = Keyboard.KEY_J; 
									state = InputState.PRESSED; action = InputAction.ONCE; }};
	}

	@Override
	protected void update(float delta) 
	{
		inputProcessor.update();
		
		if(inputProcessor.isInput(quitInput))
			stop();
		
		if(inputProcessor.isInput(rotateUpInput))
			angleZ -= 1f * delta;
		
		if(inputProcessor.isInput(rotateDownInput))
			angleZ += 1f * delta;
		
		if(inputProcessor.isInput(rotateLeftInput))
			angleY += 1f * delta;
		
		if(inputProcessor.isInput(rotateRightInput))
			angleY -= 1f * delta;
		
		if(inputProcessor.isInput(rotateOnceRight))
			angleY -= 5f * delta;
		
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
		
		Vector3 target = new Vector3();
		Vector3.add(cameraPos, cameraDir, target);
		
		Matrix4.makeLookAt(cameraPos, target, Vector3.Y_AXIS, view);
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
	
	public static void main(String... argv)
	{
		new Game().start("Elements", 800, 600, false);
	}	
}
