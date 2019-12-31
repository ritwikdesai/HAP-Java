package io.github.hapjava.impl;

import com.nimbusds.srp6.SRP6Routines;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public class HomekitUtils {

  private static volatile SecureRandom secureRandom;

  public static BigInteger generateSalt() {
    return new BigInteger(SRP6Routines.generateRandomSalt(16));
  }

  public static byte[] generateKey() throws InvalidAlgorithmParameterException {
    EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
    byte[] seed = new byte[spec.getCurve().getField().getb() / 8];
    getSecureRandom().nextBytes(seed);
    return seed;
  }

  public static String generateMac() {
    int byte1 =
        ((getSecureRandom().nextInt(255) + 1) | 2) & 0xFE; // Unicast locally administered MAC;
    return Integer.toHexString(byte1)
        + ":"
        + Stream.generate(() -> getSecureRandom().nextInt(255) + 1)
            .limit(5)
            .map(i -> Integer.toHexString(i))
            .collect(Collectors.joining(":"));
  }

  public static String generatePin() {
    String pin =
        String.format(
            "%03d-%02d-%03d",
            getSecureRandom().nextInt(1000),
            getSecureRandom().nextInt(100),
            getSecureRandom().nextInt(1000));

    if (pin == "000-00-000"
        || pin == "111-11-111"
        || pin == "222-22-222"
        || pin == "333-33-333"
        || pin == "444-44-444"
        || pin == "555-55-555"
        || pin == "666-66-666"
        || pin == "777-77-777"
        || pin == "888-88-888"
        || pin == "999-99-999"
        || pin == "123-45-678"
        || pin == "876-54-321") {
      // disallowed Pin; just recurse and generate a new one
      return generatePin();
    }

    return pin;
  }

  private static SecureRandom getSecureRandom() {
    if (secureRandom == null) {
      synchronized (HomekitUtils.class) {
        if (secureRandom == null) {
          secureRandom = new SecureRandom();
        }
      }
    }
    return secureRandom;
  }

  public static String generateSetupKey() {
    int keyValue = RandomUtils.nextInt(0, 1679616);
    return StringUtils.leftPad(Integer.toString(keyValue, 36).toUpperCase(), 4, "0");
  }

  public static String generateSetupHash(String setupKey, String mac) {
    String hashMaterial = setupKey + mac;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte[] hash = md.digest(hashMaterial.getBytes());
      byte[] hashToEncode = Arrays.copyOfRange(hash, 0, 4);
      return Base64.getEncoder().encodeToString(hashToEncode);
    } catch (NoSuchAlgorithmException e) {
    }
    return "";
  }
}
