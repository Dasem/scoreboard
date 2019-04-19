package psu.utils;

public class GlobalConstants {
    public final static int PORT = 25565;
    public static final int BUF_SIZE_FILE = 1024;
    public static final int BUF_SIZE_IP = 32;
    public static final int TIMEOUT = 500;

    public final static String SERVER_NAME = "SERVER_HOST";
    public static final String GET_SERVER_IP = "GET_SERVER_IP";

    //GUI messages
    public static final String FILE_SENDER_TITLE = "FileSender | Entered as: {0}";
    public static final String FILE_SIZE_PATTERN = "File size: {0} байт";
    public static final String SEND_FILE_SUCCESS = "Success";
    public static final String ACCEPT_FILE_SUCCESS = "File {0} available at path ''{1}''";
    public static final String CONNECTION_LOST = "Connection with ''{0}'' lost";
    public static final String MESSAGE_SEND_ERROR = "Error with sending to user ''{0}''";

    //layouts
    public final static String LOGIN_WINDOW_FXML = "/layouts/LoginTemplate.fxml";
    public final static String MAIN_WINDOW_FXML = "/layouts/FileExporterTemplate.fxml";
}
