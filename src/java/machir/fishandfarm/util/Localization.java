package machir.fishandfarm.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import machir.fishandfarm.proxy.CommonProxy;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Simple mod localization class.
 * 
 * @author Original: Jimeo Wan
 * @author Edited version: Jasper Hidding (Aka Machir)
 * @license Public domain
 * 
 */
public class Localization {

	private static String loadedLanguage = getCurrentLanguage();

	/**
	 * Adds localization from a given directory. The files must have the same name as the corresponding language file in minecraft and a ".properties" file
	 * extention e.g "en_US.properties"
	 * 
	 * @param path
	 *            The path to the localization files
	 * @param defaultLanguage
	 *            The default localization to be used when there is no localization for the selected language or if a string is missing (e.g. "en_US")
	 */
	
	public static void addLocalization(String path, String defaultLanguage) {
		load(path, defaultLanguage);
	}

	private static void load(String path, String default_language) {
		InputStream langStream = null;
		Properties modMappings = new Properties();

		FileReader reader = null;
		BufferedReader langReader = null;
		
		try {
			// Load the default language mappings
		    langReader = new BufferedReader(new InputStreamReader(Localization.class.getResourceAsStream(path + default_language + ".properties")));
			String line = "";
			while ((line = langReader.readLine()) != null) {
			    if (line.contains("=")) {
			        String[] name = line.split("=");
			        LanguageRegistry.instance().addStringLocalization(name[0], default_language, name[1]);
			    }
			}
			langReader.close();

			// Try to load the current language mappings.
			// If the file doesn't exist use the default mappings.
			langStream = Localization.class.getResourceAsStream(path + getCurrentLanguage() + ".properties");
			if (langStream != null) {
				modMappings.clear();
				modMappings.load(langStream);
			}

			// If the selected language inherits mappings from another language
			// we use those first and then we overwrite the common ones with
			// those in the selected language
			if (modMappings.containsKey("language.parent")) {
				langStream = Localization.class.getResourceAsStream(path + modMappings.get("language.parent") + ".properties");

	            langReader = new BufferedReader(new FileReader(Localization.class.getResource(path + modMappings.get("language.parent") + ".properties").getFile()));
	            line = "";
	            while ((line = langReader.readLine()) != null) {
	                String[] name = line.split("=");
	                LanguageRegistry.instance().addStringLocalization(name[0], default_language, name[1]);
	            }
	            langReader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (langStream != null) {
					langStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String getCurrentLanguage() {
		return CommonProxy.proxy.getCurrentLanguage();
	}
}
