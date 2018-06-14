package ch.heigvd.gen.mpms.file;

import ch.heigvd.gen.mpms.model.GameComponent.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;


/**
 * @brief class that create (if not exist) and handle the file that contains the configs
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class ConfigurationFileJson {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private String fileName;

	public ConfigurationFileJson() {
		fileName = new File("").getAbsolutePath() + File.separator + "configs.mine";
	}

	public boolean insertConfigurationIntoFile(Configuration config){

		try {
			File f = new File(fileName);

			// search if the name is already taken
			if(selectConfig(config.getName()) != null){
				return false;
			}

			FileWriter fw = new FileWriter(f,true);
			PrintWriter file = new PrintWriter(new BufferedWriter(fw));

			//insertion of the json in the file
			String json = objectMapper.writeValueAsString(config);
			file.println(json);

			file.close();
			fw.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	public Configuration selectConfig(String configName){
		Configuration config = null;
		BufferedReader file = null;

		try {
			File f = new File(fileName);
			file = new BufferedReader(new FileReader(f));

			String json;
			while ((json = file.readLine()) != null){
				config = objectMapper.readValue(json, Configuration.class);
				if(config.getName().equals(configName)){ //config found
					break;
				}else{
					config = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}

	public ArrayList<String> listOfConfigs(){
		ArrayList<String> list = new ArrayList<>();

		Configuration config = null;
		BufferedReader file = null;

		try {
			File f = new File(fileName);
			file = new BufferedReader(new FileReader(f));
			String json;

			while ((json = file.readLine()) != null){
				config = objectMapper.readValue(json, Configuration.class);
				list.add(config.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

}
