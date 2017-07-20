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

/**
 * 
 * @author sasmitap
 *
 */
public class RestClient {
	public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();

	/**
	 * Change according to your Jenkins JOb and GitHub repo
	 */
	public static final String JENKINS_URL = "http://localhost:8080/jenkins/job/DevOpsSecurityDemo/";
	public static final String GITHUB_COMMIT_URL = "https://api.github.com/repos/Sasmitap/SecurityDemoPOC/commits";
	public static final String API_JSON = "/api/json";
	public static final String CSV_FILE_PATH = "tools/changelog.csv";
	public static final String ACCEPT_JSON = "application/json";
	public static void main(String[] args) {
		try {

			/**
			 * Get The details of environment variables Of Jenkins from windows batch
			 *  command or Shell Scripting from Jenkins Job configuration
			 */

			String BUILD_NUMBER = args[0];
			System.out.println("BUILD_NUMBER Name Is..."+BUILD_NUMBER);

			/*String JOB_NAME=args[1];
			System.out.println("JOB_NAME Name Is..."+JOB_NAME);

			String JENKINS_URL=args[2];
			System.out.println("JENKINS_URL Name Is..."+JENKINS_URL);

			String BUILD_URL=args[3];
			System.out.println("BUILD_URL Name Is..."+BUILD_URL);

			String JOB_URL=args[4];
			System.out.println("JOB_URL Name Is..."+JOB_URL);

			String GIT_URL=args[5];
			System.out.println("GIT_URL Name Is..."+GIT_URL);

			String GIT_AUTHOR_NAME=args[6];
			System.out.println("GIT_AUTHOR_NAME Name Is..."+GIT_AUTHOR_NAME);*/

			System.out.println(
					"Fetching Results from " + JENKINS_URL + BUILD_NUMBER + API_JSON + "-----" + "application/json");
			ClientResponse response = apiCallForJenkins(JENKINS_URL + BUILD_NUMBER + API_JSON, ACCEPT_JSON, false);
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
					/**
					 * For Stash to get EmailId
					 */
					//newLine.append(getEmailIdByCommitId(item.getCommitId()));
					newLine.append(item.getAuthorEmail());
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
			System.out.println("Fetching Results from " + GITHUB_COMMIT_URL + commitId);
			ClientResponse response = apiCall(GITHUB_COMMIT_URL + commitId, ACCEPT_JSON, true);
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

	/**
	 * 
	 * @param URI
	 * @param acceptType
	 * @param isGitHub
	 * @return
	 */
	/**
	 * 
	 * Change The ID and Password according to your Repo
	 */

	public static ClientResponse apiCall(String URI, String acceptType, boolean isGitHub) {
		Client client = Client.create();
		if(isGitHub){
			client.addFilter(new HTTPBasicAuthFilter("username","password"));
		}
		WebResource webResource = client.resource(URI);

		ClientResponse response = webResource.accept(acceptType).get(ClientResponse.class);
		return response;
	}

	public static ClientResponse apiCallForJenkins(String URI, String acceptType, boolean isJenkins) {
		Client client = Client.create();
		if(isJenkins){
			client.addFilter(new HTTPBasicAuthFilter("username","password"));
		}
		WebResource webResource = client.resource(URI);

		ClientResponse response = webResource.accept(acceptType).get(ClientResponse.class);
		return response;
	}
}
