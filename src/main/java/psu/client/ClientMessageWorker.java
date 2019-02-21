package psu.client;

import javafx.scene.control.Alert;
import psu.entities.Message;
import psu.entities.MessageType;
import psu.utils.FileSender;
import psu.utils.GlobalConstants;
import psu.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.List;

import static psu.utils.GlobalConstants.*;
import static psu.utils.Utils.createNewMessage;
import static psu.utils.Utils.showAlertMessage;

public class ClientMessageWorker implements Runnable {

    private FileExporterClientController controller;
    private final static String SERVER_NAME = "SERVER_HOST";

    public static Thread clientMessager;

    private static ClientMessageWorker instance;

    private static Socket clientSocket;
    private static String clientName;

    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static ObjectInputStream messageInput;
    private static ObjectOutputStream messageOutput;

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static ObjectInputStream getMessageInput() {
        return messageInput;
    }

    public static ObjectOutputStream getMessageOutput() {
        return messageOutput;
    }

    private ClientMessageWorker(Socket socket) {
        try {
            clientSocket = socket;
            //clientName = name;
            outputStream = clientSocket.getOutputStream();
            messageOutput = new ObjectOutputStream(outputStream);
            inputStream = clientSocket.getInputStream();
            messageInput = new ObjectInputStream(inputStream);
        } catch (Exception ex) {

        }
    }

    public synchronized static ClientMessageWorker getInstance(){
        if (instance == null) {
            try {
                instance = new ClientMessageWorker(new Socket(SERVER_IP, PORT));
            } catch (IOException ex) {
                showAlertMessage("Подключение", "Статус", "Сервер недоступен", Alert.AlertType.WARNING);
            }
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                Message message = (Message) messageInput.readObject();
                System.out.println("Пришёл MessageType: "+message.getMessageType().name());
                switch (message.getMessageType()) {
//                    case AUTH:
//                        // Пришел ответ на попытку подключения
//                        break;
                    case USER_CONNECTED:
                        // Обновить список пользователей
                        controller.setUsersList((List<String>) message.getAttachment());
                        break;
                    case USER_DISCONNECTED:
                        // Обновить список пользователей
                        break;
                    case ERROR_SERVER:
                        // Обработать ошибку с сервера
                        break;
                    case NEW_FILE_REQUEST:
                        // Обработать получение файла

                        FileSender.acceptFile(new File(
                                MessageFormat.format(ACCEPTED_FILES_PATH, message.getContent())
                        ));
                        break;
                    case MESSAGE:
                        // Обработать полученное сообщение
                        controller.pushToTextArea(message.getSender(), message.getContent());
                        break;
                    case ERROR_CLIENT:
                        // ?
                        break;
                    default: //whaaat
                        System.out.println("Неизвестный тип сообщения");
                }
            }
            System.out.print("lol");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Метод для авторизации на сервере
     *
     * @return 1 - успешно, 0 - ник занят, -1 что-то пошло не так
     */
    public int tryCreateConnection(String name) throws IOException, ClassNotFoundException {
        clientName = name;

        Message authMessage = new Message();
        authMessage.setMessageType(MessageType.AUTH);
        authMessage.setSender(clientName);
        authMessage.setContent(clientName);
        authMessage.setRecipient(SERVER_NAME);
        messageOutput.writeObject(authMessage);
        messageOutput.flush();
        Message answer = (Message) messageInput.readObject();
        if (answer.getMessageType() == MessageType.AUTH
                && answer.getSender().equals(SERVER_NAME)) {
            if (answer.getContent().equals("success")) {
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    public void setController(FileExporterClientController controller) {
        this.controller = controller;
    }

    public void sendFileNotification(){
        Message notification = createNewMessage();
        notification.setMessageType(MessageType.NEW_FILE_REQUEST);
        notification.setSender(clientName);
        notification.setContent(FileExporterClientController.openedFile.getName());
        notification.setRecipient(FileExporterClientController.selectedUser.toString());
        try {
            messageOutput.writeObject(notification);
            messageOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();//сделать норм обрбоотку
        }
    }

    public void sendMessage(String message) {
        Message authMessage = createNewMessage();
        authMessage.setMessageType(MessageType.NEW_FILE_REQUEST);
        authMessage.setSender(clientName);
        authMessage.setContent(message);
        authMessage.setRecipient(SERVER_NAME);
        try {
            messageOutput.writeObject(authMessage);
            messageOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();//сделать норм обрбоотку
        }
    }
}