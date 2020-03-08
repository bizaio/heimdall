package io.biza.heimdall.shared;

import java.net.URI;
import java.util.Set;

public class TestDataConstants {

  public static final String HOLDER_LEGAL_NAME = "Biza Pty Ltd";
  public static final String HOLDER_NAME = "Biza.io";
  public static final int HOLDER_AUTH_PORT = 9202;
  public static final String HOLDER_BRAND_NAME = "Bizabank";
  public static final URI HOLDER_BRAND_LOGO_URI = URI.create("https://biza.io/wp-content/uploads/2018/07/biza-black-300x67.png");
  public static final URI HOLDER_BRAND_INFOSEC_URI = URI.create("https://localhost:9301");
  public static final URI HOLDER_BRAND_RESOURCE_URI = URI.create("https://localhost:9302");
  public static final URI HOLDER_BRAND_PUBLIC_URI = URI.create("https://localhost:9303");
  public static final URI HOLDER_BRAND_WEBSITE = URI.create("https://bizabank.com/");
  
  public static final String HOLDER_AUTH_JWKS_PATH = "/jwks";
  public static final String RECIPIENT_LEGAL_NAME = "Biza Pty Ltd";
  public static final URI RECIPIENT_LOGO_URI = URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  public static final String RECIPIENT_NAME = "DataRight.IO";
  public static final String HOLDER_CLIENT_ID = "f5392d1c-0b2f-4783-a69f-a29c450d775b";
  public static final String HOLDER_CLIENT_SECRET = "c6a1f13d-630c-4f00-8223-8af5d450f830";
  public static final String RECIPIENT_CLIENT_ID = "91c6df22-81f1-4a19-9b63-42018ddb6668";
  public static final int RECIPIENT_PORT = 54321;
  public static final String RECIPIENT_JWKS_PATH = "/jwks.json";
  public static final URI RECIPIENT_BRAND_LOGO_URI = URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  public static final String RECIPIENT_PRODUCT_NAME = "Prak Data Recipient Example";
  public static final String RECIPIENT_PRODUCT_DESCRIPTION = "A demonstration of the Prak Data Recipient example";
  public static final URI RECIPIENT_PRODUCT_WEBSITE = URI.create("https://dataright.io");
  public static final Set<URI> RECIPIENT_PRODUCT_REDIRECT_URI = Set.of(URI.create("http://localhost:4200/cb"));
  public static final URI RECIPIENT_PRODUCT_TOS = URI.create("https://dataright.io/tos");
  public static final URI RECIPIENT_PRODUCT_POLICY = URI.create("https://dataright.io/policy");
  public static final URI RECIPIENT_PRODUCT_REVOCATION_URI = URI.create("https://dataright.io/revoke");
  public static final URI RECIPIENT_PRODUCT_LOGO_URI = URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  
}
