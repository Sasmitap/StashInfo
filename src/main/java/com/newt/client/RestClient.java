package com.newt.client;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class RestClient {
	public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();

	public static final String JENKINS_URL = "http://localhost:8080/jenkins/job/DevopsSecurityDemo/";
	public static final String STASH_COMMIT_URL = "https://api.github.com/repos/Sasmitap/SecurityDemoPOC/commits";
	/**
	 * Details For STASH
	 */
	//public static final String STASH_COMMIT_URL = "http://localhost:7990/rest/api/1.0/projects/UIAM/repos/DevopsSecurityDemo/commits/";
	public static final String API_JSON = "/api/json";
	public static final String CSV_FILE_PATH = "tools/changelog.csv";
	public static final String ACCEPT_JSON = "application/json";
	public static void main(String[] args) {
		try {
			// String BUILD_NUMBER =
			// System.getenv("${env.BUILD_NUMBER}").toString();
			String BUILD_NUMBER = args[0];
			System.out.println(
					"Fetching Results from " + JENKINS_URL + BUILD_NUMBER + API_JSON + "-----" + "application/json");
			ClientResponse response = apiCall(JENKINS_URL + BUILD_NUMBER + API_JSON, ACCEPT_JSON, false);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.getEntity(String.class);

			Type objType = new TypeToken<Response>() {
			}.getType();
			Response responseObj = gson.fromJson(output, objType);
			List<Items> items = responseObj.getChangeSet().getItems();
			FileWriter writer = new FileWriter(new File(CSV_FILE_PATH));
			for (Items item : items) {
				StringBuilder newLine = new StringBuilder();
				newLine.append(BUILD_NUMBER);
				newLine.append(",");
				if (item.getSha() != null) {
					newLine.append(item.getSha());
					newLine.append(",");
				} else {
					newLine.append(",,");
				}
				if(item.getAuthor() != null && item.getAuthor().getName() !=null){
					newLine.append(item.getAuthor().getName());
					newLine.append(",");
				}else{
					newLine.append(",,");
				}

				if (item.getAuthor() != null && item.getAuthor().getEmail() != null) {
					newLine.append(item.getAuthor().getEmail());
					newLine.append(",");
				}else{
					newLine.append(",,");
				}
				if (item.getMessage() != null) {
					newLine.append(item.getMessage());
					newLine.append(",");
				}else{
					newLine.append(",,");
				}
				if (item.getAuthor() != null && item.getAuthor().getDate() != null) {
					newLine.append(item.getAuthor().getDate());
					newLine.append(",");
				}

				System.out.println(newLine.toString());
				writer.write(newLine.toString());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ClientResponse apiCall(String URI, String acceptType, boolean isGitHub) {
		Client client = Client.create();
		if(isGitHub){
			client.addFilter(new HTTPBasicAuthFilter("Sasmitap","@Sasmita32"));
		}
		WebResource webResource = client.resource(URI);

		ClientResponse response = webResource.accept(acceptType).get(ClientResponse.class);
		return response;
	}
}
