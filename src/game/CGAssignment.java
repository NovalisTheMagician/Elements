package game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.input.Keyboard;

import math.Matrix4;
import math.Vector3;
import system.GameBase;
import system.input.InputProcessor;
import system.input.InputProcessor.Input;
import system.input.InputProcessor.InputAction;
import system.input.InputProcessor.InputState;
import system.input.InputProcessor.InputType;
import system.loader.ShaderLoader;
import system.loader.TextureLoader;
import system.renderer.FreeFlyCamera;
import system.renderer.Mesh;
import system.renderer.MeshBuilder;
import system.renderer.Sampler;
import system.renderer.Texture;
import system.renderer.Sampler.FilterModes;
import system.renderer.Sampler.WrapModes;
import system.renderer.shader.Program;
import system.renderer.shader.Uniform;

public class CGAssignment extends GameBase
{
	private Texture woodHq, woodLq;
	private Sampler sampHq, sampLq, sampHqMip, sampLqMip;
	private Mesh cubeMesh, pyramidMesh, planeMesh;
	private Program blinnPhongShader, proceduralShader;
	
	private FreeFlyCamera camera;
	
	private Vector3 lightPos, lightDiffuse, lightAmbient, lightSpecular;
	private float roughness;
	
	private Vector3 cubePos, pyrPos, planePos, spherePos;
	
	private Uniform<Matrix4> worldUni, viewUni, projUni;
	private Uniform<Integer> diffMapUni;
	private Uniform<Float> roughnessUni;
	private Uniform<Vector3> lightPosUni, lightDiffuseUni, viewPosUni, 
					 lightAmbientUni, lightSpecularUni;
	
	private InputProcessor inputProcessor;
	
	private Input quitInput;
	private Input moveForwardInput, moveBackwardInput, moveLeftInput, 
				moveRightInput, moveUpInput, moveDownInput;
	
	@Override
	protected void init() 
	{
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.3f, 0.3f, 0.3f, 1);
		
		ClassLoader clazzLoader = getClass().getClassLoader();
		
		TextureLoader texLoader = new TextureLoader();
		woodHq = texLoader.load(clazzLoader.getResourceAsStream("assets/textures/wood_hq.jpg"), "");
		woodLq = texLoader.load(clazzLoader.getResourceAsStream("assets/textures/wood_lq.jpg"), "");
		
		sampHq = new Sampler();
		sampHq.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampHq.setFilterMode(FilterModes.LINEAR, FilterModes.LINEAR);
		
		sampLq = new Sampler();
		sampLq.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampLq.setFilterMode(FilterModes.NEAREST, FilterModes.NEAREST);
		
		sampHqMip = new Sampler();
		sampHqMip.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampHqMip.setFilterMode(FilterModes.LINEAR_MIPMAP_LINEAR, FilterModes.LINEAR);
		
		sampLqMip = new Sampler();
		sampLqMip.setWrapMode(WrapModes.REPEAT, WrapModes.REPEAT);
		sampLqMip.setFilterMode(FilterModes.NEAREST_MIPMAP_NEAREST, FilterModes.NEAREST);
		
		ShaderLoader shdLoader = new ShaderLoader();
		blinnPhongShader = shdLoader.loadManually(clazzLoader.getResourceAsStream("assets/shader/blinn_phong.vs"), 
				clazzLoader.getResourceAsStream("assets/shader/blinn_phong.fs"));
		
		worldUni = blinnPhongShader.getUniformMatrix4("world");
		viewUni = blinnPhongShader.getUniformMatrix4("view");
		projUni = blinnPhongShader.getUniformMatrix4("proj");
		
		lightPosUni = blinnPhongShader.getUniformVector3("light.position");
		lightDiffuseUni = blinnPhongShader.getUniformVector3("light.diffuse");
		lightAmbientUni = blinnPhongShader.getUniformVector3("light.ambient");
		lightSpecularUni = blinnPhongShader.getUniformVector3("light.specular");
		
		viewPosUni = blinnPhongShader.getUniformVector3("viewPos");
		
		diffMapUni = blinnPhongShader.getUniformInteger("material.diffuseMap");
		roughnessUni = blinnPhongShader.getUniformFloat("material.roughness");
		
		cubeMesh = MeshBuilder.createTexturedCube(1, 1, 1);
		pyramidMesh = MeshBuilder.createTexturedPyramid(1, 1, 2);
		planeMesh = MeshBuilder.createTexturedPlane(5, 5);
		
		lightPos = new Vector3(1, 0.5f, 1);
		lightDiffuse = new Vector3(1);
		lightAmbient = new Vector3(0.4f);
		lightSpecular = new Vector3(0.8f);
		roughness = 32f;
		
		cubePos = new Vector3(2, 1, 0);
		pyrPos = new Vector3(-3, 0, 0);
		planePos = new Vector3(0, 0, 0);
		
		inputProcessor = new InputProcessor();
		inputProcessor.setMouseRelativeMode(true);
		
		quitInput = new Input(InputType.KEYBOARD, Keyboard.KEY_ESCAPE, InputState.PRESSED, InputAction.ONCE);
		moveForwardInput = new Input(InputType.KEYBOARD, Keyboard.KEY_W);
		moveBackwardInput = new Input(InputType.KEYBOARD, Keyboard.KEY_S);
		moveLeftInput = new Input(InputType.KEYBOARD, Keyboard.KEY_A);
		moveRightInput = new Input(InputType.KEYBOARD, Keyboard.KEY_D);
		moveUpInput = new Input(InputType.KEYBOARD, Keyboard.KEY_LSHIFT);
		moveDownInput = new Input(InputType.KEYBOARD, Keyboard.KEY_LCONTROL);
		
		camera = new FreeFlyCamera((float)Math.toRadians(60), getAspectRatio());
		camera.setPosition(new Vector3(0, 0, 5));
	}

	@Override
	protected void update(float delta) 
	{
		inputProcessor.update();
		
		if(inputProcessor.isInput(quitInput))
			stop();
		
		if(inputProcessor.isInput(moveForwardInput))
			camera.moveForward(5);
		
		if(inputProcessor.isInput(moveBackwardInput))
			camera.moveForward(-5);
		
		if(inputProcessor.isInput(moveLeftInput))
			camera.moveSideways(-5);
		
		if(inputProcessor.isInput(moveRightInput))
			camera.moveSideways(5);
		
		if(inputProcessor.isInput(moveUpInput))
			camera.moveUpward(5);
		
		if(inputProcessor.isInput(moveDownInput))
			camera.moveUpward(-5);
		
		camera.rotateYaw(inputProcessor.getMouseDX());
		camera.rotatePitch(inputProcessor.getMouseDY());
		
		camera.update(delta);
	}

	@Override
	protected void draw() 
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Matrix4 worldMat = new Matrix4(1.0f);
		
		blinnPhongShader.use();
		
		viewUni.set(camera.getView());
		projUni.set(camera.getProjection());
		
		roughnessUni.set(roughness);
		
		lightPosUni.set(lightPos);
		lightDiffuseUni.set(lightDiffuse);
		lightAmbientUni.set(lightAmbient);
		lightSpecularUni.set(lightSpecular);
		
		viewPosUni.set(camera.getPosition());
		
		diffMapUni.set(0);
		
		woodHq.bind(0);
		sampHqMip.bind(0);
		Matrix4.makeTranslation(cubePos, worldMat);
		worldUni.set(worldMat);
		cubeMesh.draw();
		
		
		woodLq.bind(0);
		sampLq.bind(0);
		Matrix4.makeTranslation(pyrPos, worldMat);
		worldUni.set(worldMat);
		pyramidMesh.draw();
		
		
		woodHq.bind(0);
		sampHqMip.bind(0);
		Matrix4.makeTranslation(planePos, worldMat);
		worldUni.set(worldMat);
		planeMesh.draw();
	}

	@Override
	protected void destroy() 
	{
		cubeMesh.destroy();
		pyramidMesh.destroy();
		planeMesh.destroy();
		
		blinnPhongShader.destroy();
		
		woodHq.destroy();
		woodLq.destroy();
		
		sampHq.destroy();
		sampLq.destroy();
		sampHqMip.destroy();
		sampLqMip.destroy();
	}
	
	public static void main(String... argv)
	{
		new CGAssignment().start("CG Aufgabe --- Mouse to look | WASD to move | ESC to quit", 800, 600, false);
	}
}
