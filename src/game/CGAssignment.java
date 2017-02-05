package game;

import static org.lwjgl.opengl.GL11.*;

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
import system.renderer.Light;
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
	private Mesh cubeMesh, pyramidMesh, floorPlaneMesh, portalPlaneMesh, lightCube[];
	private Mesh thingCube, thingPyr1, thingPyr2;
	private Program blinnPhongShader, proceduralShader, lightVisShader;
	
	private FreeFlyCamera camera;
	
	private Light[] lights;
	
	private float roughness;
	
	private Vector3 cubePos, pyrPos, planePos, portalPos, thingPos;
	
	private Uniform<Matrix4> worldUni, viewUni, projUni;
	private Uniform<Integer> diffMapUni;
	private Uniform<Float> roughnessUni, materialSpecularUni;
	private Uniform<Vector3> lightPosUni1, lightDiffuseUni1, lightAmbientUni1, lightSpecularUni1;
	private Uniform<Vector3> lightPosUni2, lightDiffuseUni2, lightAmbientUni2, lightSpecularUni2;
	private Uniform<Vector3> viewPosUni;
	
	private Uniform<Float> plasmaTimeUni;
	private Uniform<Matrix4> plasmaWorldUni, plasmaViewUni, plasmaProjUni;
	
	private Uniform<Matrix4> lightWorldUni, lightViewUni, lightProjUni;
	
	private float time;
	
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
		
		lightVisShader = shdLoader.loadManually(clazzLoader.getResourceAsStream("assets/shader/color.vs"), 
				clazzLoader.getResourceAsStream("assets/shader/color.fs"));
		
		proceduralShader = shdLoader.loadManually(clazzLoader.getResourceAsStream("assets/shader/plasma.vs"), 
				clazzLoader.getResourceAsStream("assets/shader/plasma.fs"));
		
		worldUni = blinnPhongShader.getUniformMatrix4("world");
		viewUni = blinnPhongShader.getUniformMatrix4("view");
		projUni = blinnPhongShader.getUniformMatrix4("proj");
		
		plasmaWorldUni = proceduralShader.getUniformMatrix4("world");
		plasmaViewUni = proceduralShader.getUniformMatrix4("view");
		plasmaProjUni = proceduralShader.getUniformMatrix4("proj");
		
		lightWorldUni = lightVisShader.getUniformMatrix4("world");
		lightViewUni = lightVisShader.getUniformMatrix4("view");
		lightProjUni = lightVisShader.getUniformMatrix4("proj");
		
		lightPosUni1 = blinnPhongShader.getUniformVector3("lights[0].position");
		lightDiffuseUni1 = blinnPhongShader.getUniformVector3("lights[0].diffuse");
		lightAmbientUni1 = blinnPhongShader.getUniformVector3("lights[0].ambient");
		lightSpecularUni1 = blinnPhongShader.getUniformVector3("lights[0].specular");
		
		lightPosUni2 = blinnPhongShader.getUniformVector3("lights[1].position");
		lightDiffuseUni2 = blinnPhongShader.getUniformVector3("lights[1].diffuse");
		lightAmbientUni2 = blinnPhongShader.getUniformVector3("lights[1].ambient");
		lightSpecularUni2 = blinnPhongShader.getUniformVector3("lights[1].specular");
		
		viewPosUni = blinnPhongShader.getUniformVector3("viewPos");
		
		diffMapUni = blinnPhongShader.getUniformInteger("material.diffuseMap");
		roughnessUni = blinnPhongShader.getUniformFloat("material.roughness");
		materialSpecularUni = blinnPhongShader.getUniformFloat("material.specular");
		
		plasmaTimeUni = proceduralShader.getUniformFloat("time");
		
		cubeMesh = MeshBuilder.createTexturedNormalCube(1, 1, 1);
		pyramidMesh = MeshBuilder.createTexturedNormalPyramid(1, 1, 2);
		floorPlaneMesh = MeshBuilder.createTexturedNormalPlane(5, 5);
		portalPlaneMesh = MeshBuilder.createTexturedPlane(2, 2);
		
		thingCube = MeshBuilder.createTexturedNormalCube(0.5f, 0.5f, 0.5f);
		thingPyr1 = MeshBuilder.createTexturedNormalPyramid(0.25f, 0.25f, 0.5f);
		thingPyr2 = MeshBuilder.createTexturedNormalPyramid(0.1f, 0.1f, 0.2f);
		
		lights = new Light[2];
		lights[0] = new Light(new Vector3(-1.0f, 1.5f, -1.5f), new Vector3(1.0f), new Vector3(0.1f), new Vector3(1.0f));
		lights[1] = new Light(new Vector3(2.5f, 1.0f, 3.0f), new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f), new Vector3(0.3f));
		
		roughness = 32f;
		
		cubePos = new Vector3(2, 1, 0);
		pyrPos = new Vector3(-3, 0, 0);
		planePos = new Vector3(0, 0, 0);
		portalPos = new Vector3(0, 3, -5);
		thingPos = new Vector3(0, 1, 0);
		
		lightCube = new Mesh[2];
		lightCube[0] = MeshBuilder.createColoredCube(0.2f, 0.2f, 0.2f, new Vector3(1.0f));
		lightCube[1] = MeshBuilder.createColoredCube(0.2f, 0.2f, 0.2f, new Vector3(1.0f, 0.0f, 0.0f));
		
		inputProcessor = new InputProcessor();
		inputProcessor.setMouseRelativeMode(true);
		
		quitInput = new Input(InputType.KEYBOARD, Keyboard.KEY_ESCAPE, InputState.PRESSED, InputAction.ONCE);
		moveForwardInput = new Input(InputType.KEYBOARD, Keyboard.KEY_W);
		moveBackwardInput = new Input(InputType.KEYBOARD, Keyboard.KEY_S);
		moveLeftInput = new Input(InputType.KEYBOARD, Keyboard.KEY_A);
		moveRightInput = new Input(InputType.KEYBOARD, Keyboard.KEY_D);
		moveUpInput = new Input(InputType.KEYBOARD, Keyboard.KEY_SPACE);
		moveDownInput = new Input(InputType.KEYBOARD, Keyboard.KEY_LSHIFT);
		
		camera = new FreeFlyCamera((float)Math.toRadians(60), getAspectRatio());
		camera.setPosition(new Vector3(0, 0.5f, 5));
		
		time = 0;
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
		
		camera.rotateYaw(inputProcessor.getMouseDX() * 0.5f);
		camera.rotatePitch(inputProcessor.getMouseDY() * 0.5f);
		
		camera.update(delta);
		
		time += delta;
	}

	@Override
	protected void draw() 
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		blinnPhongShader.use();
		
		viewUni.set(camera.getView());
		projUni.set(camera.getProjection());
		
		lightPosUni1.set(lights[0].position);
		lightDiffuseUni1.set(lights[0].diffuse);
		lightAmbientUni1.set(lights[0].ambient);
		lightSpecularUni1.set(lights[0].specular);
		
		lightPosUni2.set(lights[1].position);
		lightDiffuseUni2.set(lights[1].diffuse);
		lightAmbientUni2.set(lights[1].ambient);
		lightSpecularUni2.set(lights[1].specular);
		
		viewPosUni.set(camera.getPosition());
		
		diffMapUni.set(0);
		
		drawRoom();
		
		drawProps();
		
		drawMagicThing();
		
		proceduralShader.use();
		
		plasmaViewUni.set(camera.getView());
		plasmaProjUni.set(camera.getProjection());
		
		drawPortal();
		
		lightVisShader.use();
		
		lightViewUni.set(camera.getView());
		lightProjUni.set(camera.getProjection());
		
		drawLights();
	}

	private void drawPortal()
	{
		Matrix4 worldMat = new Matrix4(1.0f);
		Matrix4 trans = new Matrix4(1.0f);
		Matrix4 rot = new Matrix4(1.0f);
		
		Matrix4.makeTranslation(portalPos, trans);
		Matrix4.makeRotationX((float)Math.toRadians(90), rot);
		Matrix4.mul(trans, rot, worldMat);
		
		plasmaTimeUni.set(time);
		plasmaWorldUni.set(worldMat);
		
		portalPlaneMesh.draw();
	}
	
	private void drawMagicThing()
	{	
		Matrix4 worldMat = new Matrix4(1.0f);
		Matrix4 tmp = new Matrix4(1.0f);
		
		Matrix4.makeTranslation(thingPos, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeRotationY(time, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		woodLq.bind(0);
		sampHqMip.bind(0);
		roughnessUni.set(32.0f);
		materialSpecularUni.set(0.7f);
		worldUni.set(worldMat);
		
		thingCube.draw();
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeRotationZ(time, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeTranslation(new Vector3(1, 0, 0), tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeRotationZ(time, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		worldUni.set(worldMat);
		
		thingPyr1.draw();
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeRotationY(time, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeTranslation(new Vector3(0, 0, 0.5f), tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		Matrix4.makeIdentity(tmp);
		Matrix4.makeRotationY(time, tmp);
		Matrix4.mul(worldMat, tmp, worldMat);
		
		worldUni.set(worldMat);
		
		thingPyr2.draw();
	}
	
	private void drawProps()
	{
		Matrix4 worldMat = new Matrix4(1.0f);
		
		woodHq.bind(0);
		sampHqMip.bind(0);
		roughnessUni.set(roughness);
		materialSpecularUni.set(1.0f);
		Matrix4.makeTranslation(cubePos, worldMat);
		worldUni.set(worldMat);
		cubeMesh.draw();
		
		woodLq.bind(0);
		sampLq.bind(0);
		roughnessUni.set(roughness);
		materialSpecularUni.set(0.5f);
		Matrix4.makeTranslation(pyrPos, worldMat);
		worldUni.set(worldMat);
		pyramidMesh.draw();
	}
	
	private void drawLights()
	{
		for(int i = 0; i < 2; ++i)
		{
			Matrix4 worldMat = new Matrix4(1.0f);
			Matrix4.makeTranslation(lights[i].position, worldMat);
			lightWorldUni.set(worldMat);
			lightCube[i].draw();
		}
	}
	
	private void drawRoom()
	{
		Matrix4 worldMat = new Matrix4(1.0f);
		
		woodHq.bind(0);
		sampHqMip.bind(0);
		roughnessUni.set(roughness);
		materialSpecularUni.set(0.1f);
		Matrix4.makeTranslation(planePos, worldMat);
		worldUni.set(worldMat);
		floorPlaneMesh.draw();
	}
	
	@Override
	protected void destroy() 
	{
		cubeMesh.destroy();
		pyramidMesh.destroy();
		floorPlaneMesh.destroy();
		for(int i = 0; i < 2; ++i)
			lightCube[i].destroy();
		
		thingCube.destroy();
		thingPyr1.destroy();
		thingPyr2.destroy();
		
		blinnPhongShader.destroy();
		lightVisShader.destroy();
		proceduralShader.destroy();
		
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
