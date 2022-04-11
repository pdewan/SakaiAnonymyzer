package anonymyzer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Drive files list API */
public class DriveAPI {
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Drive SERVICE = null;
    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DriveAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        // returns an authorized Credential object.
        return credential;
    }
    
    private static Drive getService() {
    	if (SERVICE == null) {
			try {
				NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
				SERVICE = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	                    .setApplicationName(APPLICATION_NAME)
	                    .build();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return SERVICE;
    }
    
    public static java.io.File downloadFileWithId(String fileId) throws IOException {
        Drive service = getService();
        if (service == null) {
			return null;
		}
        Get get = service.files().get(fileId);
        File file = get.execute();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get.executeMediaAndDownloadTo(outputStream);
        try(OutputStream os = new FileOutputStream(file.getName())) {
        	outputStream.writeTo(os);
        }
		return new java.io.File(file.getName());
    }
    
    public static java.io.File downloadFileWithName(String filename) throws IOException {
        Drive service = getService();
        if (service == null) {
			return null;
		}
        File file = null;
    	String pageToken = null;
    	dowhile:
    	do {
    	  FileList result = service.files().list()
//    	      .setQ("mimeType='image/jpeg'")
    	      .setSpaces("drive")
    	      .setFields("nextPageToken, files(id, name)")
    	      .setPageToken(pageToken)
    	      .execute();
    	  for (File file2 : result.getFiles()) {
    		  if (file2.getName().equals(filename)) {
    			file = file2;
				break dowhile;
			}
    	  }
    	  pageToken = result.getNextPageToken();
    	} while (pageToken != null);
    	if (file == null) {
			return null;
		}
    	return downloadFileWithId(file.getId());
    }
    
    public static boolean updateFile(String fileId, String name) {
    	try {
    		// First create a new File.
    		File file = new File();

    		// File's new metadata.
    		file.setName(name);
    		//    		file.setDescription(newDescription);
    		//    		file.setMimeType(newMimeType);

    		// File's new content.
    		java.io.File fileContent = new java.io.File(name);
    		FileContent mediaContent = new FileContent(null, fileContent);

    		// Send the request to the API.
    		Drive service = getService();
    		if (service == null) {
    			return false;
    		}
    		service.files().update(fileId, file, mediaContent).execute();
    		return true;
    	} catch (IOException e) {
    		System.out.println("An error occurred: " + e);
    		return false;
    	}
    }
}