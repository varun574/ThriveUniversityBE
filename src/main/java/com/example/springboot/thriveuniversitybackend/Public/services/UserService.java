package com.example.springboot.thriveuniversitybackend.Public.services;

import com.example.springboot.thriveuniversitybackend.Public.dtos.AdmissionDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.LoginDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UpdateProfileDto;
import com.example.springboot.thriveuniversitybackend.Public.dtos.UserRegisterDto;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.OldPasswardDoNotMatchException;
import com.example.springboot.thriveuniversitybackend.Public.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.Public.models.Admission;
import com.example.springboot.thriveuniversitybackend.Public.models.User;
import com.example.springboot.thriveuniversitybackend.Public.repositories.AdmissionRepository;
import com.example.springboot.thriveuniversitybackend.Public.repositories.UserRepository;
import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import com.example.springboot.thriveuniversitybackend.enums.AdmissionStatus;
import com.example.springboot.thriveuniversitybackend.enums.AttachmentTypes;
import com.example.springboot.thriveuniversitybackend.enums.Certificates;
import com.example.springboot.thriveuniversitybackend.enums.UserTypes;
import com.example.springboot.thriveuniversitybackend.attachment.FileService;
import com.example.springboot.thriveuniversitybackend.otp.OTPService;
import com.example.springboot.thriveuniversitybackend.student.dtos.StudentDto;
import com.example.springboot.thriveuniversitybackend.student.services.StudentService;
import com.example.springboot.thriveuniversitybackend.teacher.dtos.TeacherDto;
import com.example.springboot.thriveuniversitybackend.teacher.services.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.springboot.thriveuniversitybackend.utils.RandomStringGenerator.generateRandomString;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private OTPService otpService;

    public boolean isValidUser(LoginDto loginDto) {
        User user = repository.findByEmail(loginDto.getEmail());
        return user.getPassword().equals(loginDto.getPassword());
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void saveUser(UserRegisterDto userRegisterDto) {
        User user = modelMapper.map(userRegisterDto, User.class);
        user.setPassword(generateRandomString(48, 122, 8));
        repository.save(user);
    }

    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = repository.findByEmail(email);
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            repository.save(user);
        }
        else{
            throw new OldPasswardDoNotMatchException("Old Password do not match.");
        }
    }

    public Object updateProfile(String type, String email, UpdateProfileDto updateProfileDto) {
        User user = repository.findByEmail(email);
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto studentDto = modelMapper.map(updateProfileDto, StudentDto.class);
            StudentDto updatedStudentDto = studentService.updateProfileByUserId(studentDto, user.getId());
            updatedStudentDto.setName(user.getName());
            updatedStudentDto.setEmail(user.getEmail());
            return updatedStudentDto;
        }
        else if(type.equals(UserTypes.TEACHER.name())){
            TeacherDto studentDto = modelMapper.map(updateProfileDto, TeacherDto.class);
            TeacherDto updatedTeacherDto = teacherService.updateProfileByUserId(studentDto, user.getId());
            updatedTeacherDto.setName(user.getName());
            updatedTeacherDto.setEmail(user.getEmail());
            return updatedTeacherDto;
        }
        else {
            //admin
            return null;
        }
    }

    public void uploadProfilePicture(String email, MultipartFile multipartFile, String attachmentType) throws IOException {
        User user = repository.findByEmail(email);
        fileService.upload(multipartFile, user.getId().hashCode()+"_"+attachmentType);
    }

    public Object getProfile(String type, String email) {
        User user = repository.findByEmail(email);
        String profilePictureURL = "";
        try {
            profilePictureURL = fileService.download(user.getId().hashCode()+"_"+ AttachmentTypes.PROFILE_PICTURE.name());
        } catch (IOException | RuntimeException e) {
            profilePictureURL = null;
        }
        if(type.equals(UserTypes.STUDENT.name())){
            StudentDto studentDto = studentService.getStudentByUserId(user.getId());
            studentDto.setName(user.getName());
            studentDto.setEmail(user.getEmail());
            studentDto.setProfilePictureURL(profilePictureURL);
            List<Attachment> certificates = studentDto.getCertificates();
            try {
                for (int i = 0; i < Certificates.values().length; i++) {
                    if(certificates.get(i) != null){
                        Attachment certificate = certificates.get(i);
                        certificate.setUrl(fileService.download(certificates.get(i).getHashedFileName()));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return studentDto;
        }
        else if(type.equals(UserTypes.TEACHER.name())){
            TeacherDto teacherDto = teacherService.getTeacherByUserId(user.getId());
            teacherDto.setName(user.getName());
            teacherDto.setEmail(user.getEmail());
            teacherDto.setProfilePictureURL(profilePictureURL);
            return teacherDto;
        }
        else {
            //admin
            return null;
        }
    }

    public void sendOtpMail(String email) {
        User user = repository.findByEmail(email);
        if(user == null)
            throw new UserNotFoundException("User not found, please try again with valid roll number");
        String type = user.getType();
        String personalEmail = "";
        if(type.equals(UserTypes.STUDENT.name())){
            personalEmail = studentService.getPersonalEmailByUserId(user.getId());
        } else if (type.equals(UserTypes.TEACHER.name())) {
            personalEmail = teacherService.getPersonalEmailByUserId(user.getId());
        }
        else{
            //admin
        }
        otpService.sendOtpMail(email, personalEmail, user.getName());
    }

    public String getUserIdByEmail(String email) {
        User user = findUserByEmail(email);
        return user.getId();
    }

    public String getEmailById(String userId) {
        Optional<User> user = repository.findById(userId);
        return user.orElseThrow(() -> new UserNotFoundException("User Not found")).getEmail();
    }

    public String applyForAdmission(AdmissionDto admissionDto, MultipartFile[] educationFiles, MultipartFile[] examFiles) throws IOException {
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        for (int i = 0; i < educationFiles.length; i++) {
            String hashedFileName = timeBasedGenerator.generate().toString() + "_" + AttachmentTypes.ADMISSION_EDUCATION.name();
            fileService.upload(educationFiles[i], hashedFileName);
            Attachment attachment = Attachment.builder()
                    .hashedFileName(hashedFileName)
                    .fileName(educationFiles[i].getOriginalFilename())
                    .url(fileService.getDownloadURL(hashedFileName))
                    .build();
            admissionDto.getEducation()[i].setFile(attachment);
        }
        for (int i = 0; i < examFiles.length; i++) {
            String hashedFileName = timeBasedGenerator.generate().toString() + "_" + AttachmentTypes.ADMISSION_EXAM.name();
            fileService.upload(examFiles[i], hashedFileName);
            Attachment attachment = Attachment.builder()
                    .hashedFileName(hashedFileName)
                    .fileName(examFiles[i].getOriginalFilename())
                    .url(fileService.getDownloadURL(hashedFileName))
                    .build();
            admissionDto.getExam_details()[i].setUploaded_score(attachment);
        }
        String trackingId = generateRandomString(48, 122, 10);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(admissionDto);
        Admission admission = Admission.builder()
                .data(json)
                .trackingId(trackingId)
                .status(AdmissionStatus.APPLICATION_RECEIVED.name())
                .build();
        admissionRepository.save(admission);
        return trackingId;
    }
}
