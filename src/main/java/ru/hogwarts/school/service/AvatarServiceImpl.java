package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static io.swagger.v3.core.util.AnnotationsUtils.getExtensions;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }


    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar for student id = {}", studentId);

        Student student = studentRepository.getById(studentId);
        String extension = getExtensions(avatarFile.getOriginalFilename());
        Path filePath = Path.of(avatarsDir, student.getId() + "." + extension);

        logger.debug("Resolved file path: {}", filePath);

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
            logger.debug("Avatar file was written to {}",filePath);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(null);
        if (avatar == null) {
            logger.warn("No existing avatar found for studentId = {}, creating new", studentId);
            avatar = new Avatar();
            avatar.setStudent(student);
        }

        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateDataForDB(filePath)); // Сжатое изображение в БД
        avatarRepository.save(avatar);
        logger.info("Avatar for studentID = {} saved",studentId);
    }


    private byte[] generateDataForDB(Path filePath) throws IOException {
        logger.debug("generating preview image for path: {}",filePath);
        try (InputStream is = Files.newInputStream(filePath); BufferedInputStream bis = new BufferedInputStream(is, 1024); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                logger.error("Invalid image file at {}",filePath);
                throw new IOException("Файл не является изображением или поврежден");
            }

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            logger.debug("Preview image generated successfully for {}",filePath);
            return baos.toByteArray();
        }

    }

    public Avatar findAvatar(Long studentID) {
        logger.info("Was invoked method for find avatar with student id = {}",studentID);
        return avatarRepository.findByStudentId(studentID).orElse(new Avatar());
    }

    private String getExtensions(String fileName) {
        logger.debug("Was invoked method get extensions with file name: {}",fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
