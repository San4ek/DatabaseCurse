package org.example.Database.Classes.HandlerClasses;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.collections.ObservableList;
import org.example.Database.Classes.ClassesForDatabase.Tables.BuyerTable;
import org.example.Database.Enums.ConfigEnums.ServerConfigs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
    private static final DatabaseHandler databaseHandler=new DatabaseHandler();
    static HttpServer server=null;
    public static void startWebServer(BuyerTable buyerTable, ObservableList<BuyerTable> data) {
        try {
            server = HttpServer.create(new InetSocketAddress(80), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext(ServerConfigs.PATH.getTitle(), new MyHandler(buyerTable, data));
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        private final BuyerTable buyerTable;
        private final ObservableList<BuyerTable> data;

        MyHandler(BuyerTable buyerTable, ObservableList<BuyerTable> data){
            this.buyerTable = buyerTable;
            this.data=data;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "Approved";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            data.add(databaseHandler.insertAndGetBuyer(buyerTable));
            os.write(response.getBytes());
            os.close();

            server.stop(10);
        }
    }
}
