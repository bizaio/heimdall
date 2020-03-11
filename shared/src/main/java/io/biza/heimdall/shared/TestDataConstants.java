package io.biza.heimdall.shared;

import java.net.URI;
import java.util.List;
import java.util.Set;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;

public class TestDataConstants {

  public static final String HOLDER_LEGAL_NAME = "Biza Pty Ltd";
  public static final String HOLDER_NAME = "Biza.io";
  public static final int HOLDER_AUTH_PORT = 8103;
  public static final String HOLDER_BRAND_NAME = "Bizabank";
  public static final URI HOLDER_BRAND_LOGO_URI =
      URI.create("https://biza.io/wp-content/uploads/2018/07/biza-black-300x67.png");
  public static final URI HOLDER_BRAND_INFOSEC_URI =
      URI.create("https://localhost:8103/wikkit-auth");
  public static final URI HOLDER_BRAND_RESOURCE_URI = URI.create("https://localhost:8102/cds-au");
  public static final URI HOLDER_BRAND_PUBLIC_URI = URI.create("https://localhost:8104/cds-au");
  public static final URI HOLDER_BRAND_WEBSITE = URI.create("https://bizabank.com/");
  public static final String HOLDER_AUTH_JWKS_PATH = "/jwks";
  public static final String RECIPIENT_LEGAL_NAME = "Biza Pty Ltd";
  public static final URI RECIPIENT_LOGO_URI =
      URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  public static final String RECIPIENT_NAME = "DataRight.IO";
  public static final String HOLDER_CLIENT_ID = "f5392d1c-0b2f-4783-a69f-a29c450d775b";
  public static final String HOLDER_CLIENT_SECRET = "c6a1f13d-630c-4f00-8223-8af5d450f830";
  public static final String RECIPIENT_CLIENT_ID = "91c6df22-81f1-4a19-9b63-42018ddb6668";
  public static final String HOLDER_BRAND_ID = "cec4d233-deb7-43ce-a352-427e98d23afb";
  public static final int RECIPIENT_PORT = 7103;
  public static final String RECIPIENT_JWKS_PATH = "/dio-au/jwks";
  public static final URI RECIPIENT_BRAND_LOGO_URI =
      URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  public static final String RECIPIENT_BRAND_ID = "8d3444fd-1727-411d-be92-53ca441b1880";
  public static final String RECIPIENT_PRODUCT_ID = "b91d9148-81f2-43a1-8766-d9588d220da5";
  public static final String RECIPIENT_PRODUCT_NAME = "Prak Data Recipient Example";
  public static final String RECIPIENT_PRODUCT_DESCRIPTION =
      "A demonstration of the Prak Data Recipient example";
  public static final URI RECIPIENT_PRODUCT_WEBSITE = URI.create("https://dataright.io");
  public static final Set<URI> RECIPIENT_PRODUCT_REDIRECT_URI =
      Set.of(URI.create("http://localhost:7110"));
  public static final URI RECIPIENT_PRODUCT_TOS = URI.create("https://dataright.io/tos");
  public static final URI RECIPIENT_PRODUCT_POLICY = URI.create("https://dataright.io/policy");
  public static final URI RECIPIENT_PRODUCT_REVOCATION_URI =
      URI.create("https://dataright.io/revoke");
  public static final URI RECIPIENT_PRODUCT_LOGO_URI =
      URI.create("https://dataright.io/wp-content/uploads/2019/07/DataRightIO-Medium.png");
  public static final List<String> RECIPIENT_PRODUCT_SCOPE_REQUEST = List.of(
      CDRScope.BANK_ACCOUNTS_BASIC_READ.toString(), CDRScope.BANK_ACCOUNTS_DETAIL_READ.toString());
  public static final Integer RECIPIENT_SHARING_DURATION = 86400;

}
