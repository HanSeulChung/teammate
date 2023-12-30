package com.api.backend.team.crypt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleEncryption {

  @Value("${team.secret}")
  private String SECRET_KEY;
  private static final String ALGORITHM = "AES";
  public String encrypt(String date) {
    SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);

    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      byte[] encryptedBytes = cipher.doFinal(date.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      log.warn("date 변환 오류가 발생했습니다." + e.getMessage());
    }

    return null;
  }

  public LocalDate decrypt(String encryptedDate) {
    SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);

    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);

      byte[] decryptedBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedDate));
      return LocalDate.parse(new String(decryptedBytes, StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.warn("date 변환 오류가 발생했습니다." + e.getMessage());
    }

    return null;
  }
}