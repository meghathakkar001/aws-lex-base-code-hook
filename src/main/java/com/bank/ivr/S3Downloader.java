package com.bank.ivr;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import java.io.*;


public class S3Downloader {

    public String download(String keyName, String bucketName) {
        System.out.format("Downloading %s from S3 bucket %s...\n", keyName, bucketName);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        try {
            S3Object o = s3.getObject(bucketName, keyName);
            S3ObjectInputStream s3is = o.getObjectContent();
            String objectString= IOUtils.toString(s3is);
            System.out.println("Objectstring is :"+objectString);
            return objectString;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        throw new RuntimeException("could not return InputStream");
    }
}
