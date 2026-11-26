package com.example.FarmShopService.entities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path uploadDir = Paths.get(
                "Backend/FarmShopService/uploads"
        ).toAbsolutePath().normalize();

        // Si le microservice est lancé depuis FarmShopService
        if (!Files.isDirectory(uploadDir)) {
            uploadDir = Paths.get("uploads")
                    .toAbsolutePath()
                    .normalize();
        }

        String uploadPath = uploadDir.toUri().toString();

        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }

        System.out.println("========== IMAGES ==========");
        System.out.println("Working directory : "
                + System.getProperty("user.dir"));

        System.out.println("Upload directory : " + uploadDir);

        System.out.println("Dossier existe : "
                + Files.isDirectory(uploadDir));

        System.out.println("Image existe : "
                + Files.isRegularFile(
                uploadDir.resolve("products/p-carrots.jpg")
        ));

        System.out.println("============================");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}