package com.api.backend.file.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.api.backend.file.service.FileProcessService;
import com.api.backend.file.type.FileFolder;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class AmazonS3FileProcessService implements FileProcessService {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  @Override
  public String uploadImage(MultipartFile file, FileFolder fileFolder) {
    String fileName = getFileFolder(fileFolder) + createFileName(file.getOriginalFilename());

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    try (InputStream inputStream = file.getInputStream()) {
      amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(
          CannedAccessControlList.PublicReadWrite));

    } catch (IOException ioe) {
      throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename()));
    }

    return getFileUrl(fileName);
  }

  @Override
  public String createFileName(String originalFileName) {
    return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
  }

  @Override
  public String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

  @Override
  public void deleteImage(String url) {
    deleteFile(getFileName(url));
  }

  @Override
  public String getFileName(String url) {
    String[] paths = url.split("/");
    return paths[paths.length-2] + "/" + paths[paths.length-1];
  }

  private String getFileFolder(FileFolder fileFolder) {
    switch (fileFolder) {
      case DOCUMENT:
        return "documents/";
      case TEAM:
        return "teams/";
      case PARTICIPANT:
        return "participants/";
      default:
        return "";
    }
  }

  private void deleteFile(String fileName) {
    amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
  }

  public String getFileUrl(String fileName) {
    return amazonS3.getUrl(bucket, fileName).toString();
  }
}
