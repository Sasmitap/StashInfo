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
	public static final String STASH_COMMIT_URL = "http://localhost:7990/rest/api/1.0/projects/UIAM/repos/DevopsSecurityDemo/commits/";
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
				if (item.getCommitId() != null) {
					newLine.append(item.getCommitId());
					newLine.append(",");
					newLine.append(getEmailIdByCommitId(item.getCommitId()));
					newLine.append(",");
				} else {
					newLine.append(",,");
				}

				if (item.getAuthor() != null && item.getAuthor().getFullName() != null) {
					newLine.append(item.getAuthor().getFullName());
				}
				newLine.append(",");

				if (item.getAffectedPaths() != null && !item.getAffectedPaths().isEmpty()) {
					for (String files : item.getAffectedPaths()) {
						newLine.append(files + ";");
					}
				}
				newLine.append(",");

				if (item.getComment() != null) {
					newLine.append(item.getComment());
				}

				System.out.println(newLine.toString());
				writer.write(newLine.toString());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getEmailIdByCommitId(String commitId) {
		if (commitId != null) {
			System.out.println("Fetching Results from " + STASH_COMMIT_URL + commitId);
			ClientResponse response = apiCall(STASH_COMMIT_URL + commitId, ACCEPT_JSON, true);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			Type objType = new TypeToken<CommitResponse>() {
			}.getType();
			String output = response.getEntity(String.class);
			CommitResponse responseObj = gson.fromJson(output, objType);
			if (responseObj.getAuthor() != null && responseObj.getAuthor().getEmailAddress() != null) {
				return responseObj.getAuthor().getEmailAddress();
			}
		}
		return null;
	}

	public static ClientResponse apiCall(String URI, String acceptType, boolean isStash) {
		Client client = Client.create();
		if(isStash){
			client.addFilter(new HTTPBasicAuthFilter("micros","micros123"));
		}
		WebResource webResource = client.resource(URI);

		ClientResponse response = webResource.accept(acceptType).get(ClientResponse.class);
		return response;
	}
}
