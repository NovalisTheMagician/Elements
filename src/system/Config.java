package system;

public class Config 
{
	private final String ASSETS_ROOT_PATH;
	private final String MESH_SUB_PATH;
	private final String TEXTURE_SUB_PATH;
	private final String SHADER_SUB_PATH;
	private final String MATERIAL_SUB_PATH;
	private final String SCRIPT_SUB_PATH;
	private final String SOUND_SUB_PATH;
	private final String MUSIC_SUB_PATH;
	private final String ENTITY_SUB_PATH;
	private final String WORLD_SUB_PATH;
	
	public Config()
	{
		ASSETS_ROOT_PATH = "assets/";
		MESH_SUB_PATH = "meshes/";
		TEXTURE_SUB_PATH = "textures/";
		SHADER_SUB_PATH = "shader/";
		MATERIAL_SUB_PATH = "materials/";
		SCRIPT_SUB_PATH = "scripts/";
		SOUND_SUB_PATH = "sounds/";
		MUSIC_SUB_PATH = "music/";
		ENTITY_SUB_PATH = "entites/";
		WORLD_SUB_PATH = "worlds/";
	}
	
	public Config(String configFile)
	{
		//TODO
		this();
	}
	
	public String getRootPath()
	{
		return ASSETS_ROOT_PATH;
	}
	
	public String getMeshPath()
	{
		return ASSETS_ROOT_PATH + MESH_SUB_PATH;
	}
	
	public String getTexturePath()
	{
		return ASSETS_ROOT_PATH + TEXTURE_SUB_PATH;
	}
	
	public String getShaderPath()
	{
		return ASSETS_ROOT_PATH + SHADER_SUB_PATH;
	}
	
	public String getMaterialPath()
	{
		return ASSETS_ROOT_PATH + MATERIAL_SUB_PATH;
	}
	
	public String getScriptPath()
	{
		return ASSETS_ROOT_PATH + SCRIPT_SUB_PATH;
	}
	
	public String getSoundPath()
	{
		return ASSETS_ROOT_PATH + SOUND_SUB_PATH;
	}
	
	public String getMusicPath()
	{
		return ASSETS_ROOT_PATH + MUSIC_SUB_PATH;
	}
	
	public String getEntityPath()
	{
		return ASSETS_ROOT_PATH + ENTITY_SUB_PATH;
	}
	
	public String getWorldPath()
	{
		return ASSETS_ROOT_PATH + WORLD_SUB_PATH;
	}
}
