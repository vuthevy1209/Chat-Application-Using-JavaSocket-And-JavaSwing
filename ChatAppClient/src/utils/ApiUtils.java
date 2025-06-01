package utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import config.SocketConfig;
import dto.request.ApiRequest;
import dto.response.ApiResponse;

public class ApiUtils {
    public static ApiResponse handleRequest(ApiRequest request) {
        try (Socket socket = new Socket(SocketConfig.getIpAddress(), SocketConfig.getPort())) {
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Send the request
            outObject.writeObject(request);
            outObject.flush();

            // Read the response
            ApiResponse response = (ApiResponse) in.readObject();

            System.out.println("Response Code: " + response.getCode());
            System.out.println("Response Message: " + response.getMessage());

            return response;
        } catch (Exception ex) {
            System.err.println("Error during request handling: " + ex.getMessage());
            return ApiResponse.builder()
                .code("500")
                .message("Internal Server Error")
                .build();
        }
    }
}
