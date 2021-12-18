package com.scripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetsHandler {
    private static final String APPLICATION_NAME = "RLPC Forecast";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "C:\\Users\\Simcha\\Desktop\\Projects\\RLPC_Forecast\\forecast\\src\\main\\resources\\new_tokens";

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/creds.json";

    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the creds.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsHandler.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Creates an authorized client service
     * 
     * @return Authorized API client service
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static Sheets getService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();
        return service;
    }

    /**
     * 
     * @param spreadsheetId
     * @param range
     * @return 2-d array of values in specified range
     * @throws IOException
     * @throws GeneralSecurityException if unable to create a GoogleNetHttpTransport
     */
    public static List<List<Object>> getValues(String spreadsheetId, String range)
            throws IOException, GeneralSecurityException {
        Sheets service = getService();
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        }

        return values;
    }

    /**
     * 
     * @param spreadsheetId
     * @param range
     * @param values
     * @return Google Sheets API Response
     * @throws IOException
     * @throws GeneralSecurityException if unable to create a GoogleNetHttpTransport
     */
    public static AppendValuesResponse append(String spreadsheetId, String range, List<List<Object>> values)
            throws IOException, GeneralSecurityException {
        Sheets service = getService();
        
        ValueRange body = new ValueRange().setValues(values);
        AppendValuesResponse response = service.spreadsheets().values().append(spreadsheetId, range, body).setValueInputOption("USER_ENTERED").execute();
        return response;
    }

    /**
     * 
     * @param spreadsheetId
     * @param range
     * @return
     * @throws IOException
     * @throws GeneralSecurityException if unable to create a GoogleNetHttpTransport
     */
    public static ClearValuesResponse clear(String spreadsheetId, String range)
            throws IOException, GeneralSecurityException {
        Sheets service = getService();
        ClearValuesRequest request = new ClearValuesRequest();
        ClearValuesResponse response = service.spreadsheets().values().clear(spreadsheetId, range, request).execute();
        return response;
    }
}