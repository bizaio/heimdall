package io.biza.heimdall.shared;

import java.net.URI;

public class TestDataConstants {

  public static final String HOLDER_LEGAL_NAME = "Deep Thought Industries Pty Ltd";
  public static final String HOLDER_NAME = "Deep Thought";
  public static final int HOLDER_AUTH_PORT = 9202;
  public static final String HOLDER_BRAND_NAME = "Bizabank";
  public static final URI HOLDER_BRAND_LOGO_URI = URI.create("http://biza.io/logo.png");
  public static final URI HOLDER_BRAND_INFOSEC_URI = URI.create("https://localhost:9301");
  public static final URI HOLDER_BRAND_RESOURCE_URI = URI.create("https://localhost:9302");
  public static final URI HOLDER_BRAND_PUBLIC_URI = URI.create("https://localhost:9303");
  public static final URI HOLDER_BRAND_WEBSITE = URI.create("https://bizabank.com/");
  
  public static final String HOLDER_AUTH_JWKS_PATH = "/jwks";
  public static final String RECIPIENT_LEGAL_NAME = "Deep Thought Pty Ltd";
  public static final String RECIPIENT_NAME = "Deep Thought";
  public static final String HOLDER_CLIENT_ID = "f5392d1c-0b2f-4783-a69f-a29c450d775b";
  public static final String HOLDER_CLIENT_SECRET = "c6a1f13d-630c-4f00-8223-8af5d450f830";
  public static final String RECIPIENT_CLIENT_ID = "91c6df22-81f1-4a19-9b63-42018ddb6668";
  public static final int RECIPIENT_PORT = 54321;
  public static final String RECIPIENT_JWKS_PATH = "/jwks.json";
  
}
