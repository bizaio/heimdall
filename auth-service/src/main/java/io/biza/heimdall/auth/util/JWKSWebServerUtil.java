package io.biza.heimdall.auth.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class JWKSWebServerUtil {

  public static HttpServer serveJwks(int port, String json) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/jwks.json", httpExchange -> {
      byte response[] = json.getBytes("UTF-8");
      httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
      httpExchange.sendResponseHeaders(200, response.length);
      OutputStream out = httpExchange.getResponseBody();
      out.write(response);
      out.close();
    });

    server.start();
    return server;
  }

}
