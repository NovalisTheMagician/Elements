package system.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import system.Config;
import system.renderer.Mesh;
import system.renderer.Texture;
import system.renderer.shader.Program;

public class AssetManager 
{
	public static class AssetType<T>
	{
		public final static AssetType<Texture> 	TEXTURE = new AssetType<>(Texture.class);
		public final static AssetType<Mesh> 	MESH = new AssetType<>(Mesh.class);
		public final static AssetType<Program> 	SHADER = new AssetType<>(Program.class);
		// TODO add more AssetTypes
		
		private Class<T> clazz;
		
		private AssetType(Class<T> type)
		{
			clazz = type;
		}
		
		public T castToType(Object obj)
		{
			return clazz.cast(obj);
		}
		
		public String toString()
		{
			return clazz.getName();
		}
	}
	
	private Map<AssetType<?>, Map<String, Object>> assets;
	private Map<AssetType<?>, Loader<?>> loaders;
	
	private Map<String, AssetType<?>> assetPath;
	private Map<AssetType<?>, FileFilter> assetFileFilter;
	
	private Config config;
	
	public AssetManager(Config config)
	{
		this.config = config;
		assets = new HashMap<>();
		loaders = new HashMap<>();
		assetPath = new HashMap<>();
		assetFileFilter = new HashMap<>();
		
		assets.put(AssetType.TEXTURE, new HashMap<>());
		assets.put(AssetType.MESH, new HashMap<>());
		assets.put(AssetType.SHADER, new HashMap<>());
		// TODO add more AssetType maps
		
		buildTables();
	}
	
	private void buildTables()
	{
		assetPath.put(config.getTextureFolder(), AssetType.TEXTURE);
		assetPath.put(config.getMeshFolder(), AssetType.MESH);
		assetPath.put(config.getShaderFolder(), AssetType.SHADER);
		// TODO add remaining assets
		
		assetFileFilter.put(AssetType.TEXTURE, new FileFilter() 
			{ public boolean accept(File file) { return file.isFile() && checkExt(file, ".png"); } });
		assetFileFilter.put(AssetType.MESH, new FileFilter() 
			{ public boolean accept(File file) { return file.isFile() && checkExt(file, ".msh"); } });
		assetFileFilter.put(AssetType.SHADER, new FileFilter() 
			{ public boolean accept(File file) { return file.isFile() && checkExt(file, ".shd"); } });
		// TODO add remaining assets
	}
	
	public void registerLoader(AssetType<?> type, Loader<?> loader)
	{
		loaders.put(type, loader);
	}
	
	public void loadAssets()
	{
		File rootPath = new File(config.getRootPath());
		File[] folders = rootPath.listFiles(this::acceptAssetFolder);
		
		for(File assetFolder : folders)
		{
			AssetType<?> type = assetPath.get(assetFolder.getName());
			if(type == null) continue;
			
			Loader<?> loader = loaders.get(type);
			if(loader == null) continue;
			
			File[] files = assetFolder.listFiles(assetFileFilter.get(type));
			for(File assetFile : files)
			{
				try (InputStream stream = Files.newInputStream(assetFile.toPath(), StandardOpenOption.READ))
				{
					put(type, assetFile.getName(), loader.load(stream, assetFolder.getPath()));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean acceptAssetFolder(File folder)
	{
		return folder.isDirectory();
	}
	
	private boolean checkExt(File file, String extension)
	{
		return file.getName().toLowerCase().endsWith(extension);
	}
	
	private void put(AssetType<?> type, String identifier, Object asset)
	{
		assets.get(type).put(identifier, asset);
	}
	
	public <T> T get(AssetType<T> type, String identifier)
	{
		Object asset = assets.get(type).get(identifier);
		if(asset == null)
			throw new AssetNotFoundException("Could not find " + identifier);
		
		T castedAsset = type.castToType(asset);
		if(castedAsset == null)
			throw new AssetNotFoundException("Could not cast to type " + type.toString());
		
		return castedAsset;
	}
}
